package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.model.ForumThreadModel;
import at.technikum.springrestbackend.repository.ForumPostRepository;
import at.technikum.springrestbackend.repository.UserRepository;
import at.technikum.springrestbackend.services.MediaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
public class ForumThreadMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ForumPostRepository postRepository;
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
                userRepository.findById(commentDTO.getAuthor().getUserID())
                        .orElseThrow(),
                postRepository.findById(commentDTO.getPostID())
                        .orElseThrow(),
                new HashSet<>()
        );
    }
}
