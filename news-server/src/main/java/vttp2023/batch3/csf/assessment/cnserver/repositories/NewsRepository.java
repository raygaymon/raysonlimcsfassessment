package vttp2023.batch3.csf.assessment.cnserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.expression.spel.ast.Projection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mongodb.client.result.UpdateResult;

import jakarta.json.JsonObject;


@Repository
public class NewsRepository {
	@Autowired
	private MongoTemplate template;
	@Autowired
	private AmazonS3 s3;

	// TODO: Task 1 
	// Write the native Mongo query in the comment above the method\

	// vttpcsf.news.insert({
	// 	title: news.title,
	// 	description: news.description,
	// 	image: image name,
	// 	tags: news.tags
	// })
	public Document addNews (JsonObject n){
		Document news = Document.parse(n.toString());
        System.out.println("Trying to add to mongodb");
        Document toInsert = template.insert(news, "news");
		System.out.println("mongodb add success");
        return toInsert;
	}

	

	//vttpcsf.news.update(
	//	{ image: filename.png },
	//  {
	//	$set: {image: s3bucket url}
	//}
	//)
	public String updateNewsMongo(String url, String imageName){
		

		Query query = Query.query(Criteria.where("image").is(imageName));
		Update updateOps = new Update().set("image", url);

		UpdateResult ur = template.updateMulti(query, updateOps, Document.class, "news");

		return "Document successfully updated: %d/n".formatted(ur.getModifiedCount());
	}

	

	// TODO: Task 2 
	// Write the native Mongo query in the comment above the method

		// 	vttpcsf.news.aggregate([
			//{
			// 	$match: {postDate : {$gte: time}}
			// },
			// {
			// $project: {
			// "tags": $tags, 
			// "postDate": $postDate,
			// },
			// {
			// 	$unwind: “$tags”
			// },
			//{
			// $group: {
			// _id: “$tags”,
			// tagCcount: {
			// $sum: 1
		// },
		//{$sort: {
		// 	tagCount: -1
		// }},
		// {
			// $sort: {
			// 	_id: 1
			// }
		// }
		// }]);


	public AggregationResults<Document> getAllTags (long time) {
		MatchOperation dateMatch = Aggregation.match(
			Criteria.where("postDate").gte(time)
		);
		
		ProjectionOperation po = Aggregation.project("tags", "postDate");
		AggregationOperation unwindTags = Aggregation.unwind("tags");
		GroupOperation group = Aggregation.group("tags")
							.count().as("tagCount");
		
		SortOperation sortCount = Aggregation.sort(Sort.by(Direction.DESC, "tagCount"));
		SortOperation sortTag = Aggregation.sort(Sort.by(Direction.ASC, "_id"));
		Aggregation pipeline = Aggregation.newAggregation(dateMatch, po, unwindTags, group, sortCount, sortTag);

		AggregationResults<Document> results = template.aggregate(pipeline, "news", Document.class);
		return results;
	}



	// TODO: Task 3
//vttpcsf.news.aggregate([
		//{
		// 	$unwind: “$tags”
		// },
		// 	$match: {tags : "tag"}}
	// }])

	// Write the native Mongo query in the comment above the method
	public AggregationResults<Document> getNewsByTags (String tag){
		AggregationOperation unwind = Aggregation.unwind("tags");
		MatchOperation match = Aggregation.match(
				Criteria.where("tags").is(tag)
		);
		Aggregation pipeline = Aggregation.newAggregation(unwind, match);

		AggregationResults<Document> results = template.aggregate(pipeline, "news", Document.class);
		return results;
	}

}
