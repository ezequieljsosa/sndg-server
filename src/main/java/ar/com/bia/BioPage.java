/*
 * Fluxit S.A
 * La Plata - Buenos Aires - Argentina
 * http://www.fluxit.com.ar
 * Author: chily
 * Date:  Aug 3, 2012 - 5:21:35 PM
 */
package ar.com.bia;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Modela una página de busqueda para realizar búsquedas paginadas sobre grandes
 * volúmenes de datosS
 * 
 * @author chily
 * 
 */
public class BioPage implements Pageable{

	private int page = 0; // numero de pagina a buscar
	private int pageSize = 12; // Tamaño de página o cantidad de resultados
    private Sort sort;
    private boolean hasPrevious;
    private Pageable next;
    private Pageable previousOrFirst;
    
	
    
	
	public BioPage() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BioPage(int page, int pageSize, Sort sort) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.sort = sort;
	}


	public int getOffset() {
		
		return page * pageSize;
	}

	
	public int getPageNumber() {
	
		return page + 1;
	}

	
	public Sort getSort() {
	
		return sort;
	}

	
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "Page: " + this.getPage();
	}


	@Override
	public Pageable first() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return hasPrevious;
	}


	@Override
	public Pageable next() {
		// TODO Auto-generated method stub
		return next;
	}


	@Override
	public Pageable previousOrFirst() {
		// TODO Auto-generated method stub
		return previousOrFirst;
	}
}
