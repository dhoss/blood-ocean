package in.stonecolddev.bocean.video;

import in.stonecolddev.bocean.configuration.AwsConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    private final S3Presigner s3Presigner;

    private final AwsConfig awsConfig;

    private final VideoRepository videoRepository;

    public VideoService(S3Presigner s3Presigner,
                        AwsConfig awsConfig,
                        VideoRepository videoRepository) {
        this.s3Presigner = s3Presigner;
        this.awsConfig = awsConfig;
        this.videoRepository = videoRepository;
    }

    // TODO: directory hashing (https://medium.com/eonian-technologies/file-name-hashing-creating-a-hashed-directory-structure-eabb03aa4091)
    // TODO: mark and sweep resized images (https://www.educative.io/courses/a-quick-primer-on-garbage-collection-algorithms/jy6v)

    public List<Video> retrieve(int lastSeen, int pageSize) {
        var videos = new ArrayList<Video>();

        for (var video : videoRepository.retrieve(lastSeen, pageSize)) {
            videos.add(
                video.withUrl(generatePresignedUrl(video.path()))
                     .withThumbnailUrl(generatePresignedUrl(video.thumbnail())));
        }

        return videos;
    }

    public Integer upload(MultipartFile video) throws IOException {

        HttpURLConnection connection =
            (HttpURLConnection) s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder().putObjectRequest(
                    PutObjectRequest.builder()
                                    .bucket("trickle-media")
                                    .key(video.getOriginalFilename())
                                    .contentType("video/mp4")
                                    .build())
                                       .signatureDuration(Duration.ofMinutes(10))
                                       .build())
                                           .url()
                                           .openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "video/mp4");
        connection.setRequestMethod("PUT");
        ByteArrayOutputStream out = (ByteArrayOutputStream) connection.getOutputStream();
        out.write(video.getBytes());
        out.close();

        return connection.getResponseCode();

    }

    private URL generatePresignedUrl(String path) {
       return s3Presigner.presignGetObject(
           GetObjectPresignRequest.builder()
                                  .signatureDuration(awsConfig.signatureDurationMinutes)
                                  .getObjectRequest(
                                      GetObjectRequest.builder()
                                                      .key(path)
                                                      .bucket(awsConfig.videoBucket)
                                                      .build())
                                  .build()).url();
    }
}