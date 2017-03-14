package com.lazysong.gojob;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lazysong.gojob.com.lazysong.gojob.beans.User;
import com.lazysong.gojob.fragment.AccountFragment;
import com.lazysong.gojob.fragment.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity implements AccountFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setFragment();
    }

    void setFragment() {
        // 创建一个HomeFragment的实例，并且向其传入参数position
        AccountFragment accountFragment = new AccountFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (transaction.isEmpty())
            transaction.add(R.id.containerAccount, accountFragment).commit();
        else
            transaction.replace(R.id.containerAccount, accountFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
