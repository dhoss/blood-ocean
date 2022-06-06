package in.stonecolddev.bocean.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;

@Configuration
public class AwsConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    @Value("${cloud.aws.s3.signature-duration}")
    public Duration signatureDurationMinutes;

    @Value("${cloud.aws.s3.video-bucket}")
    public String videoBucket;

    private AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(this.accessKey, this.secretKey)
        );
    }

    private URI endpoint() {
        return URI.create(this.endpoint);
    }

    private Region region() {
        return Region.of(this.region);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(endpoint())
                .region(region())
                .credentialsProvider(awsCredentialsProvider())
                .build();

    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(endpoint())
                .region(region())
                .credentialsProvider(awsCredentialsProvider())
                .build();
    }
}