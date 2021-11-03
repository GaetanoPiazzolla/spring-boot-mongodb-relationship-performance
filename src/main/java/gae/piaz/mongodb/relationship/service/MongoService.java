package gae.piaz.mongodb.relationship.service;

import gae.piaz.mongodb.relationship.repository.CommentRepository;
import gae.piaz.mongodb.relationship.repository.PostEmbedRepository;
import gae.piaz.mongodb.relationship.model.Comment;
import gae.piaz.mongodb.relationship.model.PostEmbed;
import gae.piaz.mongodb.relationship.model.PostLink;
import gae.piaz.mongodb.relationship.repository.PostLinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MongoService {

    @Autowired
    private PostEmbedRepository embeddedRepo;
    @Autowired
    private PostLinkRepository linkedRepo;
    @Autowired
    private CommentRepository commentRepo;

    /**
     * ------------------ SAVE POST WITH COMMENTS
     * Embed is faster due to additional round-trip to the Db for saving comments in separate call.
     * Embed provides also atomicity and isolation (save comments could fail only - as there is no transaction - resulting in an inconsistent document)
     * <p>
     * How many save in 30 seconds with 30 users?
     * <p>
     * Embed: 36k
     * Link: 16k
     */
    public void savePostEmbedded() {

        PostEmbed post = new PostEmbed();
        post.setText("This is an embedded POST");

        post.setComments(new ArrayList<>());
        Comment c = new Comment();
        c.setAuthor("author");
        c.setText("text");
        post.getComments().add(c);
        Comment c2 = new Comment();
        c2.setAuthor("author2");
        c2.setText("text2");
        post.getComments().add(c2);

        embeddedRepo.save(post);

        log.info("Post embedded saved");
    }



    public void savePostLinked() {

        PostLink post = new PostLink();
        post.setText("This is a linked POST");
        post = linkedRepo.save(post);

        List<Comment> comments = new ArrayList<>();
        Comment c = new Comment();
        c.setAuthor("author");
        c.setText("text");
        c.setPostId(post.getId());
        comments.add(c);
        Comment c2 = new Comment();
        c2.setAuthor("author");
        c2.setText("text");
        c2.setPostId(post.getId());
        comments.add(c2);
        commentRepo.saveAll(comments);

        log.info("Post linked saved");
    }

    /**
     * ------------------- DELETE POST WITH COMMENTS
     * Embed (as for save) is faster due to additional round-trip to the Db for saving comments in separate call.
     * Embed provides also atomicity and isolation.
     * (deleteAll comments could fail only - as there is no transaction - resulting in orphan comments)
     */
    public void deletePostEmbedded(String postId) {
        PostEmbed p = embeddedRepo.findById(postId).get();
        embeddedRepo.delete(p);
    }

    public void deletePostLinked(String postId) {
        PostLink p = linkedRepo.findById(postId).get();
        List<Comment> cs = commentRepo.findByPostId(p.getId());
        commentRepo.deleteAll(cs);
        linkedRepo.delete(p);
    }

    /**
     * ------------------ FIND POSTS
     * Embed is faster due to additional round-trip to the Db for find comments in separate call.
     * But if comments are A LOT (high-arity) we could have memory problems, together with high mongoDB cache miss.
     * <p>
     * Having 2 documents with 2 comments each, how many find can I get in 30 seconds with 10 users?
     * <p>
     * Embed: 22k  (14 MB downloaded)
     * Link: 12k (9,4 MB downloaded)
     */
    public List<PostEmbed> findAllEmbed() {
        return embeddedRepo.findAll();
    }

    public List<PostLink> findAllLink() {
        List<PostLink> postLinkList = linkedRepo.findAll();
        for (PostLink postLink : postLinkList) {
            postLink.setComments(commentRepo.findByPostId(postLink.getId()));
        }
        return postLinkList;
    }

    /**
     * ------------------ FIND POSTS COMMENTED BY AN AUTHOR
     * Embed will return every comment for every post with one comment of that author.
     * We have to remove comments in the application.
     * Performances really depends on ARITY of relationship between document and comment.
     */
    public List<PostEmbed> findPostWithCommentOfAuthorEmbed() {
        String authorFilter = "author";
        List<PostEmbed> ps = embeddedRepo.findPostWithCommentOfAuthor(authorFilter);
        ps.forEach(p -> p.getComments().removeIf(comment -> !comment.getAuthor().equals(authorFilter)
        ));
        return ps;
    }

    public List<PostLink> findPostWithCommentOfAuthorLink() {
        List<PostLink> postList = new ArrayList<>();
        List<Comment> comments = commentRepo.findByAuthor("author");
        comments.forEach(comment -> {
            PostLink post = linkedRepo.findById(comment.getPostId()).get();
            post.setComments(Collections.singletonList(comment));
            postList.add(post);
        });
        return postList;
    }

    /**
     * ---------------- ADD COMMENT TO AN EXISTING POST (similar to updating or deleting an existing comment of a post)
     *
     * Link is faster because we don't have to fetch the whole document before.
     *
     * <p>
     * How many comment can I insert in 30 seconds with 10 users on the same document?
     * <p>
     * Embed: 8k
     * Link: 37k
     */
    public void addCommentEmbed(String postId) {
        PostEmbed p = embeddedRepo.findById(postId).get();
        Comment c = new Comment();
        c.setText("new comment");
        c.setAuthor("new author");
        p.getComments().add(c);
        embeddedRepo.save(p);
    }

    public void addCommentLink(String postId) {
        Comment c = new Comment();
        c.setText("new comment");
        c.setAuthor("new author");
        c.setPostId(postId);
        commentRepo.save(c);
    }

}
