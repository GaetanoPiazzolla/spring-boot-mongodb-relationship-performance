package gae.piaz.mongodb.relationship.repository;

import gae.piaz.mongodb.relationship.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository  extends MongoRepository<Comment, String> {

    List<Comment> findByPostId(String id);

    List<Comment> findByAuthor(String author);

}
