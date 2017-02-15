package com.lazysong.gojob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigationBar();
    }

    /*
   * 初始化主界面中的底部栏组件
   * */
    void initBottomNavigationBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, "首页").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bottomtabbar_discover, "发现").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bottomtabbar_message, "消息").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_person, "账户").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        Toast.makeText(this,"tab" + position + " is selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
