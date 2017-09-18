package org.collectiveone.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * This class is a client for interacting with Amazon S3 bucket resources.
 *
 * @author kbastani
 */
@Component
public class AmazonS3Template {

    private String accessKeyId;
    private String accessKeySecret;
    
    /**
     * Create a new instance of the {@link AmazonS3Template} with the bucket name and access credentials
     *
     * @param defaultBucket   is the name of a default bucket from the Amazon S3 provider
     * @param accessKeyId     is the access key id credential for the specified bucket name
     * @param accessKeySecret is the access key secret for the specified bucket name
     */
    public AmazonS3Template(
    		@Value("${amazon.aws.access-key-id}") String accessKeyId, 
    		@Value("${amazon.aws.access-key-secret}") String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * Gets an Amazon S3 client from basic session credentials
     *
     * @return an authenticated Amazon S3 client
     */
    public AmazonS3 getAmazonS3Client() {
    	BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);

        // Create a new S3 client using the basic session credentials of the service instance
        return AmazonS3ClientBuilder.standard().withRegion("us-east-2").withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials)).build();
    }

}
