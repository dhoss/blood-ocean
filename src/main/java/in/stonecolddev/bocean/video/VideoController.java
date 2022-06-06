package in.stonecolddev.bocean.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping
    public Integer uploadToS3(@RequestParam("video") MultipartFile video) throws IOException {
        return videoService.upload(video);
    }

    @GetMapping
    public List<Video> list(
            @RequestParam(defaultValue = "0") int lastSeen,
            @RequestParam(defaultValue = "50") int pageSize) {

        return this.videoService.retrieve(lastSeen, pageSize);
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