package org.example.final_project;

import org.assertj.core.api.SoftAssertions;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.repository.ShopRepository;
import org.example.final_project.service.ShopService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTests {

    private ShopService shopService;

    @Mock
    private ShopRepository shopRepository;

    @BeforeEach
    void setUp(){
        shopService = new ShopService(shopRepository);
    }

    @Test
    void getAllShopsReturnsPageOfShops(){
        List<ShopEntity> shops = List.of(
                ShopEntity.builder().id(1L).name("Shop #1").stocks(null).build(),
                ShopEntity.builder().id(2L).name("Shop #2").stocks(null).build());
        when(shopRepository.findAll(PageRequest.of(0,5, Sort.Direction.ASC,"id")))
                .thenReturn(new PageImpl<>(
                        shops,
                        PageRequest.of(0,5, Sort.Direction.ASC,"id"),
                        shops.size()));

        Page<ShopEntity> allShops = shopService.getAllShops(0, 5, Sort.Direction.ASC, "id");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(allShops.getContent()).isEqualTo(shops);
        softAssertions.assertThat(allShops.getPageable()).isEqualTo(PageRequest.of(0,5, Sort.Direction.ASC,"id"));
        softAssertions.assertAll();
    }

    @Test
    void getShopByIdReturnsSingleShopEntity(){
        ShopEntity shop = ShopEntity.builder().id(1L).name("Shop #1").stocks(null).build();

        when(shopRepository.findById(1L)).thenReturn(Optional.ofNullable(shop));

        ShopEntity shopById = shopService.getShopById(1L);
        assertEquals(shop,shopById);
    }

    @Test
    void getShopByIdThrowsException(){
        when(shopRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> shopService.getShopById(1L));
    }

    @Test
    void createShopEntityTest(){
        ShopEntity shop = ShopEntity.builder().id(1L).name("Shop #1").stocks(null).build();
        Mockito.when(shopRepository.save(shop)).thenReturn(shop);

        ShopEntity shopById = shopService.createShop(shop);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(shopById.getId()).isEqualTo(1L);
        softAssertions.assertThat(shopById.getName()).isEqualTo("Shop #1");
        softAssertions.assertThat(shopById.getStocks()).isNull();
        softAssertions.assertAll();
    }

    @Test
    void editShopEntityTest(){
        ShopEntity shop = ShopEntity.builder().id(2L).name("Shop #2").stocks(null).build();

        when(shopRepository.findById(2L)).thenReturn(Optional.ofNullable(shop));

        ShopEntity shopById = shopService.editShop(2L,ShopEntity.builder().name("Shop #3").stocks(null).build());
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(shopById.getId()).isEqualTo(2L);
        softAssertions.assertThat(shopById.getName()).isEqualTo("Shop #3");
        softAssertions.assertThat(shopById.getStocks()).isNull();
        softAssertions.assertAll();
    }

    @Test
    void deleteShopShouldReturnSuccessMessageWhenShopExists() {
        ShopEntity shop = ShopEntity.builder().id(2L).name("Shop #2").stocks(null).build();
        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));
        doNothing().when(shopRepository).delete(shop);

        String result = shopService.deleteShop(1L);

        assertEquals("shop with id 1 was deleted successfully", result);
        verify(shopRepository).delete(shop);
    }

    @Test
    void deleteShopShouldThrowExceptionWhenShopNotFound() {
        when(shopRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> shopService.deleteShop(1L));
    }

}
