package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video current = null;
  private boolean isPaused = false;

  private final TreeMap<String, VideoPlaylist> playlists = new TreeMap<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {

    TreeMap<String,Video> videos = new TreeMap<>();

    for (Video video : videoLibrary.getVideos()) {
      videos.put(video.getTitle(),video);
    }

    StringBuilder listOfVideos = new StringBuilder();

    for (Video video : videos.values()) {
      if (video.isFlagged()) {
        listOfVideos.append("  ").append(video.getTitle()).append(" ").append("(").append(video.getVideoId()).append(")").append(" ").append("[").append(String.join(" ",video.getTags())).append("]").append(" - FLAGGED (reason: ").append(video.getReason()).append(")").append("\n");
      } else {
        listOfVideos.append("  ").append(video.getTitle()).append(" ").append("(").append(video.getVideoId()).append(")").append(" ").append("[").append(String.join(" ",video.getTags())).append("]").append("\n");
      }
    }

    System.out.println("Here's a list of all available videos:\n" + listOfVideos);

  }

  public void playVideo(String videoId) {

    Video specifiedVideo = videoLibrary.getVideo(videoId);

    if (specifiedVideo != null) {
      if (!specifiedVideo.isFlagged()) {
        if (current == null) {
          System.out.println("Playing video: " + specifiedVideo.getTitle());
        } else {
          System.out.println("Stopping video: " + current.getTitle() + "\n" + "Playing video: " + specifiedVideo.getTitle());
        }
        current = specifiedVideo;
        isPaused = false;
      } else {
        System.out.println("Cannot play video: Video is currently flagged (reason: " + specifiedVideo.getReason() + ")");
      }
    } else {
      System.out.println("Cannot play video: Video does not exist");
    }

  }

  public void stopVideo() {

    if (current != null) {
      System.out.println("Stopping video: " + current.getTitle());
      current = null;
      isPaused = false;
    } else {
      System.out.println("Cannot stop video: No video is currently playing");
    }

  }

  public void playRandomVideo() {

    ArrayList<Video> availableVideos = new ArrayList<>();

    for (Video video : videoLibrary.getVideos()) {
      if (!video.isFlagged()) {
        availableVideos.add(video);
      }
    }

    if (!availableVideos.isEmpty()) {

      Random random = new Random();
      int number = random.nextInt(availableVideos.size());

      Video randomVideo = availableVideos.get(number);

      if (current == null) {
        System.out.println("Playing video: " + randomVideo.getTitle());
      } else {
        System.out.println("Stopping video: " + current.getTitle() + "\n" + "Playing video: " + randomVideo.getTitle());
      }
      current = randomVideo;
      isPaused = false;

    } else {
      System.out.println("No videos available");
    }

  }

  public void pauseVideo() {

    if (current != null) {
      if (!isPaused) {
        System.out.println("Pausing video: " + current.getTitle());
        isPaused = true;
      } else {
        System.out.println("Video already paused: " + current.getTitle());
      }
    } else {
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {

    if (current != null) {
      if (isPaused) {
        System.out.println("Continuing video: " + current.getTitle());
        isPaused = false;
      } else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    } else {
      System.out.println("Cannot continue video: No video is currently playing");
    }

  }

  public void showPlaying() {

    if (current != null) {
      if (!isPaused) {
        System.out.println("Currently playing: " + current.getTitle() + " " + "(" + current.getVideoId() + ")" + " " + "[" + String.join(" ",current.getTags()) + "]");
      } else {
        System.out.println("Currently playing: " + current.getTitle() + " " + "(" + current.getVideoId() + ")" + " " + "[" + String.join(" ",current.getTags()) + "]" + " - PAUSED");
      }
    } else {
      System.out.println("No video is currently playing");
    }

  }

  public void createPlaylist(String playlistName) {

    if (!playlists.containsKey(playlistName.toLowerCase())) {
      VideoPlaylist playlist = new VideoPlaylist(playlistName);
      playlists.put(playlistName.toLowerCase(),playlist);
      System.out.println("Successfully created new playlist: " + playlistName);
    } else {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {

    if (playlists.containsKey(playlistName.toLowerCase()) && videoLibrary.getVideo(videoId) != null) {

      if (!videoLibrary.getVideo(videoId).isFlagged()) {
        if (!(playlists.get(playlistName.toLowerCase()).getPlaylist().contains(videoLibrary.getVideo(videoId)))) {
          playlists.get(playlistName.toLowerCase()).addToPlaylist(videoLibrary.getVideo(videoId));
          System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
        } else {
          System.out.println("Cannot add video to " + playlistName + ": Video already added");
        }
      } else {
        System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
      }
    } else if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    } else if (videoLibrary.getVideo(videoId) == null) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
    }
  }

  public void showAllPlaylists() {

    if (playlists.size() == 0) {
      System.out.println("No playlists exist yet");
    } else {
      System.out.println("Showing all playlists:");

      StringBuilder builder = new StringBuilder();

      for (VideoPlaylist playlist : playlists.values()) {
        builder.append("  ").append(playlist.getName()).append("\n");
      }

      System.out.println(builder);

    }

  }

  public void showPlaylist(String playlistName) {

    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    } else {
      System.out.println("Showing playlist: " + playlistName);
      if (playlists.get(playlistName.toLowerCase()).getPlaylist().size() == 0) {
        System.out.println("  No videos here yet");
      } else {
        StringBuilder builder = new StringBuilder();

        for (Video video : playlists.get(playlistName.toLowerCase()).getPlaylist()) {
          if (video.isFlagged()) {
            builder.append("  ").append(video.getTitle()).append(" ").append("(").append(video.getVideoId()).append(")").append(" ").append("[").append(String.join(" ",video.getTags())).append("]").append(" - FLAGGED (reason: ").append(video.getReason()).append(")").append("\n");
          } else {
            builder.append("  ").append(video.getTitle()).append(" ").append("(").append(video.getVideoId()).append(")").append(" ").append("[").append(String.join(" ",video.getTags())).append("]").append("\n");
          }}

        System.out.print(builder);
      }
    }

  }

  public void removeFromPlaylist(String playlistName, String videoId) {

    if (playlists.containsKey(playlistName.toLowerCase()) && videoLibrary.getVideo(videoId) != null) {

      if ((playlists.get(playlistName.toLowerCase()).getPlaylist().contains(videoLibrary.getVideo(videoId)))) {
        playlists.get(playlistName.toLowerCase()).removeFromPlaylist(videoLibrary.getVideo(videoId));
        System.out.println("Removed video from " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
      } else {
        System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      }

    } else if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    } else if (videoLibrary.getVideo(videoId) == null) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
    }

  }

  public void clearPlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    } else {
      playlists.get(playlistName.toLowerCase()).clearPlaylist();
      System.out.println("Successfully removed all videos from " + playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    } else {
      playlists.get(playlistName.toLowerCase()).clearPlaylist();
      playlists.remove(playlistName.toLowerCase());
      System.out.println("Deleted playlist: " + playlistName);
    }
  }

  public void searchVideos(String searchTerm) {

    TreeMap<String,Video> videos = new TreeMap<>();

    for (Video video : videoLibrary.getVideos()) {
      if (video.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) && !video.isFlagged()) {
        videos.put(video.getTitle(),video);
      }
    }
    getSearchResults(searchTerm, videos);
  }

  public void searchVideosWithTag(String videoTag) {
    TreeMap<String,Video> videos = new TreeMap<>();

    for (Video video : videoLibrary.getVideos()) {
      if (video.getTags().toString().toLowerCase().contains(videoTag.toLowerCase()) && !video.isFlagged()) {
        videos.put(video.getTitle(),video);
      }
    }
    getSearchResults(videoTag, videos);
  }

  public void flagVideo(String videoId) {

    if (videoLibrary.getVideos().contains(videoLibrary.getVideo(videoId))) {
      if (!videoLibrary.getVideo(videoId).isFlagged()) {
        videoLibrary.getVideo(videoId).setFlag("Not supplied");
        if (current == videoLibrary.getVideo(videoId)) {
          System.out.println("Stopping video: " + videoLibrary.getVideo(videoId).getTitle());
          current = null;
        }
        System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
      } else {
        System.out.println("Cannot flag video: Video is already flagged");
      }
    } else {
      System.out.println("Cannot flag video: Video does not exist");
    }

  }

  public void flagVideo(String videoId, String reason) {
    if (videoLibrary.getVideos().contains(videoLibrary.getVideo(videoId))) {
      if (!videoLibrary.getVideo(videoId).isFlagged()) {
        videoLibrary.getVideo(videoId).setFlag(reason);
        if (current == videoLibrary.getVideo(videoId)) {
          System.out.println("Stopping video: " + videoLibrary.getVideo(videoId).getTitle());
          current = null;
        }
        System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
      } else {
        System.out.println("Cannot flag video: Video is already flagged");
      }
    } else {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {

    if (videoLibrary.getVideos().contains(videoLibrary.getVideo(videoId))) {
      if (videoLibrary.getVideo(videoId).isFlagged()) {
        System.out.println("Successfully removed flag from video: " + videoLibrary.getVideo(videoId).getTitle());
      } else {
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
    } else {
      System.out.println("Cannot remove flag from video: Video does not exist");
    }

  }

  public void getSearchResults(String searchTerm, TreeMap<String, Video> videos) {
    if (videos.size() == 0) {
      System.out.println("No search results for " + searchTerm);
    } else {
      StringBuilder searchedVideos = new StringBuilder();

      int count = 1;
      for (Video video : videos.values()) {
        searchedVideos.append("  ").append(count).append(") ").append(video.getTitle()).append(" ").append("(").append(video.getVideoId()).append(")").append(" ").append("[").append(String.join(" ", video.getTags())).append("]").append("\n");
        count += 1;
      }

      System.out.print("Here are the results for " + searchTerm + ":\n" + searchedVideos);

      System.out.println("Would you like to play any of the above? If yes, specify the number of the video. \nIf your answer is not a valid number," +
              " we will assume it's a no.");

      List<Video> videoList = new ArrayList<>(videos.values());

      var scanner = new Scanner(System.in);

      try {

        int input = Integer.parseInt(scanner.nextLine());

        if (input >= 1 && input <= (videoList.size() + 1)) {
          System.out.println("Playing video: " + videoList.get(input - 1).getTitle());
          current = videoList.get(input - 1);
          isPaused = false;
        }
      } catch (Exception ignored) {
      }
    }
  }
}