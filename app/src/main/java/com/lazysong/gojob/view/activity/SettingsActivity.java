package com.lazysong.gojob.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lazysong.gojob.R;
import com.lazysong.gojob.view.fragment.AccountFragment;
import com.tencent.tauth.Tencent;

public class SettingsActivity extends AppCompatActivity {
    private Button btnLogout;
    private Tencent mTencent;
    private Toolbar toolbar;
    private TextView titleActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setNavigationIcon(R.mipmap.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
        titleActionbar = (TextView) findViewById(R.id.titleToolbar);
        titleActionbar.setText("设置");
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
