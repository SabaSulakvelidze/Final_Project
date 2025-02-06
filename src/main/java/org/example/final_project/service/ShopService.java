package org.example.final_project.service;


import lombok.RequiredArgsConstructor;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.ShopEntity;
import org.example.final_project.repository.ShopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Page<ShopEntity> getAllShops(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy){
        return shopRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public ShopEntity getShopById(Long shopId){
        return shopRepository.findById(shopId)
                .orElseThrow(()-> new ResourceNotFoundException("shop with id %d was not found".formatted(shopId)));
    }

    public ShopEntity createShop(ShopEntity shopEntity){
        return shopRepository.save(shopEntity);
    }

    public ShopEntity editShop(Long shopId, ShopEntity shopEntity){
        ShopEntity shopToEdit = getShopById(shopId);
        shopToEdit.setName(shopEntity.getName());
        shopToEdit.setStocks(shopEntity.getStocks());
        return shopToEdit;
    }

    public String deleteShop(Long shopId){
        shopRepository.delete(getShopById(shopId));
        return "shop with id %d was deleted successfully".formatted(shopId);
    }
}
