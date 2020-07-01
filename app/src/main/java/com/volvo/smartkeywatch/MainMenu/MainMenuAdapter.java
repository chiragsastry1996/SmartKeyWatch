package com.volvo.smartkeywatch.MainMenu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.volvo.smartkeywatch.Door.DoorActivity;
import com.volvo.smartkeywatch.HVAC.HVACActivity;
import com.volvo.smartkeywatch.Headlamp.HeadlampActivity;
import com.volvo.smartkeywatch.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MyViewHolder> {

    private Context mContext;
    List<MainMenu> model;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public LinearLayout linearLayout;
        View circle1, circle2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            textView = (TextView)itemView.findViewById(R.id.function_name);
            imageView = (ImageView)itemView.findViewById(R.id.circledImageView);

        }
    }

    public MainMenuAdapter(Context mContext, List<MainMenu> model) {
        this.mContext = mContext;
        this.model = model;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.circle_view, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapter.MyViewHolder myViewHolder, final int position) {
//        Glide.with(mContext).asGif().load(R.raw.image_gif).into(myViewHolder.imageView);
        switch (position){
            case 0 : myViewHolder.textView.setText("Door Lock/Unlock");
                myViewHolder.imageView.setBackgroundResource(R.drawable.door_lock);
                break;
            case 1 : myViewHolder.textView.setText("Head Lamp");
                myViewHolder.imageView.setBackgroundResource(R.drawable.headlight);
                break;
            case 2 : myViewHolder.textView.setText("HVAC");
                myViewHolder.imageView.setBackgroundResource(R.drawable.hvac);
                break;
        }

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0) {
                    Intent intent = new Intent(mContext, DoorActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
                else if(position == 1) {
                    Intent intent = new Intent(mContext, HeadlampActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
                else if(position == 2) {
                    Intent intent = new Intent(mContext, HVACActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return 3;
    }

}


