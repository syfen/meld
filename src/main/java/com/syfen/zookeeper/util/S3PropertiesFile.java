package com.syfen.zookeeper.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * User: ToneD
 * Created: 25/08/13 12:27 PM
 */
public class S3PropertiesFile {

    private static Logger log = LoggerFactory.getLogger(S3PropertiesFile.class);
    private AmazonS3Client s3Client;
    private BasicAWSCredentials basicAWSCredentials;
    private Properties props;

    public S3PropertiesFile(String bucket, String key, String awsAccessKey, String awsSecretKey) {

        // setup S3 client
        basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        s3Client = new AmazonS3Client(basicAWSCredentials);

        // get file
        S3Object object = s3Client.getObject(bucket, key);

        // load properties
        props = new Properties();
        try {
            props.load(object.getObjectContent());
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String get(String propName) {

        if(!propName.isEmpty() && props.containsKey(propName)) {
            return props.getProperty(propName);
        }
        else {
            return null;
        }
    }
}