package at.technikum.springrestbackend.dto;

import java.util.List;

public class FullUserDTO {
    private String userID;
    private String username;
    private String email;
    private String profilePicture;
    private boolean isAdmin;
    private List<DisplayEventDTO> createdEvents;
    private List<DisplayEventDTO> attendingEvents;

    public FullUserDTO() {
    }

    public FullUserDTO(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public FullUserDTO(String userID, String username, String email, String profilePicture, boolean isAdmin,
                       List<DisplayEventDTO> createdEvents, List<DisplayEventDTO> attendingEvents) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.isAdmin = isAdmin;
        this.createdEvents = createdEvents;
        this.attendingEvents = attendingEvents;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<DisplayEventDTO> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(List<DisplayEventDTO> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public List<DisplayEventDTO> getAttendingEvents() {
        return attendingEvents;
    }

    public void setAttendingEvents(List<DisplayEventDTO> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }
}
