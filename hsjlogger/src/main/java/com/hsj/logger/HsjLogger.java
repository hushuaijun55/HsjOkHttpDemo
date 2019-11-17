package com.hsj.logger;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class HsjLogger {
    public static boolean DEBUG = true;

    static String className;
    static String methodName;
    static int lineNumber;

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    //超长文本打印
    public static void longError(String content) {
        if (!DEBUG) return;
        Logger.e(content);
    }
    //超长文本打印
    public static void longInfo(String content) {
        if (!DEBUG) return;
        Logger.i(content);
    }

    public static void d(String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                getMethodNames(new Throwable().getStackTrace());
                Log.d(className, createLog(msg));
            }
        }
    }

    public static void v(String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                getMethodNames(new Throwable().getStackTrace());
                Log.d(className, createLog(msg));
            }
        }
    }

    public static void e(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(msg));
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                getMethodNames(new Throwable().getStackTrace());
                Log.d(className, createLog(msg));
            }
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
//                Log.d(tag, msg);
                getMethodNames(new Throwable().getStackTrace());
                Log.d(className, createLog(msg));
            }
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, msg);
            }
        }
    }
}
