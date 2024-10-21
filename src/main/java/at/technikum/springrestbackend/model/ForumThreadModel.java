package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "forum_threads")
public class ForumThreadModel {
    @Id
    private String id;
    private String content;
    @Valid
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "fk_comment_author")
    private UserModel author;
    @ManyToOne
    @JoinColumn(name = "fk_belongto_post")
    private ForumPostModel post;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaModel> media = new HashSet<>();


    public ForumThreadModel() {
    }

    public ForumThreadModel(String id, String content, UserModel author,
                            ForumPostModel post, Set<MediaModel> media) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.post = post;
        this.media = media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ForumPostModel getPost() {
        return post;
    }

    public void setPost(ForumPostModel post) {
        this.post = post;
    }

    public Set<MediaModel> getMedia() {
        return media;
    }

    public void setMedia(Set<MediaModel> media) {
        this.media = media;
    }
}
