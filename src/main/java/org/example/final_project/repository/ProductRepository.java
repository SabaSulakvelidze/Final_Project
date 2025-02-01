package org.example.final_project.repository;

import org.example.final_project.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.catalog.Catalog;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}

