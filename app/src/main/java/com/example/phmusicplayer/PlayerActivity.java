package com.example.phmusicplayer;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


import static com.example.phmusicplayer.AlbumDetailsAdapter.MyAlbums;
import static com.example.phmusicplayer.MainActivity.musicFiles;
import static com.example.phmusicplayer.MainActivity.repeatBoolean;
import static com.example.phmusicplayer.MainActivity.shuffleBoolean;
import static com.example.phmusicplayer.MusicAdapter.MyMusicFiles;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView start_duration, end_duration, song_name, song_artist;
    ImageView shuffle_btn, previous_btn, next_btn, repeat_btn, back_btn, menu_btn, song_img;
    FloatingActionButton play_btn;
    SeekBar seekBar;
    static Uri uri;
    static MediaPlayer player;
    static ArrayList<MusicFiles> list;
    int position = -1;
    Handler handler = new Handler();
    static Thread play,previous,next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        inivalues();
        progress();
        song_name.setText(list.get(position).getTitle());
        song_artist.setText(list.get(position).getArtist());
        player.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    player.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                    play_btn.setImageResource(R.drawable.ic_play);

                } else {
                    int currentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                    player.start();
                    play_btn.setImageResource(R.drawable.ic_pause);
                }
            }
        });
//
//        next_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                player.stop();
//                player.release();
//                position = (position + 1) % list.size();
//                uri = Uri.parse(list.get(position).getPath());
//                player = MediaPlayer.create(getApplicationContext(), uri);
//                player.start();
//                song_name.setText(list.get(position).getTitle());
//                song_artist.setText(list.get(position).getArtist());
//                dataSet(uri);
//            }
//        });
//        previous_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                player.stop();
//                player.release();
//                position = (position - 1) % list.size();
//                uri = Uri.parse(list.get(position).getPath());
//                player = MediaPlayer.create(getApplicationContext(), uri);
//                player.start();
//                song_name.setText(list.get(position).getTitle());
//                song_artist.setText(list.get(position).getArtist());
//                dataSet(uri);
//            }
//        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    int currentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                    start_duration.setText(formattedTime(currentPosition));

                }

                handler.postDelayed(this, 1000);
            }
        });
        shuffle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean){
                shuffleBoolean=false;
                shuffle_btn.setColorFilter(null);
                }else{
                    shuffleBoolean=true;
                    Resources res= getApplicationContext().getResources();
                    int colorId=res.getColor(R.color.shuffle_color);
                    shuffle_btn.setColorFilter(colorId);
                }
            }
        });
        repeat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatBoolean){
                    repeatBoolean=false;
                    repeat_btn.setColorFilter(null);
                }else{
                    repeatBoolean=true;
                    Resources res= getApplicationContext().getResources();
                    int colorId=res.getColor(R.color.repeat_color);
                    repeat_btn.setColorFilter(colorId);
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        playButton();
        previousButton();
        nextButton();
        super.onResume();
    }

    private void nextButton() {
        next=new Thread(){
            @Override
            public void run() {
                super.run();
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextbuttonClicked();
                    }
                });
            }
        };
        next.start();
    }

    private void nextbuttonClicked() {
        if(player.isPlaying()){
            player.stop();
            player.release();

            if(shuffleBoolean&&repeatBoolean){
                position = (position + 1) % list.size();
            }
            if(shuffleBoolean&&!repeatBoolean){
                position = (position + 1) % list.size();
            }
            if(!shuffleBoolean&&!repeatBoolean) {
                position = (position + 1) % list.size();
            }
            uri=Uri.parse(list.get(position).getPath());
            dataSet(uri);
            player=MediaPlayer.create(getApplicationContext(),uri);
            song_name.setText(list.get(position).getTitle());
            song_artist.setText(list.get(position).getArtist());
            seekBar.setMax(player.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player!=null) {
                        int currentPosition = player.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }

                    handler.postDelayed(this,1000);
                }
            });
            player.setOnCompletionListener(this);
            player.start();
            play_btn.setImageResource(R.drawable.ic_pause);
        }else{
            player.stop();
            player.release();
            if(shuffleBoolean&&repeatBoolean){
                position = (position + 1) % list.size();
            }
            if(shuffleBoolean&&!repeatBoolean){
                position = (position + 1) % list.size();
            }
            if(!shuffleBoolean&&!repeatBoolean) {
                position = (position + 1) % list.size();
            }
            uri=Uri.parse(list.get(position).getPath());
            dataSet(uri);
            player=MediaPlayer.create(getApplicationContext(),uri);
            song_name.setText(list.get(position).getTitle());
            song_artist.setText(list.get(position).getArtist());
            seekBar.setMax(player.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player!=null) {
                        int currentPosition = player.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }

                    handler.postDelayed(this,1000);
                }
            });
            player.setOnCompletionListener(this);
            player.start();
            play_btn.setImageResource(R.drawable.ic_pause);
        }

    }

    private void previousButton() {
        previous=new Thread(){
            @Override
            public void run() {
                super.run();
                previous_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousbuttonClicked();
                    }
                });
            }
        };
        previous.start();
    }

    private void previousbuttonClicked() {
        if(player.isPlaying()){
            player.stop();
            player.release();
            if(shuffleBoolean&&repeatBoolean){
                position=(position-1)<0 ? (list.size()-1):(position-1);
            }
            if(shuffleBoolean&&!repeatBoolean){
                position=(position-1)<0 ? (list.size()-1):(position-1);
            }
            if(!shuffleBoolean&&!repeatBoolean) {
                position=(position-1)<0 ? (list.size()-1):(position-1);
            }

            uri=Uri.parse(list.get(position).getPath());
            dataSet(uri);
            player=MediaPlayer.create(getApplicationContext(),uri);
            song_name.setText(list.get(position).getTitle());
            song_artist.setText(list.get(position).getArtist());
            seekBar.setMax(player.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player!=null) {
                        int currentPosition = player.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }

                    handler.postDelayed(this,1000);
                }
            });
            player.start();
            play_btn.setImageResource(R.drawable.ic_pause);
        }else{
            player.stop();
            player.release();
            if(shuffleBoolean&&repeatBoolean){
                position=(position-1)<0 ? (list.size()-1):(position-1);
            }
            if(shuffleBoolean&&!repeatBoolean){
                position=(position-1)<0 ? (list.size()-1):(position-1);
            }
            if(!shuffleBoolean&&!repeatBoolean) {
                position=(position-1)<0 ? (list.size()-1):(position-1);
            }
            uri=Uri.parse(list.get(position).getPath());
            dataSet(uri);
            player=MediaPlayer.create(getApplicationContext(),uri);
            song_name.setText(list.get(position).getTitle());
            song_artist.setText(list.get(position).getArtist());
            seekBar.setMax(player.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player!=null) {
                        int currentPosition = player.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }

                    handler.postDelayed(this,1000);
                }
            });
            player.start();
            play_btn.setImageResource(R.drawable.ic_pause);
        }

    }

    private void playButton() {
        play=new Thread(){
            @Override
            public void run() {
                super.run();
                play_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playbuttonClicked();
                    }
                });
            }
        };
        play.start();
    }

    private void playbuttonClicked() {
        if(player.isPlaying()){
            player.pause();
            play_btn.setImageResource(R.drawable.ic_play);
            seekBar.setMax(player.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player!=null) {
                        int currentPosition = player.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }

                    handler.postDelayed(this,1000);
                }
            });
        }else{
            play_btn.setImageResource(R.drawable.ic_pause);
            player.start();
            seekBar.setMax(player.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player!=null) {
                        int currentPosition = player.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }

                    handler.postDelayed(this,1000);
               }
            });
        }
    }

    public String formattedTime(int currentPosition) {
        String totalTime1 = "";
        String totalTime2 = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalTime1 = minutes + ":" + seconds;
        totalTime2 = minutes + ":0" + seconds;
        if (seconds.length() == 1) {
            return totalTime2;
        } else {
            return totalTime1;
        }
    }

    private void progress() {
        position = getIntent().getIntExtra("Parminder", -1);
        String sender=getIntent().getStringExtra("Sender");
        if(sender!=null&&sender.equals("yes")){
            list=MyAlbums;
        }else{
        list = MyMusicFiles;
        }
        if (list != null) {
            play_btn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(list.get(position).getPath());
        }

        if (player != null) {
            player.stop();
            player.release();
            player = MediaPlayer.create(getApplicationContext(), uri);
            player.start();
            //getduration() returns value in milliseconds that's why divide it by 1000
            seekBar.setMax(player.getDuration() / 1000);
        } else {
            player = MediaPlayer.create(getApplicationContext(), uri);
            player.start();
            //getduration() returns value in milliseconds that's why divide it by 1000
            seekBar.setMax(player.getDuration() / 1000);
        }
        dataSet(uri);
    }

    private void dataSet(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int totalDuration = Integer.parseInt(list.get(position).getDuration()) / 1000;
        end_duration.setText(formattedTime(totalDuration));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            ImageAnimation(this,song_img,bitmap);

            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        ImageView gradient = findViewById(R.id.gradient_img);
                        ConstraintLayout layout = findViewById(R.id.my_container);
                        gradient.setBackgroundResource(R.drawable.gradient_design);
                        layout.setBackgroundResource(R.drawable.main_background);
                        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        gradient.setBackground(drawable);
                        GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb()});
                        layout.setBackground(drawable1);
                        song_name.setTextColor(swatch.getTitleTextColor());
                        song_artist.setTextColor(swatch.getBodyTextColor());
                    } else {
                        ImageView gradient = findViewById(R.id.gradient_img);
                        ConstraintLayout layout = findViewById(R.id.my_container);
                        gradient.setBackgroundResource(R.drawable.gradient_design);
                        layout.setBackgroundResource(R.drawable.main_background);
                        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0x00000000});
                        gradient.setBackground(drawable);
                        GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        layout.setBackground(drawable);
                        song_name.setTextColor(Color.WHITE);
                        song_artist.setTextColor(Color.DKGRAY);
                    }
                }
            });
        } else {
            Glide.with(this).asBitmap().load(R.drawable.download).into(song_img);
            ImageView gradient = findViewById(R.id.gradient_img);
            ConstraintLayout layout = findViewById(R.id.my_container);
            gradient.setBackgroundResource(R.drawable.gradient_design);
            layout.setBackgroundResource(R.drawable.main_background);

            song_name.setTextColor(Color.WHITE);
            song_artist.setTextColor(Color.DKGRAY);
        }
    }

    private void inivalues() {
        start_duration = findViewById(R.id.start_duration);
        end_duration = findViewById(R.id.end_duration);
        shuffle_btn = findViewById(R.id.shuffle_btn);
        previous_btn = findViewById(R.id.previous_btn);
        next_btn = findViewById(R.id.next_btn);
        repeat_btn = findViewById(R.id.repeat_btn);
        back_btn = findViewById(R.id.back_btn);
        play_btn = findViewById(R.id.play_btn);
        seekBar = findViewById(R.id.seekBar);
        song_img = findViewById(R.id.song_img);
        song_name = findViewById(R.id.song_name);
        song_artist = findViewById(R.id.song_artist);

    }

    private void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap) {
        final Animation animIn= AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
        final Animation animOut=AnimationUtils.loadAnimation(context,android.R.anim.fade_out);

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).asBitmap().load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextbuttonClicked();
        player.setOnCompletionListener(this);
    }
}