package vttp2023.batch3.csf.assessment.cnserver.repositories;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3;
	
	// TODO: Task 1
	public String saveImage(MultipartFile uploadFile, String name) {
		Map<String, String> userData = new HashMap<>();
		userData.put("uploadedBy", name);
		userData.put("filename", uploadFile.getOriginalFilename());

		// setting the information of the object to be uploaded
		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentType(uploadFile.getContentType());
		metaData.setContentLength(uploadFile.getSize());
		metaData.setUserMetadata(userData);

		String id = UUID.randomUUID().toString().substring(0, 8);

		try {
			PutObjectRequest putReq = new PutObjectRequest("day39",
					"pics/%s".formatted(uploadFile.getOriginalFilename()), uploadFile.getInputStream(), metaData);

			putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
			PutObjectResult result = s3.putObject(putReq);
			System.out.println("shit is working " + result);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return s3.getUrl("day39", "pics/%s".formatted(uploadFile.getOriginalFilename())).toExternalForm();
	}

}
