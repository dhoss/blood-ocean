package in.stonecolddev.bocean.video;

import in.stonecolddev.bocean.configuration.AwsConfig;
import in.stonecolddev.bocean.configuration.MediaConfig;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
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

  private void uploadFile(String key, String mimeType, InputStream data) throws IOException { //,byte[] data) throws IOException {

    HttpURLConnection connection =
        (HttpURLConnection) s3Presigner.presignPutObject(
                                           PutObjectPresignRequest.builder().putObjectRequest(
                                                                      PutObjectRequest.builder()
                                                                                      .bucket(awsConfig.videoBucket)
                                                                                      .key(key)
                                                                                      .contentType(mimeType)
                                                                                      .build())
                                                                  .signatureDuration(awsConfig.signatureDurationMinutes)
                                                                  .build())
                                       .url()
                                       .openConnection();
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", mimeType);
    connection.setRequestMethod("PUT");
    log.debug("**** INPUTSTREAM AVAILABLE {}", data.available());
    connection.setFixedLengthStreamingMode(data.available());

    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
    data.transferTo(out);
   // out.write(data);
    out.flush();
    out.close();

    connection.disconnect();
  }

  private void generateThumbnail(URI video) throws IOException, JCodecException {
    Picture picture = FrameGrab.getFrameFromFile(new File(video), 1);

    BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
    ImageIO.write(bufferedImage, "jpg", new File("frame42.png"));
  }

  public Video upload(MultipartFile videoFile) throws IOException {

    Video video = Video.builder()
                       .description("test")
                       .fileSize((int)videoFile.getSize())
                       .fileName(videoFile.getOriginalFilename())
                       .mimeType(mediaConfig.videoMimeType)
                       .build();

    log.info("beginning upload on {} size {} file name hash {}", video.fileName(), video.fileSize(), video.fileNameHash());

    uploadFile(video.fileNameHash(), mediaConfig.videoMimeType.getType(), videoFile.getInputStream());//videoFile.getBytes());

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