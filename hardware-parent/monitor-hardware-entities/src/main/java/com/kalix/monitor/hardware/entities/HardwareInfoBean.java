package com.kalix.monitor.hardware.entities;

import com.kalix.framework.core.api.persistence.PersistentEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator_ on 2017/12/15.
 */
@Entity
@Table(name = "monitor_hardware_info")
public class HardwareInfoBean extends PersistentEntity {
    @ApiModelProperty(value = "mac", example = "00-01-6C-06-A6-29")
    private String mac;
    @ApiModelProperty(value = "ip", example = "127.0.0.1")
    private String ip;
    @ApiModelProperty(value = "显卡", example = "NVIDIA Quadro K1200")
    private String VideoCard;
    @ApiModelProperty(value = "cpu", example = "CPU Core #1")
    private String cpu;
    @ApiModelProperty(value = "内存", example = "74.7293")
    private String memory;
    @ApiModelProperty(value = "计算机描述", example = "计算机基本信息")
    private String computer;
    @ApiModelProperty(value = "Bois", example = "Bois")
    private String Bios;
    @ApiModelProperty(value = "网络适配器", example = "网络适配器信息")
    private String networkAdapter;
    @ApiModelProperty(value = "重置标记位", example = "默认0，否则1")
    private Long flag=0L;
    @ApiModelProperty(value = "条形码", example = "")
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
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

    public String getVideoCard() {
        return VideoCard;
    }

    public void setVideoCard(String videoCard) {
        VideoCard = videoCard;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getComputer() {
        return computer;
    }

    public void setComputer(String computer) {
        this.computer = computer;
    }

    public String getBios() {
        return Bios;
    }

    public void setBios(String bios) {
        Bios = bios;
    }

    public String getNetworkAdapter() {
        return networkAdapter;
    }

    public void setNetworkAdapter(String networkAdapter) {
        this.networkAdapter = networkAdapter;
    }
}
