package com.example.final_project;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.facade.PlaylistFacade;
import com.example.final_project.model.entity.*;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.PlaylistRequest;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.model.response.PlaylistResponse;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.model.response.statistics.PlayCountStat;
import com.example.final_project.model.response.statistics.StatisticsResponse;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.PlaylistService;
import com.example.final_project.service.StatisticsService;
import com.example.final_project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistFacadeTest {

    @Mock
    private PlaylistService playlistService;
    @Mock
    private UserService userService;
    @Mock
    private MusicService musicService;
    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private PlaylistFacade playlistFacade;

    private UserEntity mockUser() {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .email("user@example.com")
                .userRole(UserRole.ARTIST)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    private PlaylistEntity mockPlaylist(UserEntity owner) {
        return PlaylistEntity.builder()
                .id(1L)
                .playlistName("Test Playlist")
                .owner(owner)
                .musicList(new HashSet<>())
                .build();
    }

    private MusicEntity mockMusic(UserEntity author) {
        return MusicEntity.builder()
                .id(2L)
                .musicName("Test Music")
                .musicGenre(MusicGenre.ROCK)
                .author(author)
                .playlists(new ArrayList<>())
                .build();
    }

    @Test
    void addPlaylist_shouldReturnPlaylistResponse() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::getCurrentUserId).thenReturn(1L);

            UserEntity currentUser = mockUser();
            PlaylistRequest request = new PlaylistRequest();
            request.setMusicIdList(Set.of(2L));
            PlaylistEntity playlistEntity = mockPlaylist(currentUser);
            MusicEntity musicEntity = mockMusic(mockUser());

            when(userService.findUserById(1L)).thenReturn(currentUser);
            when(playlistService.save(any())).thenReturn(playlistEntity);
            when(musicService.findMusicById(2L)).thenReturn(musicEntity);
            when(playlistService.save(playlistEntity)).thenReturn(playlistEntity);

            PlaylistResponse response = playlistFacade.addPlaylist(request);

            assertEquals("Test Playlist", response.getPlaylistName());
        }
    }

    @Test
    void updatePlaylistById_shouldUpdatePlaylist() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            PlaylistEntity playlistEntity = mockPlaylist(mockUser());
            PlaylistRequest request = new PlaylistRequest();

            when(playlistService.findPlaylistById(1L)).thenReturn(playlistEntity);
            when(playlistService.updatePlaylistById(eq(1L), any())).thenReturn(playlistEntity);

            PlaylistResponse response = playlistFacade.updatePlaylistById(1L, request);

            assertEquals("Test Playlist", response.getPlaylistName());
        }
    }

    @Test
    void addMusicToPlaylist_shouldAddMusic() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            PlaylistEntity playlistEntity = mockPlaylist(mockUser());
            MusicEntity musicEntity = mockMusic(mockUser());

            when(playlistService.findPlaylistById(1L)).thenReturn(playlistEntity);
            when(musicService.findMusicById(2L)).thenReturn(musicEntity);

            playlistFacade.addMusicToPlaylist(1L, Set.of(2L));

            assertTrue(playlistEntity.getMusicList().contains(musicEntity));
            verify(playlistService).save(playlistEntity);
        }
    }

    @Test
    void removeMusicFromPlaylist_shouldRemoveMusic() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            PlaylistEntity playlistEntity = mockPlaylist(mockUser());
            MusicEntity musicEntity = mockMusic(mockUser());
            playlistEntity.getMusicList().add(musicEntity);

            when(playlistService.findPlaylistById(1L)).thenReturn(playlistEntity);
            when(musicService.findMusicById(2L)).thenReturn(musicEntity);

            playlistFacade.removeMusicFromPlaylist(1L, Set.of(2L));

            assertFalse(playlistEntity.getMusicList().contains(musicEntity));
            verify(playlistService).save(playlistEntity);
        }
    }

    @Test
    void suggestPlaylistForCurrentUser_shouldReturnSuggestions() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::getCurrentUserId).thenReturn(1L);

            UserEntity user = mockUser();
            PlaylistEntity playlistEntity = mockPlaylist(user);
            MusicEntity musicEntity = mockMusic(mockUser());
            musicEntity.getPlaylists().add(playlistEntity);

            PlayCountStat playCountStat = new PlayCountStat(MusicResponse.toMusicResponse(musicEntity), 10);
            StatisticsResponse statsResponse = new StatisticsResponse(UserResponse.toUserResponse(user), Set.of(playCountStat));

            when(statisticsService.generatePlayCountReport()).thenReturn(List.of(statsResponse));
            when(musicService.getMusicEntities(any(), any())).thenReturn(new PageImpl<>(List.of(musicEntity)));

            List<PlaylistResponse> suggestions = playlistFacade.suggestPlaylistForCurrentUser();

            assertEquals(1, suggestions.size());
            assertEquals("Test Playlist", suggestions.getFirst().getPlaylistName());
        }
    }

    @Test
    void deletePlaylistById_shouldCallDelete() {
        playlistFacade.deletePlaylistById(1L);

        verify(playlistService).deletePlaylistIdById(1L);
    }

    @Test
    void getPlaylists_shouldReturnPageOfPlaylists() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "playlistName"));
        Page<PlaylistEntity> playlistPage = new PageImpl<>(Collections.emptyList());

        when(playlistService.getPlaylists(any(), eq(pageRequest))).thenReturn(playlistPage);

        Page<PlaylistResponse> result = playlistFacade.getPlaylists(
                "My Playlist", "ownerUser", 0, 10, Sort.Direction.ASC, "playlistName"
        );

        assertEquals(0, result.getTotalElements());
        verify(playlistService).getPlaylists(any(), eq(pageRequest));
    }

    @Test
    void findPlaylistById_shouldReturnPlaylistResponse() {
        PlaylistEntity playlistEntity = mockPlaylist(mockUser());

        when(playlistService.findPlaylistById(1L)).thenReturn(playlistEntity);

        PlaylistResponse response = playlistFacade.findPlaylistById(1L);

        assertEquals("Test Playlist", response.getPlaylistName());
        verify(playlistService).findPlaylistById(1L);
    }

}
