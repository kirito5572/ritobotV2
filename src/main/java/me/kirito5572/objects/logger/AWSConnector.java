package me.kirito5572.objects.logger;

import org.apache.commons.io.FilenameUtils;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AWSConnector {

    private static final Region clientRegion = Region.AP_NORTHEAST_2;
    private static final String bucketName = "ritobotv2";
    private static final DefaultCredentialsProvider provider = DefaultCredentialsProvider.builder().build();

    public void S3UploadObject(File file, String messageId) {
        try(S3Client s3Client = S3Client.builder()
                .credentialsProvider(provider)
                .region(clientRegion)
                .build()){
            Map<String, String> metadata = new HashMap<>();
            metadata.put("extension", FilenameUtils.getExtension(file.getName()));

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(messageId)
                    .metadata(metadata)
                    .build();

            s3Client.putObject(putObjectRequest, file.toPath());
        }
    }

    public File S3DownloadObject(String messageId) {
        try(S3Client s3Client = S3Client.builder()
                .credentialsProvider(provider)
                .region(clientRegion)
                .build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(messageId)
                    .build();
            Path path;
            HeadObjectRequest objectRequest = HeadObjectRequest.builder()
                    .key(messageId)
                    .bucket(bucketName)
                    .build();
            String type = s3Client.headObject(objectRequest).contentType();
            path = Paths.get(messageId + "." + type.split("/")[1]);
            s3Client.getObject(getObjectRequest, path);
            return path.toFile();
        }
    }
}
