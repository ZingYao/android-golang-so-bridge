#ifndef BRIDGE_H
#define BRIDGE_H

#ifdef __cplusplus
extern "C" {
#endif

// 基本数据类型
int GetInt();
char* GetString();
double GetDouble();
int GetBoolean();

// JSON数据
char* GetJSONString();
char* GetArray();
char* GetUserInfo();
char* GetSystemInfo();

// 数学运算
int AddNumbers(int a, int b);
double MultiplyNumbers(double a, double b);

// 计数器功能
int GetCounter();
int IncrementCounter();
int ResetCounter();
char* GetCounterInfo();

// HTTP服务功能
char* StartHTTPServer(int port);
char* CallHTTPService(char* url);

#ifdef __cplusplus
}
#endif

#endif // BRIDGE_H
