package com.alibaba.nacos.config.server.service;

/**
 * @author Carl
 */
public class ConfigMetaData {
    private String dataId;
    private String group;
    private String tenant;
    private String content;
    private String tag;
    private String appName;
    private String srcUse;
    private String configTags;
    private String desc;
    private String use;
    private String effect;
    private String type;
    private String schema;
    private String srcIp;
    private String betaIps;
    private String requestIpApp;


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSrcUse() {
        return srcUse;
    }

    public void setSrcUse(String srcUse) {
        this.srcUse = srcUse;
    }

    public String getConfigTags() {
        return configTags;
    }

    public void setConfigTags(String configTags) {
        this.configTags = configTags;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getBetaIps() {
        return betaIps;
    }

    public void setBetaIps(String betaIps) {
        this.betaIps = betaIps;
    }

    public String getRequestIpApp() {
        return requestIpApp;
    }

    public void setRequestIpApp(String requestIpApp) {
        this.requestIpApp = requestIpApp;
    }

    @Override
    public String toString() {
        return "ConfigMetaData{" +
            "dataId='" + dataId + '\'' +
            ", group='" + group + '\'' +
            ", tenant='" + tenant + '\'' +
            ", content='" + content + '\'' +
            ", tag='" + tag + '\'' +
            ", appName='" + appName + '\'' +
            ", srcUse='" + srcUse + '\'' +
            ", configTags='" + configTags + '\'' +
            ", desc='" + desc + '\'' +
            ", use='" + use + '\'' +
            ", effect='" + effect + '\'' +
            ", type='" + type + '\'' +
            ", schema='" + schema + '\'' +
            ", srcIp='" + srcIp + '\'' +
            ", betaIps='" + betaIps + '\'' +
            ", requestIpApp='" + requestIpApp + '\'' +
            '}';
    }
}

