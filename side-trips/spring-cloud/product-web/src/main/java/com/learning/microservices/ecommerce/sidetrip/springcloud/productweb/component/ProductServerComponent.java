package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.component;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.model.Product;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service.ProductAlternateServiceProxy;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service.ProductServiceProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class ProductServerComponent {
    private ProductServiceProxy proxy;
    private ProductAlternateServiceProxy alternateProxy;

    @Autowired
    public ProductServerComponent(ProductServiceProxy proxy, ProductAlternateServiceProxy alternateProxy) {
        this.proxy = proxy;
        this.alternateProxy = alternateProxy;
    }

    @HystrixCommand(fallbackMethod = "getAllProductsAlternate")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        return proxy.getAllProducts();
    }

    public ResponseEntity<EntityModel<Product>> getProduct(@PathVariable("productId") String productId) {
        return proxy.getProduct(productId);
    }

    protected ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProductsAlternate() {
        return alternateProxy.getAllProducts();
    }

    protected ResponseEntity<EntityModel<Product>> getProductAlternate(@PathVariable("productId") String productId) {
        return alternateProxy.getProduct(productId);
    }
}
