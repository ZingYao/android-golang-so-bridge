#include <jni.h>
#include "bridge.h"

// Java JNI函数映射到Golang函数

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_getInt(JNIEnv *env, jclass clazz) {
    return GetInt();
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_getString(JNIEnv *env, jclass clazz) {
    char* result = GetString();
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result); // 释放Golang分配的内存
    return jresult;
}

JNIEXPORT jdouble JNICALL Java_com_example_golangbridgetest_GolangBridge_getDouble(JNIEnv *env, jclass clazz) {
    return GetDouble();
}

JNIEXPORT jboolean JNICALL Java_com_example_golangbridgetest_GolangBridge_getBoolean(JNIEnv *env, jclass clazz) {
    return GetBoolean();
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_getJSONString(JNIEnv *env, jclass clazz) {
    char* result = GetJSONString();
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result);
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_getArray(JNIEnv *env, jclass clazz) {
    char* result = GetArray();
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result);
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_getUserInfo(JNIEnv *env, jclass clazz) {
    char* result = GetUserInfo();
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result);
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_getSystemInfo(JNIEnv *env, jclass clazz) {
    char* result = GetSystemInfo();
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result);
    return jresult;
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_addNumbers(JNIEnv *env, jclass clazz, jint a, jint b) {
    return AddNumbers(a, b);
}

JNIEXPORT jdouble JNICALL Java_com_example_golangbridgetest_GolangBridge_multiplyNumbers(JNIEnv *env, jclass clazz, jdouble a, jdouble b) {
    return MultiplyNumbers(a, b);
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_getCounter(JNIEnv *env, jclass clazz) {
    return GetCounter();
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_incrementCounter(JNIEnv *env, jclass clazz) {
    return IncrementCounter();
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_resetCounter(JNIEnv *env, jclass clazz) {
    return ResetCounter();
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_getCounterInfo(JNIEnv *env, jclass clazz) {
    char* result = GetCounterInfo();
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result);
    return jresult;
}
