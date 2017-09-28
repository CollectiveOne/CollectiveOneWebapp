package org.collectiveone.modules.files;

import java.sql.Timestamp;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.config.aws.AmazonS3Template;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelService;
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
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private ModelService modelService;
	

	@Transactional
	public FileStored handleFileUpload(UUID uploadedById, String key, MultipartFile file, UUID initiativeId) {
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
				fileStored.setLastUpdated(new Timestamp(System.currentTimeMillis()));
				
				if (initiativeId != null) fileStored.setInitiative(initiativeRepository.findById(initiativeId)); 
				
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
		FileStored fileUploaded = handleFileUpload(userId, key, file, null);
		
		if (fileUploaded != null) {
			AppUserProfile profile = appUserProfileRepository.findByUser_C1Id(userId);
			profile.setUploadedPicture(fileUploaded);
			profile.setUseUploadedPicture(true);
			
			appUserProfileRepository.save(profile);
		}
		
	}
	
	@Transactional
	public PostResult uploadCardImage(UUID userId, UUID cardWrapperId, MultipartFile file) {
		
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		String key = "CardImages/" + cardWrapperId.toString();
		FileStored fileUploaded = handleFileUpload(userId, key, file, initiative.getId());
		
		if (fileUploaded != null) {
			return new PostResult("success", "image uploaded", fileUploaded.getId().toString());
		}
		
		return new PostResult("error", "error uploading image", "");
		
	}
	
	@Transactional
	public Initiative getFileInitiative(UUID fileId) {
		return fileStoredRepository.findById(fileId).getInitiative();
	}
	
	@Transactional
	public FileStoredDto getFileData(UUID fileId) {
		return fileStoredRepository.findById(fileId).toDto();
	}
}
