package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.CreateCommentDTO;
import at.technikum.springrestbackend.dto.DisplayCommentDTO;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.services.MediaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommentMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MediaServices mediaServices;

    public CommentModel toEntity(CreateCommentDTO commentDTO, EventModel event, UserModel author) {

        return new CommentModel(
                UUID.randomUUID().toString(),
                commentDTO.getTitle(),
                commentDTO.getContent(),
                commentDTO.getReplyToCommentID(),
                author, event
        );
    }

    public DisplayCommentDTO toDisplayDTO(CommentModel commentModel) {

        return new DisplayCommentDTO(
                commentModel.getCommentID(),
                commentModel.getEvent().getEventID(),
                commentModel.getTitle(),
                userMapper.toSimpleDTO(commentModel.getAuthor()),
                commentModel.getContent(),
                commentModel.getIsReplyToCommentID(),
                mediaServices.getMediaFromComment(commentModel)
        );
    }

    public CreateCommentDTO toFullDTO(CommentModel postModel){

        return new CreateCommentDTO(
//                postModel.getCommentID(), postModel.getTitle(),
//                userMapper.toSimpleDTO(postModel.getAuthor()),
//                postModel.getEvent().getEventID(),
//                postModel.getContent(),
//                mediaServices.postToStringList(postModel),
        );
    }
}
