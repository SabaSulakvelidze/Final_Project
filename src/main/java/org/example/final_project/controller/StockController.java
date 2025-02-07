package org.example.final_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.final_project.service.ProductService;
import org.example.final_project.service.ShopService;
import org.example.final_project.service.StockService;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.entity.StockEntity;
import org.example.final_project.model.request.StockRequest;
import org.example.final_project.model.response.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    private final ProductService productService;
    private final ShopService shopService;

    @GetMapping("/getAllStocks")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<StockResponse> getAllStocks(@RequestParam(defaultValue = "0") Integer pageNumber,
                                            @RequestParam(defaultValue = "5") Integer pageSize) {
        return stockService.getAllStocks(pageNumber, pageSize).map(StockResponse::toStockResponse);
    }

    @PostMapping("/createStock")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public StockResponse createStock(@RequestBody @Valid StockRequest stockRequest) {
        ProductEntity productById = productService.getProductById(stockRequest.getProductId());
        ShopEntity shopById = shopService.getShopById(stockRequest.getShopId());

        return StockResponse.toStockResponse(stockService.addProductToShop(StockEntity.toStockEntity(stockRequest, productById, shopById)));
    }

    @PutMapping("/editStock")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public StockResponse editStock(@RequestBody @Valid StockRequest stockRequest) {
        StockEntity stockByShopIdAndProductId = stockService.getStockByShopIdAndProductId(stockRequest.getShopId(), stockRequest.getProductId());
        return StockResponse.toStockResponse(stockService.editStock(StockEntity.toStockEntity(stockRequest,
                stockByShopIdAndProductId.getProduct(), stockByShopIdAndProductId.getShop())));
    }

    @DeleteMapping("/deleteStock")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteStock(@RequestParam Long shopId, @RequestParam Long productId) {
        return stockService.deleteStock(shopId, productId);
    }


    @GetMapping("/getStockByShopId/{shopId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Page<StockResponse> getStockByShopId(@PathVariable Long shopId,
                                                @RequestParam(defaultValue = "0") Integer pageNumber,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                @RequestParam(defaultValue = "id") String sortBy) {
        return stockService.getStockByShopId(shopId, pageNumber, pageSize, direction, sortBy).map(StockResponse::toStockResponse);
    }

    @GetMapping("/getStockByProductId/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<StockResponse> getStockByProductId(@PathVariable Long productId,
                                                   @RequestParam(defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                   @RequestParam(defaultValue = "id") String sortBy) {
        return stockService.getStockByProductId(productId, pageNumber, pageSize, direction, sortBy).map(StockResponse::toStockResponse);
    }

    @GetMapping("/getStockByShopIdAndProductId")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public StockResponse getStockByShopIdAndProductId(@RequestParam Long shopId,
                                                      @RequestParam Long productId) {
        return StockResponse.toStockResponse(stockService.getStockByShopIdAndProductId(shopId, productId));
    }

    @PostMapping("/purchaseProduct")
    @PreAuthorize("hasAnyRole('USER')")
    public StockResponse purchaseProduct(@RequestParam Long shopId,
                                         @RequestParam Long productId,
                                         @RequestParam Integer desiredQuantity) {
        return StockResponse.toStockResponse(stockService.purchaseProduct(shopId, productId, desiredQuantity));
    }

}