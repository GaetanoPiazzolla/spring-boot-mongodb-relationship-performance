package gae.piaz.mongodb.relationship.repository;


import gae.piaz.mongodb.relationship.model.PostEmbed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostEmbedRepository extends MongoRepository<PostEmbed, String> {

    @Query(value = "{'comments.author': ?0}")
    List<PostEmbed> findPostWithCommentOfAuthor(String author);
}
