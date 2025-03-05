package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.PlaylistEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.request.PlaylistRequest;
import com.example.final_project.model.response.PlaylistResponse;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.PlaylistService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaylistFacade {
    private final PlaylistService playlistService;
    private final UserService userService;
    private final MusicService musicService;

    public PlaylistResponse addPlaylist(PlaylistRequest playlistRequest) {
        UserEntity currentUser = userService.findUserById(Utils.getCurrentUserId());
        PlaylistEntity playlistEntity = playlistService.save(PlaylistEntity.toPlayListEntity(playlistRequest, currentUser));
        if (playlistRequest.getMusicIdList() != null && !playlistRequest.getMusicIdList().isEmpty())
            playlistRequest.getMusicIdList().forEach(musicId -> playlistEntity.getMusicList().add(musicService.findMusicById(musicId)));
        return PlaylistResponse.toPlaylistResponse(playlistEntity);
    }

    public Page<PlaylistResponse> getPlaylists(String playlistName, String ownerName,
                                               Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return playlistService.getPlaylists(
                playlistName, ownerName,
                pageNumber, pageSize, direction, sortBy).map(PlaylistResponse::toPlaylistResponse);
    }

    public PlaylistResponse findPlaylistById(Long playlistId) {
        return PlaylistResponse.toPlaylistResponse(playlistService.findPlaylistById(playlistId));
    }

    @Transactional
    public PlaylistResponse updatePlaylistById(Long playlistId, PlaylistRequest playlistRequest) {
        Utils.checkIfCurrentUserIsOwner(playlistService.findPlaylistById(playlistId).getOwner().getUsername());
        return PlaylistResponse.toPlaylistResponse(playlistService.updatePlaylistById(playlistId, PlaylistEntity.toPlayListEntity(playlistRequest, null)));
    }

    @Transactional
    public void addMusicToPlaylist(Long playlistId, Set<Long> musicIds) {
        Utils.checkIfCurrentUserIsOwner(playlistService.findPlaylistById(playlistId).getOwner().getUsername());
        PlaylistEntity playlistEntity = playlistService.findPlaylistById(playlistId);
        musicIds.forEach(musicId -> {
            MusicEntity musicEntity = musicService.findMusicById(musicId);
            playlistEntity.getMusicList().add(musicEntity);
        });
        playlistService.save(playlistEntity);
    }

    @Transactional
    public void removeMusicFromPlaylist(Long playlistId, Set<Long> musicIds) {
        Utils.checkIfCurrentUserIsOwner(playlistService.findPlaylistById(playlistId).getOwner().getUsername());
        PlaylistEntity playlistEntity = playlistService.findPlaylistById(playlistId);
        musicIds.forEach(musicId -> {
            MusicEntity musicEntity = musicService.findMusicById(musicId);
            playlistEntity.getMusicList().remove(musicEntity);
        });
        playlistService.save(playlistEntity);
    }

    public void deletePlaylistById(Long playlistId) {
        playlistService.deletePlaylistIdById(playlistId);
    }
}
