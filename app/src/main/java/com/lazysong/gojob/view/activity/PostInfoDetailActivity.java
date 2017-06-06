package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.LocalInfoManager;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.utils.MarkInfoTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostInfoDetailActivity extends AppCompatActivity implements View.OnClickListener, MarkInfoTask.OnDataGotListener {
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

    private LocalInfoManager localManager;
    private boolean logined;
    private String userId;
    private int markCount;
    private int postInfoId;
    private MarkInfoTask markTask;
    private MarkInfoTask unmarkTask;
    private MarkInfoTask countMarkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info_detail);

        initViews();
        Intent intent = getIntent();
        String postInfoStr = intent.getStringExtra("postInfo");
        //TODO 获取userId，并查询是否传递了userid
        loadLoginState();
        if (logined) {
            userId = localManager.getUserId();
        }
        //TODO 移除测试信息
        logined = true;

        final Gson gson = new Gson();
        postInformation = gson.fromJson(postInfoStr, PostInformation.class);
        postInfoId = postInformation.getPost_id();
        showPostinfoOnView();
    }

    private void loadLoginState() {
        localManager = new LocalInfoManager(this);
        JSONObject jsonObject = localManager.getLogPref();
        try {
            logined = jsonObject.getBoolean("logined");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        toolbar.setNavigationIcon(R.mipmap.md_nav_back);
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
        loadMarkCount(postInfoId);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!logined) {
            Toast.makeText(PostInfoDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (id) {
            case R.id.imgMarkDetail:
                if ((boolean)imgMark.getTag(R.id.tag_info_is_marked)) {
                    //TODO 取消收藏
                    List<Map<String, String>> params = getParamList("1", postInfoId + "");
                    markTask = new MarkInfoTask(RequestCode.UNMARK_POST, params, this);
                    markTask.execute();
                }
                else {
                    //TODO 收藏该条信息
                    List<Map<String, String>> params = getParamList("1", postInfoId + "");
                    unmarkTask = new MarkInfoTask(RequestCode.MARK_POST, params, this);
                    unmarkTask.execute();
                }
                break;
        }
    }

    private boolean infoIsMarked(String userId, int post_id) {
        //TODO 查询该条信息是否已经被收藏
        return false;
    }
    public void loadMarkCount(int postInfoId) {
        //TODO 获取该条信息被收藏的数目
        List<Map<String, String>> params = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "POST_ID");
        map.put("value", postInfoId + "");
        params.add(map);
        countMarkTask = new MarkInfoTask(RequestCode.QUERY_COUNT_MARK_POST, params, this);
        countMarkTask.execute();
    }

    class MarkCount {
        @SerializedName("count")
        private int count;

        public MarkCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    @Override
    public void onDataGot(int requestcode, String result) {
        Gson gson = new Gson();
        if (result.equals("Update success\n")) {
            if (requestcode == RequestCode.MARK_POST) {
                imgMark.setImageResource(R.drawable.icon_mark2);
                imgMark.setTag(R.id.tag_info_is_marked, true);
                markCount++;
                txtMarkCount.setText(markCount + "人已收藏");
            }
            else if (requestcode == RequestCode.UNMARK_POST) {
                imgMark.setImageResource(R.drawable.icon_mark1);
                imgMark.setTag(R.id.tag_info_is_marked, false);
                markCount--;
                txtMarkCount.setText(markCount + "人已收藏");
            }
        }
        else if (result.equals("Update fail\n")) {
            Toast.makeText(PostInfoDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
        }
        if (requestcode == RequestCode.QUERY_COUNT_MARK_POST) {
            ArrayList<MarkCount> markCountObj = gson.fromJson(result, new TypeToken<ArrayList<MarkCount>>(){}.getType());
            markCount = markCountObj.get(0).getCount();
            txtMarkCount.setText(markCount + "人收藏");
        }


    }

    public List<Map<String, String>> getParamList(String userId, String postId) {
        List<Map<String, String>> params = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "USER_ID");
        map.put("value", userId);
        params.add(map);
        map = new HashMap<>();
        map.put("name", "POST_ID");
        map.put("value", postId);
        params.add(map);
        return params;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (markTask != null)
            markTask.cancel(true);
        if (unmarkTask != null) {
            unmarkTask.cancel(true);
        }
        if (countMarkTask != null) {
            countMarkTask.cancel(true);
        }
    }
}
