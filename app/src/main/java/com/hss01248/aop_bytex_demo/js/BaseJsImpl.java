package com.hss01248.aop_bytex_demo.js;

import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Despciption todo
 * @Author hss
 * @Date 05/08/2022 10:33
 * @Version 1.0
 */
public class BaseJsImpl {
    protected WebView webView;

    public BaseJsImpl(WebView webView, AppCompatActivity activity) {
        this.webView = webView;
        this.activity = activity;
    }

    protected AppCompatActivity activity;
}
