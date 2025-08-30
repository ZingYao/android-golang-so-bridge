#!/bin/bash

echo "=== 验证 SO 文件放置 ==="

# 检查 jniLibs 目录结构
echo "检查 jniLibs 目录结构..."
if [ -d "android/app/src/main/jniLibs" ]; then
    echo "✓ jniLibs 目录存在"
    ls -la android/app/src/main/jniLibs/
else
    echo "✗ jniLibs 目录不存在"
fi

echo ""

# 检查各个架构的 SO 文件
architectures=("armeabi-v7a" "arm64-v8a" "x86" "x86_64")

for arch in "${architectures[@]}"; do
    so_path="android/app/src/main/jniLibs/$arch/libgolang-bridge.so"
    if [ -f "$so_path" ]; then
        echo "✓ $arch: libgolang-bridge.so 存在"
        echo "  文件大小: $(ls -lh $so_path | awk '{print $5}')"
        echo "  文件类型: $(file $so_path | cut -d: -f2-)"
    else
        echo "✗ $arch: libgolang-bridge.so 不存在"
    fi
done

echo ""

# 检查头文件
header_path="android/app/src/main/cpp/include/bridge.h"
if [ -f "$header_path" ]; then
    echo "✓ 头文件存在: $header_path"
else
    echo "✗ 头文件不存在: $header_path"
fi

echo ""

# 检查 Golang 编译的原始文件
echo "检查 Golang 编译的原始文件..."
if [ -d "golang" ]; then
    echo "Golang 目录中的 SO 文件:"
    ls -la golang/*.so 2>/dev/null || echo "  没有找到 SO 文件"
else
    echo "✗ Golang 目录不存在"
fi

echo ""

# 检查 Android 构建配置
echo "检查 Android 构建配置..."
if [ -f "android/app/build.gradle" ]; then
    echo "✓ build.gradle 存在"
    echo "检查 ABI 配置:"
    grep -A 5 "abiFilters" android/app/build.gradle || echo "  未找到 ABI 配置"
else
    echo "✗ build.gradle 不存在"
fi

echo ""
echo "=== 验证完成 ==="
