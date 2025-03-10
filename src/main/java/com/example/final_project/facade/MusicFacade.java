package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.model.entity.*;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.request.MusicRequest;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.model.specification.MusicSpecification;
import com.example.final_project.service.AlbumService;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.StatisticsService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MusicFacade {
    private final MusicService musicService;
    private final UserService userService;
    private final AlbumService albumService;
    private final StatisticsService statisticsService;

    public MusicResponse addMusicEntity(MusicRequest musicRequest) {
        UserEntity currentUser = userService.findUserById(Utils.getCurrentUserId());
        AlbumEntity albumById = musicRequest.getAlbumId() != null ? albumService.findAlbumById(musicRequest.getAlbumId()) : null;
        return MusicResponse.toMusicResponse(musicService.save(MusicEntity.toMusicEntity(musicRequest, currentUser, albumById)));
    }

    public Page<MusicResponse> getMusicEntities(String musicName, MusicGenre musicGenre, String authorName, String albumName,
                                                Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return musicService.getMusicEntities(
                        MusicSpecification.searchMusicEntities(musicName, musicGenre, authorName, albumName),
                        PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy))).
                map(MusicResponse::toMusicResponse);
    }

    public MusicResponse findMusicById(Long musicId) {
        MusicEntity musicById = musicService.findMusicById(musicId);
        UserEntity currentUser = userService.findUserById(Utils.getCurrentUserId());

        StatisticsEntity build = StatisticsEntity.builder()
                .statisticsId(StatisticsId.builder().userId(currentUser.getId()).musicId(musicById.getId()).build())
                .user(currentUser)
                .music(musicById)
                .build();
        statisticsService.increasePlayCount(build);
        return MusicResponse.toMusicResponse(musicById);
    }

    @Transactional
    public MusicResponse updateMusicById(Long musicId, MusicRequest musicRequest) {
        Utils.checkIfCurrentUserIsOwner(musicService.findMusicById(musicId).getAuthor().getUsername());
        UserEntity newAuthor = musicRequest.getAuthorId() != null ? userService.findUserById(musicRequest.getAuthorId()) : null;
        AlbumEntity albumEntity = musicRequest.getAlbumId() != null ? albumService.findAlbumById(musicRequest.getAlbumId()) : null;
        MusicEntity musicEntity = MusicEntity.toMusicEntity(musicRequest, newAuthor, albumEntity);
        return MusicResponse.toMusicResponse(musicService.updateMusicById(musicId, musicEntity));
    }

    public void deleteMusicById(Long musicId) {
        Utils.checkIfCurrentUserIsOwner(musicService.findMusicById(musicId).getAuthor().getUsername());
        musicService.deleteMusicById(musicId);
    }
}
