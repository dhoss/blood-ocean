package in.stonecolddev.bocean.video;

import in.stonecolddev.bocean.configuration.AwsConfig;
import in.stonecolddev.bocean.configuration.MediaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

  private final Logger log = LoggerFactory.getLogger(VideoService.class);


  private final S3Presigner s3Presigner;

  private final AwsConfig awsConfig;

  private final MediaConfig mediaConfig;

  private final VideoRepository videoRepository;

  public VideoService(
      S3Presigner s3Presigner,
      AwsConfig awsConfig,
      VideoRepository videoRepository,
      MediaConfig mediaConfig
  ) {
    this.s3Presigner = s3Presigner;
    this.awsConfig = awsConfig;
    this.videoRepository = videoRepository;
    this.mediaConfig = mediaConfig;
  }

  // TODO: directory hashing (https://medium.com/eonian-technologies/file-name-hashing-creating-a-hashed-directory-structure-eabb03aa4091)
  // TODO: mark and sweep resized images (https://www.educative.io/courses/a-quick-primer-on-garbage-collection-algorithms/jy6v)

  public List<Video> retrieve(int lastSeen, int pageSize) {
    var videos = new ArrayList<Video>();

    for (var video : videoRepository.retrieve(lastSeen, pageSize)) {
      // TODO: think about adding a table that tracks a video/thumbnail id and a presigned url expiration timestamp
      //       only generate a new presigned URL if the current one has expired
      videos.add(
          video.toBuilder().url(generatePresignedUrl(video.fileNameHash()))
               .thumbnailUrl(generatePresignedUrl(video.thumbnail())).build());
    }

    return videos;
  }

  public Video upload(MultipartFile videoFile) throws IOException {
    var videoMimeType = mediaConfig.videoMimeType.toString();

    Video video = Video.builder()
                       .description("test")
                       .fileSize((int)videoFile.getSize())
                       .fileName(videoFile.getOriginalFilename())
                       .mimeType(mediaConfig.videoMimeType)
                       .build();

    log.info("beginning upload on {} size {} file name hash {}", video.fileName(), video.fileSize(), video.fileNameHash());

    HttpURLConnection connection =
        (HttpURLConnection) s3Presigner.presignPutObject(
                                           PutObjectPresignRequest.builder().putObjectRequest(
                                                                      PutObjectRequest.builder()
                                                                                      .bucket(awsConfig.videoBucket)
                                                                                      .key(video.fileNameHash())
                                                                                      .contentType(videoMimeType)
                                                                                      .build())
                                                                  .signatureDuration(awsConfig.signatureDurationMinutes)
                                                                  .build())
                                       .url()
                                       .openConnection();
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", videoMimeType);
    connection.setRequestMethod("PUT");
    connection.setFixedLengthStreamingMode(video.fileSize());
    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
    out.write(videoFile.getBytes());
    out.flush();
    out.close();
    connection.disconnect();

    videoRepository.create(video);

    return video;

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