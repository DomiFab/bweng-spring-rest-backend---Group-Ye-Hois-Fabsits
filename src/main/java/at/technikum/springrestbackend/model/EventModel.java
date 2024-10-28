package at.technikum.springrestbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name="events")
public class EventModel {

    @Positive
    @Id
    private String eventID;
    @NotBlank
    private String eventName;
    @NotBlank
    private String eventLocation;
    private String eventPicture;
    private String eventDescription;
    private String eventStatus;
    // Soft-Delete-Attribute in case deletion was an accident
    private boolean isDeleted = false;
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "fk_creator") //foreign key
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
    private Set<CommentModel> eventComments = new HashSet<>();

    // Constructor
    public EventModel() {
    }

    //Event Creation
    public EventModel(String eventID, String eventName, String eventLocation,
                      String eventDescription, UserModel creator, String eventStatus) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.creator = creator;
        this.eventStatus = eventStatus;
    }

    public EventModel(String eventID, String eventName, String eventLocation,
                      String eventDescription, boolean isDeleted, UserModel creator, Set<UserModel> attendingUsers,
                      Set<MediaModel> galleryPictures, Set<CommentModel> eventComments, String eventStatus,
                      String eventPicture) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.isDeleted = isDeleted;
        this.creator = creator;
        this.attendingUsers = attendingUsers;
        this.galleryPictures = galleryPictures;
        this.eventComments = eventComments;
        this.eventStatus = eventStatus;
        this.eventPicture = eventPicture;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public String getEventPicture() {
        return eventPicture;
    }

    public void setEventPicture(String eventPicture) {
        this.eventPicture = eventPicture;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public Set<CommentModel> getEventComments() {
        return eventComments;
    }

    public void setEventComments(Set<CommentModel> eventComments) {
        this.eventComments = eventComments;
    }
}