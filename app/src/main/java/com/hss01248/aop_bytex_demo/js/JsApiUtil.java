package com.hss01248.aop_bytex_demo.js;

import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.hss01248.aop.dynamicproxy.android.DynamicProxyAndroid;

import java.lang.reflect.Method;


/**
 * @Despciption todo
 * @Author hss
 * @Date 05/08/2022 10:41
 * @Version 1.0
 */
public class JsApiUtil {

    public static <T> T getInstance(Class<T> tClass, WebView webView, AppCompatActivity activity){
        try {
           T t =  tClass.getConstructor(WebView.class,AppCompatActivity.class).newInstance(webView,activity);
           //生成动态代理:
           return DynamicProxyAndroid.getProxy(t, true, true, true, false, true, new DynamicProxyAndroid.ProxyCallback() {
                @Override
                public void before(Object proxy, Method method, Object[] args) {
                    DynamicProxyAndroid.ProxyCallback.super.before(proxy, method, args);
                }

                @Override
                public void onResult(Object proxy, Object result, Method method, Object[] args,long cost) {
                    DynamicProxyAndroid.ProxyCallback.super.onResult(proxy, result, method, args,cost);
                }

                @Override
                public void onException(Object proxy, Throwable result, Method method, Object[] args,long cost) {
                    DynamicProxyAndroid.ProxyCallback.super.onException(proxy, result, method, args,cost);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
