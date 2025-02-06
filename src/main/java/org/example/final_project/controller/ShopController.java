package org.example.final_project.controller;

import jakarta.validation.Valid;
import org.example.final_project.service.ShopService;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.request.ShopRequest;
import org.example.final_project.model.response.ShopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/getAllShops")
    public Page<ShopResponse> getAllShops(@RequestParam(defaultValue = "0") Integer pageNumber,
                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                          @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        return shopService.getAllShops(pageNumber, pageSize, direction, sortBy).map(ShopResponse::toShopResponse);
    }

    @GetMapping("/getSingleShop/{shopId}")
    public ShopResponse getSingleShop(@PathVariable Long shopId) {
        return ShopResponse.toShopResponse(shopService.getShopById(shopId));
    }

    @PostMapping("/createShop")
    public ShopResponse createShop(@RequestBody @Valid ShopRequest shopRequest) {
        return ShopResponse.toShopResponse(shopService.createShop(ShopEntity.toShopEntity(shopRequest)));
    }

    @PutMapping("/editShop/{shopId}")
    public ShopResponse editShop(@PathVariable Long shopId, @RequestBody @Valid ShopRequest shopRequest) {
        return ShopResponse.toShopResponse(shopService.editShop(shopId, ShopEntity.toShopEntity(shopRequest)));
    }

    @DeleteMapping("/deleteShop/{shopId}")
    public String deleteShop(@PathVariable Long shopId) {
        return shopService.deleteShop(shopId);
    }

}