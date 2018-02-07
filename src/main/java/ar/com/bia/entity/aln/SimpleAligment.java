package ar.com.bia.entity.aln;

public class SimpleAligment {

	private AlnLine aln_query;
    private AlnLine aln_hit;
    
    
    private int query_res_start;
    private int query_res_end;
    private int hit_res_start;
    private int hit_res_end;
    
	public AlnLine getAln_query() {
		return aln_query;
	}
	public void setAln_query(AlnLine aln_query) {
		this.aln_query = aln_query;
	}
	public AlnLine getAln_hit() {
		return aln_hit;
	}
	public void setAln_hit(AlnLine aln_hit) {
		this.aln_hit = aln_hit;
	}
	public int getQuery_res_start() {
		return query_res_start;
	}
	public void setQuery_res_start(int query_res_start) {
		this.query_res_start = query_res_start;
	}
	public int getQuery_res_end() {
		return query_res_end;
	}
	public void setQuery_res_end(int query_res_end) {
		this.query_res_end = query_res_end;
	}
	public int getHit_res_start() {
		return hit_res_start;
	}
	public void setHit_res_start(int hit_res_start) {
		this.hit_res_start = hit_res_start;
	}
	public int getHit_res_end() {
		return hit_res_end;
	}
	public void setHit_res_end(int hit_res_end) {
		this.hit_res_end = hit_res_end;
	}
    
	
    
	
}
