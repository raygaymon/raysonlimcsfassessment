package vttp2023.batch3.csf.assessment.cnserver.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp2023.batch3.csf.assessment.Utility.Utils;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.repositories.ImageRepository;
import vttp2023.batch3.csf.assessment.cnserver.repositories.NewsRepository;
import vttp2023.batch3.csf.assessment.cnserver.services.NewsService;

@RestController
@RequestMapping("/api")
public class NewsController {
	@Autowired
	private NewsService serviceN;
	@Autowired
	private NewsRepository repoN;
	@Autowired
	private ImageRepository repoI;

	// TODO: Task 1
	@PostMapping("/post")
	public ResponseEntity<String> postNews (@RequestBody String news){

		String id = serviceN.postNews(news);
		if (id != null){
			return ResponseEntity.ok(
				Json.createObjectBuilder()
					.add("newsId", id)
					.build().toString()
			);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PostMapping(path="/uploadPic", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> picUpload (@RequestPart MultipartFile file, @RequestPart String name){
		System.out.println("running upload pic post");
		System.out.println(name + ".png");
		System.out.println(file.getOriginalFilename());
		String imageUrl = repoI.saveImage(file, file.getOriginalFilename());
		String success = repoN.updateNewsMongo(imageUrl, file.getOriginalFilename());
		System.out.println(success);
		return ResponseEntity.ok(success);
	}


	// TODO: Task 2
	@GetMapping("/tags")
	public ResponseEntity<String> getTags(@RequestParam("time") long time){
	
		List<TagCount> tcs = serviceN.getTags(time);
		List<JsonObject> tcJson = tcs.stream().map(m -> Utils.tagCountToJson(m)).collect(Collectors.toList());
		JsonArray ja = Json.createArrayBuilder(tcJson).build();
		return ResponseEntity.ok(Json.createObjectBuilder().add("tags", ja).build().toString());
	}


	// TODO: Task 3
	@GetMapping(path="/articles")
	public ResponseEntity<String> getNewsByTag (@RequestParam("tag") String tag){
		List<News> newsList = serviceN.getNewsByTag(tag);
		List<JsonObject> newsJson = newsList.stream().map(n -> Utils.newsToJson(n)).collect(Collectors.toList());
		JsonArray ja = Json.createArrayBuilder(newsJson).build();
		return ResponseEntity.ok(Json.createObjectBuilder().add("news", ja).build().toString());
		
	}

}
