package org.lance.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("swagger")
public class SwaggerProperties extends SwaggerInfo{

    private boolean enable;

    private Map<String, SwaggerInfo> group;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, SwaggerInfo> getGroup() {
        return group;
    }

    public void setGroup(Map<String, SwaggerInfo> group) {
        this.group = group;
    }
}
