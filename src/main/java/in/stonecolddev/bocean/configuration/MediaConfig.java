package in.stonecolddev.bocean.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeType;

@Configuration
public class MediaConfig {

    @Value("${media.video-mime-type}")
    public MimeType videoMimeType;

}