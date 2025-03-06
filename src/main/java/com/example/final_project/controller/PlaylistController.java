package com.example.final_project.controller;

import com.example.final_project.facade.PlaylistFacade;
import com.example.final_project.model.request.PlaylistRequest;
import com.example.final_project.model.response.PlaylistResponse;
import com.example.final_project.model.response.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistFacade playlistFacade;

    @PostMapping("/addPlaylist")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<PlaylistResponse> addPlaylist(@RequestBody @Valid PlaylistRequest playlistRequest) {
        return new ResponseEntity<>(playlistFacade.addPlaylist(playlistRequest), HttpStatus.CREATED);
    }

    @GetMapping("/getPlaylist/{playlistId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<PlaylistResponse> getPlaylist(@PathVariable("playlistId") Long playlistId) {
        return new ResponseEntity<>(playlistFacade.findPlaylistById(playlistId), HttpStatus.OK);
    }

    @GetMapping("/getPlaylists")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<PagedResponse<PlaylistResponse>> getPlaylistEntities(
            @RequestParam(required = false) String playlistName,
            @RequestParam(required = false) String ownerName,

            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(PagedResponse.of(playlistFacade.getPlaylists(
                playlistName, ownerName,
                pageNumber, pageSize, direction, sortBy)), HttpStatus.OK);
    }

    @PutMapping("/addMusicToPlaylist")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<String> addMusicToPlaylist(@RequestParam Long playlistId,
                                                     @RequestParam Set<Long> musicId) {
        playlistFacade.addMusicToPlaylist(playlistId, musicId);
        return ResponseEntity.ok().body("music with id %s added to playlist %s".formatted(musicId, playlistId));
    }

    @PutMapping("/removeMusicFromPlaylist")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<String> removeMusicFromPlaylist(@RequestParam Long playlistId,
                                                          @RequestParam Set<Long> musicId) {
        playlistFacade.removeMusicFromPlaylist(playlistId, musicId);
        return ResponseEntity.ok().body("music with id %s removed from playlist %s".formatted(musicId, playlistId));
    }

    @PutMapping("/update/{playlistId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public PlaylistResponse updatePlaylistEntity(@PathVariable("playlistId") Long playlistId,
                                                 @RequestBody PlaylistRequest playlistRequest) {
        return playlistFacade.updatePlaylistById(playlistId, playlistRequest);
    }

    @DeleteMapping("/delete/{playlistId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<Void> deletePlaylist(@PathVariable("playlistId") Long playlistId) {
        playlistFacade.deletePlaylistById(playlistId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getSuggestedPlaylists")
    @PreAuthorize("hasAnyRole('ADMIN','ARTIST','LISTENER')")
    public ResponseEntity<List<PlaylistResponse>> getSuggestedPlaylists() {
        List<PlaylistResponse> playlistResponses = playlistFacade.suggestPlaylistForCurrentUser();
        return ResponseEntity.ok().body(playlistResponses);
    }
}
