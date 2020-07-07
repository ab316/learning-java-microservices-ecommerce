package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.rest;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.component.ProductServerComponent;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductRestController {
    private final ProductServerComponent productServer;

    @Autowired
    public ProductRestController(ProductServerComponent productServerComponent) {
        this.productServer = productServerComponent;
    }

    @GetMapping(value = "/productsweb", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        return productServer.getAllProducts();
    }

    @GetMapping(value = "/productsweb{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Product>> getAllProducts(@PathVariable("productId") String productId) {
        return productServer.getProduct(productId);
    }
}
