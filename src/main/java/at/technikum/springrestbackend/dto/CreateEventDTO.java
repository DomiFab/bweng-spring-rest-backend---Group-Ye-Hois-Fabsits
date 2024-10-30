package at.technikum.springrestbackend.dto;


public class CreateEventDTO {

    private String eventName;
    private String eventLocation;
    private String eventStatus;
    private String eventDescription;


    public CreateEventDTO(){
    }

    //Create/UpdateEventDTO
    public CreateEventDTO(String eventName, String eventLocation, String eventStatus, String eventDescription) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
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
