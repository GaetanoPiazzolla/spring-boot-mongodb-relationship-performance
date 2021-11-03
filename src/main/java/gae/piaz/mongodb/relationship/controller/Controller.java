package gae.piaz.mongodb.relationship.controller;

import gae.piaz.mongodb.relationship.model.PostEmbed;
import gae.piaz.mongodb.relationship.model.PostLink;
import gae.piaz.mongodb.relationship.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin
public class Controller {

    @Autowired
    private MongoService service;

    // ---------------- SAVE

    @GetMapping(path = "embed/save")
    public ResponseEntity<Void> savePost() {
        service.savePostEmbedded();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "link/save")
    public ResponseEntity<Void> saveLink() {
        service.savePostLinked();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // --------------- DELETE post

    @GetMapping(path = "link/delete/{id}")
    public ResponseEntity<Void> deletePostLinked(@PathVariable("id") String id) {
        service.deletePostLinked(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "embed/delete/{id}")
    public ResponseEntity<Void> deletePostEmbed(@PathVariable("id") String id) {
        service.deletePostEmbedded(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // --------------- FIND all

    @GetMapping(path = "embed/find")
    public ResponseEntity<List<PostEmbed>> findAllEmbed() {
        return ResponseEntity.ok(service.findAllEmbed());
    }

    @GetMapping(path = "link/find")
    public ResponseEntity<List<PostLink>> findAllLink() {
        return ResponseEntity.ok(service.findAllLink());
    }

    // ---------------- FIND filter author

    @GetMapping(path = "embed/find/author")
    public ResponseEntity<List<PostEmbed>> findAuthorEmbed() {
        return ResponseEntity.ok(service.findPostWithCommentOfAuthorEmbed());
    }

    @GetMapping(path = "link/find/author")
    public ResponseEntity<List<PostLink>> findAuthorLink() {
        return ResponseEntity.ok(service.findPostWithCommentOfAuthorLink());
    }

    //---------------- ADD comment

    @GetMapping(path = "embed/add-comment/{id}")
    public ResponseEntity<Void> addCommentEmbed(@PathVariable("id") String id) {
        service.addCommentEmbed(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "link/add-comment/{id}")
    public ResponseEntity<Void> addCommentLink(@PathVariable("id") String id) {
        service.addCommentLink(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
