package at.technikum.springrestbackend.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${BUCKET_HOST}")
    private String bucketHost;

    @Value("${BUCKET_PORT}")
    private String bucketPort;

    @Value("${BUCKET_ACCESS_KEY}")
    private String accessKey;

    @Value("${BUCKET_ACCESS_SECRET}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(bucketHost + ":" + bucketPort)
                .credentials(accessKey, secretKey)
                .build();
    }
}
