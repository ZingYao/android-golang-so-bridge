package main

import (
	"encoding/json"
	"fmt"
	"sync"
	"time"
)

// 全局计数器变量
var (
	counter      int = 0
	counterMutex sync.Mutex
)

// 测试函数，用于验证功能而不需要编译为SO
func main() {
	fmt.Println("=== Golang 功能测试 ===")

	// 测试基本数据类型
	fmt.Printf("整数: %d\n", getInt())
	fmt.Printf("字符串: %s\n", getString())
	fmt.Printf("浮点数: %f\n", getDouble())
	fmt.Printf("布尔值: %t\n", getBoolean())

	// 测试JSON
	jsonStr := getJSONString()
	fmt.Printf("JSON字符串: %s\n", jsonStr)

	// 测试数组
	arrayStr := getArray()
	fmt.Printf("数组: %s\n", arrayStr)

	// 测试用户信息
	userInfo := getUserInfo()
	fmt.Printf("用户信息: %s\n", userInfo)

	// 测试系统信息
	sysInfo := getSystemInfo()
	fmt.Printf("系统信息: %s\n", sysInfo)

	// 测试数学运算
	fmt.Printf("10 + 20 = %d\n", addNumbers(10, 20))
	fmt.Printf("3.14 * 2.0 = %f\n", multiplyNumbers(3.14, 2.0))

	// 测试计数器功能
	fmt.Println("\n=== 计数器功能测试 ===")
	fmt.Printf("初始计数: %d\n", getCounter())
	fmt.Printf("增加计数: %d\n", incrementCounter())
	fmt.Printf("增加计数: %d\n", incrementCounter())
	fmt.Printf("增加计数: %d\n", incrementCounter())
	fmt.Printf("当前计数: %d\n", getCounter())
	fmt.Printf("重置计数: %d\n", resetCounter())
	fmt.Printf("重置后计数: %d\n", getCounter())

	counterInfo := getCounterInfo()
	fmt.Printf("计数器信息: %s\n", counterInfo)
}

// 复制main.go中的函数，但不使用C导出
func getInt() int {
	return 42
}

func getString() string {
	return "Hello from Golang!"
}

func getDouble() float64 {
	return 3.14159
}

func getBoolean() bool {
	return true
}

func getJSONString() string {
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
		return "{\"error\": \"JSON marshal failed\"}"
	}

	return string(jsonBytes)
}

func getArray() string {
	numbers := []int{10, 20, 30, 40, 50}
	jsonBytes, err := json.Marshal(numbers)
	if err != nil {
		return "[]"
	}
	return string(jsonBytes)
}

func getUserInfo() string {
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
		return "{\"error\": \"User info marshal failed\"}"
	}

	return string(jsonBytes)
}

func getSystemInfo() string {
	info := map[string]interface{}{
		"platform":  "Android",
		"language":  "Golang",
		"compiler":  "gccgo",
		"buildTime": time.Now().Format("2006-01-02 15:04:05"),
		"features": map[string]bool{
			"jni_support":  true,
			"json_support": true,
			"cgo_support":  true,
		},
	}

	jsonBytes, err := json.Marshal(info)
	if err != nil {
		return "{\"error\": \"System info marshal failed\"}"
	}

	return string(jsonBytes)
}

func addNumbers(a, b int) int {
	return a + b
}

func multiplyNumbers(a, b float64) float64 {
	return a * b
}

// 计数器功能
func getCounter() int {
	counterMutex.Lock()
	defer counterMutex.Unlock()
	return counter
}

func incrementCounter() int {
	counterMutex.Lock()
	defer counterMutex.Unlock()
	counter++
	return counter
}

func resetCounter() int {
	counterMutex.Lock()
	defer counterMutex.Unlock()
	counter = 0
	return counter
}

func getCounterInfo() string {
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
		return "{\"error\": \"Counter info marshal failed\"}"
	}

	return string(jsonBytes)
}
