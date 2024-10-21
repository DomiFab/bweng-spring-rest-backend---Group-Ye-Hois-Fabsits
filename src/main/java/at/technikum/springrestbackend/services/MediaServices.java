package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.*;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MediaServices {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private EventRepository eventRepository;


    public MediaModel find(String id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Event not found with id: " + id));
    }

    public MediaModel findByEventAndMedia(String mediaID, String eventID) {

        EventModel event = eventRepository.findById(eventID)
                .orElseThrow(() -> new EntityNotFoundException("No User found."));

        return mediaRepository.findByMediaIDAndEvent(mediaID, event)
                .orElseThrow(() -> new EntityExistsException("Event not found with id: " + eventID));
    }

    public List<MediaModel> findAll (){
        return mediaRepository.findAll();
    }

    public MediaModel save(MediaModel mediaModel){
        return mediaRepository.save(mediaModel);
    }

    public Set<MediaDTO> getFrontPicture (Set<MediaModel> pictures){


        return pictures.stream()
                .filter(MediaModel::isFrontPic) // Filter only front pictures
                .map(picture -> new MediaDTO(
                        picture.getMediaID(),
                        picture.getFileLocation(),
                        picture.getEvent().getEventID(),
                        new UserDTO(picture.getUploader().getUserID(), picture.getUploader().getUsername(),
                                picture.getUploader().getEmail(), picture.getUploader().getProfilePicture()),
                        picture.isFrontPic()))
                .collect(Collectors.toSet()); // Collect results into a Set
    }

    public Set<String> postToStringList(ForumPostModel postModel){
        ForumPostDTO post = new ForumPostDTO();

        for (MediaModel media : postModel.getMedia()){
            post.getMedia().add(media.getFileLocation());
        }
        return post.getMedia();
    }

    public Set<String> commentToStringList(ForumThreadModel comment){
        ForumThreadDTO post = new ForumThreadDTO();

        for (MediaModel media : comment.getMedia()){
            post.getMedia().add(media.getFileLocation());
        }
        return post.getMedia();
    }
}
