package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.ForumPostRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ForumPostServices {
    private final ForumPostRepository postRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private FileService fileService;
    @Autowired
    private EventServices eventServices;

    public ForumPostServices(ForumPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean idExists(String id){
        return postRepository.existsById(id);
    }
    public ForumPostModel find(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Post not found with id: " + id));
    }

    public List<ForumPostModel> findAll (){
        return postRepository.findAll();
    }

    public ForumPostModel save(ForumPostModel forumPostModel){
        return postRepository.save(forumPostModel);
    }

    public boolean delete(String postID, String username){
        ForumPostModel post = find(postID);
        if (!post.getAuthor().getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin() &&
                !post.getEvent().getCreator().getUsername().equals(username)) {
            return false;
        }

        UserModel user = userServices.findByUsername(username);
        user.getCreatedPosts().remove(post);
        userServices.save(user);
        EventModel event = eventServices.find(post.getEvent().getEventID());
        event.getEventPosts().remove(post);
        eventServices.save(event);
        mediaRepository.deleteAllByPost(post);
        postRepository.delete(post);
        return true;
    }

    public ForumPostModel update(String id, ForumPostDTO updatedForumPostDTO, List<MultipartFile> files, String username){
        //catching the case when an entity with the id does not exist
        if (!idExists(id)){
            throw new EntityNotFoundException("Forum Post with provided ID [" + id + "] not found.");
        }
        //get the existing Post from the DB and THEN set new values
        ForumPostModel updatedPost = find(id);
        UserModel user = userServices.findByUsername(username);
        EventModel event = eventServices.find(updatedPost.getEvent().getEventID());
        if (!userServices.find(updatedForumPostDTO.getAuthorID().getUserID()).getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this post.");
        }
        user.getCreatedPosts().remove(updatedPost);
        event.getEventPosts().remove(updatedPost);
        //update post details
        updatedPost.setTitle(updatedForumPostDTO.getTitle());
        updatedPost.setContent(updatedForumPostDTO.getContent());
        //update post media
        List<MediaModel> mediaList = fileService.updatePostMedia(files, updatedPost);
        //clear the media list to repopulate with new set of media
        updatedPost.getMedia().clear();
        updatedPost.getMedia().addAll(mediaList);
        event.getEventPosts().add(updatedPost);
        eventServices.save(event);
        user.getCreatedPosts().add(updatedPost);
        userServices.save(user);
        return postRepository.save(updatedPost);
    }

}

