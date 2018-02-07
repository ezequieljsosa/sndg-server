package ar.com.bia.dto;

public class UserDTO {

	private String username;
	private String password;
	private String password2;
	private String email;
	private String institutions;
	private String returnTo;
	
	
	
	public String getReturnTo() {
		return returnTo;
	}
	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInstitutions() {
		return institutions;
	}
	public void setInstitutions(String institutions) {
		this.institutions = institutions;
	}
	
	
	
	
}
