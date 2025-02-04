package org.example.final_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.final_project.Service.ProductService;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.request.ProductRequest;
import org.example.final_project.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllProducts")
    public Page<ProductResponse> getAllProducts(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                @RequestParam(defaultValue = "id") String sortBy) {
        return productService.getAllProducts(pageNumber, pageSize, direction, sortBy).map(ProductResponse::toProductResponse);
    }

    @GetMapping("/getSingleProduct/{productId}")
    public ProductResponse getSingleProduct(@PathVariable Long productId) {
        return ProductResponse.toProductResponse(productService.getProductById(productId));
    }

    @PostMapping("/createProduct")
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return ProductResponse.toProductResponse(productService.createProduct(ProductEntity.toProductEntity(productRequest)));
    }

    @PutMapping("/editProduct/{productId}")
    public ProductResponse editProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
        return ProductResponse.toProductResponse(productService.editProduct(productId, ProductEntity.toProductEntity(productRequest)));
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

}
