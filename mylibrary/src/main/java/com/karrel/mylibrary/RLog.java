package com.karrel.mylibrary;

import android.util.Log;

/**
 * Created by Rell on 2017. 8. 9..
 */

public class RLog {

    static final String TAG = "RLog";

    private static boolean ENABLE_LOG = true;

    /**
     * 로그를 출력할지를 boolean 값의 파라미터로 전달.
     */
    public static void setEnableLog(boolean enableLog) {
        ENABLE_LOG = enableLog;
    }

    /**
     * Log Level Error
     **/
    public static final void e(String message) {
        if (!ENABLE_LOG) return;

        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.e(TAG, String.format("%s -> %s", subPath(path), message));
    }

    public static final void e() {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.e(TAG, String.format("%s -> %s", subPath(path), getMethodName(path)));
    }

    public static final void w() {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.w(TAG, String.format("%s -> %s", subPath(path), getMethodName(path)));
    }

    /**
     * Log Level Warning
     **/
    public static final void w(String message) {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.w(TAG, String.format("%s -> %s", subPath(path), message));
    }

    public static final void i() {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.i(TAG, String.format("%s -> %s", subPath(path), getMethodName(path)));
    }

    /**
     * Log Level Information
     **/
    public static final void i(String message) {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.i(TAG, String.format("%s -> %s", subPath(path), message));
    }

    public static final void d() {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();


        Log.d(TAG, String.format("%s -> %s", subPath(path), getMethodName(path)));
    }

    private static String getMethodName(String path) {
        String message = "";
        try {
            String[] arrStr = path.split("\\.");
            if (arrStr.length > 2) {
                String str2 = arrStr[arrStr.length - 2];
                message = str2.substring(0, str2.indexOf("("));
            }
        } catch (Exception e) {

        } finally {
            return message + "()";
        }
    }

    /**
     * Log Level Debug
     **/
    public static final void d(String message) {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.d(TAG, String.format("%s -> %s", subPath(path), message));
    }

    public static final void v() {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.v(TAG, String.format("%s -> %s", subPath(path), getMethodName(path)));
    }

    /**
     * Log Level Verbose
     **/
    public static final void v(String message) {
        if (!ENABLE_LOG) return;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        StackTraceElement element = elements[3];
        String path = element.toString();

        Log.v(TAG, String.format("%s -> %s", subPath(path), message));
    }

    /**
     * 호출되는 마지막 메소드명만 자른다.
     */
    private static String subPath(String path) {
        String[] arrStr = path.split("\\.");
        if (arrStr.length <= 2) return path;

        String str = "";
        for (int i = arrStr.length - 2; i < arrStr.length; i++) {
            str += arrStr[i] + ".";
        }
        return str.substring(0, str.length() - 1);
    }
}