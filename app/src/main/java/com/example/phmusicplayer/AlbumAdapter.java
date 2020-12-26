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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {
    Context context;
    ArrayList<MusicFiles> MyAlbums;
    public AlbumAdapter(Context context, ArrayList<MusicFiles> MyAlbums) {
        this.context=context;
        this.MyAlbums=MyAlbums;
    }

    @NonNull
    @Override
    public AlbumAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.album_files,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyHolder holder, final int position) {
        holder.album_name.setText(MyAlbums.get(position).getAlbum());
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
                Intent intent=new Intent(context,AlbumDetails.class);
                intent.putExtra("album name",MyAlbums.get(position).getAlbum());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyAlbums.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView album_name;
        ImageView album_img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_img=itemView.findViewById(R.id.album_img);
            album_name=itemView.findViewById(R.id.album_name);
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
