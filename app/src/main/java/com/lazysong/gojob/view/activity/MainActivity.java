package com.lazysong.gojob.view.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lazysong.gojob.R;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.LocalInfoManager;
import com.lazysong.gojob.module.beans.MyUser;
import com.lazysong.gojob.utils.MarkInfoTask;
import com.lazysong.gojob.view.fragment.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,
        HomeFragment.OnFragmentInteractionListener, MessageFragment.OnFragmentInteractionListener,
        DiscoverFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,
        MarkInfoTask.OnDataGotListener, AccountFragmentNew.OnFragmentInteractionListener {
    private BottomNavigationBar bottomNavigationBar;
    private int position = 0;
    private Toolbar toolbar;
    private List<Fragment> fragmentList;
    private Fragment currentFragment;
    public static String appId = "1106011236";
    public static int RESULT_LOGOUT = 1;
    public static int REQUEST_SETTINGS = 2;
    public final static int FRAGMENT_HOME = 1;
    public final static int FRAGMENT_DISCOVER = 2;
    public final static int FRAGMENT_MESSAGE = 3;
    public final static int FRAGMENT_ACCOUNT = 4;
    private LocalInfoManager localManager;
    private boolean logined;
    private MyUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigationBar();

        loadLoginState();
        if (logined)
            loadUserInfo();
        createFragmentList();
        //设置默认的Fragment，即HomeFramgnet
        setDefaultFragment(position);
        //设置ToolBar
        setToolBar();
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

    private void loadUserInfo() {
        JSONObject jsonObject = localManager.getUserInfo();
        try {
            user = (MyUser) jsonObject.get("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createFragmentList() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(0, new HomeFragment());
        fragmentList.add(1, new DiscoverFragment());
        fragmentList.add(2, new MessageFragment());
        fragmentList.add(3, new AccountFragmentNew());
    }

    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
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
            currentFragment = fragmentList.get(position);
            Bundle args = new Bundle();
            args.putInt("position", position);
            currentFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, currentFragment).commit();
        }
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
        currentFragment = fragmentList.get(position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(currentFragment).commit();
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onChangeToolbarTitle(int data) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        switch (data) {
            case FRAGMENT_DISCOVER:
                actionBar.setTitle("发现");
                break;
            case FRAGMENT_MESSAGE:
                actionBar.setTitle("消息");
                break;
            case FRAGMENT_ACCOUNT:
                actionBar.setTitle("账户");
                break;
        }
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setDefaultFragment(int position) {
        currentFragment = fragmentList.get(position);
        Bundle args = new Bundle();
        args.putInt("position", position);
        currentFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    @Override
    public void onDataGot(int requestcode, String result) {
        if (requestcode == RequestCode.CAT_USER) {
            ((AccountFragmentNew)fragmentList.get(3)).onResopnse(requestcode, result);
        }
        else if (requestcode == RequestCode.USER_EXISTS) {
            ((AccountFragmentNew)fragmentList.get(3)).onResopnse(requestcode, result);
        }
        else if (requestcode == RequestCode.UPLOAD_USER) {
            ((AccountFragmentNew)fragmentList.get(3)).onResopnse(requestcode, result);
        }
    }
}
