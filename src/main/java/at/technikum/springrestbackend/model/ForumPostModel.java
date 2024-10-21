package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "forum_posts")
public class ForumPostModel {
    @Id
    private String id;
    private String title;
    private String content;
    @Valid
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "fk_author") //foreign key
    private UserModel author;
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "fk_event_to_post")
    private EventModel event;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumThreadModel> comments = new HashSet<>();
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaModel> media = new HashSet<>();

    public ForumPostModel() {
    }

    //for creating post
    public ForumPostModel(String id, String title, String content,
                          UserModel author, EventModel event, Set<MediaModel> media) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.event = event;
        this.media = media;
    }

    //for displaying post
    public ForumPostModel(String id, String title, String content, UserModel author, EventModel event,
                          Set<ForumThreadModel> comments, Set<MediaModel> media) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.event = event;
        this.comments = comments;
        this.media = media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public Set<ForumThreadModel> getComments() {
        return comments;
    }

    public void setComment(Set<ForumThreadModel> comments) {
        this.comments = comments;
    }

    public Set<MediaModel> getMedia() {
        return media;
    }

    public void setMedia(Set<MediaModel> media) {
        this.media = media;
    }
}
