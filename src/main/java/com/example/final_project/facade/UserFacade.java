package com.example.final_project.facade;

import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.ArtistResponse;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.model.specification.MusicSpecification;
import com.example.final_project.model.specification.UserSpecification;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final MusicService musicService;

    public UserResponse findUserById(Long userId) {
        UserEntity userById = userService.findUserById(userId);
        if (userById.getUserRole() == UserRole.ARTIST) {
            HashMap<Long, String> similarArtists = getSimilarArtists(userById);
            return ArtistResponse.toArtistResponse(userById, similarArtists);
        } else
            return UserResponse.toUserResponse(userById);
    }

    public Page<UserResponse> getUsers(String username, String email, UserRole userRole, UserStatus userStatus,
                                       Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return userService.getUsers(
                        UserSpecification.searchUsers(username, email, userRole, userStatus),
                        PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)))
                .map(UserResponse::toUserResponse);
    }

    public UserResponse updateUserById(Long userId, UserRequest userRequest) {
        return UserResponse.toUserResponse(userService.updateUserById(userId, UserEntity.toUserEntity(userRequest)));
    }

    public void deleteUserById(Long userId) {
        userService.deleteUserById(userId);
    }

    private HashMap<Long, String> getSimilarArtists(UserEntity artistEntity) {
        PageRequest pageRequest = PageRequest.of(0, 1000, Sort.Direction.ASC, "id");

        //get artists genresList
        List<MusicEntity> list1 = musicService
                .getMusicEntities(MusicSpecification.searchMusicEntities(null, null, artistEntity.getUsername(), null),
                        pageRequest)
                .getContent();
        Set<MusicGenre> genres = list1.stream().map(MusicEntity::getMusicGenre).collect(Collectors.toSet());
        //get all music entities with genres
        List<MusicEntity> list2 = musicService.getMusicEntities(MusicSpecification.hasGenres(genres), pageRequest).getContent();
        //get & return artists who have these genre musics
        return list2.stream()
                .filter(m -> !Objects.equals(m.getAuthor().getId(), artistEntity.getId()))
                .collect(Collectors.toMap(m -> m.getAuthor().getId(), m -> m.getAuthor().getUsername(),
                        (existing, replacement) -> existing, HashMap::new));
    }
}
