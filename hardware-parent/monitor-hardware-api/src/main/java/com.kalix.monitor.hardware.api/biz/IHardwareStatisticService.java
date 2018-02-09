package com.kalix.monitor.hardware.api.biz;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.monitor.hardware.entities.HardwareInfoBean;
import com.kalix.monitor.hardware.entities.HardwareLogBean;


/**
 * Created by dell on 14-1-17.
 */
public interface IHardwareStatisticService  extends IBizService<HardwareLogBean>  {
    JsonData getHardwareStatistic(String jsonStr);
}
