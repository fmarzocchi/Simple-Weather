package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.HourlyApi.List;
import com.example.weatherapp.HourlyApi.NextDaysModel;

public class TodayRecAdapter extends RecyclerView.Adapter<TodayRecAdapter.TodayViewHolder> {

    Context context;
    NextDaysModel nextDaysModel;

    public TodayRecAdapter(NextDaysModel nextDaysModel,Context context) {
        this.nextDaysModel = nextDaysModel;
        this.context = context;
    }

    @NonNull
    @Override
    public TodayRecAdapter.TodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today_recycler,
                parent,false);
        return new TodayRecAdapter.TodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayRecAdapter.TodayViewHolder holder, int position) {

        List list = nextDaysModel.getList().get(position);
        int i = Tools.getWeatherIcon(list.getWeather().get(0).getIcon(),false);
        // ho creato la classe UnixConverter per applicare questo metodo sia qui che nella MainActivity
        // ovviamente il metodo converte il tempo da Unix a DateTime
        holder.timeText.setText(Tools.unixConverter(
                list.getDt()));
        holder.tempText.setText(Math.round(list.getMain().getTemp())+"°");
        Glide.with(context)
                .load(i)
                .into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class TodayViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        TextView tempText;
        ImageView icon;

        public TodayViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.today_hour_textview);
            tempText = itemView.findViewById(R.id.today_temperature_textview);
            icon = itemView.findViewById(R.id.today_icon_imageview);

        }
    }

}
