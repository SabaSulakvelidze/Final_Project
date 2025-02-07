package org.example.final_project;

import org.assertj.core.api.SoftAssertions;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.repository.ProductRepository;
import org.example.final_project.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp(){
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProductsReturnsPageOfProducts(){
        List<ProductEntity> shops = List.of(
                ProductEntity.builder().id(1L).name("Product #1").stocks(null).build(),
                ProductEntity.builder().id(2L).name("Product #2").stocks(null).build());
        when(productRepository.findAll(PageRequest.of(0,5, Sort.Direction.ASC,"id")))
                .thenReturn(new PageImpl<>(
                        shops,
                        PageRequest.of(0,5, Sort.Direction.ASC,"id"),
                        shops.size()));

        Page<ProductEntity> allProducts = productService.getAllProducts(0, 5, Sort.Direction.ASC, "id");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(allProducts.getContent()).isEqualTo(shops);
        softAssertions.assertThat(allProducts.getPageable()).isEqualTo(PageRequest.of(0,5, Sort.Direction.ASC,"id"));
        softAssertions.assertAll();
    }

    @Test
    void getProductByIdReturnsSingleProductEntity(){
        ProductEntity shop = ProductEntity.builder().id(1L).name("Product #1").stocks(null).build();

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(shop));

        ProductEntity shopById = productService.getProductById(1L);
        assertEquals(shop,shopById);
    }

    @Test
    void getProductByIdThrowsException(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> productService.getProductById(1L));
    }

    @Test
    void createProductEntityTest(){
        ProductEntity shop = ProductEntity.builder().id(1L).name("Product #1").stocks(null).build();
        Mockito.when(productRepository.save(shop)).thenReturn(shop);

        ProductEntity shopById = productService.createProduct(shop);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(shopById.getId()).isEqualTo(1L);
        softAssertions.assertThat(shopById.getName()).isEqualTo("Product #1");
        softAssertions.assertThat(shopById.getStocks()).isNull();
        softAssertions.assertAll();
    }

    @Test
    void editProductEntityTest(){
        ProductEntity shop = ProductEntity.builder().id(2L).name("Product #2").stocks(null).build();

        when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(shop));

        ProductEntity shopById = productService.editProduct(2L,ProductEntity.builder().name("Product #3").stocks(null).build());
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(shopById.getId()).isEqualTo(2L);
        softAssertions.assertThat(shopById.getName()).isEqualTo("Product #3");
        softAssertions.assertThat(shopById.getStocks()).isNull();
        softAssertions.assertAll();
    }

    @Test
    void deleteProductShouldReturnSuccessMessageWhenProductExists() {
        ProductEntity product = ProductEntity.builder().id(2L).name("Product #2").stocks(null).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        String result = productService.deleteProduct(1L);

        assertEquals("product with id 1 was deleted successfully", result);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductShouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
    }

}
