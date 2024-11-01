package at.technikum.springrestbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class CommentModel {
    @Id
    private String commentID;
    private String title;
    private String content;
    private String isReplyToCommentID;
    private LocalDateTime postedAt;
    private boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "fk_author", nullable = true) //foreign key
    private UserModel author;
    @ManyToOne
    @JoinColumn(name = "fk_event_to_comment", nullable = true)
    private EventModel event;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaModel> media = new HashSet<>();

    public CommentModel() {
    }

    //for creating post
    public CommentModel(String commentID, String title, String content,
                        String isReplyToCommentID, UserModel author, EventModel event,
                        LocalDateTime postedAt) {
        this.commentID = commentID;
        this.title = title;
        this.content = content;
        this.isReplyToCommentID = isReplyToCommentID;
        this.author = author;
        this.event = event;
        this.postedAt = postedAt;
    }

    public CommentModel(String commentID, String title, String content, String isReplyToCommentID,
                        LocalDateTime postedAt, boolean isDeleted, UserModel author,
                        EventModel event, Set<MediaModel> media) {
        this.commentID = commentID;
        this.title = title;
        this.content = content;
        this.isReplyToCommentID = isReplyToCommentID;
        this.postedAt = postedAt;
        this.isDeleted = isDeleted;
        this.author = author;
        this.event = event;
        this.media = media;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public Set<MediaModel> getMedia() {
        return media;
    }

    public void setMedia(Set<MediaModel> media) {
        this.media = media;
    }

    public String getIsReplyToCommentID() {
        return isReplyToCommentID;
    }

    public void setIsReplyToCommentID(String isReplyToCommentID) {
        this.isReplyToCommentID = isReplyToCommentID;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }
}
