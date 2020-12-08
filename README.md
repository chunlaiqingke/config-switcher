# config-switcher
依赖apollo的配置中的自动化“开关”工具包

# 这是一个完整的可配置的项目，但是还没有打到中央仓库
### 把项目打包（mvn install）到本地仓库，或者使用公司的gitlab的maven deploy按钮打包到公司的nexus
项目中添加依赖
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

最后在apollo的项目中天健名称为config-switcher.properties的namespace即可

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
