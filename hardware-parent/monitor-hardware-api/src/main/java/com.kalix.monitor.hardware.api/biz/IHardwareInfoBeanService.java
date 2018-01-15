package com.kalix.monitor.hardware.api.biz;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.monitor.hardware.entities.HardwareInfoBean;


/**
 * Created by dell on 14-1-17.
 */
public interface IHardwareInfoBeanService extends IBizService<HardwareInfoBean> {
    JsonStatus resetHardwareinfo(Long id);
}
