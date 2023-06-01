package cheng.controller;

import cheng.entity.ProductDocument;
import cheng.repo.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search")
public class ProductSearchController {

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @GetMapping("/productName")
    public List<ProductDocument> searchByProductName(@RequestParam String productName) {
        return productSearchRepository.findByProductName(productName);
    }

    @GetMapping("/productId")
    public Page<ProductDocument> listProduct(@RequestParam Long productId){
        Optional<ProductDocument> productDocumentOptional = productSearchRepository.findById(productId);
        if(productDocumentOptional.isEmpty()){
            return Page.empty();
        }
        ProductDocument productDocument = productDocumentOptional.get();

        return productSearchRepository.searchSimilar(productDocument, new String[]{"productName"}, PageRequest.of(0,20));
    }

}
