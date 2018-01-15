package com.kalix.monitor.hardware.biz;


import com.google.gson.*;
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

    public JsonData restHardwareMail() {
        JsonData jsondata = new JsonData();
        Dictionary<String, Object> config=ConfigUtil.getAllConfig("config.monitor.config");

        HashMap map = new HashMap();
        Enumeration enumeration= config.keys();
        List list = new ArrayList();
        String key_str="";
        for(Enumeration e=enumeration;e.hasMoreElements();){
            String keyName=e.nextElement().toString();

            if(!"felix.fileinstall.filename".equals(keyName)&&!"service.pid".equals(keyName))
            {
                String key=keyName.split("\\.")[0];
                String value=keyName.split("\\.")[1];
                if(key_str.indexOf(key)>-1)
                {
                    map.put(value,config.get(keyName));

                }
                else
                {
                    Map map_parent = new HashMap();
                    map = new HashMap();
                    map.put(value,config.get(keyName));
                    map_parent.put(key,map);
                    list.add(map_parent);
                }
                key_str+=key+",";
            }

        }
        jsonStatus.setSuccess(true);
        jsondata.setData(list);
        return jsondata;
    }


    public  JsonStatus configureHardwareMail(String content)
    {

        Dictionary<String, Object> config=ConfigUtil.getAllConfig("config.monitor.config");
        JsonParser jsonParser = new JsonParser();
        JsonElement el= jsonParser.parse(content);
        JsonObject jobj=el.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet=jobj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            JsonObject jObject = jobj.getAsJsonObject(entry.getKey());
            String id=jObject.getAsJsonPrimitive("id").getAsString();
            String value=jObject.getAsJsonPrimitive("value").getAsString();
            config.put(id+".value",value);
        }
        ConfigUtil.saveAllConfig(config,"config.monitor.config");
        jsonStatus.setMsg("设置成功！");
        jsonStatus.setSuccess(true);
        return jsonStatus;
    }

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
