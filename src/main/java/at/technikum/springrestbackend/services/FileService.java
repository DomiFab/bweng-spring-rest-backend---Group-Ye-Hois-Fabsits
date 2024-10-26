package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.model.*;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.CommentRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class FileService {

    private final MinioClient minioClient;
    @Autowired
    private MediaRepository mediaRepository;
    @Value("${BUCKET_NAME}")
    private String bucketName;
    @Autowired
    private UserServices userServices;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommentRepository postRepository;

    public FileService(@Value("${BUCKET_HOST}") String bucketHost,
                       @Value("${BUCKET_PORT}") int bucketPort,
                       @Value("${BUCKET_ACCESS_KEY}") String accessKey,
                       @Value("${BUCKET_ACCESS_SECRET}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(bucketHost + ":" + bucketPort)
                .credentials(accessKey, secretKey)
                .build();

        // Ensure the bucket exists
        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() {
        try {
            // Check if the bucket exists
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!exists) {
                // Create the bucket if it does not exist
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (MinioException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String objectName, InputStream inputStream, String contentType) throws Exception {
        // Make sure the bucket exists, or create it
        if (!minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build())) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }

        // Upload the file
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build());
    }

    public InputStream downloadFile(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    public void deleteFile(String fileLocation) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileLocation)
                    .build());

            System.out.println("File " + fileLocation + " successfully deleted from MinIO.");
        } catch (MinioException e) {
            System.err.println("Error occurred while deleting file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String uploadProfilePicture(String userID, MultipartFile file, String authUser) {
        // Validate the input file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty.");
        }
        if (!userServices.find(userID).getUsername().equals(authUser)){
            throw new AccessDeniedException("You do not have the authority to change this Profile Picture");
        }
        // Find the user by ID
        UserModel user = userServices.find(userID); // Assuming you have this method in userServices
        if (user == null) {
            throw new EntityNotFoundException("User not found with ID: " + userID);
        }

        // Upload the file
        String fileName = UUID.randomUUID().toString(); // Generate a unique file name
        String filePath = "/profile/uploads/" + fileName; // Define the path for the profile picture

        try {
            this.uploadFile(filePath, file.getInputStream(), file.getContentType()); // Upload the file
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
        }

        // Update the user's profile picture
        user.setProfilePicture(filePath);
        userServices.save(user); // Save the updated user

        return filePath; // Return the file path if needed
    }

//    public List<MediaModel> uploadMediaToComment(List<MultipartFile> files, ForumThreadModel comment, String username){
//        if (username == null || username.isEmpty()) {
//            throw new AccessDeniedException("You need to be logged in to comment.");
//        }
//        // Find the user who is posting the comment
//        UserModel user = userServices.findByUsername(username);
//        List<MediaModel> mediaList = new ArrayList<>();
//        for (MultipartFile file : files) {
//            try {
//                String fileName = UUID.randomUUID().toString();
//                String filePath = "/comment/uploads/" + fileName;
//                this.uploadFile(filePath, file.getInputStream(), file.getContentType());
//
//                CommentModel post = postRepository.findById(comment.getPost().getId())
//                        .orElseThrow(() -> new EntityNotFoundException("No post found"));
//
//                EventModel event = eventRepository.findById(post.getEvent().getEventID())
//                        .orElseThrow(() -> new EntityNotFoundException("No event found"));
//
//                MediaModel media = new MediaModel(fileName, filePath, event, user, comment);
//                mediaRepository.save(media);
//                mediaList.add(media);
//            } catch (Exception e) {
//                throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
//            }
//        }
//        comment.getMedia().addAll(mediaList);
//        userServices.saveComment(user, mediaList, comment);
//        commentRepository.save(comment);  // Save the updated comment with media
//        return mediaList;
//    }
//
//    public List<MediaModel> updateCommentMedia(List<MultipartFile> files, ForumThreadModel comment){
//        List<MediaModel> mediaModelList = new ArrayList<>();
//        if (!files.isEmpty()) {
//            // First, remove the old media associated with the post
//            List<MediaModel> existingMedia = comment.getMedia().stream().toList();
//            for (MediaModel media : existingMedia) {
//                this.deleteFile(media.getFileLocation()); // Delete the file from MinIO
//                mediaRepository.delete(media); // Delete the media record from the DB
//            }
//            // Now, upload new media files
//            for (MultipartFile file : files) {
//                try {
//                    String fileName = UUID.randomUUID().toString(); // Generate unique file name
//                    String filePath = "/comment/uploads/" + fileName;
//                    this.uploadFile(filePath, file.getInputStream(), file.getContentType());
//
//                    CommentModel post = postRepository.findById(comment.getPost().getId())
//                            .orElseThrow(() -> new EntityNotFoundException("No post found"));
//
//                    EventModel event = eventRepository.findById(post.getEvent().getEventID())
//                            .orElseThrow(() -> new EntityNotFoundException("No event found"));
//                    MediaModel newMedia = new MediaModel(fileName, filePath, event, comment.getAuthor(), comment);
//                    mediaRepository.save(newMedia);
//                    mediaModelList.add(newMedia);
//                } catch (Exception e) {
//                    throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
//                }
//            }
//        }
//        return mediaModelList;
//    }
//
//    public List<MediaModel> updatePostMedia(List<MultipartFile> files, CommentModel post){
//        List<MediaModel> mediaModelList = new ArrayList<>();
//        if (!files.isEmpty()) {
//            // First, remove the old media associated with the post
//            List<MediaModel> existingMedia = post.getMedia().stream().toList();
//            for (MediaModel media : existingMedia) {
//                this.deleteFile(media.getFileLocation()); // Delete the file from MinIO
//                mediaRepository.delete(media); // Delete the media record from the DB
//            }
//            // Now, upload new media files
//            for (MultipartFile file : files) {
//                try {
//                    String fileName = UUID.randomUUID().toString(); // Generate unique file name
//                    String filePath = "/post/uploads/" + fileName;
//                    this.uploadFile(filePath, file.getInputStream(), file.getContentType());
//                    MediaModel newMedia = new MediaModel(fileName, filePath, post.getEvent(), post.getAuthor(), post);
//                    mediaRepository.save(newMedia);
//                    mediaModelList.add(newMedia);
//                } catch (Exception e) {
//                    throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
//                }
//            }
//        }
//        return mediaModelList;
//    }
//
//    public List<MediaModel> handleCreateEventUpload(List<MultipartFile> files, EventModel event, String username) {
//        if (!event.getCreator().getUsername().equals(username) &&
//                !userServices.findByUsername(username).isAdmin()) {
//            throw new AccessDeniedException("You do not have permission to update this event.");
//        }
//        return uploadFiles(files, event, event.getCreator(), true);
//    }
//
//    public List<MediaModel> uploadMediaToEvent(List<MultipartFile> files, String eventID, String userName) {
//        EventModel event = eventRepository.findById(eventID)
//                .orElseThrow(() -> new EntityNotFoundException("No event found"));
//
//        UserModel user = userServices.findByUsername(userName);
//        return uploadFiles(files, event, user, false);
//    }
//
//    public List<MediaModel> updateFrontPicture(List<MultipartFile> files, EventModel event) {
//        List<MediaModel> mediaModelList = new ArrayList<>();
//
//        if (!files.isEmpty()) {
//            // Handle removal of the current front picture if it exists
//            Optional<MediaModel> currentFrontPicture = event.getGalleryPictures().stream()
//                    .filter(MediaModel::isFrontPic) // Find the current front picture
//                    .findFirst();
//
//            currentFrontPicture.ifPresent(oldFrontPicture -> {
//                oldFrontPicture.setFrontPic(false); // Set old front picture as not front
//                this.deleteFile(oldFrontPicture.getFileLocation());
//                event.getGalleryPictures().remove(oldFrontPicture);
//            });
//
//            // Upload new pictures
//            mediaModelList.addAll(uploadFiles(files, event, event.getCreator(), true));
//        }
//        return mediaModelList;
//    }
//
//    private List<MediaModel> uploadFiles(List<MultipartFile> files, EventModel event, UserModel user, boolean isFrontPic) {
//        List<MediaModel> mediaList = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            try {
//                String fileName = UUID.randomUUID().toString(); // Generate unique file name
//                String filePath = "/event/uploads/" + fileName;
//                this.uploadFile(filePath, file.getInputStream(), file.getContentType());
//
//                MediaModel media = new MediaModel(fileName, filePath, event, user, isFrontPic);
//                mediaRepository.save(media);
//                mediaList.add(media);
//            } catch (Exception e) {
//                throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
//            }
//        }
//
//        user.getUploadedMedia().addAll(mediaList);
//        userServices.save(user);
//        return mediaList;
//    }
}
