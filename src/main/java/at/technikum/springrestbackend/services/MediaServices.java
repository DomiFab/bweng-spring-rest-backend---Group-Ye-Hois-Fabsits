package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.ForumThreadModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaServices {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EventServices eventServices;

    public MediaModel find(UUID id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Media not found with id: " + id));
    }

    public MediaModel findByEventAndMedia(UUID mediaID, UUID eventID) {
        return mediaRepository.findByMediaIDAndEvent(mediaID, eventServices.find(eventID))
                .orElseThrow(() -> new EntityExistsException("Media not found with id: " + mediaID + " for event id: " + eventID));
    }

    public List<MediaModel> findAll() {
        return mediaRepository.findAll();
    }

    public MediaModel save(MediaModel mediaModel) {
        return mediaRepository.save(mediaModel);
    }

    public void delete(MediaModel mediaModel) {
        mediaRepository.delete(mediaModel);
    }

    public Set<MediaDTO> getFrontPicture(Set<MediaModel> pictures) {
        return pictures.stream()
                .filter(MediaModel::isFrontPic) // Filter only front pictures
                .map(picture -> new MediaDTO(
                        picture.getMediaID(),
                        picture.getFileLocation(),
                        picture.getEvent() != null ? picture.getEvent().getEventID() : null,
                        userMapper.toSimpleDTO(picture.getUploader()),
                        picture.isFrontPic()))
                .collect(Collectors.toSet()); // Collect results into a Set
    }

    public Set<String> postToStringList(ForumPostModel postModel) {
        ForumPostDTO post = new ForumPostDTO();

        for (MediaModel media : postModel.getMedia()) {
            post.getMedia().add(media.getFileLocation());
        }
        return post.getMedia();
    }

    public Set<String> commentToStringList(ForumThreadModel comment) {
        ForumThreadDTO post = new ForumThreadDTO();

        for (MediaModel media : comment.getMedia()) {
            post.getMedia().add(media.getFileLocation());
        }
        return post.getMedia();
    }
}