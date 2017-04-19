package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lazysong.gojob.R;
import com.lazysong.gojob.view.fragment.AccountFragment;
import com.tencent.tauth.Tencent;

public class SettingsActivity extends AppCompatActivity {
    private Button btnLogout;
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
    }

    private void initViews() {
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AccountFragment.class);
                setResult(MainActivity.RESULT_LOGOUT, intent);
                finish();
            }
        });
    }
}
