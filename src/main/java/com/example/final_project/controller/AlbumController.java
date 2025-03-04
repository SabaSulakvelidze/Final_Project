package com.example.final_project.controller;

import com.example.final_project.facade.AlbumFacade;
import com.example.final_project.model.request.AlbumRequest;
import com.example.final_project.model.response.AlbumResponse;
import com.example.final_project.model.response.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumFacade albumFacade;

    @PostMapping("/addAlbum")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST')")
    public ResponseEntity<AlbumResponse> addAlbum(@RequestBody @Valid AlbumRequest albumRequest) {
        AlbumResponse albumResponse = albumFacade.addAlbum(albumRequest);
        return new ResponseEntity<>(albumFacade.findAlbumById(albumResponse.getAlbumId()), HttpStatus.CREATED);
    }

    @GetMapping("/getAlbum/{albumId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<AlbumResponse> getAlbum(@PathVariable("albumId") Long albumId) {
        return new ResponseEntity<>(albumFacade.findAlbumById(albumId), HttpStatus.OK);
    }

    @GetMapping("/getAlbums")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<PagedResponse<AlbumResponse>> getAlbums(
            @RequestParam(required = false) String albumName,

            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(PagedResponse.of(albumFacade.getAlbums(
                albumName,
                pageNumber, pageSize, direction, sortBy)), HttpStatus.OK);
    }

    @PutMapping("/update/{albumId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST')")
    public AlbumResponse updateAlbum(@PathVariable("albumId") Long albumId,
                                           @RequestBody AlbumRequest albumRequest) {
        return albumFacade.updateAlbumById(albumId, albumRequest);
    }

    @DeleteMapping("/delete/{albumId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST')")
    public ResponseEntity<Void> deleteAlbum(@PathVariable("albumId") Long albumId) {
        albumFacade.deleteAlbumById(albumId);
        return ResponseEntity.ok().build();
    }
}
