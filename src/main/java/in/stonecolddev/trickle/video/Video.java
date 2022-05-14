package in.stonecolddev.trickle.video;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.springframework.util.MimeType;

import java.time.OffsetDateTime;

@Value.Immutable
@JsonSerialize(as = ImmutableVideo.class)
@JsonDeserialize(as = ImmutableVideo.class)
public abstract class Video {
    public abstract int id();
    public abstract String fileName();
    public abstract String fileNameHash();
    public abstract String path();
    public abstract String description();
    public abstract int fileSize();
    public abstract MimeType mimeType();
    public abstract OffsetDateTime createdOn();
    public abstract OffsetDateTime updatedOn();

    @Value.Derived
    public String thumbnail() {
        return "%s_thumbnail".formatted(this.path());
    }
}