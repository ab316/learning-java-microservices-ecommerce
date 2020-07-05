package com.learning.microservices.ecommerce.sidetrip.springcloud.productweb.model;

import org.springframework.data.annotation.Id;

import java.util.StringJoiner;

public class Product {
    @Id
    private String id;
    private String name;
    private String code;
    private String title;
    private String description;
    private String imageUrl;
    private Double price;
    private String productCategoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(id, product.id)
                .append(name, product.name)
                .append(code, product.code)
                .append(title, product.title)
                .append(description, product.description)
                .append(imageUrl, product.imageUrl)
                .append(price, product.price)
                .append(productCategoryName, product.productCategoryName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(code)
                .append(title)
                .append(description)
                .append(imageUrl)
                .append(price)
                .append(productCategoryName)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .add("imageUrl='" + imageUrl + "'")
                .add("price=" + price)
                .add("productCategoryName='" + productCategoryName + "'")
                .toString();
    }
}
