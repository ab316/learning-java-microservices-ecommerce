package com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.rest;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.repository.ProductRepository;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.entity.Product;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.rest.hateoas.ProductResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    private final ProductRepository productRepository;
    private final ProductResourceAssembler productResourceAssembler;

    @Autowired
    public ProductRestController(ProductRepository productRepository, ProductResourceAssembler productResourceAssembler) {
        this.productRepository = productRepository;
        this.productResourceAssembler = productResourceAssembler;
    }

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        var products = productRepository.findAll();
        var productResources = productResourceAssembler.toCollectionModel(products);
        return ResponseEntity.ok(productResources);
    }

    @GetMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Product>> getProduct(@PathVariable("productId") String productId) {
        return productRepository.findById(productId)
                .map(productResourceAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
