package org.example.final_project.Service;

import jakarta.annotation.PostConstruct;
import org.example.final_project.model.entity.CatalogEntity;
import org.example.final_project.model.entity.ProductEntity;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.repository.CatalogRepository;
import org.example.final_project.repository.ProductRepository;
import org.example.final_project.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CatalogService {

    private CatalogRepository catalogRepository;
    private ProductRepository productRepository;
    private ShopRepository shopRepository;

    public CatalogService(CatalogRepository catalogRepository, ProductRepository productRepository, ShopRepository shopRepository) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    @PostConstruct
    public void init() {
        ProductEntity product1 = new ProductEntity();
        product1.setProductName("Product #1");
        product1.setManufacturer("Manufacturer #1");
        product1.setManufactureDate(LocalDate.of(2020,10,1));

        ProductEntity product2 = new ProductEntity();
        product2.setProductName("Product #2");
        product2.setManufacturer("Manufacturer #2");
        product2.setManufactureDate(LocalDate.of(2021,1,1));


        productRepository.save(product1);
        productRepository.save(product2);


        ShopEntity shop1 = new ShopEntity();
        shop1.setShopName("Shop #1");

        ShopEntity shop2 = new ShopEntity();
        shop2.setShopName("Shop #2");

        shopRepository.save(shop1);
        shopRepository.save(shop2);


        CatalogEntity catalogEntity1 = new CatalogEntity();
        catalogEntity1.setProductEntity(product1);
        catalogEntity1.setShopEntity(shop1);
        catalogEntity1.setQuantity(1);
        catalogEntity1.setPrice(99.9);

        CatalogEntity catalogEntity2 = new CatalogEntity();
        catalogEntity2.setProductEntity(product2);
        catalogEntity2.setShopEntity(shop1);
        catalogEntity2.setQuantity(3);
        catalogEntity2.setPrice(10.0);

        CatalogEntity catalogEntity3 = new CatalogEntity();
        catalogEntity3.setProductEntity(product1);
        catalogEntity3.setShopEntity(shop2);
        catalogEntity3.setQuantity(2);
        catalogEntity3.setPrice(80.0);

        CatalogEntity catalogEntity4 = new CatalogEntity();
        catalogEntity4.setProductEntity(product2);
        catalogEntity4.setShopEntity(shop2);
        catalogEntity4.setQuantity(4);
        catalogEntity4.setPrice(20.5);

        catalogRepository.save(catalogEntity1);
        catalogRepository.save(catalogEntity2);
        catalogRepository.save(catalogEntity3);
        catalogRepository.save(catalogEntity4);



    }
}
