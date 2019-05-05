package com.alibaba.nacos.config.server.service;

import com.alibaba.nacos.config.server.exception.NacosException;
import com.alibaba.nacos.config.server.model.ConfigAllInfo;

/**
 * @author Carl
 */
public interface ConfigManagementService {

    /**
     * @param dataId
     * @param group
     * @param tenant
     * @return
     */
    ConfigAllInfo configDetail(String dataId, String group, String tenant) throws NacosException;

    /**
     * @param configMetaData
     * @return
     * @throws NacosException
     */
    boolean publishConfig(ConfigMetaData configMetaData) throws NacosException;

    /**
     * @param dataId
     * @param group
     * @param tenant
     * @param tag
     * @return
     */
    boolean deleteConfig(String dataId, String group, String tenant, String tag, String clientIp) throws NacosException;


}
