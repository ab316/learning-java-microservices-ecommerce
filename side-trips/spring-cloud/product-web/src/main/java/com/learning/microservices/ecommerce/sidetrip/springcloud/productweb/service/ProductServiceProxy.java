package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.service;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.api.ProductService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-proxy", url = "http://localhost:8080")
public interface ProductServiceProxy extends ProductService {
}
