package com.lazysong.gojob.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lazysong.gojob.R;

/**
 * Created by lazysong on 2017/5/31.
 */
public class PostInfoItem extends RecyclerView.ViewHolder {
    public ImageView imgCompanyLogo;
    public TextView txtPostitionName;
    public TextView txtCompanyName;
    public TextView txtWorkPlace;
    public TextView txtSalary;
    public TextView txtPostTime;
    public TextView txtWatchCount;
    public ImageView imgToDetail;
    public LinearLayout linearayoutPostInfo;

    public PostInfoItem(View itemView) {
        super(itemView);
        imgCompanyLogo = (ImageView) itemView.findViewById(R.id.imgCompanyLogo);
        txtPostitionName = (TextView) itemView.findViewById(R.id.txtPositionName);
        txtCompanyName = (TextView) itemView.findViewById(R.id.txtCompanyName);
        txtWorkPlace = (TextView) itemView.findViewById(R.id.txtWorkPlace);
        txtSalary = (TextView) itemView.findViewById(R.id.txtSalary);
        txtPostTime = (TextView) itemView.findViewById(R.id.txtPostTime);
        txtWatchCount = (TextView) itemView.findViewById(R.id.txtWatchNum);
        imgToDetail = (ImageView) itemView.findViewById(R.id.imgToDetail);
        linearayoutPostInfo = (LinearLayout) itemView.findViewById(R.id.linearayoutPostInfo);
    }
}