package at.technikum.springrestbackend.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class MediaDTO {
    private String mediaID;
    @NotBlank
    @Valid
    private String filePath;
    @NotBlank
    @Valid
    private UserDTO uploader;
    @NotBlank
    private String commentID;

    public MediaDTO() {
    }

    //DisplayMediaDTO
    public MediaDTO(String mediaID, String filePath, UserDTO uploader, String commentID) {
        this.mediaID = mediaID;
        this.filePath = filePath;
        this.uploader = uploader;
        this.commentID = commentID;
    }
}
