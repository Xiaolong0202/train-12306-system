package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 秒杀令牌
 * @TableName train_token
 */
@TableName(value ="train_token")
@Data
public class TrainToken implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 每日车次id
     */
    private Long dailyTrainId;

    /**
     * 发车日期
     */
    private Date startDate;

    /**
     * 列车编号
     */
    private String trainCode;

    /**
     * 车次类型|枚举
     */
    private String type;

    /**
     * 令牌余量
     */
    private Integer tokenCount;

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
        TrainToken other = (TrainToken) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDailyTrainId() == null ? other.getDailyTrainId() == null : this.getDailyTrainId().equals(other.getDailyTrainId()))
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getTrainCode() == null ? other.getTrainCode() == null : this.getTrainCode().equals(other.getTrainCode()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getTokenCount() == null ? other.getTokenCount() == null : this.getTokenCount().equals(other.getTokenCount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDailyTrainId() == null) ? 0 : getDailyTrainId().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getTrainCode() == null) ? 0 : getTrainCode().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getTokenCount() == null) ? 0 : getTokenCount().hashCode());
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
        sb.append(", dailyTrainId=").append(dailyTrainId);
        sb.append(", startDate=").append(startDate);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", type=").append(type);
        sb.append(", tokenCount=").append(tokenCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}