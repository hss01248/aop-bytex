package com.hss01248.aop_bytex_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hss01248.aop_bytex_demo.js.JsBridgeObj;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Test.test1();
    }

    public void jsObj_fuc1(View view) {
        try {
            new JsBridgeObj(null,this).test1("dxxx","callback");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}