package in.stonecolddev.bocean.video;

import java.util.List;
import java.util.Optional;

public interface VideoRepository {

    List<Video> retrieve(int lastSeen, int pageSize);

    void create(Video video);

    Optional<Video> find(String fileNameHash);
}