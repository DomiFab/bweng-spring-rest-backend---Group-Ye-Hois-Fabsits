package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.model.*;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.CommentRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import at.technikum.springrestbackend.repository.UserRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private EventRepository eventRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

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

    public void updateProfilePicture(MultipartFile file, UserModel userModel) {
        // Delete old file
        if (userModel.getProfilePicture() != null || !userModel.getProfilePicture().isEmpty()) {
            deleteFile(userModel.getProfilePicture().replace("http://localhost:9000/files", ""));
        }
        if (file == null || file.isEmpty()) {
            return;
        }
        // Upload the new file
        String fileName = UUID.randomUUID().toString(); // Generate a unique file name
        String filePath = "/profile/" + fileName; // Define the path for the profile picture
        String fileURL = "http://localhost:9000/files" + filePath;

        try {
            this.uploadFile(filePath, file.getInputStream(), file.getContentType()); // Upload the file

            userModel.setProfilePicture(fileURL);
            userRepository.save(userModel);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
        }
    }

    public String generateSignedURL(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(60 * 60 * 5)
                            .build()
            );
        } catch (Exception e) {
            return "Error generating pre-signed URL: " + e.getMessage();
        }
    }

    public void updateFrontPicture(MultipartFile file, EventModel event) {
        try {
            if (file == null || file.isEmpty()) {
                removeOldFrontPictureData(event);
                return;
            }
            // Delete old Front Picture
            removeOldFrontPictureData(event);

            String fileName = UUID.randomUUID().toString(); // Generate unique file name
            String filePath = "/event/" + fileName;
            String fileURL = "http://localhost:9000/files" + filePath;
            this.uploadFile(filePath, file.getInputStream(), file.getContentType());

            MediaModel media = new MediaModel(fileName, fileURL, event, event.getCreator(), new CommentModel());
            mediaRepository.save(media);
            event.setEventPicture(fileURL);
            eventRepository.save(event);
            event.getCreator().getUploadedMedia().add(media);
            userRepository.save(event.getCreator());

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
        }
    }

    public void updateCommentMedia(List<MultipartFile> files, CommentModel commentModel,
                                   EventModel eventModel, UserModel author) {

        // Delete old Medias
        if (files == null || files.isEmpty()) {
            removeOldCommentPictures(eventModel, commentModel, author);
            return;
        }
        removeOldCommentPictures(eventModel, commentModel, author);

        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID().toString(); // Generate unique file name
                String filePath = "/event/uploads/" + fileName;
                String fileURL = "http://localhost:9000/files" + filePath;
                MediaModel media = new MediaModel(fileName, fileURL, eventModel, author, commentModel);

                uploadFile(filePath, file.getInputStream(), file.getContentType());

                commentModel.getMedia().add(media);
                eventModel.getGalleryPictures().add(media);
                author.getUploadedMedia().add(media);
                mediaRepository.save(media);
                commentRepository.save(commentModel);
                eventRepository.save(eventModel);
                userRepository.save(author);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading files " + file.getOriginalFilename(), e);
            }
        }
    }

    private void removeOldFrontPictureData(EventModel event) {

        if (event.getEventPicture() == null || event.getEventPicture().isEmpty()) {
            return;
        }

        MediaModel media = mediaRepository.findByFileURL(event.getEventPicture());

        mediaRepository.deleteByFileURL(event.getEventPicture());
        event.getCreator().getUploadedMedia().remove(media);
        deleteFile(event.getEventPicture().replace("http://localhost:9000/files", ""));

        event.setEventPicture("");

        eventRepository.save(event);
        userRepository.save(event.getCreator());

    }

    private void removeOldCommentPictures(EventModel event, CommentModel comment, UserModel author) {

        if (comment.getMedia() == null || comment.getMedia().isEmpty()) {
            return;
        }

        for (MediaModel media : comment.getMedia()) {

            event.getGalleryPictures().remove(media);
            comment.getMedia().remove(media);
            author.getUploadedMedia().remove(media);

            deleteFile(media.getFileURL().replace("http://localhost:9000/files", ""));
        }

        mediaRepository.deleteAllByComment(comment);
        eventRepository.save(event);
        commentRepository.save(comment);
        userRepository.save(author);

    }
}
