package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.PlaylistEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.request.AlbumRequest;
import com.example.final_project.model.request.PlaylistRequest;
import com.example.final_project.model.response.PlaylistResponse;
import com.example.final_project.model.response.statistics.PlayCountStat;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.model.specification.MusicSpecification;
import com.example.final_project.model.specification.PlaylistSpecification;
import com.example.final_project.repository.PlaylistRepository;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.PlaylistService;
import com.example.final_project.service.StatisticsService;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistFacade {
    private final PlaylistService playlistService;
    private final UserService userService;
    private final MusicService musicService;
    private final StatisticsService statisticsService;

    @Transactional
    public PlaylistResponse addPlaylist(PlaylistRequest playlistRequest) {
        UserEntity currentUser = userService.findUserById(Utils.getCurrentUserId());
        PlaylistEntity playlistEntity = playlistService.save(PlaylistEntity.toPlayListEntity(playlistRequest, currentUser));
        addMusicToPlaylist(playlistRequest,playlistEntity);
        return PlaylistResponse.toPlaylistResponse(playlistEntity);
    }

    public Page<PlaylistResponse> getPlaylists(String playlistName, String ownerName,
                                               Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return playlistService.getPlaylists(
                        PlaylistSpecification.searchPlaylists(playlistName, ownerName),
                        PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)))
                .map(PlaylistResponse::toPlaylistResponse);
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

    public void addMusicToPlaylist(PlaylistRequest playlistRequest, PlaylistEntity playlistEntity) {
        if (playlistRequest.getMusicIdList() != null && !playlistRequest.getMusicIdList().isEmpty())
            playlistRequest.getMusicIdList().forEach(musicId -> {
                MusicEntity musicById = musicService.findMusicById(musicId);
                if (musicById.getPlaylists().stream().map(PlaylistEntity::getId).toList().contains(playlistEntity.getId()))
                    throw new CustomException(HttpStatus.BAD_REQUEST, "music with id %d already exists in playList with id %d".formatted(musicById.getId(), playlistEntity.getId()));
                playlistEntity.getMusicList().add(musicById);
                playlistService.save(playlistEntity);
            });
    }

    public List<PlaylistResponse> suggestPlaylistForCurrentUser() {
        List<PlaylistResponse> result = new ArrayList<>();
        //find current users stats
        StatisticsResponse currentUserStats = statisticsService.generatePlayCountReport()
                .stream().filter(p -> Objects.equals(p.getUser().getId(), Utils.getCurrentUserId()))
                .toList().getFirst();
        //group music user listens to by genres and play counts
        Map<MusicGenre, Integer> playCountByGenre  = currentUserStats.getPlayCountStats().stream()
                .collect(Collectors.groupingBy(stat -> stat.getMusic().getMusicGenre(), Collectors.summingInt(PlayCountStat::getPlayCount)));
        //sort music by play count and get top 3
        List<Map.Entry<MusicGenre, Integer>> topGenres = playCountByGenre.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .toList();
        //get random playlist for each genre
        topGenres.forEach(entry -> {
            List<MusicEntity> musicEntitiesByGenre = musicService.getMusicEntities(
                    MusicSpecification.searchMusicEntities(null, entry.getKey(), null, null),
                    PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))).getContent()
                    //filter music with playlists
                    .stream().filter(m -> m.getPlaylists() != null && !m.getPlaylists().isEmpty()).toList();
            //find playlist of random music from list and add to result
            List<PlaylistEntity> playlists = musicEntitiesByGenre.get(new Random().nextInt(musicEntitiesByGenre.size())).getPlaylists();
            PlaylistEntity playlistEntity = playlists.get(new Random().nextInt(playlists.size()));
            result.add(PlaylistResponse.toPlaylistResponse(playlistEntity));
        });

        return result;
    }
}
