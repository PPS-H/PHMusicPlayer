package com.example.phmusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.phmusicplayer.MainActivity.musicFiles;

public class AlbumDetails extends AppCompatActivity {
    ImageView imageView;
    RecyclerView recyclerView;
    String albumName;
    ArrayList<MusicFiles> MyAlbumSongs=new ArrayList<>();
    AlbumDetailsAdapter albumDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        imageView=findViewById(R.id.song_img);
        recyclerView=findViewById(R.id.my_layout);
        albumName=getIntent().getStringExtra("album name");
        int j=0;
        for(int i=0;i<musicFiles.size();i++){
            if(albumName.equals(musicFiles.get(i).getAlbum())){
                MyAlbumSongs.add(j,musicFiles.get(i));
                j++;
            }
        }
        byte[] art=setImage(MyAlbumSongs.get(0).getPath());
        if(art!=null){
            Glide.with(this).asBitmap().load(art).into(imageView);
        }else{
            Glide.with(this).asBitmap().load(R.drawable.download).into(imageView);
        }
        albumDetailsAdapter=new AlbumDetailsAdapter(this,MyAlbumSongs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(albumDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,
                false));
    }
    public byte[]setImage(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[]art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}