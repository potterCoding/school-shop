## 项目杂记
### 数据库设计
1. 启动tomcat，如果启动日志出现 淇℃伅 乱码，修改 tomcat 下的 logging.properties这个文件 为 GBK 就好了。<br>
\#java.util.logging.ConsoleHandler.encoding = UTF-8<br>
java.util.logging.ConsoleHandler.encoding = GBK

2. 配置项目启动的默认访问页面：web项目启动默认是访问index.jsp页面 ，也可以自定义，即在web.xml中添加配置：<br>
  <welcome-file-list><br>
&emsp;&emsp;<welcome-file>index.jsp</welcome-file><br>
&emsp;&emsp;<welcome-file>index.html</welcome-file><br>
  </welcome-file-list><br>
   * 这个配置的意思是：默认访问index.jsp页面，如果index.jsp页面不存在，则去访问index.html页面
  
3. 设计数据库
   * mysql表示日期时间的类型主要有：datetime和timestamp,datetime所表示的时间范围要比timestap表示的范围类型大。datetime的范围：1000-01-01 00:00:00到9999-12-31 23:59:59，而timestap表示的日期范围为：不能早于1970年，不能晚于2037年。但timestap是可以自适应你目前机器所处的时区。
   * mysql主要使用两种引擎：InnoDB和MYISAM
      + MYISAM：表级锁，读性能很高，基于全表扫描
      + InnoDB：行级锁

### logback
配置日志的作用：故障定位和显示程序运行状态
#### logback的主要模块 
* logback-access 和servlet容器集成，提供通过http访问日志的功能
* logback-classic: 是log4j的改良版，同时完整地实现了slf4j的API，是我们可以很方便地更换为其他日志系统，和log4j是同一个作者
* logback-core：为前两个模块提供了基础的服务
#### logback的主要标签
* logger 日志记录器，主要用于存放日志对象，定义日志类型和日记级别等
* appender 指定日志输出的目的地，也就是输出媒介。输出媒介可以是控制台、文件和远程套接字服务器等
* layout 格式花日志输出信息
      
### SSM重点知识
* SpringMVC: DispatcherServlet
作用：主要用来拦截符合要求的外部请求，并把请求分发到不同的控制器中去，根据控制器中处理后的结果，生成相应的响应发送到客户端
* Spring: IOC和AOP
* MyBatis: ORM 
含义：描述对象和数据库之间映射的元数据，将程序中的对象自动持久化到关系数据库中

### 单元测试小知识
* 控制的测试方法的执行顺序：使用注解@FixMethodOrder(MethodSorters.NAME_ASCENDING),表示按照方法名称的顺序执行，例如testA,testB,testC,会按照A,B,C的顺序执行

### mybatis中的小知识
* 写like语句的时候，一般会写成like '% %',在mybatis里面就应该是like '%${name}%',而不是‘%#{name}%’， ${name}是不带单引号的，而#{name}是带单引号的

### 微信测试号的申请与连接以获取微信用户信息
* 编写 SignUtil （微信请求校验工具类）
* 编写 WechatController （设置在URL中的路由）
* 访问微信测试号登录页面，通过打开自己手机的微信，扫一扫登录
  https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login
* 【测试号信息】appID（开发者ID）和appsecret（开发者密码）
* 【接口配置信息】URL和token
* 【JS接口安全域名】填写域名信息，也可以是公网IP
* 【测试号二维码】
* 【体验接口权限表】网页服务-网页账号-修改，设置为域名，即公众号的回调地址
* 接下来获取关注此公众号的用户信息，需要编写五个类
   + WechatLoginController 主要用来获取已关注此微信号的用户信息并做对应处理
   + UserAccessToken 用户AccessToken实体类，用来接收accessToken以及openid等信息
   + WechatUser 微信用户实体类，用来接收昵称、openid等用户信息
   + WechatUtil  主要用来提交https请求给微信获取用户
   + MyX509TrustManager 主要继承X509TrustManager做https证书信任管理器
* 重新打包部署,发布完成后，使用微信开发者工具访问相应链接：
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa18b6661330ce005&redirect_uri=http://132.232.18.43/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect

### 远程调试
在本地调试远程程序，由于root启动的进程是不支持远程调试的，我们需要创建一个普通账号来启动程序，我们需要以该账号
重新装一个tomcat.
* adduser work
* passwd work
* 停掉当前root启动的tomcat
* 切换账号 su work，进入到根目录 cd ~,上传一个tomcat当该目录并进行解压，修改配置文件信息（/bin/catalina.sh）

### 加密连接数据库明文密码
* 利用PropertyPlaceHolderConfigure

### 加入缓存技术
&emsp;&emsp;高性能分布式内存缓存服务器
* 了解什么是redis和Jedis
   + 查看当前redis有没有设置密码：config get requirepass
   + redis密码设置：config set requirepass '新密码',这只是暂时的，重启后就会失效
   + 永久修改，需要修改配置文件
* redis配置
* Jedis基本通用函数配置及使用

#### Linux下设置redis使用service服务启动
1. 设置redis.conf中daemonize为yes,确保守护进程开启,也就是在后台可以运行
   + vi编辑redis安装目录里面的redis.conf文件
   + vi /usr/redis/redis-5.0.5/redis.conf 
2. 复制redis配置文件(启动脚本需要用到配置文件内容,所以要复制)
   + 在/etc下新建redis文件夹 mkdir /etc/redis
   + 把安装redis目录里面的redis.conf文件复制到/etc/redis/6379.conf里面,6379.conf是取的文件名称,启动脚本里面的变量会读取这个名称,所以要是redis的端口号改了,这里也要修改
   + cp /usr/redis/redis-5.0.5/redis.conf /etc/redis/6379.conf
3. 复制redis启动脚本
   + redis启动脚本一般在redis根目录的utils,如果不知道路径,可以先查看路径 
   + find / -name redis_init_script
   + 复制启动脚本到/etc/init.d/redis文件中
   + cp /usr/redis/redis-5.0.5//utils/redis_init_script /etc/init.d/redis
4. 修改启动脚本参数
   + vi /etc/init.d/redis
   + 修改原先配置如下：
      * EXEC=/usr/local/bin/redis-server
      * CLIEXEC=/usr/local/bin/redis-cli
5. 启动redis
   + 打开redis命令:service redis start
   + 关闭redis命令:service redis stop
   + 设为开机启动:chkconfig redis on
   + 设为开机关闭:chkconfig redis off