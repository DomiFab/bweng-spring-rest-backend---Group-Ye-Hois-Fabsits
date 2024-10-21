package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.services.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MediaMapper {

    @Autowired
    private UserUtility userUtility;

    public MediaDTO toSimpleDTO (MediaModel media){
        return new MediaDTO(
                media.getMediaID(), media.getFileLocation(),
                media.getEvent().getEventID(),
                userUtility.toSimpleDTO(media.getUploader()),
                media.isFrontPic()
        );
    }
}
