package com.example.final_project.facade;

import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.request.AlbumRequest;
import com.example.final_project.model.response.AlbumResponse;
import com.example.final_project.service.AlbumService;
import com.example.final_project.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbumFacade {

    private final AlbumService albumService;
    private final MusicService musicService;

    public AlbumResponse addAlbum(AlbumRequest albumRequest) {
        AlbumEntity albumEntity = albumService.save(AlbumEntity.toAlbumEntity(albumRequest));
        if (albumRequest.getMusicIdList() != null && !albumRequest.getMusicIdList().isEmpty())
            albumRequest.getMusicIdList().forEach(musicId -> {
                MusicEntity musicById = musicService.findMusicById(musicId);
                musicById.setAlbum(albumEntity);
                musicService.save(musicById);
            });
        return AlbumResponse.toAlbumResponse(albumEntity);
    }

    public AlbumResponse findAlbumById(Long albumId) {
        return AlbumResponse.toAlbumResponse(albumService.findAlbumById(albumId));
    }

    public Page<AlbumResponse> getAlbums(String albumName, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return albumService.getAlbums(albumName, pageNumber, pageSize, direction, sortBy).map(AlbumResponse::toAlbumResponse);
    }

    @Transactional
    public AlbumResponse updateAlbumById(Long albumId, AlbumRequest albumRequest) {
        AlbumEntity albumById = albumService.findAlbumById(albumId);
        if (albumRequest.getMusicIdList() != null && !albumRequest.getMusicIdList().isEmpty())
            albumRequest.getMusicIdList().forEach(musicId -> musicService.findMusicById(musicId).setAlbum(albumById));
        return AlbumResponse.toAlbumResponse(albumService.updateAlbumById(albumId, AlbumEntity.toAlbumEntity(albumRequest)));
    }

    public void deleteAlbumById(Long albumId) {
        albumService.deleteAlbumIdById(albumId);
    }
}
