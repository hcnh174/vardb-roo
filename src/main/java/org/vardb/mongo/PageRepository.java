package org.vardb.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PageRepository extends MongoRepository<Page, String>
{
	
}