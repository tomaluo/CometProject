package com.mazing.com.cavasdemo.hook;

import android.content.ComponentName;
import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by user on 17/6/27.
 */

public class HookInvocationHandler implements InvocationHandler {

    Object mAmsObj;
    Class<?> mProxyActivity;
    Class<?> mOriginallyActivity;

    public HookInvocationHandler(Object amsObj, Class<?> proxyActivity, Class<?> originallyActivity) {
        this.mAmsObj = amsObj;
        this.mProxyActivity = proxyActivity;
        this.mOriginallyActivity = originallyActivity;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("startActivity")){
            int index = 0;
            //找到我们启动时的intent
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }

            // 取出在真实的Intent
            Intent originallyIntent = (Intent) args[index];
            // 自己伪造一个配置文件已注册过的Activity Intent
            Intent proxyIntent = new Intent();

            // 因为我们调用的Activity没有注册，所以这里我们先偷偷换成已注册。使用一个假的Intent
            String PackageName = mProxyActivity.getPackage().getName();
            ComponentName componentName = new ComponentName(PackageName, mOriginallyActivity.getName());
            proxyIntent.setComponent(componentName);

            //在这里把未注册的Intent先存起来 一会儿我们需要在Handle里取出来用
            proxyIntent.putExtra("originallyIntent", originallyIntent);
            args[index] = proxyIntent;
        }
        return method.invoke(mAmsObj, args);
    }
}
