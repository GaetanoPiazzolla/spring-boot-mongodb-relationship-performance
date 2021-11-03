package gae.piaz.mongodb.relationship.repository;

import gae.piaz.mongodb.relationship.model.PostLink;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostLinkRepository extends MongoRepository<PostLink, String> {

}
