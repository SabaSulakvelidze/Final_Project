package org.example.final_project.Service;

import lombok.RequiredArgsConstructor;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductEntity> getAllProducts(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy){
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public ProductEntity getProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product with id %d was not found".formatted(productId)));
    }

    public ProductEntity createProduct(ProductEntity productEntity){
        return productRepository.save(productEntity);
    }

    @Transactional
    public ProductEntity editProduct(Long productId, ProductEntity productEntity){
        ProductEntity productToEdit = getProductById(productId);
        productToEdit.setName(productEntity.getName());
        productToEdit.setStocks(productEntity.getStocks());
        return productToEdit;
    }

    public String deleteProduct(Long productId){
        productRepository.delete(getProductById(productId));
        return "product with id %d was deleted successfully".formatted(productId);
    }
}
