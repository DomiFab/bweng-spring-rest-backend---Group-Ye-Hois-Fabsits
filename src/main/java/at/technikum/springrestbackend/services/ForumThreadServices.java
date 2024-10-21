package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.mapper.ForumThreadMapper;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.ForumThreadModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.ForumThreadRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public class ForumThreadServices {
    private final ForumThreadRepository commentRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private FileService fileService;
    @Autowired
    private ForumPostServices postServices;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private ForumThreadMapper commentMapper;

    @Autowired
    public ForumThreadServices(ForumThreadRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public boolean idExists(String id){
        return commentRepository.existsById(id);
    }
    public ForumThreadModel find(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Post not found with id: " + id));
    }

    public List<ForumThreadModel> findAll (){
        return commentRepository.findAll();
    }

    public ForumThreadModel save(ForumThreadModel commentModel){
        return commentRepository.save(commentModel);
    }

    public boolean delete(String commentID, String username){
        ForumThreadModel comment = find(commentID);
        ForumPostModel post = postServices.find(comment.getPost().getId());

        if (!comment.getAuthor().getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin() &&
                !eventServices.find(post.getEvent().getEventID()).getCreator().getUsername().equals(username)) {
            return false;
        }
        UserModel user = userServices.findByUsername(username);
        user.getCreatedComments().remove(comment);
        userServices.save(user);


        post.getComments().remove(comment);
        postServices.save(post);

        mediaRepository.deleteAllByComment(comment);
        commentRepository.delete(comment);
        return true;
    }

    public ForumThreadModel update(String id, ForumThreadDTO updatedCommentDTO, List<MultipartFile> files, String username){

        ForumPostModel post = postServices.find(find(id).getPost().getId());
        UserModel user = userServices.findByUsername(username);
        //catching the case when an entity with the id does not exist
        if (!idExists(id)){
            throw new EntityNotFoundException("Forum Post with provided ID [" + id + "] not found.");
        }

        //get the existing Post from the DB and THEN set new values
        ForumThreadModel updatedComment = find(id);

        if (!userServices.find(updatedComment.getAuthor().getUserID()).getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this comment.");
        }
        post.getComments().remove(updatedComment);
        user.getCreatedComments().remove(updatedComment);
        //update post details
        updatedComment.setContent(updatedCommentDTO.getContent());
        //update post media
        List<MediaModel> mediaList = fileService.updateCommentMedia(files, updatedComment);
        //clear the media list to repopulate with new set of media
        updatedComment.getMedia().clear();
        updatedComment.getMedia().addAll(mediaList);
        post.getComments().add(updatedComment);
        postServices.save(post);
        user.getCreatedComments().add(updatedComment);
        return commentRepository.save(updatedComment);
    }

    public Set<ForumThreadDTO> toDTOList(ForumPostModel postModel){
        ForumPostDTO post = new ForumPostDTO();

        for (ForumThreadModel comment : postModel.getComments()){
            post.getComments().add(commentMapper.toFullDTO(comment));
        }
        return post.getComments();
    }
}

