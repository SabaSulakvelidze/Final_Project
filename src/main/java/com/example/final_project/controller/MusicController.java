package com.example.final_project.controller;

import com.example.final_project.facade.MusicFacade;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.request.MusicRequest;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicFacade musicFacade;

    @GetMapping("/getMusic/{musicId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MusicResponse> getUser(@PathVariable("musicId") Long musicId) {
        return new ResponseEntity<>(musicFacade.findMusicById(musicId), HttpStatus.OK);
    }

    @GetMapping("/getMusicEntities")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PagedResponse<MusicResponse>> getMusicEntities(
            @RequestParam(required = false) String musicName,
            @RequestParam(required = false) MusicGenre musicGenre,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String albumName,

            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(PagedResponse.of(musicFacade.getMusicEntities(
                musicName, musicGenre, authorName, albumName,
                pageNumber, pageSize, direction, sortBy)), HttpStatus.OK);
    }

    @PutMapping("/update/{musicId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public MusicResponse updateMusicEntity(@PathVariable("musicId") Long musicId,
                                           @RequestBody MusicRequest musicRequest) {
        return musicFacade.updateMusicById(musicId, musicRequest);
    }

    @DeleteMapping("/delete/{musicId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteMusic(@PathVariable("musicId") Long musicId) {
        musicFacade.deleteMusicById(musicId);
        return ResponseEntity.ok().build();
    }
}
