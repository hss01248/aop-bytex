package com.hss01248.aop_bytex_demo.js;

import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Despciption todo
 * @Author hss
 * @Date 05/08/2022 10:17
 * @Version 1.0
 */
public class BaseJsObj {
    protected WebView webView;
    protected AppCompatActivity activity;
    public BaseJsObj(WebView webView, AppCompatActivity activity) {
        this.webView = webView;
        this.activity = activity;
    }


}
