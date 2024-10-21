package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
public class EventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventID;

    @NotBlank
    private String eventName;

    @NotBlank
    private String eventLocation;

    private ZonedDateTime eventDate; // Optional, can be null initially

    @NotBlank
    private String eventShortDescription;

    private String eventLongDescription;

    // Soft-Delete attribute in case deletion was an accident
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "fk_creator", nullable = false) // Foreign key
    private UserModel creator;

    @ManyToMany
    @JoinTable(
            name = "event_users",
            joinColumns = @JoinColumn(name = "fk_event"),
            inverseJoinColumns = @JoinColumn(name = "fk_user")
    )
    private Set<UserModel> attendingUsers = new HashSet<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaModel> galleryPictures = new HashSet<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumPostModel> eventPosts = new HashSet<>();

    // Constructors
    public EventModel() {
    }

    public EventModel(UserModel creator, String eventName, String eventLocation, String eventShortDescription) {
        this.eventID = UUID.randomUUID(); // Generate UUID
        this.creator = creator;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventShortDescription = eventShortDescription;
    }

    // Getters and Setters
    public UUID getEventID() {
        return eventID;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }

    public Set<UserModel> getAttendingUsers() {
        return attendingUsers;
    }

    public void setAttendingUsers(Set<UserModel> attendingUsers) {
        this.attendingUsers = attendingUsers;
    }

    public Set<MediaModel> getGalleryPictures() {
        return galleryPictures;
    }

    public void setGalleryPictures(Set<MediaModel> galleryPictures) {
        this.galleryPictures = galleryPictures;
    }

    public Set<ForumPostModel> getEventPosts() {
        return eventPosts;
    }

    public void setEventPosts(Set<ForumPostModel> eventPosts) {
        this.eventPosts = eventPosts;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public ZonedDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventShortDescription() {
        return eventShortDescription;
    }

    public void setEventShortDescription(String eventShortDescription) {
        this.eventShortDescription = eventShortDescription;
    }

    public String getEventLongDescription() {
        return eventLongDescription;
    }

    public void setEventLongDescription(String eventLongDescription) {
        this.eventLongDescription = eventLongDescription;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}