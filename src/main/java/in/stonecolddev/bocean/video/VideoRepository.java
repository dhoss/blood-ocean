package in.stonecolddev.bocean.video;

import java.util.List;

public interface VideoRepository {

    public List<ImmutableVideo> retrieve(int lastSeen, int pageSize);
}