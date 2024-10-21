package at.technikum.springrestbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class ForumPostDTO {
    private String id;
    private String title;
    @NotBlank
    @Valid
    private UserDTO authorID;
    @NotBlank
    @Valid
    private String eventID;
    private String content;
    private Set<String> media = new HashSet<>();
    private Set<ForumThreadDTO> comments = new HashSet<>();

    public ForumPostDTO() {
    }

    //for display when opening event page
    public ForumPostDTO(String id, String title, UserDTO authorID, String eventID,
                        String content, Set<String> media, Set<ForumThreadDTO> comments) {
        this.id = id;
        this.title = title;
        this.authorID = authorID;
        this.eventID = eventID;
        this.content = content;
        this.media = media;
        this.comments = comments;
    }

    //for saving post to database, no comments because it is only a post creation
    public ForumPostDTO(String id, String title, UserDTO authorID, String eventID,
                        String content, Set<String> media) {
        this.id = id;
        this.title = title;
        this.authorID = authorID;
        this.eventID = eventID;
        this.content = content;
        this.media = media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserDTO getAuthorID() {
        return authorID;
    }

    public void setAuthorID(UserDTO authorID) {
        this.authorID = authorID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getMedia() {
        return media;
    }

    public void setMedia(Set<String> media) {
        this.media = media;
    }

    public Set<ForumThreadDTO> getComments() {
        return comments;
    }

    public void setComments(Set<ForumThreadDTO> comments) {
        this.comments = comments;
    }
}
