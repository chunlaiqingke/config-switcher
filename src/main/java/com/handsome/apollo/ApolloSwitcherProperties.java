package com.handsome.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.handsome.ConfigSwitcherLogger;
import com.handsome.switcher.SwitcherDefinition;
import com.handsome.utils.JacksonUtil;

import java.io.IOException;
import java.util.Set;

public class ApolloSwitcherProperties {

    private final static String FILE_NAME = "config-switcher.properties";

    private static Config switcherConfig;

    public ApolloSwitcherProperties(){
        try {
            //这个类已经被注入了，所以这个静态赋值是先于静态方法调用的，这样写是为了调用时方便
            switcherConfig = ConfigService.getConfig(FILE_NAME);
        } catch (Exception e) {
            ConfigSwitcherLogger.errorLog("config-switcher.properties文件在当前应用没有找到!",e);
            throw new RuntimeException("config-switcher.properties文件在当前应用没有找到!", e);
        }
    }

    public static Set<String> getConfigSwitcherKeys(){
        return switcherConfig.getPropertyNames();
    }

    public static boolean containsKey(String key){
        return getConfigSwitcherKeys().contains(key);
    }

    /**
     * 返回指定方法的配置
     * @param key
     */
    public static SwitcherDefinition getConfigSwitcher(String key) throws IOException {
        String property = switcherConfig.getProperty(key, null);
        return JacksonUtil.parse(property, SwitcherDefinition.class);
    }

    /**
     * 参考apollo文档，这里可能需要读取apollo的服务器地址的配置，需要读取宿主项目的配置，需要在宿主项目中添加一个配置类
     * @param key
     * @param value
     */
    public static void appendUpdate(String key, String value) {
        String portalUrl = "http://localhost:8070"; // portal url
        String token = "e16e5cd903fd0c97a116c873b448544b9d086de9"; // 申请的token
        String appid = "";
        String env = "";
        String clusterName = "default";
        String namespaceName = "";
        ApolloOpenApiClient client = ApolloOpenApiClient.newBuilder()
                .withPortalUrl(portalUrl)
                .withToken(token)
                .build();
        OpenItemDTO item = new OpenItemDTO();
        item.setKey(key);
        item.setValue(value);
        OpenItemDTO result = client.createItem(appid, env, clusterName, namespaceName, item);
    }
}
