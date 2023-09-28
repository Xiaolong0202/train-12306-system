package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 火车车站
 * @TableName train_station
 */
@TableName(value ="train_station")
@Data
public class TrainStation implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 车次编号
     */
    private Long trainId;

    /**
     * 车站序号
     */
    private Integer trainIndex;

    /**
     * 站名
     */
    private String stationName;

    /**
     * 站名拼音
     */
    private String namePinyin;

    /**
     * 进站时间
     */
    private Date inTime;

    /**
     * 出站时间
     */
    private Date outTime;

    /**
     * 停站时间
     */
    private Date stopTime;

    /**
     * 里程（公里）| 从上一站到本站的距离
     */
    private BigDecimal km;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TrainStation other = (TrainStation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTrainId() == null ? other.getTrainId() == null : this.getTrainId().equals(other.getTrainId()))
            && (this.getTrainIndex() == null ? other.getTrainIndex() == null : this.getTrainIndex().equals(other.getTrainIndex()))
            && (this.getStationName() == null ? other.getStationName() == null : this.getStationName().equals(other.getStationName()))
            && (this.getNamePinyin() == null ? other.getNamePinyin() == null : this.getNamePinyin().equals(other.getNamePinyin()))
            && (this.getInTime() == null ? other.getInTime() == null : this.getInTime().equals(other.getInTime()))
            && (this.getOutTime() == null ? other.getOutTime() == null : this.getOutTime().equals(other.getOutTime()))
            && (this.getStopTime() == null ? other.getStopTime() == null : this.getStopTime().equals(other.getStopTime()))
            && (this.getKm() == null ? other.getKm() == null : this.getKm().equals(other.getKm()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTrainId() == null) ? 0 : getTrainId().hashCode());
        result = prime * result + ((getTrainIndex() == null) ? 0 : getTrainIndex().hashCode());
        result = prime * result + ((getStationName() == null) ? 0 : getStationName().hashCode());
        result = prime * result + ((getNamePinyin() == null) ? 0 : getNamePinyin().hashCode());
        result = prime * result + ((getInTime() == null) ? 0 : getInTime().hashCode());
        result = prime * result + ((getOutTime() == null) ? 0 : getOutTime().hashCode());
        result = prime * result + ((getStopTime() == null) ? 0 : getStopTime().hashCode());
        result = prime * result + ((getKm() == null) ? 0 : getKm().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", trainCode=").append(trainId);
        sb.append(", trainIndex=").append(trainIndex);
        sb.append(", stationName=").append(stationName);
        sb.append(", namePinyin=").append(namePinyin);
        sb.append(", inTime=").append(inTime);
        sb.append(", outTime=").append(outTime);
        sb.append(", stopTime=").append(stopTime);
        sb.append(", km=").append(km);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}