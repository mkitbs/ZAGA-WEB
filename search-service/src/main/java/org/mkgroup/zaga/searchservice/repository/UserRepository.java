package org.mkgroup.zaga.searchservice.repository;

import org.mkgroup.zaga.searchservice.model.UserElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<UserElastic, Long> {

}
