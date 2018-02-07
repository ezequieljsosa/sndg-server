package ar.com.bia.dto;

public class GOTermTuple {

	private String id;
	private String name;
	private Long count;
	
	public GOTermTuple(String id, String name, Long count) {
		super();
		this.id = id;
		this.name = name;
		this.count = count;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "GOTermTuple [name=" + name + ", count=" + count + "]";
	}
	
	
	
}
