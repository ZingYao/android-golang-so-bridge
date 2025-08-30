package com.example.golangbridgetest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    private TextView resultTextView;
    private Gson gson;
    private SimpleDateFormat dateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化组件
        initViews();
        initData();
        setupClickListeners();
    }
    
    private void initViews() {
        resultTextView = findViewById(R.id.resultTextView);
    }
    
    private void initData() {
        gson = new Gson();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }
    
    private void setupClickListeners() {
        // 基本数据类型测试
        findViewById(R.id.btnTestInt).setOnClickListener(v -> testInt());
        findViewById(R.id.btnTestString).setOnClickListener(v -> testString());
        findViewById(R.id.btnTestDouble).setOnClickListener(v -> testDouble());
        findViewById(R.id.btnTestBoolean).setOnClickListener(v -> testBoolean());
        
        // JSON数据测试
        findViewById(R.id.btnTestJson).setOnClickListener(v -> testJSON());
        findViewById(R.id.btnTestArray).setOnClickListener(v -> testArray());
        findViewById(R.id.btnTestUserInfo).setOnClickListener(v -> testUserInfo());
        findViewById(R.id.btnTestSystemInfo).setOnClickListener(v -> testSystemInfo());
        
        // 数学运算测试
        findViewById(R.id.btnTestMath).setOnClickListener(v -> testMath());
        
        // 计数器功能
        findViewById(R.id.btnGetCounter).setOnClickListener(v -> getCounter());
        findViewById(R.id.btnIncrementCounter).setOnClickListener(v -> incrementCounter());
        findViewById(R.id.btnResetCounter).setOnClickListener(v -> resetCounter());
        findViewById(R.id.btnTestCounter).setOnClickListener(v -> testCounter());
        
        // HTTP服务功能
        findViewById(R.id.btnStartHTTPServer).setOnClickListener(v -> startHTTPServer());
        findViewById(R.id.btnTestHTTPServer).setOnClickListener(v -> testHTTPServer());
        findViewById(R.id.btnCallHTTPService).setOnClickListener(v -> callHTTPService());
        
        // 清除结果
        findViewById(R.id.btnClear).setOnClickListener(v -> clearResult());
    }
    
    private void testInt() {
        try {
            int result = GolangBridge.getInt();
            appendResult("测试整数: " + result);
        } catch (Exception e) {
            appendResult("测试整数失败: " + e.getMessage());
        }
    }
    
    private void testString() {
        try {
            String result = GolangBridge.getString();
            appendResult("测试字符串: " + result);
        } catch (Exception e) {
            appendResult("测试字符串失败: " + e.getMessage());
        }
    }
    
    private void testDouble() {
        try {
            double result = GolangBridge.getDouble();
            appendResult("测试浮点数: " + result);
        } catch (Exception e) {
            appendResult("测试浮点数失败: " + e.getMessage());
        }
    }
    
    private void testBoolean() {
        try {
            boolean result = GolangBridge.getBoolean();
            appendResult("测试布尔值: " + result);
        } catch (Exception e) {
            appendResult("测试布尔值失败: " + e.getMessage());
        }
    }
    
    private void testJSON() {
        try {
            String jsonResult = GolangBridge.getJSONString();
            appendResult("测试JSON:");
            appendResult(formatJSON(jsonResult));
        } catch (Exception e) {
            appendResult("测试JSON失败: " + e.getMessage());
        }
    }
    
    private void testArray() {
        try {
            String arrayResult = GolangBridge.getArray();
            appendResult("测试数组:");
            appendResult(formatJSON(arrayResult));
        } catch (Exception e) {
            appendResult("测试数组失败: " + e.getMessage());
        }
    }
    
    private void testUserInfo() {
        try {
            String userInfo = GolangBridge.getUserInfo();
            appendResult("测试用户信息:");
            appendResult(formatJSON(userInfo));
        } catch (Exception e) {
            appendResult("测试用户信息失败: " + e.getMessage());
        }
    }
    
    private void testSystemInfo() {
        try {
            String systemInfo = GolangBridge.getSystemInfo();
            appendResult("测试系统信息:");
            appendResult(formatJSON(systemInfo));
        } catch (Exception e) {
            appendResult("测试系统信息失败: " + e.getMessage());
        }
    }
    
    private void testMath() {
        try {
            int addResult = GolangBridge.addNumbers(10, 20);
            double multiplyResult = GolangBridge.multiplyNumbers(3.14, 2.0);
            
            appendResult("数学运算测试:");
            appendResult("10 + 20 = " + addResult);
            appendResult("3.14 * 2.0 = " + multiplyResult);
        } catch (Exception e) {
            appendResult("数学运算测试失败: " + e.getMessage());
        }
    }
    
    private void getCounter() {
        try {
            int counter = GolangBridge.getCounter();
            appendResult("获取计数器: " + counter);
        } catch (Exception e) {
            appendResult("获取计数器失败: " + e.getMessage());
        }
    }
    
    private void incrementCounter() {
        try {
            int newValue = GolangBridge.incrementCounter();
            appendResult("增加计数: " + newValue);
        } catch (Exception e) {
            appendResult("增加计数失败: " + e.getMessage());
        }
    }
    
    private void resetCounter() {
        try {
            int resetValue = GolangBridge.resetCounter();
            appendResult("重置计数: " + resetValue);
        } catch (Exception e) {
            appendResult("重置计数失败: " + e.getMessage());
        }
    }
    
    private void testCounter() {
        try {
            String counterInfo = GolangBridge.getCounterInfo();
            appendResult("计数器信息:");
            appendResult(formatJSON(counterInfo));
        } catch (Exception e) {
            appendResult("获取计数器信息失败: " + e.getMessage());
        }
    }
    
    private void clearResult() {
        resultTextView.setText("点击下方按钮测试Golang SO库功能");
    }
    
    // HTTP服务相关方法
    private void startHTTPServer() {
        try {
            String result = GolangBridge.startHTTPServer(8080);
            appendResult("启动HTTP服务:");
            appendResult(formatJSON(result));
        } catch (Exception e) {
            appendResult("启动HTTP服务失败: " + e.getMessage());
        }
    }
    
    private void testHTTPServer() {
        try {
            String result = GolangBridge.callHTTPService("http://localhost:8080/health");
            appendResult("测试HTTP服务健康检查:");
            appendResult(formatJSON(result));
        } catch (Exception e) {
            appendResult("测试HTTP服务失败: " + e.getMessage());
        }
    }
    
    private void callHTTPService() {
        try {
            // 测试多个HTTP端点
            String[] urls = {
                "http://localhost:8080/health",
                "http://localhost:8080/counter",
                "http://localhost:8080/system/info"
            };
            
            for (String url : urls) {
                String result = GolangBridge.callHTTPService(url);
                appendResult("调用 " + url + ":");
                appendResult(formatJSON(result));
                appendResult("---");
            }
        } catch (Exception e) {
            appendResult("调用HTTP服务失败: " + e.getMessage());
        }
    }
    
    private void appendResult(String text) {
        String currentText = resultTextView.getText().toString();
        String timestamp = dateFormat.format(new Date());
        String newText = currentText + "\n[" + timestamp + "] " + text;
        resultTextView.setText(newText);
    }
    
    private String formatJSON(String jsonString) {
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            return gson.toJson(jsonElement);
        } catch (Exception e) {
            return jsonString;
        }
    }
}
