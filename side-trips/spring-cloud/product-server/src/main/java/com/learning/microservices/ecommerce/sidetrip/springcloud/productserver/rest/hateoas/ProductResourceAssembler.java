package com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.rest.hateoas;

import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.data.entity.Product;
import com.learning.microservices.ecommerce.sidetrip.springcloud.productserver.rest.ProductRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductResourceAssembler implements SimpleRepresentationModelAssembler<Product> {
    @Override
    public void addLinks(EntityModel<Product> resource) {
        Objects.requireNonNull(resource.getContent());
        resource.add(
                linkTo(methodOn(ProductRestController.class).getProduct(resource.getContent().getId())).withSelfRel(),
                linkTo(methodOn(ProductRestController.class).getAllProducts()).withRel("products"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Product>> resources) {
        resources.add(linkTo(methodOn(ProductRestController.class).getAllProducts()).withSelfRel());
    }
}
