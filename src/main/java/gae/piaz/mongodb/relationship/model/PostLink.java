package gae.piaz.mongodb.relationship.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document("post_link")
@Data
public class PostLink {

    @Id
    private String id;
    private String text;

    // @DBRef(lazy = true, db = "first")
    // from docs.mongodb.com - > Unless you have a compelling reason to use DBRefs, use manual references instead.
    // public List<Comment> comments;

    // we do MANUAL MAPPING
    @Transient
    public List<Comment> comments;
}