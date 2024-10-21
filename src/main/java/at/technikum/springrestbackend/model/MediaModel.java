package at.technikum.springrestbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class MediaModel {

    @Id
    private String mediaID;
    private String fileLocation;
    private boolean isFrontPic = false;
    @ManyToOne
    @JoinColumn(name = "fk_belong_to_event") //foreign key
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

    //profilepicture
    public MediaModel(String mediaID, String fileLocation, UserModel uploader) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.uploader = uploader;
    }



    //event gallery
    public MediaModel(String mediaID, String fileLocation, EventModel event, UserModel uploader,
                      boolean isHeader) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.event = event;
        this.uploader = uploader;
        this.isFrontPic = isHeader;
    }

    //event post
    public MediaModel(String mediaID, String fileLocation, EventModel event, UserModel uploader,
                      ForumPostModel post) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.event = event;
        this.uploader = uploader;
        this.post = post;
    }

    //post comment
    public MediaModel(String mediaID, String fileLocation, EventModel event, UserModel uploader,
                      ForumThreadModel comment) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
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
