package in.stonecolddev.bocean.video;

import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class VideoService {

    // TODO: directory hashing (https://medium.com/eonian-technologies/file-name-hashing-creating-a-hashed-directory-structure-eabb03aa4091)
    // TODO: mark and sweep resized images (https://www.educative.io/courses/a-quick-primer-on-garbage-collection-algorithms/jy6v)

    public List<Video> retrieve(int page, int pageSize) {

        return List.of(ImmutableVideo.builder()
                .id(1)
                .fileName("dark-souls.mp4")
                .fileNameHash("dark-souls-hash")
                .path("/path/to/dark-souls-hash")
                .description("dark-souls")
                .fileSize(1024)
                .mimeType(MimeType.valueOf("video/mp4"))
                .createdOn(OffsetDateTime.now())
                .updatedOn(OffsetDateTime.now())
                .build());
    }

}