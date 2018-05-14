package com.kalix.monitor.hardware.entities;

import com.kalix.framework.core.api.persistence.PersistentEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator_ on 2017/12/15.
 */
@Entity
@Table(name = "monitor_hardware_log")
public class HardwareLogBean extends PersistentEntity {
    @ApiModelProperty(value = "外键", example = "10248")
    private Long infoid;
    @ApiModelProperty(value = "mac", example = "00-01-6C-06-A6-29")
    private String mac;
    @ApiModelProperty(value = "ip", example = "127.0.0.1")
    private String ip;
    @ApiModelProperty(value = "比对结果", example = "相同")
    private String Comparison;

    public Long getInfoid() {
        return infoid;
    }

    public void setInfoid(Long infoid) {
        this.infoid = infoid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getComparison() {
        return Comparison;
    }

    public void setComparison(String comparison) {
        Comparison = comparison;
    }
}
