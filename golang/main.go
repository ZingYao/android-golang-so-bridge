package main

import (
	"C"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
)

// 全局变量
var (
	counter      int = 0
	counterMutex sync.Mutex

	// HTTP服务相关
	server      *gin.Engine
	serverPort  int = 0
	serverMutex sync.Mutex
)

//export GetInt
func GetInt() C.int {
	return C.int(42)
}

//export GetString
func GetString() *C.char {
	return C.CString("Hello from Golang!")
}

//export GetDouble
func GetDouble() C.double {
	return C.double(3.14159)
}

//export GetBoolean
func GetBoolean() C.int {
	return C.int(1) // true
}

//export GetJSONString
func GetJSONString() *C.char {
	data := map[string]interface{}{
		"message":   "Hello from Golang SO!",
		"timestamp": time.Now().Unix(),
		"version":   "1.0.0",
		"features":  []string{"int", "string", "double", "boolean", "json"},
		"numbers":   []int{1, 2, 3, 4, 5},
		"nested": map[string]interface{}{
			"key":   "value",
			"count": 100,
		},
	}

	jsonBytes, err := json.Marshal(data)
	if err != nil {
		return C.CString("{\"error\": \"JSON marshal failed\"}")
	}

	return C.CString(string(jsonBytes))
}

//export GetArray
func GetArray() *C.char {
	numbers := []int{10, 20, 30, 40, 50}
	jsonBytes, err := json.Marshal(numbers)
	if err != nil {
		return C.CString("[]")
	}
	return C.CString(string(jsonBytes))
}

//export GetUserInfo
func GetUserInfo() *C.char {
	user := map[string]interface{}{
		"name":     "张三",
		"age":      25,
		"email":    "zhangsan@example.com",
		"isActive": true,
		"hobbies":  []string{"编程", "阅读", "运动"},
		"address": map[string]string{
			"city":   "北京",
			"street": "中关村大街",
		},
	}

	jsonBytes, err := json.Marshal(user)
	if err != nil {
		return C.CString("{\"error\": \"User info marshal failed\"}")
	}

	return C.CString(string(jsonBytes))
}

//export GetSystemInfo
func GetSystemInfo() *C.char {
	systemInfo := map[string]interface{}{
		"platform":    "Android",
		"language":    "Golang",
		"version":     "1.0.0",
		"timestamp":   time.Now().Unix(),
		"description": "Golang SO库系统信息",
		"capabilities": map[string]interface{}{
			"json":      true,
			"math":      true,
			"counter":   true,
			"threading": true,
		},
	}

	jsonBytes, err := json.Marshal(systemInfo)
	if err != nil {
		return C.CString("{\"error\": \"System info marshal failed\"}")
	}

	return C.CString(string(jsonBytes))
}

//export AddNumbers
func AddNumbers(a, b C.int) C.int {
	return a + b
}

//export MultiplyNumbers
func MultiplyNumbers(a, b C.double) C.double {
	return a * b
}

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

//export GetCounterInfo
func GetCounterInfo() *C.char {
	counterMutex.Lock()
	defer counterMutex.Unlock()

	info := map[string]interface{}{
		"current_value": counter,
		"timestamp":     time.Now().Unix(),
		"description":   "Golang计数器",
		"operations": map[string]string{
			"get":       "获取当前值",
			"increment": "增加1",
			"reset":     "重置为0",
		},
	}

	jsonBytes, err := json.Marshal(info)
	if err != nil {
		return C.CString("{\"error\": \"Counter info marshal failed\"}")
	}

	return C.CString(string(jsonBytes))
}

//export StartHTTPServer
func StartHTTPServer(port C.int) *C.char {
	serverMutex.Lock()
	defer serverMutex.Unlock()

	// 如果服务已经在运行，返回错误
	if server != nil {
		result := map[string]interface{}{
			"success": false,
			"error":   "HTTP服务已在运行",
			"port":    serverPort,
		}
		jsonBytes, _ := json.Marshal(result)
		return C.CString(string(jsonBytes))
	}

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

	// 等待服务启动
	time.Sleep(1 * time.Second)

	// 检查服务是否启动成功
	resp, err := http.Get(fmt.Sprintf("http://localhost:%d/health", port))
	if err == nil && resp.StatusCode == 200 {
		serverPort = int(port)
		result := map[string]interface{}{
			"success": true,
			"message": "HTTP服务启动成功",
			"port":    serverPort,
		}
		jsonBytes, _ := json.Marshal(result)
		return C.CString(string(jsonBytes))
	}

	// 启动失败
	server = nil
	result := map[string]interface{}{
		"success": false,
		"error":   "HTTP服务启动失败",
		"port":    0,
	}
	jsonBytes, _ := json.Marshal(result)
	return C.CString(string(jsonBytes))
}

//export CallHTTPService
func CallHTTPService(url *C.char) *C.char {
	serverMutex.Lock()
	defer serverMutex.Unlock()

	if server == nil {
		result := map[string]interface{}{
			"success": false,
			"error":   "HTTP服务未启动",
		}
		jsonBytes, _ := json.Marshal(result)
		return C.CString(string(jsonBytes))
	}

	urlStr := C.GoString(url)

	// 发送HTTP请求
	resp, err := http.Get(urlStr)
	if err != nil {
		result := map[string]interface{}{
			"success": false,
			"error":   fmt.Sprintf("请求失败: %v", err),
		}
		jsonBytes, _ := json.Marshal(result)
		return C.CString(string(jsonBytes))
	}
	defer resp.Body.Close()

	// 读取响应
	body := make([]byte, 1024)
	n, _ := resp.Body.Read(body)
	body = body[:n]

	result := map[string]interface{}{
		"success":  true,
		"status":   resp.StatusCode,
		"response": string(body),
		"url":      urlStr,
	}
	jsonBytes, _ := json.Marshal(result)
	return C.CString(string(jsonBytes))
}

// 设置路由
func setupRoutes(r *gin.Engine) {
	// 健康检查
	r.GET("/health", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"status":  "ok",
			"message": "HTTP服务运行正常",
			"time":    time.Now().Unix(),
		})
	})

	// 获取计数器
	r.GET("/counter", func(c *gin.Context) {
		counterMutex.Lock()
		defer counterMutex.Unlock()
		c.JSON(200, gin.H{
			"counter": counter,
			"time":    time.Now().Unix(),
		})
	})

	// 增加计数器
	r.POST("/counter/increment", func(c *gin.Context) {
		counterMutex.Lock()
		defer counterMutex.Unlock()
		counter++
		c.JSON(200, gin.H{
			"counter": counter,
			"message": "计数器已增加",
			"time":    time.Now().Unix(),
		})
	})

	// 重置计数器
	r.POST("/counter/reset", func(c *gin.Context) {
		counterMutex.Lock()
		defer counterMutex.Unlock()
		counter = 0
		c.JSON(200, gin.H{
			"counter": counter,
			"message": "计数器已重置",
			"time":    time.Now().Unix(),
		})
	})

	// 获取系统信息
	r.GET("/system/info", func(c *gin.Context) {
		info := map[string]interface{}{
			"platform":    "Android",
			"language":    "Golang",
			"version":     "1.0.0",
			"timestamp":   time.Now().Unix(),
			"description": "Golang SO库HTTP服务",
			"capabilities": map[string]interface{}{
				"json":      true,
				"math":      true,
				"counter":   true,
				"threading": true,
				"http":      true,
			},
		}
		c.JSON(200, info)
	})
}

func main() {}
