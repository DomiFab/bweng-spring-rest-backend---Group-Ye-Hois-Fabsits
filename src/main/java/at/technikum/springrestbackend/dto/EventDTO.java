package at.technikum.springrestbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;


public class EventDTO {

    private String eventID;
    private String eventName;
    @NotBlank
    private String eventLocation;
    @NotBlank
    private ZonedDateTime eventDate; // or LocalDateTime without TimeZone
    private String eventShortDescription;
    private String eventLongDescription;
    // Soft-Delete-Attribute in case deletion was an accident
    private boolean isDeleted = false;
    @NotBlank
    @Valid
    private UserDTO creator;
    private Set<UserDTO> attendingUsers = new HashSet<>();
    private Set<MediaDTO> galleryPictures = new HashSet<>();
    private Set<ForumPostDTO> eventPosts = new HashSet<>();

    public EventDTO(){
    }

    //bare bone event option
    public EventDTO(String eventID, String eventName, String eventLocation, ZonedDateTime eventDate,
                    boolean isDeleted, UserDTO creatorID) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.isDeleted = isDeleted;
        this.creator = creatorID;
    }

    //event display for (un)authorized users
    public EventDTO(String eventID, String eventName, String eventLocation, ZonedDateTime eventDate,
                    String eventShortDescription, String eventLongDescription, boolean isDeleted,
                    UserDTO creatorID, Set<MediaDTO> galleryPictures) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventShortDescription = eventShortDescription;
        this.eventLongDescription = eventLongDescription;
        this.isDeleted = isDeleted;
        this.creator = creatorID;
        this.galleryPictures = galleryPictures; //only main picture needs to be displayed
    }

    public EventDTO(String eventID, String eventName, String eventLocation,
                    ZonedDateTime eventDate, String eventShortDescription, String eventLongDescription,
                    UserDTO creatorID) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventShortDescription = eventShortDescription;
        this.eventLongDescription = eventLongDescription;
        this.creator = creatorID;
    }

    //for full event details on event page
    public EventDTO(String eventID, String eventName, String eventLocation,
                    ZonedDateTime eventDate, String eventShortDescription, String eventLongDescription,
                    boolean isDeleted, UserDTO creatorID, Set<UserDTO> attendingUsers,
                    Set<MediaDTO> galleryPictures, Set<ForumPostDTO> eventPosts) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventShortDescription = eventShortDescription;
        this.eventLongDescription = eventLongDescription;
        this.isDeleted = isDeleted;
        this.creator = creatorID;
        this.attendingUsers = attendingUsers;
        this.galleryPictures = galleryPictures;
        this.eventPosts = eventPosts;
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

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public Set<UserDTO> getAttendingUsers() {
        return attendingUsers;
    }

    public void setAttendingUsers(Set<UserDTO> attendingUsers) {
        this.attendingUsers = attendingUsers;
    }

    public Set<MediaDTO> getGalleryPictures() {
        return galleryPictures;
    }

    public void setGalleryPictures(Set<MediaDTO> galleryPictures) {
        this.galleryPictures = galleryPictures;
    }

    public Set<ForumPostDTO> getEventPosts() {
        return eventPosts;
    }

    public void setEventPosts(Set<ForumPostDTO> eventPosts) {
        this.eventPosts = eventPosts;
    }
}
