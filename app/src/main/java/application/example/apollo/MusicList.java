package application.example.apollo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicList extends RecyclerView.Adapter<MusicList.ViewHolder> {
    ArrayList<AutoModel> musicsList;
    Context context;

    public MusicList(ArrayList<AutoModel> musicsList, Context context) {
        this.musicsList = musicsList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_item,parent,false);
        return new MusicList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicList.ViewHolder holder, int position) {
        AutoModel musicData = musicsList.get(position);
        holder.titleTextView.setText(musicData.getTitle());

        if(PlayMedia.currentIndex==position){
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMedia.getInstance().reset();
                PlayMedia.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayActivity.class);
                intent.putExtra("LIST",musicsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageView;
        public  ViewHolder(View itemView){
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageView = itemView.findViewById(R.id.icon_view);
        }
    }
}

