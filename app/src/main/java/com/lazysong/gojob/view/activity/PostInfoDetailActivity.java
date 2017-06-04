package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.PostInformation;

import java.util.Date;

public class PostInfoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private PostInformation postInformation;
    private TextView txtCompanyName;
    private TextView txtPositionName;
    private TextView txtSalary;
    private TextView txtWelfare;
    private TextView txtWorkplace;
    private TextView txtEduReq;
    private TextView txtWorkType;
    private TextView txtWorkExperience;
    private TextView txtPostTime;
    private TextView txtMarkCount;
    private ImageView imgMark;
    private TextView titleToolbar;
    private Toolbar toolbar;

    private String userId;
    private int markCount;
    private int postInfoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info_detail);

        initViews();
        Intent intent = getIntent();
        String postInfoStr = intent.getStringExtra("postInfo");
        //TODO 获取userId，并查询是否传递了userid

        final Gson gson = new Gson();
        postInformation = gson.fromJson(postInfoStr, PostInformation.class);
        postInfoId = postInformation.getPost_id();
        showPostinfoOnView();
    }

    private void showPostinfoOnView() {
        if (postInformation != null) {
            txtCompanyName.setText(postInformation.getCompany_name());
            txtPositionName.setText(postInformation.getPosition_type());
            txtSalary.setText(postInformation.getSalary_month());
//            txtWelfare
            txtWorkplace.setText(postInformation.getWork_place());
            txtEduReq.setText(postInformation.getEducation_requirement());
            txtWorkType.setText(postInformation.getWork_type());
            txtWorkExperience.setText(postInformation.getExperience_requirement());
            Date date = postInformation.getPost_date();
            txtPostTime.setText(date.getMonth() + "月" + date.getDay());
//            txtMarkCount.setText();
            imgMark.setOnClickListener(this);
        }
    }

    private void initViews() {
        titleToolbar = (TextView) findViewById(R.id.titleToolbar);
        titleToolbar.setText("详情");
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.pre);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostInfoDetailActivity.this.finish();
            }
        });

        txtCompanyName = (TextView) findViewById(R.id.txtCompanyNameDetail);
        txtPositionName = (TextView) findViewById(R.id.txtPositionNameDetail);
        txtSalary = (TextView) findViewById(R.id.txtSalaryDetail);
        txtWelfare = (TextView) findViewById(R.id.txtWelfareDetail);
        txtWorkplace = (TextView) findViewById(R.id.txtWorkplaceDetail);
        txtEduReq = (TextView) findViewById(R.id.txtEduReqDetail);
        txtWorkType = (TextView) findViewById(R.id.txtWorkTypeDetail);
        txtWorkExperience = (TextView) findViewById(R.id.txtWorkExperienceetail);
        txtPostTime = (TextView) findViewById(R.id.txtPostTimeDetail);

        txtMarkCount = (TextView) findViewById(R.id.txtMarkCountDetail);
        markCount = getMarkCount(postInfoId);
        txtMarkCount.setText(markCount + "人已收藏");

        imgMark = (ImageView) findViewById(R.id.imgMarkDetail);
        boolean isMarked = infoIsMarked(userId, postInfoId);
        if(isMarked) {
            imgMark.setImageResource(R.drawable.icon_mark2);
            imgMark.setTag(R.id.tag_info_is_marked, true);
        }
        else {
            imgMark.setImageResource(R.drawable.icon_mark1);
            imgMark.setTag(R.id.tag_info_is_marked, false);
        }
        imgMark.setOnClickListener(this);

    }

    private boolean infoIsMarked(String userId, int post_id) {
        //TODO 查询该条信息是否已经被收藏
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgMark:
                if ((boolean)imgMark.getTag(R.id.tag_info_is_marked)) {
                    //TODO 取消收藏

                    imgMark.setImageResource(R.drawable.icon_mark1);
                    imgMark.setTag(R.id.tag_info_is_marked, false);
                    markCount--;
                    txtMarkCount.setText(markCount + "人已收藏");
                }
                else {
                    //TODO 收藏该条信息
                    imgMark.setImageResource(R.drawable.icon_mark2);
                    imgMark.setTag(R.id.tag_info_is_marked, true);
                    markCount++;
                    txtMarkCount.setText(markCount + "人已收藏");
                }
                break;
        }
    }

    public int getMarkCount(int postInfoId) {
        //TODO 获取该条信息被收藏的数目
        return markCount;
    }
}
