package com.example.golangbridgetest;

import android.util.Log;
import java.lang.reflect.Method;

/**
 * JNI接口类，用于调用Golang SO库
 */
public class GolangBridge {
    
    private static boolean libraryLoaded = false;
    
    static {
        try {
            Log.d("GolangBridge", "开始加载原生库...");
            System.loadLibrary("golang-bridge-jni");
            libraryLoaded = true;
            Log.d("GolangBridge", "原生库加载成功");
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "原生库加载失败: " + e.getMessage(), e);
            libraryLoaded = false;
        }
    }
    
    // 基本数据类型
    public static int getInt() {
        if (!libraryLoaded) return -1;
        try {
            return nativeGetInt();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getInt调用失败: " + e.getMessage());
            return -1;
        }
    }
    
    public static String getString() {
        if (!libraryLoaded) return "Library not loaded";
        try {
            return nativeGetString();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getString调用失败: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    public static double getDouble() {
        if (!libraryLoaded) return -1.0;
        try {
            return nativeGetDouble();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getDouble调用失败: " + e.getMessage());
            return -1.0;
        }
    }
    
    public static boolean getBoolean() {
        if (!libraryLoaded) return false;
        try {
            return nativeGetBoolean();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getBoolean调用失败: " + e.getMessage());
            return false;
        }
    }
    
    // JSON数据
    public static String getJSONString() {
        if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
        try {
            return nativeGetJSONString();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getJSONString调用失败: " + e.getMessage());
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    public static String getArray() {
        if (!libraryLoaded) return "[]";
        try {
            return nativeGetArray();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getArray调用失败: " + e.getMessage());
            return "[]";
        }
    }
    
    public static String getUserInfo() {
        if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
        try {
            return nativeGetUserInfo();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getUserInfo调用失败: " + e.getMessage());
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    public static String getSystemInfo() {
        if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
        try {
            return nativeGetSystemInfo();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getSystemInfo调用失败: " + e.getMessage());
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    // 数学运算
    public static int addNumbers(int a, int b) {
        if (!libraryLoaded) return a + b;
        try {
            return nativeAddNumbers(a, b);
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "addNumbers调用失败: " + e.getMessage());
            return a + b;
        }
    }
    
    public static double multiplyNumbers(double a, double b) {
        if (!libraryLoaded) return a * b;
        try {
            return nativeMultiplyNumbers(a, b);
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "multiplyNumbers调用失败: " + e.getMessage());
            return a * b;
        }
    }
    
    // 计数器功能
    public static int getCounter() {
        if (!libraryLoaded) return -1;
        try {
            return nativeGetCounter();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getCounter调用失败: " + e.getMessage());
            return -1;
        }
    }
    
    public static int incrementCounter() {
        if (!libraryLoaded) return -1;
        try {
            return nativeIncrementCounter();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "incrementCounter调用失败: " + e.getMessage());
            return -1;
        }
    }
    
    public static int resetCounter() {
        if (!libraryLoaded) return -1;
        try {
            return nativeResetCounter();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "resetCounter调用失败: " + e.getMessage());
            return -1;
        }
    }
    
    public static String getCounterInfo() {
        if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
        try {
            return nativeGetCounterInfo();
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "getCounterInfo调用失败: " + e.getMessage());
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    // HTTP服务功能
    public static String startHTTPServer(int port) {
        if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
        try {
            return nativeStartHTTPServer(port);
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "startHTTPServer调用失败: " + e.getMessage());
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    public static String callHTTPService(String url) {
        if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
        try {
            return nativeCallHTTPService(url);
        } catch (UnsatisfiedLinkError e) {
            Log.e("GolangBridge", "callHTTPService调用失败: " + e.getMessage());
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    // Native方法声明
    private static native int nativeGetInt();
    private static native String nativeGetString();
    private static native double nativeGetDouble();
    private static native boolean nativeGetBoolean();
    private static native String nativeGetJSONString();
    private static native String nativeGetArray();
    private static native String nativeGetUserInfo();
    private static native String nativeGetSystemInfo();
    private static native int nativeAddNumbers(int a, int b);
    private static native double nativeMultiplyNumbers(double a, double b);
    private static native int nativeGetCounter();
    private static native int nativeIncrementCounter();
    private static native int nativeResetCounter();
    private static native String nativeGetCounterInfo();
    private static native String nativeStartHTTPServer(int port);
    private static native String nativeCallHTTPService(String url);
}
