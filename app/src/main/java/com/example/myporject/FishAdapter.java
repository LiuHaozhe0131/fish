package com.example.myporject;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public class FishAdapter extends BaseQuickAdapter<FishData, BaseViewHolder> {
    private Context context;
    private IFishListener listener;
    public FishAdapter(Context context, IFishListener listener) {
        super(R.layout.item);
        this.context =context;
        this.listener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FishData goldFishBean) {

        if (goldFishBean.isCheck()){
            ImageView imageView = baseViewHolder.getView(R.id.image);
            Glide.with(context).load(goldFishBean.getPic()).into(imageView);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation);
            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
            animation.setInterpolator(lin);
            imageView.startAnimation(animation);
        }else {
            ImageView imageView = baseViewHolder.getView(R.id.image);
            Glide.with(context).load(R.drawable.tileback_g).into(imageView);
        }
        baseViewHolder.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.OnFishClick(goldFishBean);
                }
            }
        });
    }
}
