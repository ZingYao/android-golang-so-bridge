# Android + Golang SO 测试项目

这个项目演示了如何在Android应用中通过JNI调用Golang编译的SO库。

## 项目结构

```
├── android/                 # Android应用
│   ├── app/                # Android应用代码
│   │   ├── src/main/
│   │   │   ├── jniLibs/    # SO库文件目录
│   │   │   │   ├── armeabi-v7a/libgolang-bridge.so
│   │   │   │   └── arm64-v8a/libgolang-bridge.so
│   │   │   └── cpp/include/bridge.h  # 头文件
│   │   └── build.gradle    # 应用构建配置
│   └── build.gradle        # 项目构建配置
├── golang/                 # Golang SO库源码
│   ├── main.go            # 主要实现
│   ├── bridge.h           # C头文件
│   └── build.sh           # Golang编译脚本
├── test_golang/           # Golang功能测试
│   └── main.go            # 独立测试程序
├── build.sh               # 主构建脚本
├── quick_build.sh         # 快速构建脚本
├── verify_so.sh           # SO文件验证脚本
└── README.md              # 项目说明
```

## 功能特性

- Android应用通过JNI调用Golang SO库
- Golang返回多种数据类型（int, string, JSON等）
- Android解析并显示Golang返回的内容
- 支持Android 6.0及以上版本
- 支持ARM和ARM64架构

## 构建步骤

1. 编译Golang SO库
2. 构建Android应用
3. 运行测试

## 技术栈

- Android: Java + JNI (API 23+)
- Golang: CGO + 跨平台编译
- 数据交换: JSON格式
- 目标设备: Android 6.0+ (ARM/ARM64)
