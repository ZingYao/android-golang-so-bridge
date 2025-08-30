# SO文件放置指南

## 正确的SO文件结构

在Android项目中，SO文件必须放置在特定的目录结构中才能被正确加载。

### 标准目录结构

```
android/app/src/main/
├── jniLibs/                    # SO库文件目录
│   ├── armeabi-v7a/           # ARM 32位架构
│   │   └── libgolang-bridge.so
│   └── arm64-v8a/             # ARM 64位架构
│       └── libgolang-bridge.so
└── cpp/include/               # 头文件目录（可选）
    └── bridge.h
```

### 重要说明

1. **SO文件命名**：
   - Android期望的SO文件名：`libgolang-bridge.so`
   - Golang编译生成的文件名：`libgolang-bridge-xxx.so`
   - 需要重命名为Android期望的名称

2. **架构目录**：
   - `armeabi-v7a/` - ARM 32位
   - `arm64-v8a/` - ARM 64位

3. **JNI加载**：
   ```java
   System.loadLibrary("golang-bridge");  // 加载 libgolang-bridge.so
   ```

## 构建脚本说明

### 主构建脚本 (`build.sh`)
- 编译所有架构的SO文件
- 自动复制到正确的jniLibs目录
- 复制头文件到cpp/include目录

### 快速构建脚本 (`quick_build.sh`)
- 只编译当前系统架构的SO文件
- 用于快速测试和开发

### 验证脚本 (`verify_so.sh`)
- 检查SO文件是否正确放置
- 显示文件大小和类型信息
- 验证目录结构

## 常见问题

### 1. SO文件未找到
```
UnsatisfiedLinkError: dalvik.system.PathClassLoader
```
**解决方案**：
- 确保SO文件在正确的jniLibs目录中
- 检查文件名是否为`libgolang-bridge.so`
- 运行`./verify_so.sh`验证文件放置

### 2. 架构不匹配
```
java.lang.UnsatisfiedLinkError: dlopen failed
```
**解决方案**：
- 确保编译了目标设备的架构
- 检查`build.gradle`中的`abiFilters`配置
- 使用`adb shell getprop ro.product.cpu.abi`查看设备架构

### 3. 编译错误
```
错误: Golang SO 库编译失败
```
**解决方案**：
- 检查`ANDROID_NDK_HOME`环境变量
- 确保NDK版本兼容
- 检查Go版本和CGO支持

## 验证步骤

1. **编译SO文件**：
   ```bash
   cd golang
   ./build.sh
   ```

2. **复制到Android项目**：
   ```bash
   ./quick_build.sh
   ```

3. **验证文件放置**：
   ```bash
   ./verify_so.sh
   ```

4. **检查目录结构**：
   ```bash
   ls -la android/app/src/main/jniLibs/*/
   ```

## 最佳实践

1. **开发阶段**：
   - 使用`quick_build.sh`快速测试
   - 只编译目标架构，节省时间

2. **发布阶段**：
   - 使用`build.sh`编译所有架构
   - 确保支持所有目标设备

3. **调试阶段**：
   - 使用`verify_so.sh`检查文件
   - 查看`adb logcat`日志
   - 检查APK内容：`unzip -l app-debug.apk | grep lib`

4. **版本管理**：
   - 将SO文件添加到版本控制
   - 记录编译环境和参数
   - 保持头文件同步更新
