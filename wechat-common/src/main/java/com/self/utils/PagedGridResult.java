package com.self.utils;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @Title: PagedGridResult.java
 * @Package com.imooc.utils
 * @Description: 用来返回分页Grid的数据格式
 * Copyright: Copyright (c) 2021
 */
@Getter
public class PagedGridResult<T> {
	
	private long page;			// 当前页数
	private long total;			// 总页数
	private long records;		// 总记录数
	private List<T> rows;		// 每行显示的内容

	private long pageSize;			// 每一页显示数量



	public PagedGridResult() {
		this.rows = Collections.emptyList();
		this.total = 0L;
		this.pageSize = 10L;
		this.page = 1L;
		this.records = 0L;

	}
	public PagedGridResult(long page, long pageSize, long records) {
		this.page = page;
		this.pageSize = pageSize;
		this.records = records;
		if (this.pageSize > 0){
			this.total = (records / this.pageSize) + (records % this.pageSize == 0 ? 0 : 1);
		} else {
			this.total = 0;
		}
	}

	public PagedGridResult(long page, long pageSize, long records, List<T> rows) {
		this(page, pageSize, records);
		this.rows = rows;
	}

	public static <T> PagedGridResult<T> getPageInfo(long page, long pageSize, long records, List<T> rows){
		PagedGridResult<T> pageInfo = new PagedGridResult<>(page, pageSize, records, rows);
		return pageInfo;
	}

	public void setRows(List<T> rows){
		this.rows = rows;
//		if (rows != null && rows.size() > 0){
//			this.setTotal(rows.size());
//		}
	}

	public void setTotal(long records) {
		this.records = records;
		if (this.pageSize > 0){
			this.total = (records / this.pageSize) + (records % this.pageSize == 0 ? 0 : 1);
		} else {
			this.total = 0;
		}

	}


}
