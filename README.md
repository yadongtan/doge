# doge

#### 介绍

[doge] 是一款Java高性能Rpc框架, 提供了动态的服务注册和发现, 支持Redis/Zookeeper, 完善的监控中心实时监控服务与调用信息, 基于Netty的异步高性能网络通信框架让并发能力大幅提升, 同时提供有多种集群容错策略和负载均衡策略可选择。

#### 软件架构

架构:
![输入图片说明](https://gitee.com/t2209931449/doge/raw/master/doge-config/src/main/resources/Doge%E6%9E%B6%E6%9E%84%20(1).png)

运行时大体流程图:
![输入图片说明]([doge-config/src/main/resources/Doge%E6%B5%81%E7%A8%8B%E5%9B%BE.png](https://gitee.com/t2209931449/doge/raw/master/doge-config/src/main/resources/Doge%E6%B5%81%E7%A8%8B%E5%9B%BE.png))

本框架构建于SpringBoot基础之上, 并采用了大量的自动装配技术, 只需最简单的配置即可使用。

程序运行时, 通过Spring会扫描所有的Bean, 并将其中标注了@Reference的字段进行代理注入, 代理由ServiceProxy类负责创建,在本框架中, 接口选择的Jdk动态代理, 而非接口的选择的是Cglib动态代理, 并且Cglib代理调用invoke时也未采用反射的方式(出于效率考虑)。

发起远程调用时通过ServerProxy.invoke()调用到内部的Invoker对象执行一系列Rpc流程, 该对象是在创建代理期间生成的, 拥有Rpc调用所需要的全部信息。

注册中心支持Redis/Zookeeper, 顶级接口RegistryClient, 通过扩展相应的接口即可拓展出更多的可支持的注册中心。

网络传输方面采用Netty + TCP长连接, 调用时采用异步非阻塞的方式, 提高了NettyClient的并发性。同时也可以实现异步非阻塞式的调用。

服务提供者的信息由AbstractDirectory接口的实现类负责处理, 有DynamicDirectory和StaticDiretory(暂未完全实现)。

负载均衡层由LoadBalance的五个实现类负责:

```
    1. RandomLoadBalance 随机加权
    2. RoundRobinLoadBalance 加权轮训
    3. ShortestResponseLoadBalance 最短响应时间
    4. LeastActiveLoadBalance 最少活跃连接
    5. ConsistentHashLoadBalance 一致性Hash
    
```

服务调用时具体的负载均衡选择可在@DogeReference中通过loadBalance指定。

服务容错层由Cluster的六个实现类负责:


```
    1.FailoverCluster
    当出现失败时，会重试其他服务器, 会对请求做负载均衡。

    2.FailfastCluster
    快速失败，当请求失败后，快速返回异常结果，不做任何重试，该容错机制会对请求做负载均衡。

    3.FailsafeCluster
    当出现异常时，直接忽略异常。会对请求做负载均衡。比如不关心数据是否调用成功，并且不想抛异常影响外层调用可使用。

    4.failbackCluster
    请求失败后，会自动记录在失败队列中，并由一个定时线程池定时重试，适用于一些异步或最终一致性的请求。请求会做负载均衡。

    5.ForkingCluster
    同时调用多个相同的服务，只要其中一个返回，则立即返回结果。

    6.broadcastCluster
    广播调用所有可用的服务，任意一个节点报错则报错。
```


网络传输层通过实现了SyncDogeRpcMessageClient接口的子类DogeClientMessageHandler来负责, 传输时通过Netty采用异步发送, 但发送线程在发送消息等待期间获取一把锁并进入阻塞, 收到消息时被唤醒释放锁, 可返回Future对象实现异步调用。

在完成一次调用后调用的记录通过Netty与监控中心(如果配置了的话)传递MonitorFinishedRpcMark对象, 监控中心会将数据实时更新。

监控中心的数据由MonitorMap类负责, 该类记录了所有消费者的调用记录, 记录的属性有下:


```
    private final AtomicLong total = new AtomicLong();  //总次数
    private final AtomicInteger failed = new AtomicInteger();   //失败次数
    private final AtomicLong totalElapsed = new AtomicLong();   //总响应时间
    private final AtomicLong failedElapsed = new AtomicLong(); //失败的总响应时间
    private final AtomicLong maxElapsed = new AtomicLong();    //最大响应时间
    private final AtomicLong failedMaxElapsed = new AtomicLong(); //失败的最大响应时间
    private final AtomicLong succeededMaxElapsed = new AtomicLong();    //最大成功响应时间

```


同时消费者本地也维护了一个自己的调用记录, 由RpcStatus类负责, 记录的属性有下:

```
    private final AtomicInteger active = new AtomicInteger();   //计数器
    private final AtomicLong total = new AtomicLong();  //总次数
    private final AtomicInteger failed = new AtomicInteger();   //失败次数
    private final AtomicLong totalElapsed = new AtomicLong();   //总响应时间
    private final AtomicLong failedElapsed = new AtomicLong(); //失败的总响应时间
    private final AtomicLong maxElapsed = new AtomicLong();    //最大响应时间
    private final AtomicLong failedMaxElapsed = new AtomicLong(); //失败的最大响应时间
    private final AtomicLong succeededMaxElapsed = new AtomicLong();    //最大成功响应时间
```


更多内容, 等你来发现...


#### 安装教程

Mavan导入依赖即可:

```
        <dependency>
            <groupId>com.yadong.doge</groupId>
            <artifactId>doge-rpc</artifactId>
            <version>1.0.0</version>
        </dependency>
```


#### 使用说明

1. 在application.yml中添加配置信息:


zookeeper为注册中心时:
```
doge:
  registry: zookeeper         #可选[redis/zookeeper], 不填默认zookeeper
  zookeeper:
    host: 45.2xx.76.100       #必须, zookeeper注册中心的地址
    port: 2181                
    connection-timeout: 60000 
    session-timeout: 60000    
    retry-sleep-time: 5000    
    max-retries: 5            
  server:
    port: 20002               #必须, 服务暴露的端口

```
若以redis为注册中心, 则应该:

```
doge:
  registry: redis#可选[redis/zookeeper], 不填默认zookeeper
  server:
    port: 20002               #必须, 服务暴露的端口

spring:
  redis:  # must if use redis
    database: 0
    host: 46.2xx.10.15        #必须 redis主机的ip
    port: 6379                #必须 redis主机的端口
    timeout: 10s              
    password: xxxxxxx         #必须 redis连接的密码
    jedis:                    #下面是一些jedis的参数配置
      pool:
        min-idle: 0
        max-idle: 8
        max-active: 8
        max-wait: -1m
```

2. 消费者应该在上面的配置基础上添加:

```
  monitor:
    enable: true    #是否开启监控中心
  remote:
    monitor-host: 127.0.0.1     #监控中心的地址
    monitor-port: 18888    #监控中心的netty服务器端口

```


3. 如果要添加监控中心, 并修改默认参数, 下面是一些可供修改的参数:

```
doge:
  registry: zookeeper
  zookeeper:
    host: 45.2xx.76.100     #必须, 注册中心的地址
    port: 2181
    connection-timeout: 60000
    session-timeout: 60000
    retry-sleep-time: 5000
    max-retries: 5
  monitor:
    monitor-host: 127.0.0.1
    monitor-port: 18888     #必须, 监控中心启动的netty服务器端口
server:
  port: 8888                #必须, 提供给用户查看信息的web界面端口
```

4. 服务提供者在接口的实现类上标注@DogeService:

```

@DogeService
public class UserServiceImpl implements UserService {



    @Override
    public String loginUser(String username, String password) {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + properties.getPort() + "]用户[" + username + "]登录成功";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}

```

5. 消费者在要进行Rpc调用的接口上标注@DogeReference, 并写对应的方法进行调用:

```

@RestController
public class UserController {

    
    @DogeReference(loadBalance = LoadBalanceFactory.ROUND)
    UserService userService;

    @RequestMapping("/login/{username}/{password}")
    public String loginUser(@PathVariable("username")String username,
                            @PathVariable("password")String password){

        return userService.loginUser(username, password);
    }

}

```

6. 完成调用, 如果配置了监控中心, 可通过:

 监控中心ip:port/monitor查看所有调用过的记录信息, 

 监控中心ip:port/hostmap查看所有服务提供者的实时信息, 如权重,版本,运行状态。




#### 参与贡献

All: YadongTan


