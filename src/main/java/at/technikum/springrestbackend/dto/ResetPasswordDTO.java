package at.technikum.springrestbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDTO {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

    public ResetPasswordDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
