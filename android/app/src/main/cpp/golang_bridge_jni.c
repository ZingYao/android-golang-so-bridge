#include <jni.h>
#include <dlfcn.h>
#include <android/log.h>

#define LOG_TAG "GolangBridgeJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// 函数指针类型定义
typedef int (*GetIntFn)();
typedef char* (*GetStringFn)();
typedef double (*GetDoubleFn)();
typedef int (*GetBooleanFn)();
typedef char* (*GetJSONStringFn)();
typedef char* (*GetArrayFn)();
typedef char* (*GetUserInfoFn)();
typedef char* (*GetSystemInfoFn)();
typedef int (*AddNumbersFn)(int, int);
typedef double (*MultiplyNumbersFn)(double, double);
typedef int (*GetCounterFn)();
typedef int (*IncrementCounterFn)();
typedef int (*ResetCounterFn)();
typedef char* (*GetCounterInfoFn)();
typedef char* (*StartHTTPServerFn)(int);
typedef char* (*CallHTTPServiceFn)(char*);

static void* handle = NULL;

// 获取函数指针
static void* getFunction(const char* name) {
    if (!handle) {
        handle = dlopen("libgolang-bridge.so", RTLD_NOW);
        if (!handle) {
            LOGE("Failed to load libgolang-bridge.so: %s", dlerror());
            return NULL;
        }
    }
    
    void* func = dlsym(handle, name);
    if (!func) {
        LOGE("Failed to find function %s: %s", name, dlerror());
    }
    return func;
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetInt(JNIEnv *env, jclass clazz) {
    GetIntFn fn = (GetIntFn)getFunction("GetInt");
    if (!fn) return -1;
    return fn();
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetString(JNIEnv *env, jclass clazz) {
    GetStringFn fn = (GetStringFn)getFunction("GetString");
    if (!fn) return (*env)->NewStringUTF(env, "Function not found");
    
    char* result = fn();
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "");
    return jresult;
}

JNIEXPORT jdouble JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetDouble(JNIEnv *env, jclass clazz) {
    GetDoubleFn fn = (GetDoubleFn)getFunction("GetDouble");
    if (!fn) return -1.0;
    return fn();
}

JNIEXPORT jboolean JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetBoolean(JNIEnv *env, jclass clazz) {
    GetBooleanFn fn = (GetBooleanFn)getFunction("GetBoolean");
    if (!fn) return JNI_FALSE;
    return fn() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetJSONString(JNIEnv *env, jclass clazz) {
    GetJSONStringFn fn = (GetJSONStringFn)getFunction("GetJSONString");
    if (!fn) return (*env)->NewStringUTF(env, "{\"error\": \"Function not found\"}");
    
    char* result = fn();
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "{\"error\": \"null result\"}");
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetArray(JNIEnv *env, jclass clazz) {
    GetArrayFn fn = (GetArrayFn)getFunction("GetArray");
    if (!fn) return (*env)->NewStringUTF(env, "[]");
    
    char* result = fn();
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "[]");
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetUserInfo(JNIEnv *env, jclass clazz) {
    GetUserInfoFn fn = (GetUserInfoFn)getFunction("GetUserInfo");
    if (!fn) return (*env)->NewStringUTF(env, "{\"error\": \"Function not found\"}");
    
    char* result = fn();
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "{\"error\": \"null result\"}");
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetSystemInfo(JNIEnv *env, jclass clazz) {
    GetSystemInfoFn fn = (GetSystemInfoFn)getFunction("GetSystemInfo");
    if (!fn) return (*env)->NewStringUTF(env, "{\"error\": \"Function not found\"}");
    
    char* result = fn();
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "{\"error\": \"null result\"}");
    return jresult;
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeAddNumbers(JNIEnv *env, jclass clazz, jint a, jint b) {
    AddNumbersFn fn = (AddNumbersFn)getFunction("AddNumbers");
    if (!fn) return a + b;
    return fn(a, b);
}

JNIEXPORT jdouble JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeMultiplyNumbers(JNIEnv *env, jclass clazz, jdouble a, jdouble b) {
    MultiplyNumbersFn fn = (MultiplyNumbersFn)getFunction("MultiplyNumbers");
    if (!fn) return a * b;
    return fn(a, b);
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetCounter(JNIEnv *env, jclass clazz) {
    GetCounterFn fn = (GetCounterFn)getFunction("GetCounter");
    if (!fn) return -1;
    return fn();
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeIncrementCounter(JNIEnv *env, jclass clazz) {
    IncrementCounterFn fn = (IncrementCounterFn)getFunction("IncrementCounter");
    if (!fn) return -1;
    return fn();
}

JNIEXPORT jint JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeResetCounter(JNIEnv *env, jclass clazz) {
    ResetCounterFn fn = (ResetCounterFn)getFunction("ResetCounter");
    if (!fn) return -1;
    return fn();
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetCounterInfo(JNIEnv *env, jclass clazz) {
    GetCounterInfoFn fn = (GetCounterInfoFn)getFunction("GetCounterInfo");
    if (!fn) return (*env)->NewStringUTF(env, "{\"error\": \"Function not found\"}");
    
    char* result = fn();
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "{\"error\": \"null result\"}");
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeStartHTTPServer(JNIEnv *env, jclass clazz, jint port) {
    StartHTTPServerFn fn = (StartHTTPServerFn)getFunction("StartHTTPServer");
    if (!fn) return (*env)->NewStringUTF(env, "{\"error\": \"Function not found\"}");
    
    char* result = fn(port);
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "{\"error\": \"null result\"}");
    return jresult;
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeCallHTTPService(JNIEnv *env, jclass clazz, jstring url) {
    CallHTTPServiceFn fn = (CallHTTPServiceFn)getFunction("CallHTTPService");
    if (!fn) return (*env)->NewStringUTF(env, "{\"error\": \"Function not found\"}");
    
    const char* urlStr = (*env)->GetStringUTFChars(env, url, 0);
    char* result = fn((char*)urlStr);
    (*env)->ReleaseStringUTFChars(env, url, urlStr);
    
    jstring jresult = (*env)->NewStringUTF(env, result ? result : "{\"error\": \"null result\"}");
    return jresult;
}
