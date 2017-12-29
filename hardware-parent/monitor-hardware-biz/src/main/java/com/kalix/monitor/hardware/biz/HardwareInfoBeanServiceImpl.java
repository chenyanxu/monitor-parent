package com.kalix.monitor.hardware.biz;


import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.Assert;
import com.kalix.monitor.hardware.api.biz.IHardwareInfoBeanService;
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
public class HardwareInfoBeanServiceImpl extends ShiroGenericBizServiceImpl<IHardwareInfoBeanDao, HardwareInfoBean> implements IHardwareInfoBeanService {

    private JsonStatus jsonStatus = new JsonStatus();
    private IHardwareLogBeanDao hardwareLogBeanDao;

    public void setHardwareLogBeanDao(IHardwareLogBeanDao hardwareLogBeanDao) {
        this.hardwareLogBeanDao = hardwareLogBeanDao;
    }

    @Override
    public JsonStatus restHardwareinfo(Long id) {

        HardwareInfoBean Entity = dao.get(id);
        Entity.setFlag(1L);
        return  super.updateEntity(Entity);
    }


    public void beforeSaveEntity(HardwareLogBean entity, JsonStatus status) {
        Assert.notNull(entity, "实体不能为空.");

        String userName = shiroService.getCurrentUserLoginName();
        if (userName != null) {
            entity.setCreateBy(userName);
            entity.setUpdateBy(userName);
        }
    }

    /**
     * 添加任务
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public JsonStatus saveEntity(HardwareInfoBean entity) {

        if(entity!=null)
        {
            String mac=entity.getMac();
            String ishave=isHave(mac);

            if(!"".equals(ishave))
            {
               String[] strValue= ishave.split(",");
                if("1".equals(strValue[0]))
                {
                    // Long id=entity.getId();
                    dao.remove(Long.valueOf(strValue[1]));
                    super.beforeSaveEntity(entity,jsonStatus);
                    entity.setCreationDate(new Date());
                    dao.save(entity);
                    jsonStatus.setSuccess(true);
                    jsonStatus.setMsg("重置硬件信息成功！");
                }
                else
                {
                    HardwareInfoBean infoBean= dao.get(Long.valueOf(strValue[1]));

                    //比对信息

                    StringBuffer comparResult= new StringBuffer();
                    if(!entity.getCpu().equals(infoBean.getCpu()))
                    {
                        comparResult.append("CPU不同,");
                    }
                    if(!entity.getBios().equals(infoBean.getBios()))
                    {
                        comparResult.append("BIOS信息不同,");
                    }
                    if(!entity.getMemory().equals(infoBean.getMemory()))
                    {
                        comparResult.append("内存信息不同,");
                    }
                    if(!entity.getNetworkAdapter().equals(infoBean.getNetworkAdapter()))
                    {
                        comparResult.append("网络适配器信息不同,");
                    }
                    if(!entity.getVideoCard().equals(infoBean.getVideoCard()))
                    {
                        comparResult.append("显卡信息不同,");
                    }
                    if(!entity.getComputer().equals(infoBean.getComputer()))
                    {
                        comparResult.append("计算机描述信息不同");
                    }

                    if(!"".equals(comparResult.toString())){
                        HardwareLogBean logBean= new HardwareLogBean();
                        logBean.setIp(infoBean.getIp());
                        logBean.setMac(infoBean.getMac());
                        logBean.setInfoid(infoBean.getId());
                        logBean.setComparison(comparResult.toString());

                        this.beforeSaveEntity(logBean,jsonStatus);
                        logBean.setCreationDate(new Date());
                        hardwareLogBeanDao.save(logBean);
                    }else
                    {
                        HardwareLogBean logBean= new HardwareLogBean();
                        logBean.setIp(infoBean.getIp());
                        logBean.setMac(infoBean.getMac());
                        logBean.setInfoid(infoBean.getId());
                        logBean.setComparison("信息相同");

                        this.beforeSaveEntity(logBean,jsonStatus);
                        logBean.setCreationDate(new Date());
                        hardwareLogBeanDao.save(logBean);
                    }
                }
            }else {
                super.beforeSaveEntity(entity,jsonStatus);
                entity.setCreationDate(new Date());
                dao.save(entity);
                jsonStatus.setSuccess(true);
                jsonStatus.setMsg("新增硬件信息成功！");
            }


        }

        return jsonStatus;
    }


    public String isHave(String mac) {
        String returnVlue="";
       // HardwareInfoBean bean = (HardwareInfoBean) entity;
        List<HardwareInfoBean> hardwareInfoBeans = dao.find("select ob from HardwareInfoBean ob where ob.mac = ?1", mac);
        if (hardwareInfoBeans != null && hardwareInfoBeans.size() > 0) {
            HardwareInfoBean infoBean=  hardwareInfoBeans.get(0);
            returnVlue=infoBean.getFlag().toString()+","+infoBean.getId();
            return returnVlue;
        }
        return returnVlue;
    }



}
