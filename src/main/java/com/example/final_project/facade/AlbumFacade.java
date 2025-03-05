package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.request.AlbumRequest;
import com.example.final_project.model.response.AlbumResponse;
import com.example.final_project.model.specification.AlbumSpecification;
import com.example.final_project.service.AlbumService;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbumFacade {

    private final AlbumService albumService;
    private final MusicService musicService;
    private final UserService userService;

    @Transactional
    public AlbumResponse addAlbum(AlbumRequest albumRequest) {
        UserEntity currentUser = userService.findUserById(Utils.getCurrentUserId());
        AlbumEntity albumEntity = albumService.save(AlbumEntity.toAlbumEntity(albumRequest, currentUser));
        addMusicToAlbum(albumRequest, albumEntity);
        return AlbumResponse.toAlbumResponse(albumEntity);
    }

    public AlbumResponse findAlbumById(Long albumId) {
        return AlbumResponse.toAlbumResponse(albumService.findAlbumById(albumId));
    }

    public Page<AlbumResponse> getAlbums(String albumName, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return albumService.getAlbums(
                        AlbumSpecification.searchAlbums(albumName),
                        PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)))
                .map(AlbumResponse::toAlbumResponse);
    }


    @Transactional
    public AlbumResponse updateAlbumById(Long albumId, AlbumRequest albumRequest) {
        AlbumEntity albumById = albumService.findAlbumById(albumId);
        Utils.checkIfCurrentUserIsOwner(albumById.getOwner().getUsername());
        addMusicToAlbum(albumRequest, albumById);
        return AlbumResponse.toAlbumResponse(albumService.updateAlbumById(albumId, AlbumEntity.toAlbumEntity(albumRequest, albumById.getOwner())));
    }

    public void deleteAlbumById(Long albumId) {
        AlbumEntity albumById = albumService.findAlbumById(albumId);
        Utils.checkIfCurrentUserIsOwner(albumById.getOwner().getUsername());
        albumService.deleteAlbumIdById(albumId);
    }

    private void addMusicToAlbum(AlbumRequest albumRequest, AlbumEntity albumEntity) {
        if (albumRequest.getMusicIdList() != null && !albumRequest.getMusicIdList().isEmpty())
            albumRequest.getMusicIdList().forEach(musicId -> {
                MusicEntity musicById = musicService.findMusicById(musicId);
                if (musicById.getAlbum() != null)
                    throw new CustomException(HttpStatus.BAD_REQUEST, "music with id %d exist in album with id %d".formatted(musicById.getId(), musicById.getAlbum().getId()));
                musicById.setAlbum(albumEntity);
                musicService.save(musicById);
            });
    }
}
