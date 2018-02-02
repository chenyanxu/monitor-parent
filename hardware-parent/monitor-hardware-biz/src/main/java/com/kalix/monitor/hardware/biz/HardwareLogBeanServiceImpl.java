package com.kalix.monitor.hardware.biz;

import com.kalix.framework.core.api.config.IConfigService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.monitor.hardware.api.biz.IHardwareLogBeanService;
import com.kalix.monitor.hardware.api.dao.IHardwareLogBeanDao;
import com.kalix.monitor.hardware.entities.HardwareLogBean;
import com.kalix.framework.core.util.ConfigUtil;
import java.util.*;


/**
 * Created by fj on 2017-8-17.
 */
public class HardwareLogBeanServiceImpl extends ShiroGenericBizServiceImpl<IHardwareLogBeanDao, HardwareLogBean> implements IHardwareLogBeanService {

    private JsonStatus jsonStatus = new JsonStatus();

    /**
     * 获取邮箱地址
     *
     * @return
     */
    public  Map getHardwareMail()
    {
        Map map = new HashMap();
        Dictionary<String, Object> config=ConfigUtil.getAllConfig("config.monitor.config");
        Enumeration enumeration= config.keys();
        for(Enumeration e=enumeration;e.hasMoreElements();) {
            String keyName = e.nextElement().toString();

            if ("mail.value".equals(keyName)) {
               String mail= (String) config.get(keyName);
               if(!StringUtils.isEmpty(mail))
               {
                   if(mail.indexOf("|")>-1)
                   {
                       String [] mailStr=mail.split("\\|");
                       for(int i =0;i<mailStr.length;i++)
                       {
                           map.put(mailStr[i],mailStr[i]);
                       }
                   }else
                   {
                       map.put(mail,mail);
                   }
               }
            }
        }
        return map;
    }
}
