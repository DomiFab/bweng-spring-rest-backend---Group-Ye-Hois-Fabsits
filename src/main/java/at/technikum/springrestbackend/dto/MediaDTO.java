package at.technikum.springrestbackend.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class MediaDTO {
    private String mediaID;
    @NotBlank
    @Valid
    private String filePath;
    private String eventID;
    @NotBlank
    @Valid
    private UserDTO uploader;
    private String commentID;

    public MediaDTO() {
    }

    //DisplayMediaDTO
    public MediaDTO(String mediaID, String filePath,
                    String eventID, UserDTO uploader, String commentID) {
        this.mediaID = mediaID;
        this.filePath = filePath;
        this.eventID = eventID;
        this.uploader = uploader;
        this.commentID = commentID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UserDTO getUploader() {
        return uploader;
    }

    public void setUploader(UserDTO uploader) {
        this.uploader = uploader;
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
}
