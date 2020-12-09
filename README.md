# config-switcher
依赖apollo的配置中的自动化“开关”工具包

### 项目中配开关遇到的问题
1.开关经常忘记在生产环境配置
2.开关很多是一次性的，很多僵尸配置
3.开关的配置和其他配置在同一个文件，杂乱，影响阅读
4.时间到的时候忘记开开关或者关开关
5.开关起名字令人头疼，而且开关的名字只有开发自己知道，不好沟通和  交接，一段时间以后开发自己都不记得了
6.开关会有代码侵入

此组件就是解决上述问题

# 这是一个完整的可配置的项目，但是还没有打到中央仓库
### 把项目打包（mvn install）到本地仓库，或者使用公司的gitlab的maven deploy按钮打包到公司的nexus
项目中添加依赖(版本号按照自己的打包来)
```
<dependency>
    <groupId>com.handsome</groupId>
    <artifactId>config-switcher</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```
在启动类上添加启动注解@EnableConfigSwitcher,让jar包生效
```
@SpringBootApplication
@EnableConfigSwitcher
public class HelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class);
    }
}
```
在项目的resources下创建portal-openapi.properties文件，文件内容就是你的apollo地址
```
apollo.openapi.portalUrl=http://localhost:8070
apollo.openapi.token=e7f87afa6b74a86d8d2896fe02c889ff587e4aed
apollo.openapi.appid=springbootdemo
apollo.openapi.env=dev
```

最后在apollo的项目中添加名称为config-switcher.properties的namespace即可

到这，项目的配置就结束了~~~~


然后创建一个开关类，最好和业务相关，比如OrderSwitcher
```
@Component
public class ControllerSwitcher {

    @Switcher
    public boolean isOk(){
        return false;
    }
}
```
使用时调用这个isOk的方法即可，会自动使用config中的配置，如果获取config失败，就会返回方法默认的return值

```
@RestController
@RequestMapping("hello")
public class HelloController {

    @Autowired
    ControllerSwitcher controllerSwitcher;

    @RequestMapping("/")
    public String hello(){
        if(controllerSwitcher.isOk()){
            return "ok";
        }
        return "not ok";
    }
}
```
