package com.lazysong.gojob.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lazysong.gojob.view.activity.AccountActivity;
import com.lazysong.gojob.view.activity.NetworkConnActivity;
import com.lazysong.gojob.view.activity.QQLoginActivity;
import com.lazysong.gojob.view.activity.QueryActivity;
import com.lazysong.gojob.R;
import com.lazysong.gojob.view.activity.UserManageActivity;

public class HomeFragmentTest extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView textView;
    private Button buttonLogin;
    private Button buttonQuery;
    private Button buttonToAccount;
    private int position;
    private EditText editTextLimit;
    private ImageView usrPic;
    private Button buttonUpload;
    private Button testNetworkConnection;
    private Button userInfoManage;
    private ActionBar actionBar;

    public HomeFragmentTest() {
        // Required empty public constructor
    }

    public static HomeFragmentTest newInstance(String param1, String param2) {
        HomeFragmentTest fragment = new HomeFragmentTest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_test, container, false);
        //获取当前点击的位置并显示
        position = getArguments().getInt("position");
        textView = (TextView) view.findViewById(R.id.textview);
        textView.setText("Hello this is fragment " + position);
        editTextLimit = (EditText) view.findViewById(R.id.editTextLimit);
        testNetworkConnection = (Button)view.findViewById(R.id.testNetworkConnection);
        userInfoManage = (Button)view.findViewById(R.id.userInfoManage);

        buttonLogin = (Button) view.findViewById(R.id.login);
        buttonLogin.setOnClickListener(this);
        buttonQuery = (Button) view.findViewById(R.id.btnQuery);
        buttonQuery.setOnClickListener(this);
        buttonToAccount = (Button)view.findViewById(R.id.btnToAccount);
        buttonToAccount.setOnClickListener(this);
        testNetworkConnection.setOnClickListener(this);
        userInfoManage.setOnClickListener(this);
        buttonUpload = (Button) view.findViewById(R.id.uploadData);
        buttonUpload.setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        final int id = v.getId();
        switch (id) {
            case R.id.login:
                intent.setClass(getActivity(), QQLoginActivity.class);
                intent.putExtra("appId", "1106011236");
                startActivity(intent);
                break;
            case R.id.btnQuery:
                intent.setClass(getActivity(), QueryActivity.class);
                String limit = editTextLimit.getText().toString();
                intent.putExtra("limit", limit);
                startActivity(intent);
                break;
            case R.id.btnToAccount:
                intent.setClass(getActivity(), AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.testNetworkConnection:
                intent.setClass(getActivity(), NetworkConnActivity.class);
                startActivity(intent);
                break;
            case R.id.userInfoManage:
                intent.setClass(getActivity(), UserManageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setShowHideAnimationEnabled(false);
        actionBar.show();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.actionbar_home);
    }

}
