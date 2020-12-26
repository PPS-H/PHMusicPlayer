package com.example.phmusicplayer;

public class MusicFiles {
    private String title;
    private String path;
    private String duration;
    private String album;
    private String artist;
    private String Id;

    public MusicFiles(String title, String path, String duration, String album, String artist,String Id) {
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.album = album;
        this.artist = artist;
        this.Id=Id;
    }
    public MusicFiles(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
