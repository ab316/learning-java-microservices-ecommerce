package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.api.ProductService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "product-alternate-proxy",
        url = "http://localhost:8082"
)
public interface ProductAlternateServiceProxy extends ProductService {
}
