package org.collectiveone.modules.files;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.config.aws.AmazonS3Template;
import org.collectiveone.modules.users.AppUserProfile;
import org.collectiveone.modules.users.AppUserProfileRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class FileService {
	
	@Autowired
	private AmazonS3Template amazonS3Template;
	
	@Value("${amazon.aws.bucket}")
    String bucketName;
	
	@Value("${amazon.aws.baseUrl}")
	String baseUrl;
	
	@Autowired
	private AppUserProfileRepositoryIf appUserProfileRepository;

	public String handleFileUpload(String name, MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(file.getContentType());
				
				// Upload the file for public read
				amazonS3Template.getAmazonS3Client().putObject(new PutObjectRequest(bucketName, name, file.getInputStream(), objectMetadata)
				        .withCannedAcl(CannedAccessControlList.PublicRead));
				
				return "success";
				
			} catch (Exception e) {
				return "error: " + e.getMessage();
			}
		} else {
			return "error: file is empty";
		}
		
	}
	
	@Transactional
	public void uploadImageProfile(UUID userId, MultipartFile file) {
		
		String key = "ProfileImages/" + userId.toString();
		String res = handleFileUpload(key, file);
		
		if (res.equals("success")) {
			AppUserProfile profile = appUserProfileRepository.findByUser_C1Id(userId);
			profile.setPictureUrl(baseUrl + "/" + bucketName + "/" + key);
			appUserProfileRepository.save(profile);
		}
		
	}
}
