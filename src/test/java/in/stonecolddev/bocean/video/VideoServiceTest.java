package in.stonecolddev.bocean.video;

import in.stonecolddev.bocean.configuration.AwsConfig;
import in.stonecolddev.bocean.configuration.MediaConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.Mockito.mock;

@ActiveProfiles("unit-test")
@Tag("unit-test")
public class VideoServiceTest {


  private final S3Presigner s3Presigner = mock(S3Presigner.class);

  private final AwsConfig awsConfig = mock(AwsConfig.class);

  private final MediaConfig mediaConfig = mock(MediaConfig.class);

  private final VideoRepository videoRepository = mock(VideoRepository.class);

  @Test
  public void upload() throws IOException {

    var videoService = new VideoService(s3Presigner, awsConfig, videoRepository, mediaConfig);

    // TODO: move somewhere common
    var clipFile = new MockMultipartFile(
        "video",
        "apex-clip.mp4",
        "video/mp4",
        new FileInputStream("src/test/resources/apex-test.mp4"));



  }
}
