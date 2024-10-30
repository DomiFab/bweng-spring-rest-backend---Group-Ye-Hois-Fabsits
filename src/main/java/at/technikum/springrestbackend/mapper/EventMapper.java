package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.CreateEventDTO;
import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventMapper {

    @Autowired
    private UserMapper userMapper;

    public DisplayEventDTO toDisplayDTO (EventModel eventModel, String userID) {

        boolean isCreator = eventModel.getCreator().getUserID().equals(userID);

        return new DisplayEventDTO(
                eventModel.getEventID(),
                eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventStatus(),
                eventModel.getEventDescription(),
                eventModel.getEventPicture(),
                eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                isCreator,
                (long) eventModel.getAttendingUsers().size()
        );
    }

    public DisplayEventDTO toNoAuthDTO (EventModel eventModel) {

        return new DisplayEventDTO(
                eventModel.getEventID(),
                eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventStatus(),
                eventModel.getEventDescription(),
                eventModel.getEventPicture(),
                eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                false,
                (long) eventModel.getAttendingUsers().size()
        );
    }

    //creating event
    public EventModel toEntity(CreateEventDTO eventDTO, UserModel creator) {
        //DataBank entry requires the id as a primary key
        return new EventModel(
                UUID.randomUUID().toString(),
                eventDTO.getEventName(),
                eventDTO.getEventLocation(),
                eventDTO.getEventDescription(),
                creator,
                eventDTO.getEventStatus()
        );
    }

    //for full event page
    public CreateEventDTO toFullDTO(EventModel eventModel) {


//        CreateEventDTO displayedEvent = new CreateEventDTO();
//        for (CommentModel post : eventModel.getEventPosts()){
//            displayedEvent.getEventPosts().add(forumPostMapper.toFullDTO(post));
//        }
//        for (UserModel user : eventModel.getAttendingUsers()){
//            displayedEvent.getAttendingUsers().add(new UserDTO(
//                    user.getUserID(), user.getUsername(),
//                    user.getEmail(), user.getProfilePicture()
//                    )
//            );
//        }
//        for (MediaModel media : eventModel.getGalleryPictures()){
//            displayedEvent.getGalleryPictures().add(mediaMapper.toSimpleDTO(media));
//        }

        return new CreateEventDTO(
//                eventModel.getEventID(), eventModel.getEventName(),
//                eventModel.getEventLocation(),
//                eventModel.getEventDate(), eventModel.getEventShortDescription(),
//                eventModel.getEventLongDescription(), eventModel.isDeleted(),
//                userMapper.toSimpleDTO(eventModel.getCreator()),
//                displayedEvent.getAttendingUsers(), displayedEvent.getGalleryPictures(),
//                displayedEvent.getEventPosts()
        );
    }


}
