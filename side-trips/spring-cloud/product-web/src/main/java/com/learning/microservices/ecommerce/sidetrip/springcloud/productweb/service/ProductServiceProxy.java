package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.api.ProductService;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.component.ProductAlternateServiceComponent;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "product-proxy",
        url = "http://localhost:8081",
        fallback = ProductAlternateServiceComponent.class
)
public interface ProductServiceProxy extends ProductService {
}
