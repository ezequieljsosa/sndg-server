package ar.com.bia.dto;

import java.util.List;

public class DynTablePaginatedResult<E> {

	private List<E> records;
	private Integer queryRecordCount;
	private Long totalRecordCount;
	
	
	
	public List<E> getRecords() {
		return records;
	}
	public void setRecords(List<E> records) {
		this.records = records;
	}
	public Integer getQueryRecordCount() {
		return queryRecordCount;
	}
	public void setQueryRecordCount(Integer queryRecordCount) {
		this.queryRecordCount = queryRecordCount;
	}
	public Long getTotalRecordCount() {
		return totalRecordCount;
	}
	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	
	
	
}
