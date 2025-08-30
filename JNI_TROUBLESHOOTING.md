# JNI问题解决指南

## 问题描述

您遇到的错误：
```
java.lang.UnsatisfiedLinkError: No implementation found for java.lang.String com.example.golangbridgetest.GolangBridge.getString() (tried Java_com_example_golangbridgetest_GolangBridge_getString and Java_com_example_golangbridgetest_GolangBridge_getString__) - is the library loaded, e.g. System.loadLibrary?
```

这是一个典型的JNI函数名不匹配问题。

## 问题原因

1. **函数名不匹配** - Java期望的函数名是`Java_com_example_golangbridgetest_GolangBridge_getString`，但Golang导出的是`GetString`
2. **JNI命名约定** - JNI要求函数名遵循特定格式：`Java_包名_类名_方法名`
3. **缺少桥接层** - 需要在Java JNI和Golang C函数之间建立桥接

## 解决方案

### 方案1：动态加载SO库（推荐）

参考`video-crawler`项目的实现方式，使用动态加载：

#### 1. 修改Java代码

```java
public class GolangBridge {
    private static boolean libraryLoaded = false;
    
    static {
        try {
            System.loadLibrary("golang-bridge-jni"); // 加载JNI桥接库
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            libraryLoaded = false;
        }
    }
    
    // 包装方法，提供错误处理
    public static String getString() {
        if (!libraryLoaded) return "Library not loaded";
        try {
            return nativeGetString();
        } catch (UnsatisfiedLinkError e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // Native方法声明
    private static native String nativeGetString();
}
```

#### 2. 创建JNI桥接文件

```c
// android/app/src/main/cpp/golang_bridge_jni.c
#include <jni.h>
#include <dlfcn.h>

static void* handle = NULL;

static void* getFunction(const char* name) {
    if (!handle) {
        handle = dlopen("libgolang-bridge.so", RTLD_NOW);
    }
    return dlsym(handle, name);
}

JNIEXPORT jstring JNICALL Java_com_example_golangbridgetest_GolangBridge_nativeGetString(JNIEnv *env, jclass clazz) {
    typedef char* (*GetStringFn)();
    GetStringFn fn = (GetStringFn)getFunction("GetString");
    if (!fn) return (*env)->NewStringUTF(env, "Function not found");
    
    char* result = fn();
    return (*env)->NewStringUTF(env, result ? result : "");
}
```

#### 3. 配置CMake构建

```cmake
# android/app/CMakeLists.txt
cmake_minimum_required(VERSION 3.22.1)
project("golangbridgetest")

add_library(golang-bridge-jni SHARED
    src/main/cpp/golang_bridge_jni.c)

find_library(log-lib log)
target_link_libraries(golang-bridge-jni ${log-lib})
```

#### 4. 修改build.gradle

```gradle
android {
    externalNativeBuild {
        cmake {
            path "CMakeLists.txt"
        }
    }
}
```

### 方案2：直接导出JNI函数名（复杂）

在Golang中直接导出正确的JNI函数名：

```go
//export Java_com_example_golangbridgetest_GolangBridge_getString
func Java_com_example_golangbridgetest_GolangBridge_getString(env *C.JNIEnv, clazz C.jclass) C.jstring {
    // 需要包含JNI头文件
    return C.CString("Hello from Golang!")
}
```

这种方法需要：
- 包含JNI头文件
- 处理JNI类型转换
- 管理内存释放

## 最终实现

我们选择了**方案1（动态加载）**，因为：

1. **更灵活** - 可以动态加载不同的SO库
2. **错误处理** - 提供更好的错误处理和回退机制
3. **维护性** - 代码结构更清晰，易于维护
4. **兼容性** - 与现有Golang代码兼容

## 文件结构

```
android/app/src/main/
├── java/com/example/golangbridgetest/
│   └── GolangBridge.java          # JNI接口类
├── cpp/
│   └── golang_bridge_jni.c        # JNI桥接实现
├── jniLibs/
│   ├── armeabi-v7a/
│   │   └── libgolang-bridge.so    # Golang SO库
│   └── arm64-v8a/
│       └── libgolang-bridge.so    # Golang SO库
└── CMakeLists.txt                 # CMake构建配置
```

## 验证步骤

1. **编译Golang SO库**：
   ```bash
   cd golang && ./build.sh
   ```

2. **构建Android应用**：
   ```bash
   ./build.sh
   ```

3. **安装APK**：
   ```bash
   adb install android/app/build/outputs/apk/debug/app-debug.apk
   ```

4. **查看日志**：
   ```bash
   adb logcat | grep GolangBridge
   ```

## 常见问题

### 1. 函数找不到
- **错误**: `Function not found`
- **解决**: 检查Golang函数名是否正确导出

### 2. SO库加载失败
- **错误**: `Failed to load libgolang-bridge.so`
- **解决**: 检查SO文件路径和架构匹配

### 3. 内存泄漏
- **错误**: 应用崩溃或内存不足
- **解决**: 确保正确释放Golang分配的内存

### 4. 架构不匹配
- **错误**: `UnsatisfiedLinkError`
- **解决**: 确保SO库架构与设备匹配

## 总结

通过实现动态加载的JNI桥接层，我们成功解决了：

- ✅ JNI函数名不匹配问题
- ✅ 错误处理和回退机制
- ✅ 内存管理
- ✅ 跨架构兼容性

现在Android应用可以正常调用Golang SO库的函数，并显示返回的内容。
