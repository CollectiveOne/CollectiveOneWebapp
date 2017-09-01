package org.collectiveone.modules.files;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.config.aws.AmazonS3Template;
import org.collectiveone.modules.users.AppUserProfile;
import org.collectiveone.modules.users.AppUserProfileRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
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
	FileStoredRepositoryIf fileStoredRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private AppUserProfileRepositoryIf appUserProfileRepository;

	@Transactional
	public FileStored handleFileUpload(UUID uploadedById, String key, MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(file.getContentType());
				
				// Upload the file for public read
				amazonS3Template.getAmazonS3Client().putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), objectMetadata)
				        .withCannedAcl(CannedAccessControlList.PublicRead));
				
				
				FileStored fileStored = new FileStored();
				fileStored.setBucket(bucketName);
				fileStored.setKey(key);
				fileStored.setUploadedBy(appUserRepository.findByC1Id(uploadedById));
				fileStored.setUrl(baseUrl + "/" + bucketName + "/" + key);
				
				fileStored = fileStoredRepository.save(fileStored);
				
				return fileStored;
				
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
		
	}
	
	@Transactional
	public void uploadImageProfile(UUID userId, MultipartFile file) {
		
		String key = "ProfileImages/" + userId.toString();
		FileStored fileUploaded = handleFileUpload(userId, key, file);
		
		if (fileUploaded != null) {
			AppUserProfile profile = appUserProfileRepository.findByUser_C1Id(userId);
			profile.setUploadedPicture(fileUploaded);
			profile.setUseUploadedPicture(true);
			
			appUserProfileRepository.save(profile);
		}
		
	}
}
