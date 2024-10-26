package at.technikum.springrestbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class CreateCommentDTO {
    private String commentID;
    @NotBlank
    @Valid
    private String eventID;
    private String title;
    @NotBlank
    @Valid
    private String authorID;
    private String content;
    private String replyToCommentID;

    public CreateCommentDTO() {
    }

    //CreateComment
    public CreateCommentDTO(String eventID, String title, String authorID,
                            String content, String replyToCommentID) {
        this.eventID = eventID;
        this.title = title;
        this.authorID = authorID;
        this.content = content;
        this.replyToCommentID = replyToCommentID;
    }

    //UpdateComment
    public CreateCommentDTO(String commentID, String eventID, String title, String authorID,
                            String content, String replyToCommentID) {
        this.commentID = commentID;
        this.eventID = eventID;
        this.title = title;
        this.authorID = authorID;
        this.content = content;
        this.replyToCommentID = replyToCommentID;
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

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
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

}
