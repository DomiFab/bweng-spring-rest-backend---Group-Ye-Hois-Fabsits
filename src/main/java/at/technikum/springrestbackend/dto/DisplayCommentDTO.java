package at.technikum.springrestbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class DisplayCommentDTO {
    private String commentID;
    @NotBlank
    @Valid
    private String eventID;
    private String title;
    @NotBlank
    @Valid
    private UserDTO author;
    private String content;
    private String replyToCommentID;
    private Set<String> mediaURLs = new HashSet<>();

    public DisplayCommentDTO() {
    }

    public DisplayCommentDTO(String commentID, String eventID, String title, UserDTO author,
                             String content, String replyToCommentID, Set<String> mediaURLs) {
        this.commentID = commentID;
        this.eventID = eventID;
        this.title = title;
        this.author = author;
        this.content = content;
        this.replyToCommentID = replyToCommentID;
        this.mediaURLs = mediaURLs;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyToCommentID() {
        return replyToCommentID;
    }

    public void setReplyToCommentID(String replyToCommentID) {
        this.replyToCommentID = replyToCommentID;
    }

    public Set<String> getMediaURLs() {
        return mediaURLs;
    }

    public void setMediaURLs(Set<String> mediaURLs) {
        this.mediaURLs = mediaURLs;
    }
}
