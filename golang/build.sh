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
echo "✓ ARM64 版本编译完成"

echo "编译 ARM 版本..."
export GOARCH=arm
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-arm.so
echo "✓ ARM 版本编译完成"

echo "编译 x86 版本..."
export GOARCH=386
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-x86.so
echo "✓ x86 版本编译完成"

echo "编译 x86_64 版本..."
export GOARCH=amd64
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android23-clang
go build -buildmode=c-shared -o libgolang-bridge.so main.go
mv libgolang-bridge.so libgolang-bridge-x86_64.so
echo "✓ x86_64 版本编译完成"

echo ""
echo "编译完成！"
echo "生成的 SO 文件："
ls -la *.so
echo ""
echo "支持的架构："
echo "- ARM64 (arm64-v8a): libgolang-bridge-arm64.so"
echo "- ARM (armeabi-v7a): libgolang-bridge-arm.so"
echo "- x86: libgolang-bridge-x86.so"
echo "- x86_64: libgolang-bridge-x86_64.so"
