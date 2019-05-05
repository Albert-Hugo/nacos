package com.alibaba.nacos.config.server.service;

import com.alibaba.nacos.config.server.exception.NacosException;
import com.alibaba.nacos.config.server.model.ConfigAllInfo;
import com.alibaba.nacos.config.server.model.ConfigInfo;
import com.alibaba.nacos.config.server.service.trace.ConfigTraceService;
import com.alibaba.nacos.config.server.utils.ParamUtils;
import com.alibaba.nacos.config.server.utils.TimeUtils;
import com.alibaba.nacos.config.server.utils.event.EventDispatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.nacos.core.utils.SystemUtils.LOCAL_IP;

/**
 * @author Carl
 */

@Service
public class ConfigManagementServiceImpl implements ConfigManagementService {
    private static final Logger log = LoggerFactory.getLogger(ConfigManagementServiceImpl.class);
    private static final String INNER_FLAG = "Nacos Inner";

    private final transient PersistService persistService;

    @Autowired
    public ConfigManagementServiceImpl(PersistService persistService) {
        this.persistService = persistService;
    }

    @Override
    public ConfigAllInfo configDetail(String dataId, String group, String tenant) throws NacosException {
        // check params
        ParamUtils.checkParam(dataId, group, "datumId", "content");
        return persistService.findConfigAllInfo(dataId, group, tenant);
    }

    @Override
    public boolean publishConfig(ConfigMetaData metaData) throws NacosException {
        ParamUtils.checkParam(metaData.getDataId(), metaData.getGroup(), "datumId", metaData.getContent());
        ParamUtils.checkParam(metaData.getTag());

        Map<String, Object> configAdvanceInfo = new HashMap<String, Object>(10);
        if (metaData.getConfigTags() != null) {
            configAdvanceInfo.put("config_tags", metaData.getConfigTags());
        }
        if (metaData.getDesc() != null) {
            configAdvanceInfo.put("desc", metaData.getDesc());
        }
        if (metaData.getUse() != null) {
            configAdvanceInfo.put("use", metaData.getUse());
        }
        if (metaData.getEffect() != null) {
            configAdvanceInfo.put("effect", metaData.getEffect());
        }
        if (metaData.getType() != null) {
            configAdvanceInfo.put("type", metaData.getType());
        }
        if (metaData.getSchema() != null) {
            configAdvanceInfo.put("schema", metaData.getSchema());
        }
        ParamUtils.checkParam(configAdvanceInfo);

        if (AggrWhitelist.isAggrDataId(metaData.getContent())) {
            log.warn("[aggr-conflict] {} attemp to publish single data, {}, {}",
                metaData.getSrcIp(), metaData.getDataId(), metaData.getGroup());
            throw new NacosException(NacosException.NO_RIGHT, "dataId:" + metaData.getDataId() + " is aggr");
        }

        final Timestamp time = TimeUtils.getCurrentTime();
        String betaIps = metaData.getBetaIps();
        ConfigInfo configInfo = new ConfigInfo(metaData.getDataId(), metaData.getGroup(), metaData.getTenant(), metaData.getAppName(), metaData.getContent());
        if (StringUtils.isBlank(betaIps)) {
            if (StringUtils.isBlank(metaData.getTag())) {
                persistService.insertOrUpdate(metaData.getSrcIp(), metaData.getSrcUse(), configInfo, time, configAdvanceInfo, false);
                EventDispatcher.fireEvent(new ConfigDataChangeEvent(false, metaData.getDataId(), metaData.getGroup(), metaData.getTenant(), time.getTime()));
            } else {
                persistService.insertOrUpdateTag(configInfo, metaData.getTag(), metaData.getSrcIp(), metaData.getSrcUse(), time, false);
                EventDispatcher.fireEvent(new ConfigDataChangeEvent(false, metaData.getDataId(), metaData.getGroup(), metaData.getTenant(), metaData.getTag(), time.getTime()));
            }
        } else { // beta publish
            persistService.insertOrUpdateBeta(configInfo, betaIps, metaData.getSrcIp(), metaData.getSrcUse(), time, false);
            EventDispatcher.fireEvent(new ConfigDataChangeEvent(true, metaData.getDataId(), metaData.getGroup(), metaData.getTenant(), time.getTime()));
        }

        ConfigTraceService.logPersistenceEvent(metaData.getDataId(), metaData.getGroup(), metaData.getTenant(), metaData.getRequestIpApp() == null ? INNER_FLAG : metaData.getRequestIpApp(), time.getTime(),
            LOCAL_IP, ConfigTraceService.PERSISTENCE_EVENT_PUB, metaData.getContent());

        return true;
    }

    @Override
    public boolean deleteConfig(String dataId, String group, String tenant, String tag, String clientIp) throws NacosException {
        ParamUtils.checkParam(dataId, group, "datumId", "rm");
        ParamUtils.checkParam(tag);
        if (StringUtils.isBlank(tag)) {
            persistService.removeConfigInfo(dataId, group, tenant, clientIp, null);
        } else {
            persistService.removeConfigInfoTag(dataId, group, tenant, tag, clientIp, null);
        }
        final Timestamp time = TimeUtils.getCurrentTime();
        ConfigTraceService.logPersistenceEvent(dataId, group, tenant, null, time.getTime(), clientIp,
            ConfigTraceService.PERSISTENCE_EVENT_REMOVE, null);
        EventDispatcher.fireEvent(new ConfigDataChangeEvent(false, dataId, group, tenant, tag, time.getTime()));
        return true;
    }
}
