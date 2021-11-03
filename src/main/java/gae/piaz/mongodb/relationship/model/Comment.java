package gae.piaz.mongodb.relationship.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("comment")
@Data
public class Comment {

    @Id
    private String id;
    private String text;
    private String author;

    private String postId;

}