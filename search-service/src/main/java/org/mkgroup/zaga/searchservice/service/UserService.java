package org.mkgroup.zaga.searchservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

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
import org.mkgroup.zaga.searchservice.dto.NewUserDTO;
import org.mkgroup.zaga.searchservice.model.UserElastic;
import org.mkgroup.zaga.searchservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.Comparator.comparingLong;

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
		Iterable<UserElastic> users = userRepo.findAll();
		
		List<UserElastic> result = new ArrayList<UserElastic>();
		users.forEach(result::add);
		
		List<UserElastic> unique =  result.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(UserElastic::getPerNumber))),
                		ArrayList::new));
		return unique;
	}

	public UserElastic saveUser(UserElastic user) {
		return userRepo.save(user);
	}
	
	public void editUser(NewUserDTO emp) {
		UserElastic user = userRepo.findByUserId(emp.getUserId()).get();
		user.setDepartment(emp.getDepartment());
		user.setPosition(emp.getPosition());
		userRepo.save(user);
	}

}
