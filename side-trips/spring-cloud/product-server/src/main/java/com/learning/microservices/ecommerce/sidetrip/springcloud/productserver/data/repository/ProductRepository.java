package com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.repository;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductRepository extends MongoRepository<Product, String> {
}
