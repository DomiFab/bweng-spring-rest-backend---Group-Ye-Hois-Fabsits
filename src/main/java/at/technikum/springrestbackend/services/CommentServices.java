package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.repository.CommentRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServices {
    private final CommentRepository postRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private FileService fileService;
    @Autowired
    private EventServices eventServices;

    public CommentServices(CommentRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean idExists(String id){
        String url = fileService.generateSignedURL("test123");
        return postRepository.existsById(id);
    }
    public CommentModel find(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Post not found with id: " + id));
    }

    public List<CommentModel> findAll (){
        return postRepository.findAll();
    }

    public CommentModel save(CommentModel forumPostModel){
        return postRepository.save(forumPostModel);
    }

    public boolean delete(String postID, String username){
//        CommentModel post = find(postID);
//        if (!post.getAuthor().getUsername().equals(username) &&
//                !userServices.findByUsername(username).isAdmin() &&
//                !post.getEvent().getCreator().getUsername().equals(username)) {
//            return false;
//        }
//
//        UserModel user = userServices.findByUsername(username);
//        user.getCreatedComments().remove(post);
//        userServices.save(user);
//        EventModel event = eventServices.find(post.getEvent().getEventID());
//        event.getEventPosts().remove(post);
//        eventServices.save(event);
//        mediaRepository.deleteAllByPost(post);
//        postRepository.delete(post);
        return true;
    }

//    public CommentModel update(String id, CreateCommentDTO updatedForumPostDTO, List<MultipartFile> files, String username){
//        //catching the case when an entity with the id does not exist
//        if (!idExists(id)){
//            throw new EntityNotFoundException("Forum Post with provided ID [" + id + "] not found.");
//        }
//        //get the existing Post from the DB and THEN set new values
//        CommentModel updatedPost = find(id);
//        UserModel user = userServices.findByUsername(username);
//        EventModel event = eventServices.find(updatedPost.getEvent().getEventID());
//        if (!userServices.find(updatedForumPostDTO.getAuthorID().getUserID()).getUsername().equals(username) &&
//                !userServices.findByUsername(username).isAdmin()) {
//            throw new AccessDeniedException("You do not have permission to update this post.");
//        }
//        user.getCreatedComments().remove(updatedPost);
//        event.getEventPosts().remove(updatedPost);
//        //update post details
//        updatedPost.setTitle(updatedForumPostDTO.getTitle());
//        updatedPost.setContent(updatedForumPostDTO.getContent());
//        //update post media
//        List<MediaModel> mediaList = fileService.updatePostMedia(files, updatedPost);
//        //clear the media list to repopulate with new set of media
//        updatedPost.getMedia().clear();
//        updatedPost.getMedia().addAll(mediaList);
//        event.getEventPosts().add(updatedPost);
//        eventServices.save(event);
//        user.getCreatedComments().add(updatedPost);
//        userServices.save(user);
//        return postRepository.save(updatedPost);
//    }

}

