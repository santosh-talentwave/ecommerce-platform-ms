package com.eshop.product.service;

import com.eshop.product.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    Product getProduct(Long id);

    List<Product> getProducts();
}
