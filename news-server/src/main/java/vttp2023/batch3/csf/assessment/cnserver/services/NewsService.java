package vttp2023.batch3.csf.assessment.cnserver.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.JsonObject;
import vttp2023.batch3.csf.assessment.Utility.Utils;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.repositories.NewsRepository;

@Service
public class NewsService {
	@Autowired
	private NewsRepository repoN;
	
	// TODO: Task 1
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns the news id
	public String postNews(String fromNg) {
		JsonObject n = Utils.readAngularOutput(fromNg);
		News noose = Utils.jsonToNews(n);
		Document toInsert = repoN.addNews(Utils.newsToJson(noose));
		return toInsert.getObjectId("_id").toString();
	}


	 
	// TODO: Task 2
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns a list of tags and their associated count
	public List<TagCount> getTags(long time) {

		List<TagCount> tcs = new LinkedList<>();
		AggregationResults<Document> results = repoN.getAllTags(time);
		for (Document d : results.getMappedResults()){
			String tag = d.getString("_id");
			System.out.println(tag);
			Integer count = d.getInteger("tagCount");
			System.out.println(count);
			TagCount tc = new TagCount(tag, count);
			tcs.add(tc);
		}
		return tcs;
	}

	// TODO: Task 3
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns a list of news
	public List<News> getNewsByTag(String tag) {
		AggregationResults<Document> ar = repoN.getNewsByTags(tag);
		List<News> newsList = new LinkedList<>();
		for (Document d : ar.getMappedResults()){
			News n = new News();
			String title = d.getString("title");
			n.setTitle(title);
			long postDate = d.getLong("postDate");
			n.setPostDate(postDate);
			String url = d.getString("image");
			n.setImage(url);
			String description = d.getString("description");
			n.setDescription(description);
			newsList.add(n);
		}
		return newsList;
	}
	
}
