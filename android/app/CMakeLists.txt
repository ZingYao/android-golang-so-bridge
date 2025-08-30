cmake_minimum_required(VERSION 3.22.1)

project("golangbridgetest")

# 设置C标准
set(CMAKE_C_STANDARD 11)

# 添加JNI库
find_library(log-lib log)

# 创建共享库
add_library(golang-bridge-jni SHARED
    src/main/cpp/golang_bridge_jni.c)

# 链接库
target_link_libraries(golang-bridge-jni
    ${log-lib})
