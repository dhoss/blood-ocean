package in.stonecolddev.bocean.configuration;

import org.immutables.value.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "video")
@Value.Immutable
public abstract class VideoStore {
    public abstract String endpoint();
    public abstract String region();
    public abstract Credentials credentials();
}