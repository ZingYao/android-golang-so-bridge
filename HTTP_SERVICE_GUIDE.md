# HTTP服务功能指南

## 功能概述

我们为Android + Golang SO库项目添加了HTTP服务功能，使用Gin框架在Golang中启动HTTP服务器，并提供RESTful API接口。

## 新增功能

### 1. HTTP服务启动
- **功能**: 在指定端口启动HTTP服务
- **端口**: 默认8080端口
- **框架**: 使用Gin Web框架
- **状态检查**: 自动检查服务启动状态

### 2. HTTP服务调用
- **功能**: 调用HTTP服务的各个端点
- **支持**: GET和POST请求
- **响应**: JSON格式返回

## API端点

### 健康检查
```
GET /health
```
返回服务运行状态

### 计数器相关
```
GET /counter          # 获取当前计数器值
POST /counter/increment  # 增加计数器
POST /counter/reset      # 重置计数器
```

### 系统信息
```
GET /system/info
```
返回系统信息和能力

## Golang实现

### 主要函数

#### StartHTTPServer
```go
//export StartHTTPServer
func StartHTTPServer(port C.int) *C.char {
    // 创建Gin引擎
    gin.SetMode(gin.ReleaseMode)
    server = gin.New()
    server.Use(gin.Logger(), gin.Recovery())
    
    // 设置路由
    setupRoutes(server)
    
    // 启动服务
    go func() {
        portStr := strconv.Itoa(int(port))
        err := server.Run(":" + portStr)
        if err != nil {
            fmt.Printf("HTTP服务启动失败: %v\n", err)
        }
    }()
    
    // 返回启动结果
    return C.CString(jsonResult)
}
```

#### CallHTTPService
```go
//export CallHTTPService
func CallHTTPService(url *C.char) *C.char {
    urlStr := C.GoString(url)
    
    // 发送HTTP请求
    resp, err := http.Get(urlStr)
    if err != nil {
        // 返回错误信息
        return C.CString(errorResult)
    }
    defer resp.Body.Close()
    
    // 读取响应
    body := make([]byte, 1024)
    n, _ := resp.Body.Read(body)
    body = body[:n]
    
    // 返回响应结果
    return C.CString(jsonResult)
}
```

### 路由设置
```go
func setupRoutes(r *gin.Engine) {
    // 健康检查
    r.GET("/health", func(c *gin.Context) {
        c.JSON(200, gin.H{
            "status":  "ok",
            "message": "HTTP服务运行正常",
            "time":    time.Now().Unix(),
        })
    })
    
    // 计数器相关
    r.GET("/counter", func(c *gin.Context) {
        counterMutex.Lock()
        defer counterMutex.Unlock()
        c.JSON(200, gin.H{
            "counter": counter,
            "time":    time.Now().Unix(),
        })
    })
    
    // 系统信息
    r.GET("/system/info", func(c *gin.Context) {
        info := map[string]interface{}{
            "platform":    "Android",
            "language":    "Golang",
            "version":     "1.0.0",
            "capabilities": map[string]interface{}{
                "json":     true,
                "math":     true,
                "counter":  true,
                "threading": true,
                "http":     true,
            },
        }
        c.JSON(200, info)
    })
}
```

## Android实现

### Java接口
```java
// HTTP服务功能
public static String startHTTPServer(int port) {
    if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
    try {
        return nativeStartHTTPServer(port);
    } catch (UnsatisfiedLinkError e) {
        return "{\"error\": \"" + e.getMessage() + "\"}";
    }
}

public static String callHTTPService(String url) {
    if (!libraryLoaded) return "{\"error\": \"Library not loaded\"}";
    try {
        return nativeCallHTTPService(url);
    } catch (UnsatisfiedLinkError e) {
        return "{\"error\": \"" + e.getMessage() + "\"}";
    }
}
```

### UI按钮
- **启动HTTP服务**: 在端口8080启动HTTP服务
- **测试HTTP服务**: 调用健康检查端点
- **调用HTTP服务**: 测试多个API端点

## 使用方法

### 1. 启动HTTP服务
点击"启动HTTP服务"按钮，系统会：
- 在端口8080启动Gin HTTP服务
- 检查服务启动状态
- 返回启动结果（成功/失败）

### 2. 测试HTTP服务
点击"测试HTTP服务"按钮，系统会：
- 调用`http://localhost:8080/health`
- 显示健康检查结果

### 3. 调用HTTP服务
点击"调用HTTP服务"按钮，系统会：
- 依次调用多个端点：
  - `/health` - 健康检查
  - `/counter` - 获取计数器
  - `/system/info` - 系统信息
- 显示每个端点的响应

## 响应格式

### 启动服务响应
```json
{
  "success": true,
  "message": "HTTP服务启动成功",
  "port": 8080
}
```

### 调用服务响应
```json
{
  "success": true,
  "status": 200,
  "response": "{\"status\":\"ok\",\"message\":\"HTTP服务运行正常\"}",
  "url": "http://localhost:8080/health"
}
```

## 错误处理

### 服务未启动
```json
{
  "success": false,
  "error": "HTTP服务未启动"
}
```

### 请求失败
```json
{
  "success": false,
  "error": "请求失败: connection refused"
}
```

## 技术特点

1. **线程安全**: 使用互斥锁保护共享资源
2. **异步启动**: HTTP服务在后台goroutine中启动
3. **状态检查**: 自动验证服务启动状态
4. **错误处理**: 完善的错误处理和回退机制
5. **RESTful API**: 标准的REST API设计
6. **JSON响应**: 统一的JSON响应格式

## 依赖管理

### Go模块
```go
module golang-bridge

go 1.21

require github.com/gin-gonic/gin v1.9.1
```

### 自动依赖下载
```bash
cd golang && go mod tidy
```

## 构建和部署

1. **编译Golang SO库**:
   ```bash
   cd golang && ./build.sh
   ```

2. **构建Android应用**:
   ```bash
   ./build.sh
   ```

3. **安装APK**:
   ```bash
   adb install android/app/build/outputs/apk/debug/app-debug.apk
   ```

## 总结

通过添加HTTP服务功能，我们实现了：

- ✅ 在Golang中启动HTTP服务器
- ✅ 提供RESTful API接口
- ✅ Android应用调用HTTP服务
- ✅ 完整的错误处理机制
- ✅ 线程安全的实现
- ✅ 统一的JSON响应格式

现在Android应用不仅可以调用Golang SO库的本地函数，还可以通过HTTP服务进行网络通信，大大扩展了应用的功能和灵活性。
