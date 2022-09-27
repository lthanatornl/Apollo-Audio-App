package application.example.apollo;
import android.media.MediaPlayer;

public class PlayMedia {
    static MediaPlayer instance;
    public static  MediaPlayer getInstance(){
        if(instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }
    public static  int currentIndex = -1;
}
