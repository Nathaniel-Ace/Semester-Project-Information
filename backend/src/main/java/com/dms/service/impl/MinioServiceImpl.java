package com.dms.service.impl;

import com.dms.service.MinioService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
public class MinioServiceImpl implements MinioService {

    private static final Logger logger = LoggerFactory.getLogger(MinioServiceImpl.class); // Logger hinzufügen
    private final MinioClient minioClient;

    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void uploadFile(String bucketName, String objectName, InputStream stream, long size, String contentType) throws Exception {
        try {
            logger.info("Starting file upload to bucket: {}, object: {}", bucketName, objectName); // Log: Beginn des Uploads
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(stream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            logger.info("Successfully uploaded file: {} to bucket: {}", objectName, bucketName); // Log: Erfolgreicher Upload
        } catch (Exception e) {
            logger.error("Error occurred while uploading file: {} to bucket: {}", objectName, bucketName, e); // Log: Fehler beim Upload
            throw e; // Fehler weiterwerfen
        }
    }
}
