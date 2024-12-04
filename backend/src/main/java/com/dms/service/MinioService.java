package com.dms.service;

import java.io.InputStream;

public interface MinioService {
    void uploadFile(String bucketName, String objectName, InputStream stream, long size, String contentType) throws Exception;
}