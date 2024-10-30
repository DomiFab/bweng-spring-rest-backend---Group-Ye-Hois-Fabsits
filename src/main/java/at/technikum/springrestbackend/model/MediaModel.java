package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "media")
public class MediaModel {

    @Id
    private String mediaID;
    @NotBlank
    private String fileURL;
    @ManyToOne
    @JoinColumn(name = "fk_belong_to_event", nullable = true) //foreign key
    private EventModel event;
    @ManyToOne
    @JoinColumn(name = "fk_uploader", nullable = true)
    private UserModel uploader;
    @ManyToOne
    @JoinColumn(name = "fk_comment", nullable = true)
    private CommentModel comment;

    public MediaModel() {
    }

    public MediaModel(String mediaID, String fileURL, EventModel event,
                      UserModel uploader, CommentModel comment) {
        this.mediaID = mediaID;
        this.fileURL = fileURL;
        this.event = event;
        this.uploader = uploader;
        this.comment = comment;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public UserModel getUploader() {
        return uploader;
    }

    public void setUploader(UserModel uploader) {
        this.uploader = uploader;
    }

    public CommentModel getComment() {
        return comment;
    }

    public void setComment(CommentModel comment) {
        this.comment = comment;
    }
}
