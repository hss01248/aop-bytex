package com.hss01248.aop_bytex_demo.js;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.hss01248.aop_bytex_demo.js.bridges.IJsFunc1;
import com.hss01248.aop_bytex_demo.js.bridges.IJsFunc2;
import com.hss01248.aop_bytex_demo.js.bridges.JsFunc1Impl;
import com.hss01248.aop_bytex_demo.js.bridges.JsFunc2Impl;

/**
 * @Despciption ASM在运行时通过修改字节码动态给接口添加Annotation https://blog.csdn.net/iteye_17984/article/details/82652360
 *
 * bridge适用于aspectj进行try-catch,计时等aop操作,不会改变接口注解
 * @Author hss
 * @Date 05/08/2022 10:08
 * @Version 1.0
 */
@JsInterfaceObject
public class JsBridgeObj extends BaseJsObj implements IJsFunc1, IJsFunc2 {
    IJsFunc1 func1;
    IJsFunc2 func2;
    public JsBridgeObj(WebView webView, AppCompatActivity activity) {
        super(webView, activity);
        func1 = JsApiUtil.getInstance(JsFunc1Impl.class,webView,activity);
        func2 = JsApiUtil.getInstance(JsFunc2Impl.class,webView,activity);
    }


    @JavascriptInterface
    @Override
    public void test1(String paramJson, String callbackName) throws Throwable {
        func1.test1(paramJson, callbackName);
    }
    @JavascriptInterface
    @Override
    public void test2(String paramJson, String callbackName) throws Throwable {
        func2.test2(paramJson, callbackName);
    }
}
