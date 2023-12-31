package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车厢
 * @TableName train_carriage
 */
@TableName(value ="train_carriage")
@Data
public class TrainCarriage implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 火车编号
     */

    private Long trainId;

    /**
     * 火车箱号
     */
    private Integer trainIndex;

    /**
     * 座位类型|枚举
     */
    private String seatType;

    /**
     * 座位数
     */
    private Integer seatCount;

    /**
     * 排数
     */
    private Integer rowCount;

    /**
     * 列数
     */
    private Integer columnCount;

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
        TrainCarriage other = (TrainCarriage) that;
        return (this.getTrainId() == null ? other.getTrainId() == null : this.getTrainId().equals(other.getTrainId()))
            && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTrainIndex() == null ? other.getTrainIndex() == null : this.getTrainIndex().equals(other.getTrainIndex()))
            && (this.getSeatType() == null ? other.getSeatType() == null : this.getSeatType().equals(other.getSeatType()))
            && (this.getSeatCount() == null ? other.getSeatCount() == null : this.getSeatCount().equals(other.getSeatCount()))
            && (this.getRowCount() == null ? other.getRowCount() == null : this.getRowCount().equals(other.getRowCount()))
            && (this.getColumnCount() == null ? other.getColumnCount() == null : this.getColumnCount().equals(other.getColumnCount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTrainId() == null) ? 0 : getTrainId().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTrainIndex() == null) ? 0 : getTrainIndex().hashCode());
        result = prime * result + ((getSeatType() == null) ? 0 : getSeatType().hashCode());
        result = prime * result + ((getSeatCount() == null) ? 0 : getSeatCount().hashCode());
        result = prime * result + ((getRowCount() == null) ? 0 : getRowCount().hashCode());
        result = prime * result + ((getColumnCount() == null) ? 0 : getColumnCount().hashCode());
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
        sb.append(", trainCode=").append(trainId);
        sb.append(", id=").append(id);
        sb.append(", trainIndex=").append(trainIndex);
        sb.append(", seatType=").append(seatType);
        sb.append(", seatCount=").append(seatCount);
        sb.append(", rowCount=").append(rowCount);
        sb.append(", columnCount=").append(columnCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}