package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.request.MusicRequest;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.repository.MusicRepository;
import com.example.final_project.service.AlbumService;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MusicFacade {
    private final MusicService musicService;
    private final UserService userService;
    private final AlbumService albumService;

    public MusicResponse addMusicEntity(MusicRequest musicRequest) {
        UserEntity currentUser = userService.findUserById(Utils.getPrincipalDatabaseId());
        if (musicRequest.getAlbum_id() != null)
            return MusicResponse.toMusicResponse(musicService.save(MusicEntity.toMusicEntity(musicRequest, currentUser, albumService.findAlbumById(musicRequest.getAlbum_id()))));
        else
            return MusicResponse.toMusicResponse(musicService.save(MusicEntity.toMusicEntity(musicRequest, currentUser)));
    }

    public Page<MusicResponse> findAllMusicByName(String musicName, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return musicService.findAllMusicByName(musicName, pageNumber, pageSize, direction, sortBy).map(MusicResponse::toMusicResponse);
    }

    public Page<MusicResponse> findAllMusic(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return musicService.findAllMusic(pageNumber, pageSize, direction, sortBy).map(MusicResponse::toMusicResponse);
    }

    public MusicResponse findMusicById(Long musicId) {
        return MusicResponse.toMusicResponse(musicService.findMusicById(musicId));
    }

    public MusicResponse updateMusicById(Long musicId, MusicRequest musicRequest) {
        return null;
    }

    public void deleteMusicById(Long musicId) {
        musicService.deleteMusicById(musicId);
    }
}
