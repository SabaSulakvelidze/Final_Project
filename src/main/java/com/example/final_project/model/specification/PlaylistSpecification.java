package com.example.final_project.model.specification;

import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.PlaylistEntity;
import com.example.final_project.model.enums.MusicGenre;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSpecification {
    public static Specification<PlaylistEntity> searchPlaylists(String playlistName, String ownerName){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (playlistName != null && !playlistName.isEmpty()) predicates.add(criteriaBuilder.like(root.get("playlistName"), "%" + playlistName.toLowerCase() + "%"));
            if (StringUtils.hasText(ownerName))
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("owner", JoinType.LEFT).get("username")), "%" + ownerName.toLowerCase() + "%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
