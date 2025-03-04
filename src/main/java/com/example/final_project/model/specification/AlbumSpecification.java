package com.example.final_project.model.specification;

import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.enums.MusicGenre;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlbumSpecification {

    public static Specification<AlbumEntity> searchAlbums(String albumName){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (albumName != null && !albumName.isEmpty()) predicates.add(criteriaBuilder.like(root.get("albumName"), "%" + albumName.toLowerCase() + "%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
