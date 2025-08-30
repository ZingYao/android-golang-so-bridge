#!/bin/bash

echo "=== Android + Golang SO 测试项目构建脚本 ==="

# 检查必要的工具
check_requirements() {
    echo "检查构建环境..."
    
    if ! command -v go &> /dev/null; then
        echo "错误: 未找到 Go 编译器"
        exit 1
    fi
    
    if ! command -v java &> /dev/null; then
        echo "错误: 未找到 Java 编译器"
        exit 1
    fi
    
    if [ -z "$ANDROID_NDK_HOME" ]; then
        echo "警告: ANDROID_NDK_HOME 环境变量未设置"
        echo "请设置 Android NDK 路径，例如:"
        echo "export ANDROID_NDK_HOME=/path/to/android-ndk"
        exit 1
    fi
    
    echo "构建环境检查通过"
}

# 编译 Golang SO 库
build_golang_lib() {
    echo "开始编译 Golang SO 库..."
    cd golang
    
    if [ ! -f "build.sh" ]; then
        echo "错误: 未找到 Golang 构建脚本"
        exit 1
    fi
    
    chmod +x build.sh
    ./build.sh
    
    if [ $? -ne 0 ]; then
        echo "错误: Golang SO 库编译失败"
        exit 1
    fi
    
    echo "Golang SO 库编译完成"
    cd ..
}

# 复制 SO 文件到 Android 项目
copy_so_files() {
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
    else
        echo "✗ 未找到 ARM 版本 SO 文件"
    fi
    
    if [ -f "golang/libgolang-bridge-arm64.so" ]; then
        cp golang/libgolang-bridge-arm64.so android/app/src/main/jniLibs/arm64-v8a/libgolang-bridge.so
        echo "✓ 复制 ARM64 版本到 arm64-v8a"
    else
        echo "✗ 未找到 ARM64 版本 SO 文件"
    fi
    
    if [ -f "golang/libgolang-bridge-x86.so" ]; then
        cp golang/libgolang-bridge-x86.so android/app/src/main/jniLibs/x86/libgolang-bridge.so
        echo "✓ 复制 x86 版本到 x86"
    else
        echo "✗ 未找到 x86 版本 SO 文件"
    fi
    
    if [ -f "golang/libgolang-bridge-x86_64.so" ]; then
        cp golang/libgolang-bridge-x86_64.so android/app/src/main/jniLibs/x86_64/libgolang-bridge.so
        echo "✓ 复制 x86_64 版本到 x86_64"
    else
        echo "✗ 未找到 x86_64 版本 SO 文件"
    fi
    
    # 复制头文件到 Android 项目（可选，用于开发参考）
    mkdir -p android/app/src/main/cpp/include
    if [ -f "golang/bridge.h" ]; then
        cp golang/bridge.h android/app/src/main/cpp/include/
        echo "✓ 复制头文件到 cpp/include"
    fi
    
    echo "SO 文件和头文件复制完成（支持所有架构）"
}

# 构建 Android 应用
build_android_app() {
    echo "开始构建 Android 应用..."
    cd android
    
    # 检查是否有 gradlew
    if [ ! -f "gradlew" ]; then
        echo "错误: 未找到 gradlew 脚本"
        exit 1
    fi
    
    chmod +x gradlew
    
    # 清理并构建
    ./gradlew clean
    ./gradlew assembleDebug
    
    if [ $? -ne 0 ]; then
        echo "错误: Android 应用构建失败"
        exit 1
    fi
    
    echo "Android 应用构建完成"
    echo "APK 文件位置: app/build/outputs/apk/debug/app-debug.apk"
    cd ..
}

# 主函数
main() {
    check_requirements
    build_golang_lib
    copy_so_files
    build_android_app
    
    echo "=== 构建完成 ==="
    echo "APK 文件: android/app/build/outputs/apk/debug/app-debug.apk"
    echo "可以使用以下命令安装到设备:"
    echo "adb install android/app/build/outputs/apk/debug/app-debug.apk"
}

# 执行主函数
main
