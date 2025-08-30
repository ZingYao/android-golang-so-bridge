# Android版本兼容性说明

## 版本支持

### 当前配置
- **最低支持版本**: Android 6.0 (API 23)
- **目标版本**: Android 13 (API 33)
- **编译工具链**: Android NDK API 23

### 版本选择原因

#### 为什么选择Android 6.0作为最低版本？

1. **市场份额**: Android 6.0+覆盖了95%以上的活跃Android设备
2. **功能稳定性**: Android 6.0引入了重要的安全特性和API改进
3. **JNI兼容性**: 更好的JNI支持和性能优化
4. **开发效率**: 减少对老旧设备的兼容性处理

#### Android版本分布（2024年数据）
- Android 6.0-7.0: ~5%
- Android 8.0-9.0: ~15%
- Android 10-11: ~35%
- Android 12-13: ~45%

## 技术细节

### API级别对应关系
```
Android 6.0 (Marshmallow) → API 23
Android 7.0 (Nougat)      → API 24-25
Android 8.0 (Oreo)        → API 26-27
Android 9.0 (Pie)         → API 28
Android 10                → API 29
Android 11                → API 30
Android 12                → API 31-32
Android 13                → API 33
```

### NDK工具链配置
```bash
# ARM 64位
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android23-clang

# ARM 32位
export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi23-clang
```

### build.gradle配置
```gradle
android {
    defaultConfig {
        minSdk 23          // Android 6.0
        targetSdk 33       // Android 13
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a'
        }
    }
}
```

## 兼容性测试

### 测试设备列表
- Android 6.0设备（最低版本测试）
- Android 7.0-8.0设备（中间版本测试）
- Android 10-11设备（主流版本测试）
- Android 12-13设备（最新版本测试）

### 测试项目
1. **JNI加载**: 验证SO库是否正确加载
2. **功能测试**: 测试所有Golang函数调用
3. **性能测试**: 验证在不同Android版本上的性能表现
4. **内存测试**: 检查内存使用和泄漏情况

## 如果需要支持更低版本

### 支持Android 5.0 (API 21)
如果需要支持Android 5.0，需要：

1. **修改build.gradle**:
   ```gradle
   minSdk 21  // Android 5.0
   ```

2. **修改NDK工具链**:
   ```bash
   # ARM 64位
   export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android21-clang
   
   # ARM 32位
   export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi21-clang
   ```

3. **代码兼容性检查**:
   - 检查使用的API是否在API 21中可用
   - 添加必要的兼容性代码
   - 测试在Android 5.0设备上的运行情况

### 支持Android 4.4 (API 19)
如果需要支持Android 4.4，需要：

1. **修改build.gradle**:
   ```gradle
   minSdk 19  // Android 4.4
   ```

2. **修改NDK工具链**:
   ```bash
   # ARM 64位
   export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android19-clang
   
   # ARM 32位
   export CC=$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi19-clang
   ```

3. **重大兼容性问题**:
   - Android 4.4的JNI实现可能有差异
   - 某些现代API不可用
   - 需要更多的兼容性处理

## 性能考虑

### 不同Android版本的性能差异
- **Android 6.0-7.0**: 基础性能，可能需要优化
- **Android 8.0-9.0**: 较好的性能表现
- **Android 10-11**: 优秀的性能表现
- **Android 12-13**: 最佳性能表现

### 优化建议
1. **JNI调用优化**: 减少JNI调用次数
2. **内存管理**: 注意内存泄漏问题
3. **错误处理**: 增强错误处理机制
4. **性能监控**: 添加性能监控代码

## 发布策略

### 推荐策略
1. **主要版本**: 支持Android 6.0+
2. **扩展版本**: 支持Android 5.0+（如果需要）
3. **兼容性版本**: 支持Android 4.4+（不推荐）

### 版本选择建议
- **新项目**: 建议使用Android 6.0+作为最低版本
- **现有项目**: 根据用户群体决定最低版本
- **企业项目**: 根据企业设备策略决定

## 总结

当前配置（Android 6.0+）提供了：
- ✅ 95%+的设备覆盖率
- ✅ 稳定的API支持
- ✅ 良好的性能表现
- ✅ 简化的开发维护
- ✅ 较小的APK大小

这是一个平衡了兼容性、性能和开发效率的最佳选择。
