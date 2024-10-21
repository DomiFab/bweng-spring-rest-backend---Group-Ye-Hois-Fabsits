package at.technikum.springrestbackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;


public class UserDTO {
    private String userID;
    @NotBlank
    private String username;
    private String password;
    @NotBlank
    private String email;
    // Zusätzliche Attribute für Soft-Delete und Admin
    private boolean isDeleted = false;  // Hinzugefügt: Kennzeichnet, ob der Benutzer gelöscht ist
    private boolean isAdmin = false;
    private String profileDescription;
    private String profilePicture;
    private Set<EventDTO> attendingEvents = new HashSet<>();
    private Set<EventDTO> createdEvents = new HashSet<>();
    private Set<ForumPostDTO> createdPosts = new HashSet<>();
    private Set<ForumThreadDTO> createdComments = new HashSet<>();
    private Set<MediaDTO> uploadedMedia = new HashSet<>();

    public UserDTO() {
    }

    //for login form
    public UserDTO(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    //for event to display list of attending users
    public UserDTO(String userID, String username, String email, String profilePicture) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    //dashboard display with all uploaded Media
    public UserDTO(String userID, String username, String email, String profileDescription, String profilePicture,
                   Set<EventDTO> attendingEvents, Set<EventDTO> createdEvents, Set<MediaDTO> uploadedMedia) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.profileDescription = profileDescription;
        this.profilePicture = profilePicture;
        this.attendingEvents = attendingEvents;
        this.createdEvents = createdEvents;
        this.uploadedMedia = uploadedMedia;
    }

    //dashboard without any Media
    public UserDTO(String userID, String username, String email, String profileDescription, String profilePicture,
                   Set<EventDTO> attendingEvents, Set<EventDTO> createdEvents) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.profileDescription = profileDescription;
        this.profilePicture = profilePicture;
        this.attendingEvents = attendingEvents;
        this.createdEvents = createdEvents;
    }

    //all-purpose use
    public UserDTO(String userID, String username, String password, String email, boolean isDeleted, boolean isAdmin,
                   String profileDescription, String profilePicture, Set<EventDTO> attendingEvents,
                   Set<EventDTO> createdEvents, Set<ForumPostDTO> createdPosts, Set<ForumThreadDTO> createdComments,
                   Set<MediaDTO> uploadedMedia) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isDeleted = isDeleted;
        this.isAdmin = isAdmin;
        this.profileDescription = profileDescription;
        this.profilePicture = profilePicture;
        this.attendingEvents = attendingEvents;
        this.createdEvents = createdEvents;
        this.createdPosts = createdPosts;
        this.createdComments = createdComments;
        this.uploadedMedia = uploadedMedia;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<EventDTO> getAttendingEvents() {
        return attendingEvents;
    }

    public void setAttendingEvents(Set<EventDTO> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }

    public Set<EventDTO> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<EventDTO> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public Set<ForumPostDTO> getCreatedPosts() {
        return createdPosts;
    }

    public void setCreatedPosts(Set<ForumPostDTO> createdPosts) {
        this.createdPosts = createdPosts;
    }

    public Set<ForumThreadDTO> getCreatedComments() {
        return createdComments;
    }

    public void setCreatedComments(Set<ForumThreadDTO> createdComments) {
        this.createdComments = createdComments;
    }

    public Set<MediaDTO> getUploadedMedia() {
        return uploadedMedia;
    }

    public void setUploadedMedia(Set<MediaDTO> uploadedMedia) {
        this.uploadedMedia = uploadedMedia;
    }
}
