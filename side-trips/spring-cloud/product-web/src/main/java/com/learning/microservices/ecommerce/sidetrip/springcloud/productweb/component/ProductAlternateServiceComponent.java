package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.component;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.model.Product;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service.ProductAlternateServiceProxy;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service.ProductServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class ProductAlternateServiceComponent implements ProductServiceProxy {
    private ProductAlternateServiceProxy proxy;

    @Autowired
    public ProductAlternateServiceComponent(ProductAlternateServiceProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        return proxy.getAllProducts();
    }

    @Override
    public ResponseEntity<EntityModel<Product>> getProduct(@PathVariable("productId") String productId) {
        return proxy.getProduct(productId);
    }
}
