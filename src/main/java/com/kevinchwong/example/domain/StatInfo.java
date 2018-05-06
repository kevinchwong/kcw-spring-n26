package com.kevinchwong.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "statinfo")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatInfo {

    @Id
    private Long timestamp;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Double sum;

    @Column(nullable = false)
    private Double max;

    @Column(nullable = false)
    private Double min;

    @Column(nullable = false)
    private Long count;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatInfo statInfo = (StatInfo) o;

        if (timestamp != null ? !timestamp.equals(statInfo.timestamp) : statInfo.timestamp != null) return false;
        return level != null ? level.equals(statInfo.level) : statInfo.level == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatInfo{" +
                "timestamp=" + timestamp +
                ", level=" + level +
                ", sum=" + sum +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}
