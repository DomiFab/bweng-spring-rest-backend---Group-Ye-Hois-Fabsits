package at.technikum.springrestbackend.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class ForumThreadDTO {
    private String commentID;
    private String content;
    @NotBlank
    @Valid
    private UserDTO author;
    private String postID;
    private Set<String> media = new HashSet<>();

    public ForumThreadDTO() {
    }


    public ForumThreadDTO(String id, String content, UserDTO author, String postID, Set<String> media) {
        this.commentID = id;
        this.content = content;
        this.author = author;
        this.postID = postID;
        this.media = media;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Set<String> getMedia() {
        return media;
    }

    public void setMedia(Set<String> media) {
        this.media = media;
    }
}
