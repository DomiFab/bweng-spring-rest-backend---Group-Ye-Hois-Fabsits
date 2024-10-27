package at.technikum.springrestbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class DisplayEventDTO {
    private String eventID;
    private String eventName;
    @NotBlank
    @Valid
    private UserDTO creator;
    private boolean isCreator;
    @NotBlank
    private String eventLocation;
    @NotBlank
    private LocalDateTime eventDate;
    @NotBlank
    private String eventStatus;
    private String eventPicture;
    private String eventDescription;
    private Long attendeeCount;
    private boolean isDeleted = false;


    public DisplayEventDTO(){
    }

    //ABGESPECKT, weil Media, Comment (Attendees) separat geladen werden!
    public DisplayEventDTO(String eventID, String eventName, String eventLocation, LocalDateTime eventDate,
                    String eventStatus, String eventDescription, String eventPicture,
                    boolean isDeleted, UserDTO creator, boolean isCreator, Long attendeeCount) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStatus = eventStatus;
        this.eventDescription = eventDescription;
        this.eventPicture = eventPicture;
        this.isDeleted = isDeleted;
        this.creator = creator;
        this.isCreator = isCreator;
        this.attendeeCount = attendeeCount;
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

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
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

    public Long getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(Long attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }
}
