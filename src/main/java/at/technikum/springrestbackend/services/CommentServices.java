package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.CreateCommentDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.CommentRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServices {
    private final CommentRepository commentRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private EventServices eventServices;

    public CommentServices(CommentRepository postRepository) {
        this.commentRepository = postRepository;
    }

    public boolean idExists(String id){
      //  String url = fileService.generateSignedURL("test123");
        return commentRepository.existsById(id);
    }
    public CommentModel find(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Post not found with id: " + id));
    }

    public List<CommentModel> findAll (){
        return commentRepository.findAll();
    }

    public CommentModel save(CommentModel forumPostModel){
        return commentRepository.save(forumPostModel);
    }

    public CommentModel update(String eventID, String commentID, CreateCommentDTO commentDTO, String userID) {

        if (!idExists(commentID)) {
            throw new EntityNotFoundException("Comment with provided ID [" + commentID + "] not found.");
        }

        CommentModel updatedComment = find(commentID);
        EventModel event = eventServices.find(eventID);
        isAuthorized(updatedComment, userID);

        if (updatedComment.getEvent() != event) {
            throw new EntityNotFoundException("Event does not match the Comment's.");
        }

        updatedComment.setContent(commentDTO.getContent());

        return save(updatedComment);
    }

    @Transactional
    public CommentModel delete(String eventID, String commentID, String userID){

        CommentModel comment = find(commentID);
        EventModel event = eventServices.find(eventID);
        UserModel user = userServices.findByID(userID);

        if (!event.getEventComments().contains(comment)) {
            throw new EntityNotFoundException("This Event does not have this Comment.");
        }

        isTrulyAuthorized(comment, event, user);

//        List<MediaModel> mediaList = comment.getMedia().stream().toList();
//        if (!mediaList.isEmpty()) {
//            mediaRepository.delete(mediaList.getFirst());
//        }
        mediaRepository.deleteAllByComment(comment);

        comment.setContent("");
        comment.setDeleted(true);
        comment.setTitle("<deleted>");

        return save(comment);
    }

    public void isTrulyAuthorized(CommentModel comment, EventModel event, UserModel user) {
        if (!comment.getAuthor().getUserID().equals(user.getUserID()) &&
                !event.getCreator().getUserID().equals(user.getUserID()) &&
                !user.isAdmin()) {
            throw new AccessDeniedException("You do not have permission to perform this action.");
        }
    }

    public void isAuthorized(CommentModel comment, String userID) {
        if (!comment.getAuthor().getUserID().equals(userID) &&
                !userServices.findByID(userID).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to perform this action.");
        }
    }


}

