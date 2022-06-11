package in.stonecolddev.bocean.video;

import java.util.List;

public interface VideoRepository {

    List<Video> retrieve(int lastSeen, int pageSize);

    void create(Video video);
}