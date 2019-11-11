package com.example.camerademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void open(View view) {

        PermissionsUtil.checkPermissions(this, new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                if (value != null && value) {
                    startActivity(new Intent(TestActivity.this, MainActivity.class));
                }
            }
        }, PermissionsUtil.PERMISSIONS_CAMERA);
    }
}
