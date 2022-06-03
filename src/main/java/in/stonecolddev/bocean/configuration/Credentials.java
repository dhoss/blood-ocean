package in.stonecolddev.bocean.configuration;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Credentials {
    public abstract String accessKeyId();
    public abstract String secretAccessKey();
}