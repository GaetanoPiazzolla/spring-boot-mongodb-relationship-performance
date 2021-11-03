package gae.piaz.mongodb.relationship.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;



@Document("post_embed")
@Data
public class PostEmbed {

    @Id
    private String id;
    private String text;
    private List<Comment> comments;

}

