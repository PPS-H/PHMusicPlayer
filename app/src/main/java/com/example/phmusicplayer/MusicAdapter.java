package com.example.phmusicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    Context context;
    static ArrayList<MusicFiles> MyMusicFiles;
    public MusicAdapter(Context context, ArrayList<MusicFiles> musicFiles) {
        this.context=context;
        this.MyMusicFiles=musicFiles;
    }

    @NonNull
    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.songs_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyViewHolder holder, final int position) {
        holder.textView.setText(MyMusicFiles.get(position).getTitle());
        //because the data type of the arrayList is musicFiles so we can use the methods of
        //musicFile class......
        final byte img[]=setImage(MyMusicFiles.get(position).getPath());
        if(img!=null){
            Glide.with(context).asBitmap().load(img).into(holder.imageView);
        }else{
            Glide.with(context).asBitmap().load(R.drawable.download).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PlayerActivity.class);
                intent.putExtra("Parminder",position);
                context.startActivity(intent);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu=new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_options,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                deleteFile(position,v);
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }
    private void deleteFile(int position,View view){
        Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        Long.parseLong(MyMusicFiles.get(position).getId()));
        File file=new File(MyMusicFiles.get(position).getPath());
        boolean deleted=file.delete();
        if (deleted){
        context.getContentResolver().delete(contentUri,null,null);
        MyMusicFiles.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,MyMusicFiles.size());
        Toast.makeText(context, "Song deleted!!!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Can't delete this song!!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return MyMusicFiles.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView,menu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.SongText);
            imageView=itemView.findViewById(R.id.SongImage);
            menu=itemView.findViewById(R.id.menu);
        }
    }
    public byte[]setImage(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[]art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    public void updateSongs(ArrayList<MusicFiles> music){
        MyMusicFiles=new ArrayList<>();
        MyMusicFiles.addAll(music);
        notifyDataSetChanged();
    }
}
