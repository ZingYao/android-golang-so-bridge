# 多架构支持指南

## 概述

我们成功为Android + Golang SO库项目添加了全架构支持，现在可以编译和部署到所有主要的Android CPU架构。

## 支持的架构

### 1. ARM架构
- **ARM (armeabi-v7a)**: 32位ARM处理器
- **ARM64 (arm64-v8a)**: 64位ARM处理器

### 2. x86架构
- **x86**: 32位Intel/AMD处理器
- **x86_64**: 64位Intel/AMD处理器

## 编译配置

### Golang编译脚本 (golang/build.sh)

```bash
#!/bin/bash

echo "开始编译 Golang SO 库..."

# 设置基础环境变量
export CGO_ENABLED=1
export GOOS=android

# 编译所有架构
echo "编译 ARM64 版本..."
export GOARCH=arm64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-arm64.so

echo "编译 ARM 版本..."
export GOARCH=arm
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-arm.so

echo "编译 x86 版本..."
export GOARCH=386
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-x86.so

echo "编译 x86_64 版本..."
export GOARCH=amd64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-x86_64.so
```

### Android构建配置 (android/app/build.gradle)

```gradle
android {
    defaultConfig {
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
        }
    }
}
```

## 文件结构

```
android/app/src/main/jniLibs/
├── armeabi-v7a/
│   └── libgolang-bridge.so    # ARM 32位
├── arm64-v8a/
│   └── libgolang-bridge.so    # ARM 64位
├── x86/
│   └── libgolang-bridge.so    # x86 32位
└── x86_64/
    └── libgolang-bridge.so    # x86 64位
```

## 编译工具链

### NDK工具链映射
| 架构 | GOARCH | NDK工具链 | 目标目录 |
|------|--------|-----------|----------|
| ARM | arm | armv7a-linux-androideabi23-clang | armeabi-v7a |
| ARM64 | arm64 | aarch64-linux-android23-clang | arm64-v8a |
| x86 | 386 | i686-linux-android23-clang | x86 |
| x86_64 | amd64 | x86_64-linux-android23-clang | x86_64 |

## 构建命令

### 1. 编译所有架构的SO库
```bash
cd golang && ./build.sh
```

### 2. 完整构建（包含Android应用）
```bash
./build.sh
```

### 3. 快速构建测试
```bash
./quick_build.sh
```

### 4. 验证SO文件放置
```bash
./verify_so.sh
```

## 文件大小对比

| 架构 | 文件大小 | 说明 |
|------|----------|------|
| ARM | 12MB | 32位ARM，适合旧设备 |
| ARM64 | 13MB | 64位ARM，现代设备主流 |
| x86 | 12MB | 32位x86，模拟器支持 |
| x86_64 | 13MB | 64位x86，高性能模拟器 |

## 兼容性说明

### Android版本支持
- **minSdk**: 23 (Android 6.0)
- **targetSdk**: 33 (Android 13)
- **支持范围**: Android 6.0及以上版本

### 设备兼容性
- **ARM设备**: 所有Android手机和平板
- **x86设备**: Intel/AMD处理器的Android设备
- **模拟器**: 支持所有架构的Android模拟器

## 性能优化

### 1. 按需编译
如果只需要特定架构，可以修改构建脚本：

```bash
# 只编译ARM架构
export GOARCH=arm64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge-arm64.so main.go
```

### 2. 减小APK大小
在`build.gradle`中只包含需要的架构：

```gradle
ndk {
    abiFilters 'arm64-v8a'  // 只包含ARM64
}
```

### 3. 动态加载
可以根据设备架构动态加载对应的SO库：

```java
String arch = System.getProperty("os.arch");
if (arch.contains("aarch64")) {
    System.loadLibrary("golang-bridge-arm64");
} else if (arch.contains("arm")) {
    System.loadLibrary("golang-bridge-arm");
}
```

## 验证步骤

### 1. 检查编译结果
```bash
ls -la golang/*.so
```

### 2. 验证文件类型
```bash
file golang/libgolang-bridge-arm64.so
file golang/libgolang-bridge-x86_64.so
```

### 3. 检查Android项目
```bash
ls -la android/app/src/main/jniLibs/*/libgolang-bridge.so
```

### 4. 构建验证
```bash
./verify_so.sh
```

## 常见问题

### 1. 编译失败
- **问题**: `buildmode=c-shared not supported on android/0`
- **解决**: 确保设置了正确的`GOARCH`和`CC`环境变量

### 2. 架构不匹配
- **问题**: `UnsatisfiedLinkError`
- **解决**: 检查设备架构和SO库架构是否匹配

### 3. 文件大小过大
- **问题**: APK文件过大
- **解决**: 只包含目标架构，或使用动态加载

### 4. 性能问题
- **问题**: 某些架构性能较差
- **解决**: 优先使用ARM64架构，x86主要用于开发测试

## 最佳实践

1. **开发阶段**: 使用快速构建脚本，只编译需要的架构
2. **测试阶段**: 使用完整构建，验证所有架构
3. **发布阶段**: 根据目标用户选择架构组合
4. **维护阶段**: 定期验证所有架构的编译和运行

## 总结

通过多架构支持，我们实现了：

- ✅ 支持所有主要Android CPU架构
- ✅ 统一的构建和部署流程
- ✅ 完整的验证和测试机制
- ✅ 灵活的架构选择策略
- ✅ 优化的性能和兼容性

现在项目可以在任何Android设备上运行，包括：
- 现代ARM64设备（主流）
- 旧版ARM设备（兼容性）
- x86开发设备（开发便利）
- 各种Android模拟器（测试支持）
