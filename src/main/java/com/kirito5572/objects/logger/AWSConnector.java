package com.kirito5572.objects.logger;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AWSConnector {

    private static final Regions clientRegion = Regions.AP_NORTHEAST_2;
    private static final String bucketName = "ritobotv2";

    public void S3UploadObject(File file, String messageId) {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).withCredentials(new EnvironmentVariableCredentialsProvider()).build();
            PutObjectRequest request = new PutObjectRequest(bucketName, messageId, file);
            ObjectMetadata metadata = new ObjectMetadata();
            request.setMetadata(metadata);
            request.setStorageClass(StorageClass.StandardInfrequentAccess);
            s3Client.putObject(request);
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

    }

    public File S3DownloadObject(String messageId) {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).withCredentials(new EnvironmentVariableCredentialsProvider()).build();
            GetObjectRequest request = new GetObjectRequest(bucketName, messageId);
            S3Object object = s3Client.getObject(request);
            ObjectMetadata metadata = object.getObjectMetadata();
            InputStream inputStream = object.getObjectContent();
            Path path = Files.createTempFile(messageId, "." + metadata.getContentType().split("/")[1]);

            try (FileOutputStream out = new FileOutputStream(path.toFile())){
                try {
                    byte[] buffer = new byte[1024];

                    int len;
                    while((len = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } catch (Throwable e) {
                    try {
                        out.close();
                    } catch (Throwable e1) {
                        e.addSuppressed(e1);
                    }
                    throw e;
                }
            } catch (Exception ignored) {
                return null;
            }
            return path.toFile();
        } catch (IOException | SdkClientException ignored) {
            return null;
        }
    }
}
