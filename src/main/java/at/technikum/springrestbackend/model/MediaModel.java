package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "media")
public class MediaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID mediaID;
    private String fileLocation;
    private boolean isFrontPic = false;

    @ManyToOne
    @JoinColumn(name = "fk_belong_to_event") // foreign key
    private EventModel event;

    @ManyToOne
    @JoinColumn(name = "fk_uploader")
    private UserModel uploader;

    @ManyToOne
    @JoinColumn(name = "fk_post")
    private ForumPostModel post;

    @ManyToOne
    @JoinColumn(name = "fk_comment")
    private ForumThreadModel comment;

    public MediaModel() {
    }

    // Profile picture
    public MediaModel(String fileLocation, UserModel uploader) {
        this.mediaID = UUID.randomUUID(); // Generate UUID
        this.fileLocation = fileLocation;
        this.uploader = uploader;
    }

    // Event gallery
    public MediaModel(String fileLocation, EventModel event, UserModel uploader, boolean isHeader) {
        this.mediaID = UUID.randomUUID(); // Generate UUID
        this.fileLocation = fileLocation;
        this.event = event;
        this.uploader = uploader;
        this.isFrontPic = isHeader;
    }

    // Event post
    public MediaModel(String fileLocation, EventModel event, UserModel uploader, ForumPostModel post) {
        this.mediaID = UUID.randomUUID(); // Generate UUID
        this.fileLocation = fileLocation;
        this.event = event;
        this.uploader = uploader;
        this.post = post;
    }

    // Post comment
    public MediaModel(String fileLocation, EventModel event, UserModel uploader, ForumThreadModel comment) {
        this.mediaID = UUID.randomUUID(); // Generate UUID
        this.fileLocation = fileLocation;
        this.event = event;
        this.uploader = uploader;
        this.comment = comment;
    }

    // Getters and Setters
    public UUID getMediaID() {
        return mediaID;
    }

    public void setMediaID(UUID mediaID) {
        this.mediaID = mediaID;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
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

    public ForumPostModel getPost() {
        return post;
    }

    public void setPost(ForumPostModel post) {
        this.post = post;
    }

    public ForumThreadModel getComment() {
        return comment;
    }

    public void setComment(ForumThreadModel comment) {
        this.comment = comment;
    }

    public boolean isFrontPic() {
        return isFrontPic;
    }

    public void setFrontPic(boolean frontPic) {
        isFrontPic = frontPic;
    }
}