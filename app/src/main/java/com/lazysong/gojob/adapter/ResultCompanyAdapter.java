package com.lazysong.gojob.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.view.activity.PostInfoDetailActivity;

import java.util.List;
import java.util.Random;

/**
 * Created by lazysong on 2017/5/31.
 */
public class ResultCompanyAdapter extends RecyclerView.Adapter<PostInfoItem> {
    private List<PostInformation> postList;
    private Context context;

    public ResultCompanyAdapter(List<PostInformation> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }
    @Override
    public PostInfoItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.listview_item_recommand_postinfo, parent, false);
        PostInfoItem viewCache = new PostInfoItem(itemView);
        return viewCache;
    }

    @Override
    public void onBindViewHolder(PostInfoItem holder, int position) {
        final PostInformation postInfo = postList.get(position);
        holder.txtPostitionName.setText(postInfo.getPosition_type());
        holder.txtCompanyName.setText(postInfo.getCompany_name());
        holder.txtWorkPlace.setText(postInfo.getWork_place());
        holder.txtSalary.setText(postInfo.getSalary_month());
        holder.txtPostTime.setText(postInfo.getPost_date().toString());
        holder.txtWatchCount.setText(new Random().nextInt(1000) + "");
        holder.linearayoutPostInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("postId", postInfo.getPost_id());
                Gson gson = new Gson();
                intent.putExtra("postInfo", gson.toJson(postInfo));
                intent.setClass(context, PostInfoDetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
