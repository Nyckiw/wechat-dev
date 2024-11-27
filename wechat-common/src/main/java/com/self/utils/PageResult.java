package com.self.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页返回的实体
 * @param <T>
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 8055166668023696184L;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private Integer total = 0;
    private Integer totalPages = 0;
    private Integer start = 0;
    private Integer end = 0;

    private List<T> result = Collections.emptyList();


    public void setRecords(List<T> result){
        this.result = result;
        if (result != null && result.size() > 0){
            this.setTotal(result.size());
        }
    }

    public void setTotal(int total) {
        this.total = total;
        if (this.pageSize > 0){
            this.totalPages = (total / this.pageSize) + (total % this.pageSize == 0 ? 0 : 1);
        } else {
            this.totalPages = 0;
        }
        this.start = (this.pageSize > 0 ? (this.pageNo - 1) * this.pageSize : 1) + 1;
        this.end = this.start - 1 + this.pageSize * (this.pageNo > 0 ? 1 : 0);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }



    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
