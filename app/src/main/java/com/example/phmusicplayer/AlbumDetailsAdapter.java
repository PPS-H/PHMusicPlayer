package com.example.phmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {
    Context context;
    static ArrayList<MusicFiles> MyAlbums;
    public AlbumDetailsAdapter(Context context, ArrayList<MusicFiles> MyAlbums) {
        this.context=context;
        this.MyAlbums=MyAlbums;
    }

    @NonNull
    @Override
    public AlbumDetailsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.songs_items,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.MyHolder holder, final int position) {
        holder.song_name.setText(MyAlbums.get(position).getTitle());
        //because the data type of the arrayList is musicFiles so we can use the methods of
        //musicFile class......
        final byte img[]=setImage(MyAlbums.get(position).getPath());
        if(img!=null){
            Glide.with(context).asBitmap().load(img).into(holder.album_img);
        }else{
            Glide.with(context).asBitmap().load(R.drawable.download).into(holder.album_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PlayerActivity.class);
                intent.putExtra("Sender","yes");
                intent.putExtra("Parminder",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return MyAlbums.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView song_name;
        ImageView album_img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_img=itemView.findViewById(R.id.SongImage);
            song_name=itemView.findViewById(R.id.SongText);
        }
    }
    public byte[]setImage(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[]art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
