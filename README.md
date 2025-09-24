AI 角色扮演应用运行指南
1. 概述
本项目是一个前后端分离的全栈应用。后端使用 Java Spring Boot 构建，负责处理核心的 AI 对话逻辑并作为 API 服务器；前端使用原生的 HTML, CSS 和 JavaScript 构建，负责用户交互和界面展示。
本指南将指导您如何配置环境、启动后端服务，并通过浏览器访问前端应用。
2. 先决条件
在开始之前，请确保您的电脑上已经安装了以下软件：
●Java Development Kit (JDK): 版本 17 或更高。
●Maven: Java 项目管理工具。IntelliJ IDEA 通常会内置，无需单独安装。
●Git: 版本控制工具，用于从 GitHub 克隆项目。
●IntelliJ IDEA: 推荐使用社区版或旗舰版作为 Java 开发环境。
●现代 Web 浏览器: 推荐使用 Google Chrome，因为它对 Web Speech API 的支持最好。
●(可选) 网络代理工具: 如果您所在的地区无法直接访问 Google 服务，则需要一个网络代理工具 (例如 Clash Verge)。
3. 从 GitHub 克隆项目
打开您的终端 (Terminal 或 CMD)，执行以下命令将项目代码克隆到本地：
# 请将 URL 替换为您自己的仓库地址
git clone [https://github.com/ljh1723871365-cloud/ai-roleplay-fullstack.git](https://github.com/ljh1723871365-cloud/ai-roleplay-fullstack.git)

4. 后端设置与运行
1.在 IntelliJ IDEA 中打开项目:
○启动 IntelliJ IDEA，选择 Open。
○找到并选中您刚刚克隆下来的 ai-roleplay-fullstack 文件夹。
○IDEA 会自动识别为 Maven 项目并开始下载依赖，请稍等片刻。
2.配置 API 密钥:
○在项目目录中，找到并打开 src/main/resources/application.properties 文件。
○将您自己的 Google Gemini API 密钥粘贴到 google.api.key= 的后面。
google.api.key=AIzaSyYOUR_REAL_API_KEY_HERE...

3.配置 JVM 代理 (如果需要):
○仅当您需要网络代理时执行此步骤。
○在 IDEA 顶部菜单栏，选择 Run -> Edit Configurations...。
○选择 AiRoleplayApplication。
○点击 Modify options -> 勾选 Add VM options。
○在 VM options 输入框中，根据您的代理地址填入配置，例如：
-Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=7897 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7897

○点击 Apply 和 OK。
4.启动后端服务:
○在项目浏览器中，找到 src/main/java/com/example/airoleplay/AiRoleplayApplication.java 文件。
○点击 main 方法左侧的绿色三角“播放”按钮，选择 Run 'AiRoleplayApplication.main()'。
5.验证成功:
○观察底部 Run 窗口的日志。当您看到类似以下信息且没有红色报错时，代表后端已成功启动：
Tomcat started on port(s): 8080 (http) with context path ''
Started AiRoleplayApplication in X.XXX seconds

5. 访问和使用应用
1.保持后端运行: 请不要关闭 IntelliJ IDEA 中正在运行的程序。
2.打开浏览器: 打开 Google Chrome 浏览器。
3.访问应用: 在地址栏输入 http://localhost:8080 并回车。
4.开始使用: 您现在应该可以看到应用界面了。点击角色卡片，按住麦克风按钮即可开始语音对话。
6. 故障排查
●端口 8080 被占用: 如果启动时报错 Port 8080 was already in use，请在 application.properties 文件中修改 server.port=8080 为其他端口，例如 server.port=8081，然后访问 http://localhost:8081。
●500 内部服务器错误:
○检查 application.properties 中的 API 密钥是否正确、有效。
○检查您的网络代理是否已开启，并且 JVM 代理配置是否正确。
●前端无法录音: 检查浏览器是否已授权麦克风权限。
