package at.fhtw.repository;

import at.fhtw.properties.MinioProperties;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Repository
public class ImageRepositoryImpl implements ImageRepository {
    private final MinioProperties minioProperties;
    private MinioClient client;

    private ImageRepositoryImpl(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
        try {
            client = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccesskey(), minioProperties.getSecretKey())
                    .build();

            boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
            } else {
                log.info("Bucket 'tourplanner' already exists.");
            }

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error occurred: " + e);
        }
    }

    @Override
    public boolean hasImage(String key) {
        try {
            client.statObject(StatObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
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

    @Override
    public byte[] getImage(String key) {
        try {
            var res = client.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
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

    @Override
    public void putImage(String key, byte[] value) {
        try {
            var inputStream = new ByteArrayInputStream(value);
            client.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(key)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
        } catch (InvalidKeyException | MinioException | IOException | NoSuchAlgorithmException e) {
            log.error("Exception during put!", e);
        }
    }
}
