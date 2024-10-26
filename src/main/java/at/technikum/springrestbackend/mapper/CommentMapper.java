package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.CreateCommentDTO;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.services.MediaServices;
import at.technikum.springrestbackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private MediaServices mediaServices;
    @Autowired
    private UserMapper userMapper;

    public CommentModel toEntity(CreateCommentDTO forumPostDTO) {

        return new CommentModel();
    }

    public CreateCommentDTO toSimpleDTO(CommentModel postModel) {

        return new CreateCommentDTO(
//                postModel.getCommentID(), postModel.getTitle(),
//                userMapper.toSimpleDTO(postModel.getAuthor()),
//                postModel.getEvent().getEventID(),
//                postModel.getContent(),
//                mediaServices.postToStringList(postModel)
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
