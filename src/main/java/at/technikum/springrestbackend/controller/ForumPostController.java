package at.technikum.springrestbackend.controller;


import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.mapper.ForumPostMapper;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.ForumPostRepository;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.EventServices;
import at.technikum.springrestbackend.services.FileService;
import at.technikum.springrestbackend.services.ForumPostServices;
import at.technikum.springrestbackend.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/forumposts")
@CrossOrigin
public class ForumPostController {

    private final ForumPostMapper postMapper;
    private final ForumPostServices postServices;
    private final ForumPostRepository postRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private UserServices userServices;

    @Autowired
    public ForumPostController(
            ForumPostMapper postMapper,
            ForumPostServices postServices,
            ForumPostRepository postRepository) {
        this.postMapper = postMapper;
        this.postServices = postServices;
        this.postRepository = postRepository;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ForumPostDTO read(@PathVariable String id) {
        ForumPostModel forumPost = postServices.find(id);
        return postMapper.toFullDTO(forumPost);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ForumPostDTO create(@RequestPart("postData") @Valid ForumPostDTO forumPostDTO,
                               @RequestPart("files") List<MultipartFile> files){
        ForumPostModel forumPost = postMapper.toEntity(forumPostDTO);
        String user = SecurityUtil.getCurrentUserName();

        //upload media and save post to database
        List<MediaModel> mediaList = fileService.uploadMediaToEvent(files, forumPostDTO.getEventID(),user);
        forumPost.getMedia().addAll(mediaList);
        postServices.save(forumPost);

        //save the post to the event
        EventModel event = eventServices.find(forumPostDTO.getEventID());
        event.getEventPosts().add(forumPost);
        eventServices.save(event);

        //save the post to the user
        UserModel userModel = userServices.findByUsername(user);
        userModel.getCreatedPosts().add(forumPost);
        userServices.save(userModel);

        return postMapper.toSimpleDTO(forumPost);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ForumPostDTO update(@PathVariable String id,
                               @RequestPart("postData") @Valid ForumPostDTO updatedForumPostDTO,
                               @RequestPart(value = "files", required = false) List<MultipartFile> files){

        String username = SecurityUtil.getCurrentUserName();
        return postMapper.toFullDTO(
                postServices.update(id, updatedForumPostDTO, files, username));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<String> delete(@PathVariable String id){
        String username = SecurityUtil.getCurrentUserName();
        boolean isDeleted = postServices.delete(id, username);
        return isDeleted
                ? ResponseEntity.ok("Post deleted successfully.")
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to delete this post.");
    }
}