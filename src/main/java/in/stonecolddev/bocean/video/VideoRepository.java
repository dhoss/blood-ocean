package in.stonecolddev.bocean.video;

import java.util.List;

public interface VideoRepository {

    List<ImmutableVideo> retrieve(int lastSeen, int pageSize);

    void create(ImmutableVideo video);
}