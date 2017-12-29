package com.kalix.monitor.system.dict.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.monitor.system.dict.api.dao.IMonitorDictBeanDao;
import com.kalix.monitor.system.dict.entities.MonitorDictBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MonitorDictBeanDaoImpl extends GenericDao<MonitorDictBean, Long> implements IMonitorDictBeanDao {
    @Override
    @PersistenceContext(unitName = "monitor-system-dict-unit")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
