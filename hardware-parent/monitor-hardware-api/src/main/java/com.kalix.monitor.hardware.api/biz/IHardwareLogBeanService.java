package com.kalix.monitor.hardware.api.biz;


import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.monitor.hardware.entities.HardwareLogBean;

import java.util.Map;


/**
 * Created by dell on 14-1-17.
 */
public interface IHardwareLogBeanService extends IBizService<HardwareLogBean> {
    Map getHardwareMail();
    JsonData getHardwareOffline(Integer page, Integer limit, String jsonStr,Integer sort);
}
