package com.citytransit.repository.mongo;

import com.citytransit.model.document.ApiLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepository extends MongoRepository<ApiLog, String> {
}
