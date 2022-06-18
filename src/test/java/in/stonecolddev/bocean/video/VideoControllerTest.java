package in.stonecolddev.bocean.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MimeType;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoController.class)
@ActiveProfiles("unit-test")
@Tag("unit-test")
public class VideoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private VideoService videoService;

  private static final ObjectMapper objectMapper = new ObjectMapper()
      .registerModules(
          new ProblemModule(),
          new ConstraintViolationProblemModule(),
          new JavaTimeModule(),
          new Jdk8Module())
      .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

  @Test
  public void list() throws Exception {
    List<Video> videos = List.of(
        Video.builder()
             .id(1)
             .fileName("apex-clip.mp4")
             .fileSize(1024)
             .description("test")
             .mimeType(MimeType.valueOf("video/mp4"))
             .updatedOn(OffsetDateTime.now())
             .createdOn(OffsetDateTime.now())
             .build()
    );

    when(videoService.retrieve(0, 50)).thenReturn(videos);

    mockMvc.perform(
        get("/api/videos")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(videos)));

    verify(videoService, times(1)).retrieve(0, 50);
  }
}