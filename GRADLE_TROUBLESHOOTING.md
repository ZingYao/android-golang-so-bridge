# Gradle问题解决指南

## 问题描述

您遇到的错误：
```
'org.gradle.api.artifacts.Dependency org.gradle.api.artifacts.dsl.DependencyHandler.module(java.lang.Object)'
```

这是一个典型的Gradle依赖缓存损坏和Java版本兼容性问题。

## 问题原因

1. **Gradle Wrapper文件损坏** - `gradle-wrapper.jar`文件为空（0字节）
2. **Java版本不兼容** - 使用Java 24，但Gradle 7.5不支持
3. **Gradle版本不匹配** - wrapper配置和项目配置版本不一致
4. **缓存文件损坏** - 某些缓存文件被不兼容的Java版本编译

## 解决步骤

### 1. 修复Gradle Wrapper

```bash
# 删除损坏的wrapper文件
rm -rf android/gradle/wrapper/gradle-wrapper.jar

# 使用代理下载正确的wrapper文件
export https_proxy=http://127.0.0.1:7897
curl -L -o android/gradle/wrapper/gradle-wrapper.jar \
  https://github.com/gradle/gradle/raw/v7.5.0/gradle/wrapper/gradle-wrapper.jar
```

### 2. 升级Gradle版本

修改 `android/gradle/wrapper/gradle-wrapper.properties`：
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.8-bin.zip
```

修改 `android/build.gradle`：
```gradle
classpath 'com.android.tools.build:gradle:8.4.0'
```

### 3. 设置兼容的Java版本

```bash
# 查看可用的Java版本
/usr/libexec/java_home -V

# 设置JAVA_HOME为Java 17
export JAVA_HOME=/Users/zing/Library/Java/JavaVirtualMachines/ms-17.0.16/Contents/Home

# 验证Java版本
java -version
```

### 4. 清理缓存

```bash
# 清理Gradle缓存
rm -rf ~/.gradle/caches

# 清理项目缓存
rm -rf android/.gradle android/build android/app/build

# 停止Gradle守护进程
./gradlew --stop
```

### 5. 添加JVM参数

修改 `android/gradle.properties`：
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8 --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED
```

### 6. 创建缺失的资源文件

```bash
# 创建图标目录
mkdir -p android/app/src/main/res/mipmap-anydpi-v26

# 创建图标文件
# (见项目中的具体文件)
```

## 最终配置

### Gradle版本
- **Gradle**: 8.8
- **Android Gradle Plugin**: 8.4.0
- **Java版本**: 17 (OpenJDK 17.0.16)

### 项目配置
- **minSdk**: 23 (Android 6.0)
- **targetSdk**: 33 (Android 13)
- **支持的架构**: ARM (armeabi-v7a) 和 ARM64 (arm64-v8a)

## 验证步骤

1. **检查Gradle版本**：
   ```bash
   cd android && ./gradlew --version
   ```

2. **测试清理**：
   ```bash
   ./gradlew clean
   ```

3. **构建APK**：
   ```bash
   ./gradlew assembleDebug
   ```

4. **检查APK**：
   ```bash
   ls -la app/build/outputs/apk/debug/
   ```

## 常见问题

### 1. Java版本问题
- **错误**: `Unsupported class file major version 68`
- **解决**: 使用Java 17或更低版本

### 2. 网络问题
- **错误**: 下载Gradle失败
- **解决**: 使用代理或VPN

### 3. 权限问题
- **错误**: 无法创建目录或文件
- **解决**: 检查文件权限，使用`chmod +x gradlew`

### 4. 缓存问题
- **错误**: 构建失败但原因不明
- **解决**: 清理所有缓存文件

## 预防措施

1. **使用稳定的Java版本** - 推荐Java 17 LTS
2. **定期清理缓存** - 避免缓存文件损坏
3. **使用版本控制** - 保存正确的配置文件
4. **备份重要文件** - 特别是gradle-wrapper.jar

## 总结

通过以上步骤，我们成功解决了：
- ✅ Gradle Wrapper文件损坏
- ✅ Java版本兼容性问题
- ✅ Gradle版本不匹配
- ✅ 缓存文件损坏
- ✅ 缺失资源文件

项目现在可以正常构建，生成的APK文件大小约9.4MB，支持Android 6.0及以上版本。
