package com.moviebook.qrcode.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Service
public class S3BucketService {
    private static final Logger LOG = LoggerFactory.getLogger(S3BucketService.class);


    @Autowired
    private AmazonS3 amazonS3;

    @Value("${amazonProperties.bucketName}")
    private String s3BucketName;

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOG.error("Error {} occurred while converting the multipart file", e.getLocalizedMessage());
        }
        return file;
    }

    // @Async annotation ensures that the method is executed in a different thread

    @Async
    public S3ObjectInputStream findByName(String fileName) {
        LOG.info("Downloading file with name {}", fileName);
        return amazonS3.getObject(s3BucketName, fileName).getObjectContent();
    }

    @Async
    public String save(final String multipartFile,String requestId) {
        try {
//            final File file = convertMultiPartFileToFile(multipartFile);
            final String fileName = requestId+".png";
            byte[] bI = Base64.decodeBase64((multipartFile.substring(multipartFile.indexOf(",") + 1)).getBytes());

            InputStream fis = new ByteArrayInputStream(bI);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bI.length);
            metadata.setContentType("image/png");
            metadata.setCacheControl("public, max-age=31536000");

            LOG.info("Uploading file with name {}", fileName);
            final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, fis,metadata );
            amazonS3.putObject(putObjectRequest);
            URL s3Url = amazonS3.getUrl(s3BucketName, fileName);

//            Files.delete(file.toPath()); // Remove the file locally created in the project folder
            return s3Url.toString();
        } catch (AmazonServiceException e) {
            LOG.error("Error {} occurred while uploading file", e.getLocalizedMessage());
        }
//        catch (IOException ex) {
//            LOG.error("Error {} occurred while deleting temporary file", ex.getLocalizedMessage());
//        }

  return "Success";
    }


}
