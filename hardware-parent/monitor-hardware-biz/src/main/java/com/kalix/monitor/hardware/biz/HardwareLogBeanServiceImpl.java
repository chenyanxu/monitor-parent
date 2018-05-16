package com.kalix.monitor.hardware.biz;

import com.kalix.framework.core.api.config.IConfigService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.monitor.hardware.api.biz.IHardwareLogBeanService;
import com.kalix.monitor.hardware.api.dao.IHardwareLogBeanDao;
import com.kalix.monitor.hardware.entities.HardwareLogBean;
import com.kalix.framework.core.util.ConfigUtil;

import java.text.SimpleDateFormat;
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

    /**
     * 添加任务
     *
     * @param jsonStr
     * @return
     */

    public JsonData getHardwareOffline(Integer page, Integer limit, String jsonStr,Integer sort)
    {
        String searchType="";
        JsonData jsonData = new JsonData();
        Map<String, String> jsonMap = SerializeUtil.json2Map(jsonStr);
        if(jsonMap!=null&&jsonMap.size()!=0)
        {
            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().equals("")) {
                    if (entry.getKey().equals("%barcode%")) {
                        if (entry.getValue() != null && !entry.getValue().equals("")) {
                            searchType = entry.getValue();
                            jsonData = dao.findByNativeSql("SELECT * from monitor_hardware_log where barcode like '%"+searchType+"%' ",page, limit, HardwareLogBean.class, null);
                        }
                    }
                    if (entry.getKey().equals("%offline%")) {
                        if (entry.getValue() != null && !entry.getValue().equals("")) {
                            searchType = entry.getValue();
                            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                            Date d=new Date();
                            String currDate=df.format(new Date(d.getTime() - Integer.parseInt(searchType) * 24 * 60 * 60 * 1000));
                            System.out.println(currDate);
                            jsonData = dao.findByNativeSql("SELECT * from monitor_hardware_log where id in  (SELECT id from (SELECT max(t.creationdate) as newDate,barcode,max(id) as id from monitor_hardware_log t GROUP BY barcode) a where newDate<='"+currDate+"')",page, limit, HardwareLogBean.class, null);
                        }
                    }
                }
            }
        }
        else
        {
            jsonData = dao.findByNativeSql("SELECT * from monitor_hardware_log",page, limit, HardwareLogBean.class, null);
        }

        return  jsonData;
    }
}
