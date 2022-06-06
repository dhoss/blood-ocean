package in.stonecolddev.bocean.video;

import in.stonecolddev.bocean.configuration.AwsConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
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

       // String bucketName = awsConfig.videoBucket;
       // for (var os : s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents()) {
       //     if (!os.key().contains("thumbnail")) {
       //         videos.add(
       //                 ImmutableVideo.builder()
       //                         .id(1)
       //                         .fileName("fixed-trimmed-end-sample.mp4")
       //                         .fileNameHash("dark-souls-hash")
       //                         .path(os.key())
       //                         .url(s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
       //                                 .signatureDuration(Duration.ofMinutes(10))
       //                                 .getObjectRequest(GetObjectRequest.builder().key(os.key()).bucket(bucketName).build())
       //                                 .build()).url())
       //                         .thumbnailUrl(s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
       //                                 .signatureDuration(Duration.ofMinutes(Integer.parseInt(awsConfig.signatureDurationMinutes)))
       //                                 .getObjectRequest(GetObjectRequest.builder().key(String.format("%s_thumbnail.jpg", os.key())).bucket(bucketName).build())
       //                                 .build()).url())
       //                         .description("dark-souls")
       //                         .fileSize(Math.toIntExact(os.size()))
       //                         .mimeType(MimeType.valueOf("video/mp4"))
       //                         .createdOn(OffsetDateTime.now())
       //                         .updatedOn(OffsetDateTime.now())
       //                         .build()
       //         );
       //     }
       // }

        return videos;
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