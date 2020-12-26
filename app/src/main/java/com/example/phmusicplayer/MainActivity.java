package com.example.phmusicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    static ArrayList<MusicFiles> musicFiles;
    public static final int Request_Code=1;
    static boolean shuffleBoolean=false,repeatBoolean=false;
    static ArrayList<MusicFiles> albums=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},Request_Code);
        }else{
            musicFiles=getAllSongs(this);
        }
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.ViewPager);
        //ViewPagerAdapter is used to fix the fragment into layout
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(),"Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(),"Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==Request_Code){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //write code for what you want to do
                musicFiles=getAllSongs(this);
            }else{
                ActivityCompat.requestPermissions(this,new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},Request_Code);
            }
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments=new ArrayList<>();
        private ArrayList<String> titles=new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        //Here,,,fragments and songs are added into arrayList
        public void addFragments(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            //add fragments into the arrayList
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        //it is compulsary to show the tabs (Songs & Albums)
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    public ArrayList<MusicFiles> getAllSongs(Context context){
        ArrayList<String> duplicate=new ArrayList<>();
        ArrayList<MusicFiles> tempSongsList=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] DATA={
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA, //For Path
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };

        Cursor cursor=context.getContentResolver().query(uri,DATA,null,
                null,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String title=cursor.getString(0);
                String data=cursor.getString(1);
                String duration=cursor.getString(2);
                String album=cursor.getString(3);
                String artist=cursor.getString(4);
                String Id=cursor.getString(5);
                MusicFiles musicFiles=new MusicFiles(title,data,duration,album,artist,Id);
                tempSongsList.add(musicFiles);
                if(!duplicate.contains(album)){
                    albums.add(musicFiles);
                    duplicate.add(album);
                }
            }
            cursor.close();
        }
        return tempSongsList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput=newText.toLowerCase();
        ArrayList<MusicFiles> MyList=new ArrayList<>();
        for(MusicFiles songs:musicFiles){
            if(songs.getTitle().toLowerCase().contains(userInput)){
                MyList.add(songs);
            }
        }
        SongsFragment.musicAdapter.updateSongs(MyList);
        return true;
    }
}