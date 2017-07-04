package com.hs.damnicomniplusvic.controltemp.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by DAMNICOMNIPLUSVIC on 2017/7/4.
 * (c) 2017 DAMNICOMNIPLUSVIC Inc,All Rights Reserved.
 */

public class PermissionUtil {
    /**
     * 判断软件是否具有某项危险的权限
     *
     * @param object      类
     * @param permission  要判断的权限
     * @param requestCode 回调码
     * @param methodName  拥有某项权限时，自动回调类的方法
     * @param args        回调的方法的参数
     */
    public static void judgePermission(Object object, String permission, int requestCode, String methodName, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (ContextCompat.checkSelfPermission((Context) object, permission) == PackageManager.PERMISSION_GRANTED) {
            Class<?> clazz = object.getClass();
            if (args != null) {
                Class[] argsClass = new Class[args.length];
                for (int i = 0, j = args.length; i < j; i++) {
                    argsClass[i] = args[i].getClass();
                }
                Method method = clazz.getMethod(methodName, argsClass);
                method.setAccessible(true);
                method.invoke(object, args);
            } else {
                Method method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(object);
            }
        } else {
            ActivityCompat.requestPermissions((Activity) object, new String[]{permission}, requestCode);
        }
    }

}
