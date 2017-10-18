package com.dkbrothers.app.gofy.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.models.Guide;

import java.util.List;


/**
 * Created by kevin on 22/12/2016.
 */

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.GuideViewHolder> {


    private List<Guide> listGuides;
    private Context context;
    private LayoutInflater ensamblador = null;

    //views
    Drawable rectangulo;

    public GuidesAdapter(Context context, List<Guide> listGuides) {
        this.context = context;
        this.listGuides = listGuides;
        this.ensamblador = LayoutInflater.from(this.context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                allowAnimate = false;
            }
        }, 100);
    }





    class GuideViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescription, txtTitle;
        View itemView;
        GuideViewHolder(View itemView) {
            super(itemView);

             txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
             txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            //contanerViews = (LinearLayout) itemView.findViewById(R.id.txt_description);
            this.itemView = itemView;

        }

    }


    @Override
    public GuidesAdapter.GuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuidesAdapter.GuideViewHolder(ensamblador.inflate(R.layout.item_user, parent, false));
    }


    @Override
    public void onBindViewHolder(final GuidesAdapter.GuideViewHolder holder, final int position) {

        final Guide guide = listGuides.get(position);

        holder.txtTitle.setText(guide.getTitle());
        holder.txtDescription.setText(guide.getDescription());
        if (guide.isShowDescription()) {
            holder.txtDescription.setVisibility(View.VISIBLE);
            //holder.imgIndicador.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
        } else {
            //holder.imgIndicador.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            holder.txtDescription.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(guide.isShowDescription()) {
                    guide.setShowDescription(false);
                    //holder.txtDescription.setVisibility(View.VISIBLE);
                    //holder.imgIndicador.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                } else {
                    guide.setShowDescription(true);
                    //holder.imgIndicador.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                    //holder.txtDescription.setVisibility(View.GONE);
                }
                listGuides.set(holder.getAdapterPosition(),guide);
                notifyItemChanged(holder.getAdapterPosition());
                //notifyDataSetChanged();
            }
        });


        //setAnimation(holder.itemView, position);

    }





    @Override
    public int getItemCount() {
        return listGuides.size();
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
