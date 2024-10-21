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
    // Zusätzliche Attribute für Soft-Delete und Admin
    private boolean isDeleted = false;  // Hinzugefügt: Kennzeichnet, ob der Benutzer gelöscht ist
    private boolean isAdmin = false;
    private String profileDescription;
    private String profilePicture;
    @ManyToMany(mappedBy = "attendingUsers")
    private Set<EventModel> attendingEvents = new HashSet<>();
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventModel> createdEvents = new HashSet<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumPostModel> createdPosts = new HashSet<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumThreadModel> createdComments = new HashSet<>();
    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaModel> uploadedMedia = new HashSet<>();


    public UserModel() {

    }

    public UserModel(String userId, String username, String password, String email) {
        this.userID = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserModel(String userID, Set<EventModel> attendingEvents, Set<EventModel> createdEvents,
                     String username, String password, String email, Set<MediaModel> uploadedMedia,
                     boolean isDeleted, String profileDescription, String profilePicture) {
        this.userID = userID;
        this.attendingEvents = attendingEvents;
        this.createdEvents = createdEvents;
        this.username = username;
        this.password = password;
        this.email = email;
        this.uploadedMedia = uploadedMedia;
        this.isDeleted = isDeleted;
        this.profileDescription = profileDescription;
        this.profilePicture = profilePicture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public Set<ForumPostModel> getCreatedPosts() {
        return createdPosts;
    }

    public void setCreatedPosts(Set<ForumPostModel> createdPosts) {
        this.createdPosts = createdPosts;
    }

    public Set<ForumThreadModel> getCreatedComments() {
        return createdComments;
    }

    public void setCreatedComments(Set<ForumThreadModel> createdComments) {
        this.createdComments = createdComments;
    }

    public Set<MediaModel> getUploadedMedia() {
        return uploadedMedia;
    }

    public void setUploadedMedia(Set<MediaModel> uploadedMedia) {
        this.uploadedMedia = uploadedMedia;
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
}
