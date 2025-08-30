#!/bin/bash

echo "=== 快速构建测试 ==="

# 检查环境
if [ -z "$ANDROID_NDK_HOME" ]; then
    echo "错误: 请设置 ANDROID_NDK_HOME 环境变量"
    echo "例如: export ANDROID_NDK_HOME=/path/to/android-ndk"
    exit 1
fi

# 编译 Golang SO 库（只编译当前架构）
echo "编译 Golang SO 库..."
cd golang

# 编译所有架构版本
echo "编译所有架构版本..."
export CGO_ENABLED=1
export GOOS=android

# ARM64
echo "编译 ARM64 版本..."
export GOARCH=arm64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-arm64.so

# ARM
echo "编译 ARM 版本..."
export GOARCH=arm
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-arm.so

# x86
echo "编译 x86 版本..."
export GOARCH=386
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-x86.so

# x86_64
echo "编译 x86_64 版本..."
export GOARCH=amd64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-x86_64.so

cd ..

# 复制 SO 文件到 Android 项目
echo "复制 SO 文件到 Android 项目..."

# 创建所有架构的目标目录
mkdir -p android/app/src/main/jniLibs/armeabi-v7a
mkdir -p android/app/src/main/jniLibs/arm64-v8a
mkdir -p android/app/src/main/jniLibs/x86
mkdir -p android/app/src/main/jniLibs/x86_64

# 复制所有架构的 SO 文件
if [ -f "golang/libgolang-bridge-arm.so" ]; then
    cp golang/libgolang-bridge-arm.so android/app/src/main/jniLibs/armeabi-v7a/libgolang-bridge.so
    echo "✓ 复制 ARM 版本到 armeabi-v7a"
fi

if [ -f "golang/libgolang-bridge-arm64.so" ]; then
    cp golang/libgolang-bridge-arm64.so android/app/src/main/jniLibs/arm64-v8a/libgolang-bridge.so
    echo "✓ 复制 ARM64 版本到 arm64-v8a"
fi

if [ -f "golang/libgolang-bridge-x86.so" ]; then
    cp golang/libgolang-bridge-x86.so android/app/src/main/jniLibs/x86/libgolang-bridge.so
    echo "✓ 复制 x86 版本到 x86"
fi

if [ -f "golang/libgolang-bridge-x86_64.so" ]; then
    cp golang/libgolang-bridge-x86_64.so android/app/src/main/jniLibs/x86_64/libgolang-bridge.so
    echo "✓ 复制 x86_64 版本到 x86_64"
fi

# 复制头文件
mkdir -p android/app/src/main/cpp/include
if [ -f "golang/bridge.h" ]; then
    cp golang/bridge.h android/app/src/main/cpp/include/
    echo "✓ 复制头文件到 cpp/include"
fi

echo ""
echo "=== 快速构建完成 ==="
echo "现在可以运行 ./verify_so.sh 来验证文件放置"
echo "或者运行 ./build.sh 进行完整构建"
