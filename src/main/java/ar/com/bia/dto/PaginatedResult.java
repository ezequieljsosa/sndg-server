package ar.com.bia.dto;

import java.util.ArrayList;
import java.util.List;

public class PaginatedResult<E> {

	private List<E> data;
	private Long recordsFiltered;
	private Long recordsTotal;
	
	
	
	public PaginatedResult() {
		super();
		this.setRecordsTotal(0L);
		this.setRecordsFiltered(0L);
		this.setData(new ArrayList<E>());
	}
	public List<E> getData() {
		return data;
	}
	public void setData(List<E> data) {
		this.data = data;
	}
	public Long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	
	
	
	
	
	
	
	
	
}
