package at.technikum.springrestbackend.controller;


import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.mapper.ForumThreadMapper;
import at.technikum.springrestbackend.model.*;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


//Forum Thread == Comment to a Post
@RestController
@RequestMapping("/forumthreads")
@CrossOrigin
public class ForumThreadController {

    private final ForumThreadMapper commentMapper;
    private final ForumThreadServices commentServices;
    private final FileService fileService;
    @Autowired
    private UserServices userServices;
    @Autowired
    private ForumPostServices postServices;

    @Autowired
    public ForumThreadController(ForumThreadMapper commentMapper, ForumThreadServices commentServices,
                                 FileService fileService) {
        this.commentMapper = commentMapper;
        this.commentServices = commentServices;
        this.fileService = fileService;
    }

    @GetMapping("/{commentID}")
    @ResponseStatus(HttpStatus.FOUND)
    public ForumThreadDTO read(@PathVariable String commentID) {
        ForumThreadModel comment = commentServices.find(commentID);
        return commentMapper.toFullDTO(comment);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ForumThreadDTO create(@RequestPart("commentData") @Valid ForumThreadDTO commentDTO,
                                 @RequestPart("files") List<MultipartFile> files){
        ForumThreadModel comment = commentMapper.toEntity(commentDTO);
        String username = SecurityUtil.getCurrentUserName();

        List<MediaModel> mediaList = fileService.uploadMediaToComment(files, comment, username);
        comment.getMedia().addAll(mediaList);
        commentServices.save(comment);

        //save the comment to the post
        ForumPostModel post = postServices.find(comment.getPost().getId());
        post.getComments().add(comment);
        postServices.save(post);

        //save the comment to the user
        UserModel userModel = userServices.findByUsername(username);
        userModel.getCreatedComments().add(comment);
        userServices.save(userModel);

        return commentMapper.toFullDTO(comment);
    }

    @PutMapping("/{commentID}")
    @ResponseStatus(HttpStatus.OK)
    public ForumThreadDTO update(@PathVariable String commentID,
                               @RequestPart("commentData") @Valid ForumThreadDTO updatedCommentDTO,
                               @RequestPart(value = "files", required = false) List<MultipartFile> files){

        String username = SecurityUtil.getCurrentUserName();
        return commentMapper.toFullDTO(
                commentServices.update(commentID, updatedCommentDTO, files, username));
    }

    @DeleteMapping("/{commentID}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<String> delete(@PathVariable String commentID){

        String username = SecurityUtil.getCurrentUserName();
        boolean isDeleted = commentServices.delete(commentID, username);
        return isDeleted
                ? ResponseEntity.ok("Comment deleted successfully.")
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to delete this comment.");
    }
}