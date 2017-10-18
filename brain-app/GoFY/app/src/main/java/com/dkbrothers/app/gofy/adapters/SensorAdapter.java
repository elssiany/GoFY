package com.dkbrothers.app.gofy.adapters;


import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.dkbrothers.app.gofy.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by kevin.
 */

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ItemViewHolder> {


    private List<Map<String,Object>> listDatas =new ArrayList<>();
    private Context context;
    private LayoutInflater ensamblador = null;



    public SensorAdapter(Context context) {
        this.context = context;
        this.ensamblador = LayoutInflater.from(this.context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                allowAnimate = false;
            }
        }, 100);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescription, txtTitle,txtNow;
        View itemView;
        ItemViewHolder(View itemView) {
            super(itemView);
             txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
             txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            txtNow = (TextView) itemView.findViewById(R.id.txt_now);
            this.itemView = itemView;

        }

    }


    @Override
    public SensorAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SensorAdapter.ItemViewHolder(ensamblador.inflate(R.layout.item_sensor, parent, false));
    }


    @Override
    public void onBindViewHolder(final SensorAdapter.ItemViewHolder holder, final int position) {

        final Map<String,Object> data = listDatas.get(position);

            holder.txtTitle.setText(data.get("name").toString());

            //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            //dateFormat.format(getDate((long) data.get("fecha")))
            holder.txtDescription.setText("  ► "+data.get("description").toString());
            holder.txtNow.setText(DateUtils.getRelativeTimeSpanString(
                    (long) data.get("dateTime")));

            setAnimation(holder.itemView, position);

    }


    private Date getDate(long dateInMillis){
        return  new Date(dateInMillis);
    }


    @Override
    public int getItemCount() {
        return listDatas.size();
    }


    public void addDatas(List<Map<String,Object>> listDatas){
        this.listDatas =listDatas;
        notifyDataSetChanged();
    }


    public void removeDatas(){
        listDatas.clear();
    }


    private boolean allowAnimate = true;


    private void setAnimation(final View viewToAnimate, int position) {
        //animate for list fade in
        if (!allowAnimate) {
            return;
        }
        viewToAnimate.setVisibility(View.INVISIBLE);
        final Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        int TRANSLATION_FAST = 1600;
        animation.setDuration(TRANSLATION_FAST);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewToAnimate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewToAnimate.startAnimation(animation);
            }
        }, (position + 2) * 60);
    }


}
