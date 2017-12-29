package com.kalix.monitor.system.dict.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.system.BaseDictServiceImpl;
import com.kalix.monitor.system.dict.api.biz.IMonitorDictBeanService;
import com.kalix.monitor.system.dict.api.dao.IMonitorDictBeanDao;
import com.kalix.monitor.system.dict.entities.MonitorDictBean;

public class MonitorDictBeanServiceImpl extends BaseDictServiceImpl<IMonitorDictBeanDao, MonitorDictBean> implements IMonitorDictBeanService {
    @Override
    public JsonStatus saveEntity(MonitorDictBean entity) {
        Integer maxValue = dao.getFieldMaxValue("value","type='"+entity.getType()+"'");

        maxValue=maxValue+1;

        entity.setValue(maxValue);

        return super.saveEntity(entity);
    }
}
