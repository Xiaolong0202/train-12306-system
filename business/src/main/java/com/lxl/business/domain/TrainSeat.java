package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 座位
 * @TableName train_seat
 */
@TableName(value ="train_seat")
@Data
public class TrainSeat implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 火车编号
     */
    private String trainCode;

    /**
     * 火车箱号
     */
    private Integer carriageIndex;

    /**
     * 座位类型|枚举
     */
    private String seatType;

    /**
     * 排
     */
    private String seatRow;

    /**
     * 列
     */
    private String seatCol;

    /**
     * 同车厢座序
     */
    private Integer carriageSeatIndex;

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
        TrainSeat other = (TrainSeat) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTrainCode() == null ? other.getTrainCode() == null : this.getTrainCode().equals(other.getTrainCode()))
            && (this.getCarriageIndex() == null ? other.getCarriageIndex() == null : this.getCarriageIndex().equals(other.getCarriageIndex()))
            && (this.getSeatType() == null ? other.getSeatType() == null : this.getSeatType().equals(other.getSeatType()))
            && (this.getSeatRow() == null ? other.getSeatRow() == null : this.getSeatRow().equals(other.getSeatRow()))
            && (this.getSeatCol() == null ? other.getSeatCol() == null : this.getSeatCol().equals(other.getSeatCol()))
            && (this.getCarriageSeatIndex() == null ? other.getCarriageSeatIndex() == null : this.getCarriageSeatIndex().equals(other.getCarriageSeatIndex()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTrainCode() == null) ? 0 : getTrainCode().hashCode());
        result = prime * result + ((getCarriageIndex() == null) ? 0 : getCarriageIndex().hashCode());
        result = prime * result + ((getSeatType() == null) ? 0 : getSeatType().hashCode());
        result = prime * result + ((getSeatRow() == null) ? 0 : getSeatRow().hashCode());
        result = prime * result + ((getSeatCol() == null) ? 0 : getSeatCol().hashCode());
        result = prime * result + ((getCarriageSeatIndex() == null) ? 0 : getCarriageSeatIndex().hashCode());
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
        sb.append(", trainCode=").append(trainCode);
        sb.append(", carriageIndex=").append(carriageIndex);
        sb.append(", seatType=").append(seatType);
        sb.append(", rowCount=").append(seatRow);
        sb.append(", columnCount=").append(seatCol);
        sb.append(", carriageSeatIndex=").append(carriageSeatIndex);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}