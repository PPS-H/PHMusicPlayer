 package com.example.phmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

 public class Gradient extends AppCompatActivity {
    ImageView img;
    TextView text;
    Button button;
     Animation image,textView,Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
        img=findViewById(R.id.img);
        text=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        image= AnimationUtils.loadAnimation(this,R.anim.image);
        textView=AnimationUtils.loadAnimation(this,R.anim.text);
        Button=AnimationUtils.loadAnimation(this,R.anim.button);
        img.startAnimation(image);
        text.startAnimation(textView);
        button.startAnimation(Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}