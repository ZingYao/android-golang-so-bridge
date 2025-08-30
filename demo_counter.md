# 计数器功能演示

## 功能概述

新增的计数器功能提供了以下特性：

- **线程安全**: 使用互斥锁保证多线程环境下的数据一致性
- **持久化**: 计数器值在应用运行期间保持状态
- **多种操作**: 支持获取、增加、重置和获取详细信息

## 功能列表

### 1. 获取当前计数
- **函数**: `GetCounter()`
- **返回**: 当前计数值 (int)
- **按钮**: "获取计数"

### 2. 增加计数
- **函数**: `IncrementCounter()`
- **功能**: 计数器值加1
- **返回**: 增加后的新值 (int)
- **按钮**: "增加计数"

### 3. 重置计数
- **函数**: `ResetCounter()`
- **功能**: 将计数器重置为0
- **返回**: 重置后的值 (int)
- **按钮**: "重置计数"

### 4. 获取计数器信息
- **函数**: `GetCounterInfo()`
- **返回**: JSON格式的详细信息
- **按钮**: "测试计数器"

## 使用示例

### 在Android应用中

1. **启动应用**
2. **点击"获取计数"** - 显示当前计数值（初始为0）
3. **点击"增加计数"** - 计数器加1，显示新值
4. **多次点击"增加计数"** - 观察计数器的递增
5. **点击"测试计数器"** - 查看计数器的详细信息
6. **点击"重置计数"** - 将计数器重置为0

### 预期输出示例

```
[2025-08-30 11:30:00] 获取计数: 0
[2025-08-30 11:30:05] 增加计数: 1
[2025-08-30 11:30:10] 增加计数: 2
[2025-08-30 11:30:15] 增加计数: 3
[2025-08-30 11:30:20] 测试计数器:
{
  "current_value": 3,
  "description": "Golang计数器",
  "operations": {
    "get": "获取当前值",
    "increment": "增加1",
    "reset": "重置为0"
  },
  "timestamp": 1756524620
}
[2025-08-30 11:30:25] 重置计数: 0
```

## 技术实现

### Golang端

```go
// 全局计数器变量
var (
    counter int = 0
    counterMutex sync.Mutex
)

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
```

### Android端

```java
// JNI接口
public static native int getCounter();
public static native int incrementCounter();
public static native int resetCounter();
public static native String getCounterInfo();

// 调用示例
private void incrementCounter() {
    try {
        int newValue = GolangBridge.incrementCounter();
        appendResult("增加计数: " + newValue);
    } catch (Exception e) {
        appendResult("增加计数失败: " + e.getMessage());
    }
}
```

## 特性说明

### 线程安全
- 使用`sync.Mutex`确保多线程环境下的数据一致性
- 所有计数器操作都是原子性的

### 状态保持
- 计数器值在应用运行期间保持状态
- 每次调用`IncrementCounter()`都会增加计数
- 只有调用`ResetCounter()`才会重置为0

### 错误处理
- Android端包含完整的异常处理
- Golang端返回错误信息用于调试

### JSON格式
- `GetCounterInfo()`返回结构化的JSON数据
- 包含当前值、时间戳、描述和操作说明

## 扩展建议

1. **持久化存储**: 可以将计数器值保存到文件或数据库
2. **多计数器**: 可以扩展为支持多个命名计数器
3. **历史记录**: 可以记录计数器的操作历史
4. **配置选项**: 可以添加计数器的配置选项（如最大值限制）

## 测试验证

可以通过以下方式验证计数器功能：

1. **Golang测试**: 运行`test_golang/main.go`
2. **Android测试**: 在设备上运行应用并测试各个按钮
3. **并发测试**: 可以添加并发测试来验证线程安全性
