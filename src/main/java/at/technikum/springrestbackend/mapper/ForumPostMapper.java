package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.services.ForumThreadServices;
import at.technikum.springrestbackend.services.MediaServices;
import at.technikum.springrestbackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
public class ForumPostMapper {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private MediaServices mediaServices;
    @Autowired
    private ForumThreadServices commentServices;
    @Autowired
    private UserMapper userMapper;

    public ForumPostModel toEntity(ForumPostDTO forumPostDTO) {

        return new ForumPostModel(
                UUID.randomUUID().toString(),
                forumPostDTO.getTitle(), forumPostDTO.getContent(),
                userServices.find(forumPostDTO.getAuthorID().getUserID()),
                eventRepository.findById(forumPostDTO.getEventID())
                        .orElseThrow(() -> new EntityNotFoundException("Event not found")),
                new HashSet<>()
        );
    }

    public ForumPostDTO toSimpleDTO(ForumPostModel postModel) {

        return new ForumPostDTO(
                postModel.getId(), postModel.getTitle(),
                userMapper.toSimpleDTO(postModel.getAuthor()),
                postModel.getEvent().getEventID(),
                postModel.getContent(),
                mediaServices.postToStringList(postModel)
        );
    }

    public ForumPostDTO toFullDTO(ForumPostModel postModel){

        return new ForumPostDTO(
                postModel.getId(), postModel.getTitle(),
                userMapper.toSimpleDTO(postModel.getAuthor()),
                postModel.getEvent().getEventID(),
                postModel.getContent(),
                mediaServices.postToStringList(postModel),
                commentServices.toDTOList(postModel)
        );
    }



}
