package org.collectiveone.modules.contexts.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.collectiveone.modules.contexts.repositories.CardContentRepositoryIf;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
	
	@Autowired
	public CardContentRepositoryIf cardContentRepository;
	
	@Autowired
	public FileStoredRepositoryIf fileStoredRepository;
	

	public CardContent createCardContent(String base) {
		
		CardContent cardContent = new CardContent();
		cardContent = cardContentRepository.save(cardContent);
		
		cardContent.getNumberFields().addAll(extractNumberFields(base));
		cardContent.getStringFields().addAll(extractStringFields(base));
		
		/* extract file fields (they should be already stored in the DB) */
		List<FileStored> fileFields = extractFilesStored(base);
		
		cardContent.getFilesStored().addAll(fileFields);
		
		return cardContent;
	}
	
	private List<CardNumberField> extractNumberFields(String base) {
		
		Pattern pattern = Pattern.compile("<numeric[^>]+name=[\\\"']?([^\\\"']+)+[\\\"']?[^>]*>(.+?)<\\/numeric>");
		Matcher matcher = pattern.matcher(base);
		
		List<CardNumberField> cardFields = new ArrayList<CardNumberField>();
		
		while (matcher.find()) {
			
			String fieldName = matcher.group(1);
			Double fieldValue = Double.valueOf(matcher.group(2));
			CardNumberField field = new CardNumberField(fieldName, fieldValue);
			
			cardFields.add(field);
        }
		
		return cardFields;
		
	}
	
	private List<CardStringField> extractStringFields(String base) {
		
		Pattern pattern = Pattern.compile("<string[^>]+name=[\\\"']?([^\\\"']+)+[\\\"']?[^>]*>(.+?)<\\/string>");
		Matcher matcher = pattern.matcher(base);
		
		List<CardStringField> cardFields = new ArrayList<CardStringField>();
		
		while (matcher.find()) {
			
			String fieldName = matcher.group(1);
			String fieldValue = matcher.group(2);
			
			CardStringField field = new CardStringField(fieldName, fieldValue);
			cardFields.add(field);
        }
		
		return cardFields;
	}
	
	private List<FileStored> extractFilesStored(String base) {
		
		Pattern pattern = Pattern.compile("<file[^>]+id=[\\\"']?([^\\\"']+)+[\\\"']?[^>]*>(.+?)<\\/file>");
		Matcher matcher = pattern.matcher(base);
		
		List<FileStored> cardFiles = new ArrayList<FileStored>();
		
		while (matcher.find()) {
			
			UUID fileId = UUID.fromString(matcher.group(1));
			FileStored file = fileStoredRepository.findById(fileId);
			cardFiles.add(file);
        }
		
		return cardFiles;		
	}
}
