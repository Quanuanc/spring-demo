package cheng.service;

import cheng.entity.Product;
import cheng.entity.ProductDocument;
import cheng.repo.ProductRepository;
import cheng.repo.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Transactional
    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);

        ProductDocument productDocument = new ProductDocument();
        productDocument.setId(product.getId());
        productDocument.setProductName(product.getProductName());
        productDocument.setProductPrice(product.getProductPrice());

        productSearchRepository.save(productDocument);

        return savedProduct;
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    @Transactional
    public Product updateProduct(Long id, Product newProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setProductPrice(newProduct.getProductPrice());
                    product.setImageUrl(newProduct.getImageUrl());

                    Product updatedProduct = productRepository.save(product);

                    ProductDocument productDocument = new ProductDocument();
                    productDocument.setId(updatedProduct.getId());
                    productDocument.setProductName(updatedProduct.getProductName());
                    productDocument.setProductPrice(updatedProduct.getProductPrice());

                    productSearchRepository.save(productDocument);

                    return updatedProduct;
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    Product savedProduct = productRepository.save(newProduct);

                    ProductDocument productDocument = new ProductDocument();
                    productDocument.setId(savedProduct.getId());
                    productDocument.setProductName(savedProduct.getProductName());
                    productDocument.setProductPrice(savedProduct.getProductPrice());

                    productSearchRepository.save(productDocument);

                    return savedProduct;
                });
    }
}
