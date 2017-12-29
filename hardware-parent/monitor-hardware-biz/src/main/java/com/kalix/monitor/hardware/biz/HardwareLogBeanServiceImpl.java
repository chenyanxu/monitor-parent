package com.kalix.monitor.hardware.biz;


import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.monitor.hardware.api.biz.IHardwareInfoBeanService;
import com.kalix.monitor.hardware.api.biz.IHardwareLogBeanService;
import com.kalix.monitor.hardware.api.dao.IHardwareInfoBeanDao;
import com.kalix.monitor.hardware.api.dao.IHardwareLogBeanDao;
import com.kalix.monitor.hardware.entities.HardwareInfoBean;
import com.kalix.monitor.hardware.entities.HardwareLogBean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by fj on 2017-8-17.
 */
public class HardwareLogBeanServiceImpl extends ShiroGenericBizServiceImpl<IHardwareLogBeanDao, HardwareLogBean> implements IHardwareLogBeanService {


}
