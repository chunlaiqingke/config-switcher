package com.handsome.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.handsome.ConfigSwitcherLogger;
import com.handsome.switcher.SwitcherDefinition;
import com.handsome.utils.JacksonUtil;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApolloSwitcherProperties {

    private final static String FILE_NAME = "config-switcher.properties";

//    private static Config switcherConfig;

    private static Map<String, OpenItemDTO> switcherConfig;

    private static Properties properties;

    static String portalUrl ; // portal url
    static String token ; //"e16e5cd903fd0c97a116c873b448544b9d086de9"; // 申请的token
    static String appid ;
    static String env ;
    final static String clusterName = "default";
    final static String namespace = "config-switcher";

    static ApolloOpenApiClient client;

    public ApolloSwitcherProperties(){
        try {
            //这个类已经被注入了，所以这个静态赋值是先于静态方法调用的，这样写是为了调用时方便
//            switcherConfig = ConfigService.getConfig(FILE_NAME);
            //获取openapi的配置
            properties = PropertiesLoaderUtils.loadAllProperties("portal-openapi.properties");
            portalUrl = properties.getProperty("apollo.openapi.portalUrl"); // portal url
            token = properties.getProperty("apollo.openapi.token"); //"e16e5cd903fd0c97a116c873b448544b9d086de9"; // 申请的token
            appid = properties.getProperty("apollo.openapi.appid");
            env = properties.getProperty("apollo.openapi.env");
            client = ApolloOpenApiClient.newBuilder()
                    .withPortalUrl(portalUrl)
                    .withToken(token)
                    .build();
            OpenNamespaceDTO namespace = client.getNamespace(appid, env, clusterName, ApolloSwitcherProperties.namespace);
            List<OpenItemDTO> items = namespace.getItems();
            switcherConfig = items.stream().collect(Collectors.toMap(OpenItemDTO::getKey, Function.identity()));
        } catch (IOException e) {
            ConfigSwitcherLogger.errorLog("portal-openapi.properties文件在当前应用没有找到!",e);
            throw new RuntimeException("portal-openapi.properties文件在当前应用没有找到!", e);
        } catch (Exception e) {
            ConfigSwitcherLogger.errorLog("config-switcher.properties文件在当前应用没有找到!",e);
            throw new RuntimeException("config-switcher.properties文件在当前应用没有找到!", e);
        }
    }

    public static Set<String> getConfigSwitcherKeys(){
        return switcherConfig.keySet();
    }

    public static boolean containsKey(String key){
        return getConfigSwitcherKeys().contains(key);
    }

    /**
     * 返回指定方法的配置
     * @param key
     */
    public static SwitcherDefinition getConfigSwitcher(String key) throws IOException {
        String property = switcherConfig.get(key).getValue();
        return JacksonUtil.parse(property, SwitcherDefinition.class);
    }

    /**
     * 参考apollo文档，这里可能需要读取apollo的服务器地址的配置，需要读取宿主项目的配置，需要在宿主项目中添加一个配置类
     * @param key
     * @param value
     */
    public static void appendUpdate(String key, String value) {
        OpenItemDTO item = new OpenItemDTO();
        item.setKey(key);
        item.setValue(value);
        item.setDataChangeCreatedBy("apollo");
        client.createItem(appid, env, clusterName, namespace, item);
    }

    public static void releaseNamespace(){
        NamespaceReleaseDTO releaseDTO = new NamespaceReleaseDTO();
        releaseDTO.setReleasedBy("apollo");
        releaseDTO.setReleaseTitle("apollo");
        client.publishNamespace(appid, env, clusterName, namespace, releaseDTO);
    }
}
