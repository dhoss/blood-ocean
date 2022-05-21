package in.stonecolddev.trickle.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api/videos",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class VideoController {

    private final Logger log = LoggerFactory.getLogger(VideoController.class);


    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("")
    public List<Video> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int pageSize) {
        log.debug("*** INSIDE LIST");
        return this.videoService.retrieve(page, pageSize);
    }

    @GetMapping("/{path}")
    public Video find(@PathVariable String path) {
        return ImmutableVideo.builder()
                .id(1)
                .fileName("fart")
                .fileNameHash("frrrrt")
                .description("foart!")
                .fileSize(1)
                .mimeType(MimeType.valueOf("video/mp4"))
                .path(path)
                .createdOn(OffsetDateTime.now())
                .updatedOn(OffsetDateTime.now())
                .build();
    }
}