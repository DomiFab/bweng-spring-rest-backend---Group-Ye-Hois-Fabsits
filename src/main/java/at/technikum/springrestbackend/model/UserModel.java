package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    private String userID;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    private String profilePicture;
    // Zusätzliche Attribute für Soft-Delete und Admin
    private boolean isDeleted = false;  // Hinzugefügt: Kennzeichnet, ob der Benutzer gelöscht ist
    private boolean isAdmin = false;
    @ManyToMany(mappedBy = "attendingUsers")
    private Set<EventModel> attendingEvents = new HashSet<>();
    @OneToMany(mappedBy = "creator")
    private Set<EventModel> createdEvents = new HashSet<>();
    @OneToMany(mappedBy = "author")
    private Set<CommentModel> createdComments = new HashSet<>();
    @OneToMany(mappedBy = "uploader")
    private Set<MediaModel> uploadedMedia = new HashSet<>();


    public UserModel() {

    }

    //registration
    public UserModel(String userId, String username, String password, String email) {
        this.userID = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserModel(String userID, String username, String password, String email, String profilePicture,
                     boolean isDeleted, boolean isAdmin, Set<EventModel> attendingEvents,
                     Set<EventModel> createdEvents, Set<CommentModel> createdPosts,
                     Set<MediaModel> uploadedMedia) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePicture = profilePicture;
        this.isDeleted = isDeleted;
        this.isAdmin = isAdmin;
        this.attendingEvents = attendingEvents;
        this.createdEvents = createdEvents;
        this.createdComments = createdPosts;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public Set<EventModel> getAttendingEvents() {
        return attendingEvents;
    }

    public void setAttendingEvents(Set<EventModel> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }

    public Set<EventModel> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<EventModel> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public Set<CommentModel> getCreatedComments() {
        return createdComments;
    }

    public void setCreatedComments(Set<CommentModel> createdComments) {
        this.createdComments = createdComments;
    }

    public Set<MediaModel> getUploadedMedia() {
        return uploadedMedia;
    }

    public void setUploadedMedia(Set<MediaModel> uploadedMedia) {
        this.uploadedMedia = uploadedMedia;
    }
}
