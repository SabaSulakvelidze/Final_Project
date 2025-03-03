package com.example.final_project.model.specification;

import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MusicSpecification {

    public static Specification<MusicEntity> searchMusicEntities(String musicName, MusicGenre musicGenre, String authorName, String albumName){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (musicName != null && !musicName.isEmpty()) predicates.add(criteriaBuilder.like(root.get("musicName"), "%" + musicName.toLowerCase() + "%"));
            if (musicGenre != null) predicates.add(criteriaBuilder.equal(root.get("musicGenre"), musicGenre));
            if (authorName != null && !authorName.isEmpty()) predicates.add(criteriaBuilder.like(root.get("authorName"), "%" + authorName.toLowerCase() + "%"));
            if (albumName != null && !albumName.isEmpty()) predicates.add(criteriaBuilder.like(root.get("albumName"), "%" + albumName.toLowerCase() + "%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
