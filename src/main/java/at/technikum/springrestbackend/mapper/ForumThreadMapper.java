package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.model.ForumThreadModel;
import at.technikum.springrestbackend.services.ForumPostServices;
import at.technikum.springrestbackend.services.MediaServices;
import at.technikum.springrestbackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
public class ForumThreadMapper {

    @Autowired
    private UserServices userServices;
    @Autowired
    private ForumPostServices postServices;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MediaServices mediaServices;


    public ForumThreadDTO toFullDTO(ForumThreadModel commentModel){

        return new ForumThreadDTO(
                commentModel.getId(), commentModel.getContent(),
                userMapper.toSimpleDTO(commentModel.getAuthor()),
                commentModel.getPost().getId(),
                mediaServices.commentToStringList(commentModel)
        );
    }

    public ForumThreadModel toEntity(ForumThreadDTO commentDTO) {

        return new ForumThreadModel(
                UUID.randomUUID().toString(),
                commentDTO.getContent(),
                userServices.find(commentDTO.getAuthor().getUserID()),
                postServices.find(commentDTO.getPostID()),
                new HashSet<>()
        );
    }
}
