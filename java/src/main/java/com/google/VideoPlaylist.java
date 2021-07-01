package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {

    private final String name;
    private ArrayList<Video> playlist = new ArrayList<>();


    VideoPlaylist(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public ArrayList<Video> getPlaylist() {
        return playlist;
    }

    public void addToPlaylist(Video video) {
        playlist.add(video);
    }

    public void removeFromPlaylist(Video video) {
        playlist.remove(video);
    }

    public void clearPlaylist() {
        playlist.clear();
    }
}
