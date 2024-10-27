package at.technikum.springrestbackend.dto;

import java.util.HashSet;
import java.util.Set;


public class UserDTO {
    private String userID;
    private String username;
    private String password;
    private String email;
    // Zusätzliche Attribute für Soft-Delete und Admin
    private boolean isDeleted = false;  // Hinzugefügt: Kennzeichnet, ob der Benutzer gelöscht ist
    private boolean isAdmin = false;
    private String profilePicture;
    private Set<DisplayEventDTO> attendingEvents = new HashSet<>();
    private Set<DisplayEventDTO> createdEvents = new HashSet<>();
    private Set<CreateCommentDTO> createdComments = new HashSet<>();
    private Set<MediaDTO> uploadedMedia = new HashSet<>();

    public UserDTO() {
    }

    //GET,PUT UserDashboardDTO
    public UserDTO(String userID, String username, String email, String profilePicture) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    //GET UserMediaDTO
    public UserDTO(Set<MediaDTO> uploadedMedia){
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<DisplayEventDTO> getAttendingEvents() {
        return attendingEvents;
    }

    public void setAttendingEvents(Set<DisplayEventDTO> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }

    public Set<DisplayEventDTO> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<DisplayEventDTO> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public Set<CreateCommentDTO> getCreatedComments() {
        return createdComments;
    }

    public Set<MediaDTO> getUploadedMedia() {
        return uploadedMedia;
    }
}
