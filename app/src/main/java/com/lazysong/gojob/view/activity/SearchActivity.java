package com.lazysong.gojob.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.adapter.ResultCompanyAdapter;
import com.lazysong.gojob.module.beans.PostInformation;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTitle;
    private ImageView imgBack;
    private EditText edtSearch;

    private List<View> viewContainer = new ArrayList<View>();
    private View v0;
    private View v1;
    private View v2;
    private View v3;
    private ViewPager viewPager;
    private RecyclerView listResIntegrate;
    private RecyclerView listResCompany;
    private RecyclerView listResPlace;
    private RecyclerView listResPosition;
    private TabLayout mTabLayout;

    private List<PostInformation> postInfoCompany;
    private List<PostInformation> postInfoPlace;
    private List<PostInformation> postInfoPosition;

    private final String[] tableTitles = new String[]{"综合", "公司", "地点", "职位"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        txtTitle = (TextView) findViewById(R.id.titleToolbar);
//        txtTitle.setText("搜索");

        /*toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.drawable.pre);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });*/

        //消除actionBar的显示/隐藏动画，并且隐藏actionBar
//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setShowHideAnimationEnabled(false);
//        actionBar.hide();


        edtSearch = (EditText) findViewById(R.id.edtSearch);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

        LayoutInflater layoutInflater = getLayoutInflater();
        v0 = layoutInflater.inflate(R.layout.layout_result_integrate, null);
        v1 = layoutInflater.inflate(R.layout.layout_result_company, null);
        v2 = layoutInflater.inflate(R.layout.layout_result_place, null);
        v3 = layoutInflater.inflate(R.layout.layout_result_position, null);
        if(viewContainer.isEmpty()) {
            viewContainer.add(0, v0);
            viewContainer.add(1, v1);
            viewContainer.add(2, v2);
            viewContainer.add(3, v3);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpagerResult);
        viewPager.setAdapter(pagerAdapter);
        initData();

        //消除actionBar的显示/隐藏动画，并且隐藏actionBar
//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setShowHideAnimationEnabled(false);
//        actionBar.hide();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("0"), true);
        for(int i = 1; i < viewContainer.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        //将TabLayout和ViewPager关联起来
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(viewPager);
            }
        });
        //给Tabs设置适配器
        mTabLayout.setTabsFromPagerAdapter(pagerAdapter);
    }

    private void initData() {
        String result = "[{\"post_id\":890,\"company_name\":\"达富电脑（常熟）有限公司\",\"salary_month\":\"面议\",\"work_place\":\"苏州\",\"post_date\":\"Sep 26, 2016 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"不限\",\"position_count\":1,\"position_type\":\"软件测试\"},{\"post_id\":891,\"company_name\":\"苏州达内信息科技有限公司第一分公司\",\"salary_month\":\"4001-6000元/月\",\"work_place\":\"苏州\",\"post_date\":\"Mar 2, 2017 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"不限\",\"position_count\":5,\"position_type\":\"用户界面（UI）设计\"},{\"post_id\":892,\"company_name\":\"苏州科技城管理委员会\",\"salary_month\":\"面议\",\"work_place\":\"苏州\",\"post_date\":\"Mar 2, 2017 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"大专\",\"position_count\":1,\"position_type\":\"采购专员/助理\"},{\"post_id\":893,\"company_name\":\"苏州达内信息科技有限公司第一分公司\",\"salary_month\":\"4001-6000元/月\",\"work_place\":\"苏州\",\"post_date\":\"Mar 2, 2017 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"不限\",\"education_requirement\":\"不限\",\"position_count\":5,\"position_type\":\"广告文案策划\"},{\"post_id\":894,\"company_name\":\"苏州赛科计算机信息系统有限公司\",\"salary_month\":\"4000-7000元/月\",\"work_place\":\"苏州姑苏区\",\"post_date\":\"Mar 2, 2017 12:00:00 AM\",\"work_type\":\"全职\",\"experience_requirement\":\"1-3年\",\"education_requirement\":\"大专\",\"position_count\":2,\"position_type\":\"软件工程师\"}]";
        Gson gson = new Gson();
        postInfoCompany = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
        postInfoPlace = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
        postInfoPosition = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
    }

    private void initTabIntegrate(View v) {
        listResIntegrate = (RecyclerView) v.findViewById(R.id.listResIntegrate);
    }

    private void initTabCompany(View v) {
        listResCompany = (RecyclerView) v.findViewById(R.id.listResICompany);
        listResCompany.setAdapter(new ResultCompanyAdapter(postInfoCompany, this));
        listResCompany.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initTypeTab(View v) {

    }

    private void initMark(View v) {

    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        //viewpager中的组件数量
        @Override
        public int getCount() {
            return viewContainer.size();
        }
        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(viewContainer.get(position));
        }
        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(viewContainer.get(position));
            return viewContainer.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tableTitles[position];
        }
    };
    class SearchTask extends AsyncTask<Void, Void, String> {
        private final String keyword;

        public SearchTask(String keyword) {
            this.keyword = keyword;
        }

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTabIntegrate(v0);
        initTabCompany(v1);
    }
}
