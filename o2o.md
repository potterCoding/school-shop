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
