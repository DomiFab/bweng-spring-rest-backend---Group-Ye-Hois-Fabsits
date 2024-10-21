package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    @Email
    private String email;

    private boolean isDeleted = false;
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

    public UserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters and Setters

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
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

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
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
}