package at.technikum.springrestbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;

    public RegisterDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
}
