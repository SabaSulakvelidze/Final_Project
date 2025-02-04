package org.example.final_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.final_project.Service.ProductService;
import org.example.final_project.Service.ShopService;
import org.example.final_project.Service.StockService;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.model.entity.StockEntity;
import org.example.final_project.model.request.StockRequest;
import org.example.final_project.model.response.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;
    private final ProductService productService;
    private final ShopService shopService;

    public StockController(StockService stockService, ProductService productService, ShopService shopService) {
        this.stockService = stockService;
        this.productService = productService;
        this.shopService = shopService;
    }

    @GetMapping("/getAllStocks")
    public Page<StockResponse> getAllStocks(@RequestParam(defaultValue = "0") Integer pageNumber,
                                            @RequestParam(defaultValue = "5") Integer pageSize,
                                            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                            @RequestParam(defaultValue = "id") String sortBy) {
        return stockService.getAllStocks(pageNumber, pageSize, direction, sortBy).map(StockResponse::toStockResponse);
    }

    @GetMapping("/getSingleStock/{stockId}")
    public StockResponse getSingleStock(@PathVariable Long stockId) {
        return StockResponse.toStockResponse(stockService.getStockById(stockId));
    }

    @PostMapping("/createStock")
    public StockResponse createStock(@RequestBody @Valid StockRequest stockRequest) {
        ProductEntity productById = productService.getProductById(stockRequest.getProductId());
        ShopEntity shopById = shopService.getShopById(stockRequest.getShopId());
        return StockResponse.toStockResponse(stockService.addProductToShop(StockEntity.toStockEntity(stockRequest, productById, shopById)));
    }

    @PutMapping("/editStock/{stockId}")
    public StockResponse editStock(@PathVariable Long stockId, @RequestBody @Valid StockRequest stockRequest) {
        ProductEntity productById = productService.getProductById(stockRequest.getProductId());
        ShopEntity shopById = shopService.getShopById(stockRequest.getShopId());
        return StockResponse.toStockResponse(stockService.editStock(stockId, StockEntity.toStockEntity(stockRequest, productById, shopById)));
    }

    @DeleteMapping("/deleteStock/{stockId}")
    public String deleteStock(@PathVariable Long stockId) {
        return stockService.deleteStock(stockId);
    }


    @GetMapping("/getStockByShopId/{shopId}")
    public Page<StockResponse> getStockByShopId(@PathVariable Long shopId,
                                                @RequestParam(defaultValue = "0") Integer pageNumber,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                @RequestParam(defaultValue = "id") String sortBy) {
        return stockService.getStockByShopId(shopId, pageNumber, pageSize, direction, sortBy).map(StockResponse::toStockResponse);
    }

    @GetMapping("/getStockByProductId/{productId}")
    public Page<StockResponse> getStockByProductId(@PathVariable Long productId,
                                                   @RequestParam(defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                   @RequestParam(defaultValue = "id") String sortBy) {
        return stockService.getStockByProductId(productId, pageNumber, pageSize, direction, sortBy).map(StockResponse::toStockResponse);
    }

    @GetMapping("/getStockByShopIdAndProductId")
    public StockResponse getStockByShopIdAndProductId(@RequestParam Long shopId,
                                                      @RequestParam Long productId) {
        return StockResponse.toStockResponse(stockService.getStockByShopIdAndProductId(shopId, productId));
    }

    @PostMapping("/purchaseProduct")
    public StockResponse purchaseProduct(@RequestParam Long shopId,
                                         @RequestParam Long productId,
                                         @RequestParam Integer desiredQuantity) {
        return StockResponse.toStockResponse(stockService.purchaseProduct(shopId, productId, desiredQuantity));
    }

}