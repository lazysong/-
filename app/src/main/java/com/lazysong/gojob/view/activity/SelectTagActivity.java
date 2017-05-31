package com.lazysong.gojob.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.Place;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class SelectTagActivity extends AppCompatActivity {
    private TagGroup tagGroupSelected;
    private TagGroup tagGroupRecommand;
    private TagGroup tagGroupCompany;
    private TagGroup tagGroupPlace;
    private Toolbar toolbar;
    private TextView titleToolbar;


    private List<Place> listPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        tagGroupSelected = (TagGroup) findViewById(R.id.tag_group_selected);
        tagGroupRecommand = (TagGroup) findViewById(R.id.tag_group_recommand);
        tagGroupCompany = (TagGroup) findViewById(R.id.tag_group_company);
        tagGroupPlace = (TagGroup) findViewById(R.id.tag_group_place);

        tagGroupSelected.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {

            }
        });

        titleToolbar = (TextView) findViewById(R.id.titleToolbar);
        titleToolbar.setText("选择标签");
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.pre);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTagActivity.this.finish();
            }
        });

        initTagGroup();
    }

    private void initTagGroup() {
        prepareData();
        List<String> listPlaceName = new ArrayList<String>();
        for (int i = 0; i < listPlace.size(); i++) {
            listPlaceName.add(i, listPlace.get(i).getPlaceName());
        }
        tagGroupPlace.setTags(listPlaceName);
    }

    private void prepareData() {
        listPlace = new ArrayList<Place>();
        listPlace.add(0, new Place("北京"));
        listPlace.add(1, new Place("上海"));
        listPlace.add(2, new Place("杭州"));
        listPlace.add(3, new Place("苏州"));
    }
}
