package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.api;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.model.Product;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductService {
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts();

    @GetMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<Product>> getProduct(@PathVariable("productId") String productId);
}
