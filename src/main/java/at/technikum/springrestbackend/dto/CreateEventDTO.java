package at.technikum.springrestbackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public class CreateEventDTO {

    private String eventName;
    @NotBlank
    private String eventLocation;
    @NotBlank
    private LocalDateTime eventDate;
    @NotBlank
    private String eventStatus;
    private String eventDescription;


    public CreateEventDTO(){
    }

    //Create/UpdateEventDTO
    public CreateEventDTO(String eventName, String eventLocation,
                          LocalDateTime eventDate, String eventStatus, String eventDescription) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStatus = eventStatus;
        this.eventDescription = eventDescription;
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

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
