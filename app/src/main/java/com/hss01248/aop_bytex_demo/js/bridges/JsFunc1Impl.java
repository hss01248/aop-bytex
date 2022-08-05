package com.hss01248.aop_bytex_demo.js.bridges;

import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.hss01248.aop_bytex_demo.js.BaseJsImpl;
import com.hss01248.aop_bytex_demo.js.bridges.IJsFunc1;

/**
 * @Despciption todo
 * @Author hss
 * @Date 05/08/2022 10:13
 * @Version 1.0
 */
public class JsFunc1Impl extends BaseJsImpl implements IJsFunc1 {
    public JsFunc1Impl(WebView webView, AppCompatActivity activity) {
        super(webView, activity);
    }


    @Override
    public void test1(String paramJson, String callbackName) throws Throwable {
        System.out.println("js test1");
    }
}
