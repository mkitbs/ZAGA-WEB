package org.mkgroup.zaga.searchservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.mkgroup.zaga.searchservice.model.UserElastic;
import org.mkgroup.zaga.searchservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RestHighLevelClient highLevelClient;

	public List<UserElastic> findUserByName(String queryName) throws IOException {

		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		BoolQueryBuilder boolBuilder = new BoolQueryBuilder();

		WildcardQueryBuilder parameter = QueryBuilders.wildcardQuery("name", "*" + queryName + "*");
		boolBuilder.must(parameter);
		boolQueryBuilder.must(boolBuilder);

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

		QueryBuilder qb = QueryBuilders.wrapperQuery(nativeSearchQueryBuilder.build().getQuery().toString());
		SearchRequest searchRequest = new SearchRequest("user");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(qb);
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

		List<UserElastic> returnList = new ArrayList<UserElastic>();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {

			UserElastic user = new ObjectMapper().readValue(searchHit.getSourceAsString(), UserElastic.class);
			returnList.add(user);
		}

		return returnList;
	}

	public Iterable<UserElastic> getAll() {
		return userRepo.findAll();
	}

	public UserElastic saveUser(UserElastic user) {
		return userRepo.save(user);
	}

}
