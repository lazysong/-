package com.lazysong.gojob;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lazysong.gojob.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, HomeFragment.OnFragmentInteractionListener{
    private BottomNavigationBar bottomNavigationBar;
    private int position = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigationBar();
        //设置默认的Fragment，即HomeFramgnet
        setFragment(position);
        //设置ToolBar
        setToolBar();
    }

    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(this, "setToolbarTitle() is called in setToolBar() position is " + position, Toast.LENGTH_SHORT).show();
        setToolbarTitle(position);
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
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, R.string.home).setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bottomtabbar_discover, R.string.explore).setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_bottomtabbar_message, R.string.message).setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_person, R.string.account).setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        this.position = position;
        if(findViewById(R.id.main_container) != null) {
            setFragment(position);
        }
        setToolbarTitle(position);
    }

    // 设置顶部栏的标题
    private void setToolbarTitle(int position) {
        switch (position) {
            case 0:
                toolbar.setTitle(R.string.home);
                break;
            case 1:
                toolbar.setTitle(R.string.explore);
                break;
            case 2:
                toolbar.setTitle(R.string.message);
                break;
            case 3:
                toolbar.setTitle(R.string.account);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void setFragment(int position) {
        // 创建一个HomeFragment的实例，并且向其传入参数position
        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (transaction.isEmpty())
            transaction.add(R.id.main_container, fragment).commit();
        else
            transaction.replace(R.id.main_container, fragment).commit();
    }
}
