package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.beans.Industry_category;
import com.lazysong.gojob.module.beans.Place;
import com.lazysong.gojob.utils.BaseAsyncTask;
import com.lazysong.gojob.utils.GetAllTask;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class SelectTagActivity extends AppCompatActivity implements BaseAsyncTask.HandleView{
    private TagContainerLayout tagGroupIndustry;
    private TagContainerLayout tagGroupPlace;
    private Toolbar toolbar;
    private TextView titleToolbar;
    private ImageView imgFinishSelectTag;

    private List<Place> listPlace;
    private List<Industry_category> listIndustry;
    private ArrayList<String> listPlaceSelected = new ArrayList<>();
    private ArrayList<String> listIndustrySelected = new ArrayList<>();
    private TagView tagView;
    private GetAllTask placeTask;
    private GetAllTask industryTask;

    public static final int COLOR_PRIMARY = 0xFF00d196;
    public static final int RESULT_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        tagGroupIndustry = (TagContainerLayout) findViewById(R.id.tag_group_industry);
        tagGroupPlace = (TagContainerLayout) findViewById(R.id.tag_group_place);
        imgFinishSelectTag = (ImageView) findViewById(R.id.imgFinishSelectTag);
        imgFinishSelectTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("listPlaceSelected", listPlaceSelected);
                intent.putStringArrayListExtra("listIndustrySelected", listIndustrySelected);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        titleToolbar = (TextView) findViewById(R.id.titleToolbar);
        titleToolbar.setText("选择标签");
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.mipmap.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTagActivity.this.finish();
            }
        });

        prepareTagData();
    }

    private void initTagView(TagContainerLayout tagGroup) {
        TagView tagview;
        List<String> tagStrs = tagGroup.getTags();
        for (int i = 0; i < tagStrs.size(); i++) {
            tagview = tagGroup.getTagView(i);
            tagview.setTag(R.id.tag_info_is_marked, false);
        }
    }

    class MyOnTagClickListener implements TagView.OnTagClickListener {
        private TagContainerLayout tagGroup;

        public MyOnTagClickListener(TagContainerLayout tagGroup) {
            this.tagGroup = tagGroup;
        }
        @Override
        public void onTagClick(int position, String text) {
            tagView = tagGroup.getTagView(position);
            if ((boolean)tagView.getTag(R.id.tag_info_is_marked)) {
                tagView.setTagTextColor(Color.BLACK);
                tagView.setTagBackgroundColor(Color.WHITE);
                tagView.setTag(R.id.tag_info_is_marked, false);
                unselectTag(text, tagGroup);
                tagView.postInvalidate();
            }
            else {
                tagView.setTagTextColor(Color.WHITE);
                tagView.setTagBackgroundColor(COLOR_PRIMARY);
                tagView.setTag(R.id.tag_info_is_marked, true);
                tagView.postInvalidate();
                selectTag(text, tagGroup);
            }
        }

        @Override
        public void onTagLongClick(int position, String text) {

        }

        @Override
        public void onTagCrossClick(int position) {

        }
    }

    private void unselectTag(String text, TagContainerLayout tagGroup) {
        int id = tagGroup.getId();
        if (id == R.id.tag_group_place) {
            if (listPlaceSelected.contains(text)) {
                listPlaceSelected.remove(text);
            }
        }
        else if (id == R.id.tag_group_industry) {
            if (listIndustrySelected.contains(text)) {
                listIndustrySelected.remove(text);
            }
        }
    }

    private void selectTag(String text, TagContainerLayout tagGroup) {
        int id = tagGroup.getId();
        if (id == R.id.tag_group_place) {
            if (!listPlaceSelected.contains(text)) {
                listPlaceSelected.add(text);
            }
        }
        else if (id == R.id.tag_group_industry) {
            if (!listIndustrySelected.contains(text)) {
                listIndustrySelected.add(text);
            }
        }
    }

    private void prepareTagData() {
        //TODO 获取地点列表
        //TODO 获取行业类别表
        placeTask = new GetAllTask(RequestCode.GET_PLACES, this);
        placeTask.execute();
        industryTask = new GetAllTask(RequestCode.GET_INDUSTRY, this);
        industryTask.execute();
    }

    @Override
    public void HandleView(int requestcode, String result) {
        Gson gson = new Gson();
        if (requestcode == RequestCode.GET_PLACES) {
            Log.v("songhui", "result:" + result);
            listPlace = gson.fromJson(result, new TypeToken<List<Place>>(){}.getType());
            List<String> listPlaceName = new ArrayList<String>();
            for (int i = 0; i < listPlace.size(); i++) {
                listPlaceName.add(i, listPlace.get(i).getPlace_name());
            }
            tagGroupPlace.setTags(listPlaceName);
            initTagView(tagGroupPlace);
            tagGroupPlace.setOnTagClickListener(new MyOnTagClickListener(tagGroupPlace));
        }
        else if (requestcode == RequestCode.GET_INDUSTRY) {
            Log.v("songhui", "result:" + result);
            listIndustry = gson.fromJson(result, new TypeToken< List<Industry_category>>(){}.getType());
            List<String> listIndustryName = new ArrayList<>();
            for (int i = 0; i < listIndustry.size(); i++) {
                listIndustryName.add(i, listIndustry.get(i).getCategory_name());
            }
            tagGroupIndustry.setTags(listIndustryName);
            initTagView(tagGroupIndustry);
            tagGroupIndustry.setOnTagClickListener(new MyOnTagClickListener(tagGroupIndustry));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (placeTask != null)
            placeTask.cancel(true);
        if (industryTask != null) {
            industryTask.cancel(true);
        }
    }
}
