package com.learning.microservices.ecommerce.sidetrip.springcloud.productserver;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.entity.Product;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DevRunner implements CommandLineRunner {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Product p1 = new Product();
        p1.setId("1");
        p1.setDescription("Product 1");

        Product p2 = new Product();
        p2.setId("2");
        p2.setDescription("Product 2");

        Product p3 = new Product();
        p3.setId("3");
        p3.setDescription("Product 3");

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
    }
}
