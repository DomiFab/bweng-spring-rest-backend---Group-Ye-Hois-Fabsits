package at.technikum.springrestbackend.services;


import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.mapper.MediaMapper;
import at.technikum.springrestbackend.model.*;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MediaServices {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MediaMapper mediaMapper;
    @Autowired
    private EventRepository eventRepository;


    public MediaModel find(String id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Event not found with id: " + id));
    }

    public List<MediaModel> findAll (){
        return mediaRepository.findAll();
    }

    public List<MediaDTO> getMediaFromUser (UserModel userModel){

        List<MediaDTO> mediaList = new ArrayList<>();

        for (MediaModel media : userModel.getUploadedMedia()){
            mediaList.add(mediaMapper.toDTO(media));
        }

        return mediaList;
    }

    public MediaModel findByEventAndMedia(String mediaID, String eventID) {

        EventModel event = eventRepository.findById(eventID)
                .orElseThrow(() -> new EntityNotFoundException("No User found."));

        return mediaRepository.findByMediaIDAndEvent(mediaID, event)
                .orElseThrow(() -> new EntityExistsException("Event not found with id: " + eventID));
    }

    public MediaModel save(MediaModel mediaModel){
        return mediaRepository.save(mediaModel);
    }



}
