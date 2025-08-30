# Android + Golang SO 测试项目使用说明

## 项目概述

这是一个演示Android应用通过JNI调用Golang编译的SO库的测试项目。项目包含：

- **Golang SO库**: 实现各种数据类型的返回方法
- **Android应用**: 通过JNI调用Golang方法并显示结果
- **构建脚本**: 自动化编译和部署流程

## 项目结构

```
xiaomitv-golang-so-test/
├── README.md                 # 项目说明
├── USAGE.md                  # 使用说明（本文件）
├── build.sh                  # 主构建脚本
├── golang/                   # Golang SO库源码
│   ├── main.go              # 主要实现（C导出函数）
│   ├── bridge.h             # C头文件
│   └── build.sh             # Golang编译脚本
├── android/                  # Android应用
│   ├── app/                 # Android应用模块
│   │   ├── src/main/
│   │   │   ├── java/com/example/golangbridgetest/
│   │   │   │   ├── MainActivity.java    # 主界面
│   │   │   │   └── GolangBridge.java    # JNI接口
│   │   │   ├── res/                     # 资源文件
│   │   │   └── AndroidManifest.xml      # 应用清单
│   │   └── build.gradle                 # 应用构建配置
│   ├── build.gradle                     # 项目构建配置
│   └── gradle.properties                # Gradle配置
└── test_golang/             # Golang功能测试
    └── main.go              # 独立测试程序
```

## 环境要求

### 必需工具

1. **Go编译器** (版本 1.16+)
   ```bash
   # 检查Go版本
   go version
   ```

2. **Java开发环境** (JDK 8+)
   ```bash
   # 检查Java版本
   java -version
   ```

3. **Android NDK** (版本 21+)
   ```bash
   # 设置NDK环境变量
   export ANDROID_NDK_HOME=/path/to/android-ndk
   ```

4. **Android SDK** (用于构建Android应用)

### 版本要求

- **最低Android版本**: 6.0 (API 23)
- **目标Android版本**: 13 (API 33)
- **支持的架构**: ARM (armeabi-v7a) 和 ARM64 (arm64-v8a)

### 可选工具

- **Android Studio**: 用于开发和调试
- **ADB**: 用于安装和测试APK

## 快速开始

### 1. 测试Golang功能

首先测试Golang代码是否正常工作：

```bash
cd test_golang
go run main.go
```

预期输出：
```
=== Golang 功能测试 ===
整数: 42
字符串: Hello from Golang!
浮点数: 3.141590
布尔值: true
JSON字符串: {"features":["int","string","double","boolean","json"],...}
数组: [10,20,30,40,50]
用户信息: {"address":{"city":"北京","street":"中关村大街"},...}
系统信息: {"buildTime":"2025-08-30 11:18:50",...}
10 + 20 = 30
3.14 * 2.0 = 6.280000
```

### 2. 编译Golang SO库

```bash
cd golang
chmod +x build.sh
./build.sh
```

这将生成以下SO文件：
- `libgolang-bridge-arm.so` (ARM 32位)
- `libgolang-bridge-arm64.so` (ARM 64位)

构建脚本会自动将这些文件复制到Android项目的正确位置：
- `android/app/src/main/jniLibs/armeabi-v7a/libgolang-bridge.so`
- `android/app/src/main/jniLibs/arm64-v8a/libgolang-bridge.so`

同时会复制头文件到：
- `android/app/src/main/cpp/include/bridge.h`

### 3. 构建Android应用

```bash
# 回到项目根目录
cd ..

# 运行完整构建脚本
chmod +x build.sh
./build.sh
```

构建脚本会自动：
1. 检查环境要求
2. 编译Golang SO库
3. 复制SO文件到Android项目
4. 构建Android APK

### 4. 安装和测试

```bash
# 安装APK到设备
adb install android/app/build/outputs/apk/debug/app-debug.apk

# 启动应用
adb shell am start -n com.example.golangbridgetest/.MainActivity
```

## 功能说明

### Golang SO库功能

#### 基本数据类型
- `GetInt()`: 返回整数 42
- `GetString()`: 返回字符串 "Hello from Golang!"
- `GetDouble()`: 返回浮点数 3.14159
- `GetBoolean()`: 返回布尔值 true

#### JSON数据
- `GetJSONString()`: 返回复杂JSON对象
- `GetArray()`: 返回整数数组
- `GetUserInfo()`: 返回用户信息JSON
- `GetSystemInfo()`: 返回系统信息JSON

#### 数学运算
- `AddNumbers(a, b)`: 整数加法
- `MultiplyNumbers(a, b)`: 浮点数乘法

#### 计数器功能
- `GetCounter()`: 获取当前计数值
- `IncrementCounter()`: 计数器加1并返回新值
- `ResetCounter()`: 重置计数器为0
- `GetCounterInfo()`: 获取计数器详细信息（JSON格式）

### Android应用功能

应用提供以下测试按钮：

1. **测试整数**: 调用`GetInt()`并显示结果
2. **测试字符串**: 调用`GetString()`并显示结果
3. **测试浮点数**: 调用`GetDouble()`并显示结果
4. **测试布尔值**: 调用`GetBoolean()`并显示结果
5. **测试JSON**: 调用`GetJSONString()`并格式化显示
6. **测试数组**: 调用`GetArray()`并格式化显示
7. **测试用户信息**: 调用`GetUserInfo()`并格式化显示
8. **测试系统信息**: 调用`GetSystemInfo()`并格式化显示
9. **测试数学运算**: 调用数学运算函数并显示结果
10. **获取计数**: 调用`GetCounter()`获取当前计数值
11. **增加计数**: 调用`IncrementCounter()`增加计数并显示新值
12. **重置计数**: 调用`ResetCounter()`重置计数器为0
13. **测试计数器**: 调用`GetCounterInfo()`获取计数器详细信息
14. **清除结果**: 清空显示区域

## 技术细节

### JNI接口

`GolangBridge.java`定义了JNI接口：

```java
public class GolangBridge {
    static {
        System.loadLibrary("golang-bridge");
    }
    
    // 基本数据类型
    public static native int getInt();
    public static native String getString();
    public static native double getDouble();
    public static native boolean getBoolean();
    
    // JSON数据
    public static native String getJSONString();
    public static native String getArray();
    public static native String getUserInfo();
    public static native String getSystemInfo();
    
    // 数学运算
    public static native int addNumbers(int a, int b);
    public static native double multiplyNumbers(double a, double b);
    
    // 计数器功能
    public static native int getCounter();
    public static native int incrementCounter();
    public static native int resetCounter();
    public static native String getCounterInfo();
}
```

### C导出函数

Golang使用`//export`注释导出C函数：

```go
//export GetInt
func GetInt() C.int {
    return C.int(42)
}

//export GetJSONString
func GetJSONString() *C.char {
    data := map[string]interface{}{
        "message": "Hello from Golang SO!",
        "timestamp": time.Now().Unix(),
        // ...
    }
    
    jsonBytes, err := json.Marshal(data)
    if err != nil {
        return C.CString("{\"error\": \"JSON marshal failed\"}")
    }
    
    return C.CString(string(jsonBytes))
}

//export GetCounter
func GetCounter() C.int {
    counterMutex.Lock()
    defer counterMutex.Unlock()
    return C.int(counter)
}

//export IncrementCounter
func IncrementCounter() C.int {
    counterMutex.Lock()
    defer counterMutex.Unlock()
    counter++
    return C.int(counter)
}

//export ResetCounter
func ResetCounter() C.int {
    counterMutex.Lock()
    defer counterMutex.Unlock()
    counter = 0
    return C.int(counter)
}

//export GetCounterInfo
func GetCounterInfo() *C.char {
    counterMutex.Lock()
    defer counterMutex.Unlock()
    
    info := map[string]interface{}{
        "current_value": counter,
        "timestamp": time.Now().Unix(),
        "description": "Golang计数器",
        "operations": map[string]string{
            "get": "获取当前值",
            "increment": "增加1",
            "reset": "重置为0",
        },
    }
    
    jsonBytes, err := json.Marshal(info)
    if err != nil {
        return C.CString("{\"error\": \"Counter info marshal failed\"}")
    }
    
    return C.CString(string(jsonBytes))
}
```

## 故障排除

### 常见问题

1. **NDK路径未设置**
   ```
   错误: ANDROID_NDK_HOME 环境变量未设置
   ```
   解决：设置NDK环境变量
   ```bash
   export ANDROID_NDK_HOME=/path/to/android-ndk
   ```

2. **Go编译器未找到**
   ```
   错误: 未找到 Go 编译器
   ```
   解决：安装Go并确保在PATH中

3. **SO库加载失败**
   ```
   UnsatisfiedLinkError: dalvik.system.PathClassLoader
   ```
   解决：确保SO文件正确复制到jniLibs目录

4. **编译错误**
   ```
   错误: Golang SO 库编译失败
   ```
   解决：检查NDK版本和Go版本兼容性

### 调试技巧

1. **验证SO文件放置**
   ```bash
   ./verify_so.sh
   ```

2. **查看SO文件**
   ```bash
   ls -la android/app/src/main/jniLibs/*/
   ```

3. **检查APK内容**
   ```bash
   unzip -l android/app/build/outputs/apk/debug/app-debug.apk | grep lib
   ```

4. **查看日志**
   ```bash
   adb logcat | grep GolangBridgeTest
   ```

## 扩展开发

### 添加新的Golang函数

1. 在`golang/main.go`中添加新函数：
   ```go
   //export GetNewData
   func GetNewData() *C.char {
       // 实现逻辑
       return C.CString("new data")
   }
   ```

2. 在`golang/bridge.h`中添加声明：
   ```c
   char* GetNewData();
   ```

3. 在`GolangBridge.java`中添加JNI方法：
   ```java
   public static native String getNewData();
   ```

4. 在`MainActivity.java`中添加测试按钮和逻辑

### 修改UI界面

编辑`android/app/src/main/res/layout/activity_main.xml`来修改界面布局。

### 添加新的数据类型

参考现有实现，添加新的数据类型支持。

## 许可证

本项目仅用于学习和测试目的。

## 贡献

欢迎提交Issue和Pull Request来改进项目。
