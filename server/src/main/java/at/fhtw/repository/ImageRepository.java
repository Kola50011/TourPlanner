package at.fhtw.repository;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class ImageRepository {
    private static final String BUCKET_NAME = "tourplanner";
    private static ImageRepository instance;
    private MinioClient client;

    private ImageRepository() {
        try {
            client = MinioClient.builder()
                    .endpoint("http://localhost:9000")
                    .credentials("minio", "minioKey123")
                    .build();

            boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            } else {
                log.info("Bucket 'tourplanner' already exists.");
            }

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error occurred: " + e);
        }
    }

    public static ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }

    public boolean hasImage(String key) {
        try {
            client.statObject(StatObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(key)
                    .build());
            return true;
        } catch (InvalidKeyException e) {
            return false;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equalsIgnoreCase("NoSuchKey")) {
                return false;
            }
            log.error("Exception during stat!", e);
        } catch (MinioException | IOException | NoSuchAlgorithmException e) {
            log.error("Exception during stat!", e);
        }
        return false;
    }

    public byte[] getImage(String key) {
        try {
            var res = client.getObject(GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(key)
                    .build());
            return res.readAllBytes();
        } catch (InvalidKeyException e) {
            return new byte[0];
        } catch (MinioException | IOException | NoSuchAlgorithmException e) {
            log.error("Exception during stat!", e);
        }
        return new byte[0];
    }

    public void putImage(String key, byte[] value) {
        try {
            var inputStream = new ByteArrayInputStream(value);
            client.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(key)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
        } catch (InvalidKeyException | MinioException | IOException | NoSuchAlgorithmException e) {
            log.error("Exception during put!", e);
        }
    }
}
