package com.lazysong.gojob.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.adapter.ResultCompanyAdapter;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.utils.MarkInfoTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnKeyListener, MarkInfoTask.OnDataGotListener {
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
    private MarkInfoTask searchTaskPlace;
    private MarkInfoTask searchTaskCompany;
    private MarkInfoTask searchTaskPosition;

    private final String[] tableTitles = new String[]{"综合", "公司", "地点", "职位"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        edtSearch.setOnKeyListener(this);
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


    private void initTab() {
        listResIntegrate = (RecyclerView) v0.findViewById(R.id.listResIntegrate);

        listResCompany = (RecyclerView) v1.findViewById(R.id.listResICompany);
//        listResCompany.setAdapter(new ResultCompanyAdapter(postInfoCompany, this));
//        listResCompany.setLayoutManager(new LinearLayoutManager(this));

        listResPlace = (RecyclerView) v2.findViewById(R.id.listResPlace);

        listResPosition = (RecyclerView) v3.findViewById(R.id.listResPosition);
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
//            currentPosition = position;
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER){
            String keyword = edtSearch.getText().toString().trim();
            Toast.makeText(this, keyword, Toast.LENGTH_SHORT).show();
            doSearch(keyword, mTabLayout.getSelectedTabPosition());
            return true;
        }
        return false;
    }

    private void doSearch(String keyword, int search_by) {
        Log.v("songhui", "selectedPositon = " + mTabLayout.getSelectedTabPosition());
        List<Map<String, String>> params = new ArrayList();
        Map<String, String> map = new HashMap<>();
        switch (search_by) {
            case 0:
                //TODO 查询综合结果并显示
                break;
            case 1:
                //TODO 根据公司查询并显示结果
                map.put("name", "COMPANY_NAME");
                map.put("value", keyword);
                params.add(map);
                searchTaskCompany = new MarkInfoTask(RequestCode.SEARCH_BY_COMPANY, params, this);
                searchTaskCompany.execute();
                break;
            case 2:
                //TODO 根据地点查询并显示结果
                map.put("name", "PLACE_NAME");
                map.put("value", keyword);
                params.add(map);
                searchTaskPlace = new MarkInfoTask(RequestCode.SEARCH_BY_PLACES, params, this);
                searchTaskPlace.execute();
                break;
            case 3:
                //TODO 根据职位查询并显示结果
                break;
            default:
                //TODO 根据职位查询并显示结果
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTab();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (searchTaskPlace != null) {
            searchTaskPlace.cancel(true);
        }
        if (searchTaskCompany != null) {
            searchTaskCompany.cancel(true);
        }
        if (searchTaskPosition != null) {
            searchTaskPosition.cancel(true);
        }
    }

    @Override
    public void onDataGot(int requestcode, String result) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        switch (requestcode) {
            case RequestCode.SEARCH_BY_PLACES:
                Log.v("songhui", "result search by places:" + result);
                postInfoPlace = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
                listResPlace.setAdapter(new ResultCompanyAdapter(postInfoPlace, this));
                listResPlace.setLayoutManager(new LinearLayoutManager(this));
                break;
            case RequestCode.SEARCH_BY_COMPANY:
                Log.v("songhui", "result search by company:" + result);
                postInfoCompany = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
                listResCompany.setAdapter(new ResultCompanyAdapter(postInfoCompany, this));
                listResCompany.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

}
