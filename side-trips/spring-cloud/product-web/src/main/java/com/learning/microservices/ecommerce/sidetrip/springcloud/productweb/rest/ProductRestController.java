package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.rest;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.model.Product;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service.ProductServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@EnableFeignClients(basePackageClasses = ProductServiceProxy.class)
public class ProductRestController {
    private final ProductServiceProxy productServiceProxy;

    @Autowired
    public ProductRestController(ProductServiceProxy productServiceProxy) {
        this.productServiceProxy = productServiceProxy;
    }

    @GetMapping(value = "/productsweb", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        return productServiceProxy.getAllProducts();
    }

    @GetMapping(value = "/productsweb{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Product>> getAllProducts(@PathVariable("productId") String productId) {
        return productServiceProxy.getProduct(productId);
    }
}
