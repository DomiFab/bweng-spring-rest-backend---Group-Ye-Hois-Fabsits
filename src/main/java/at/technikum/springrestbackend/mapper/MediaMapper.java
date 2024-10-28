package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.model.MediaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MediaMapper {

    @Autowired
    private UserMapper userMapper;

    public MediaDTO toDTO(MediaModel media){
        return new MediaDTO(
                media.getMediaID(), media.getFileURL(),
                media.getEvent().getEventID(),
                userMapper.toSimpleDTO(media.getUploader()),
                media.getComment().getCommentID()
        );
    }
}
