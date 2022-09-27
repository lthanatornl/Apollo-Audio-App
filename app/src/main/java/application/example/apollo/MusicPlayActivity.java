package application.example.apollo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayActivity extends AppCompatActivity {
    TextView title,currentTime,totalTime;
    SeekBar seekBar;
    ImageView pausePlay,next,previous,musicIcon;
    ArrayList<AutoModel> musicsList;
    AutoModel currentMusic;
    MediaPlayer mediaPlayer = PlayMedia.getInstance();
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        title = findViewById(R.id.music_title);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seekbar);
        pausePlay = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        musicIcon = findViewById(R.id.apollo_media_icon);
        title.setSelected(true);

        musicsList = (ArrayList<AutoModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        MusicPlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        musicIcon.setRotation(x++);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        musicIcon.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    void setResourcesWithMusic(){
        currentMusic = musicsList.get(PlayMedia.currentIndex);
        title.setText(currentMusic.getTitle());
        totalTime.setText(convertToMMSS(currentMusic.getDuration()));
        pausePlay.setOnClickListener(v-> pausePlay());
        next.setOnClickListener(v-> playNext());
        previous.setOnClickListener(v-> playPrevious());

        playMusic();
    }


    private void playMusic(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentMusic.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playNext(){

        if(PlayMedia.currentIndex== musicsList.size()-1)
            return;
        PlayMedia.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPrevious(){
        if(PlayMedia.currentIndex== 0)
            return;
        PlayMedia.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}