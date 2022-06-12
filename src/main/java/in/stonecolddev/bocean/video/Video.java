package in.stonecolddev.bocean.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import org.springframework.util.DigestUtils;
import org.springframework.util.MimeType;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.UUID;

@AutoValue
@JsonSerialize(as = Video.class)
@JsonDeserialize(builder = Video.Builder.class)
public abstract class Video {
  @Nullable
  @JsonProperty
  public abstract Integer id();

  @JsonProperty
  public abstract String fileName();

  @Memoized
  @JsonProperty
  public String fileNameHash() {
    int hash = ByteBuffer.wrap(
        DigestUtils.md5Digest(
            this.fileName().getBytes()
        )
    ).getInt();

    int mask = 255;
    var fileSeparator = File.separator;
    return String.format(
        "%s%02x%s%02x",
        fileSeparator,
        (hash & mask),
        fileSeparator,
        ((hash >> 8) & mask)
    );
//           + String.format("%02x", )
//           + fileSeparator
//           + String.format("%02x", (hash >> 8) & mask);
  }

  @JsonProperty
  public abstract String path();

  @Nullable
  @JsonProperty
  public abstract URL url();

  @Nullable
  @JsonProperty
  public abstract URL thumbnailUrl();

  @JsonProperty
  public abstract String description();

  @JsonProperty
  public abstract int fileSize();

  @JsonProperty
  public abstract MimeType mimeType();

  @Nullable
  @JsonProperty
  public abstract OffsetDateTime createdOn();

  @Nullable
  @JsonProperty
  public abstract OffsetDateTime updatedOn();

  public abstract Builder toBuilder();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_Video.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonProperty
    public abstract Builder id(int id);

    @JsonProperty
    public abstract Builder fileName(String fileName);

    @JsonProperty
    public abstract Builder path(String path);

    @JsonProperty
    public abstract Builder url(URL url);

    @JsonProperty
    public abstract Builder thumbnailUrl(URL thumbnailUrl);

    @JsonProperty
    public abstract Builder description(String description);

    @JsonProperty
    public abstract Builder fileSize(int fileSize);

    @JsonProperty
    public abstract Builder mimeType(MimeType mimeType);

    @JsonProperty
    public abstract Builder createdOn(OffsetDateTime createdOn);

    @JsonProperty
    public abstract Builder updatedOn(OffsetDateTime updatedOn);

    public abstract Video build();
  }

  @Memoized
  public String thumbnail() {
    return "%s_thumbnail.jpg".formatted(this.path());
  }
}