package at.technikum.springrestbackend.dto;

public class CreateCommentDTO {
    private String title;
    private String content;
    private String replyToCommentID;
    private boolean deleteImage;

    public CreateCommentDTO() {
    }

    //CreateComment
    public CreateCommentDTO(String title,String content, String replyToCommentID) {
        this.title = title;
        this.content = content;
        this.replyToCommentID = replyToCommentID;
    }

    public CreateCommentDTO(String title, String content, String replyToCommentID, boolean deleteImage) {
        this.title = title;
        this.content = content;
        this.replyToCommentID = replyToCommentID;
        this.deleteImage = deleteImage;
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

    public String getReplyToCommentID() {
        return replyToCommentID;
    }

    public void setReplyToCommentID(String replyToCommentID) {
        this.replyToCommentID = replyToCommentID;
    }

    public boolean isDeleteImage() {
        return deleteImage;
    }

    public void setDeleteImage(boolean deleteImage) {
        this.deleteImage = deleteImage;
    }
}
