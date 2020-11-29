package com.handsome.apollo;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.handsome.ConfigSwitcherLogger;
import com.handsome.switcher.SwitcherDefinition;

import java.util.HashMap;
import java.util.Map;

public class ApolloSwitcherProperties {

    private static Map<String, String> configSwitcherMap = new HashMap<>();

    private final static String FILE_NAME = "config-switcher.properties";

    public ApolloSwitcherProperties(){
        try {

        } catch (Exception e) {
            ConfigSwitcherLogger.errorLog("config-switcher.properties文件在当前应用没有找到!",e);
            throw new RuntimeException("config-switcher.properties文件在当前应用没有找到!", e);
        }
    }

    public static Map<String, String> getConfigSwitcherMap(){
        return configSwitcherMap;
    }


    /**
     * 返回指定方法的配置
     * @param methodName
     */
    public static SwitcherDefinition getConfigSwitcher(String methodName) {
        String configStr = configSwitcherMap.get(methodName);
        try {
            return null;
        } catch (Exception e) {
            ConfigSwitcherLogger.errorLog("获取开关失败！，e:", e);
            return null;
        }
    }

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
