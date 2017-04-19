package com.lazysong.gojob.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
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

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        /*usrPic = (ImageView) view.findViewById(R.id.userPic);
        Bitmap bitmap = null;
        try {
            FileInputStream inputStream = getContext().openFileInput("userPic.png");
            byte[] buffer = new byte[1024*1000];
            inputStream.read(buffer);
            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            usrPic.setImageBitmap(bitmap);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        buttonUpload = (Button) view.findViewById(R.id.uploadData);
        buttonUpload.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
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
                intent.setClass(getContext(), QQLoginActivity.class);
                intent.putExtra("appId", "1106011236");
                startActivity(intent);
                break;
            case R.id.btnQuery:
                intent.setClass(getContext(), QueryActivity.class);
                String limit = editTextLimit.getText().toString();
                intent.putExtra("limit", limit);
                startActivity(intent);
                break;
            case R.id.btnToAccount:
                intent.setClass(getContext(), AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.testNetworkConnection:
                intent.setClass(getContext(), NetworkConnActivity.class);
                startActivity(intent);
                break;
            case R.id.userInfoManage:
                intent.setClass(getContext(), UserManageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
