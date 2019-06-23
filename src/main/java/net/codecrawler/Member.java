package net.codecrawler;

import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "members")
public class Member implements Serializable {
	
	private static final long serialVersionUID = -3009157732242241606L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "username")
	private String username;
	@Column(name = "email")
	private String email;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "pass")
	private String pass;
	@Column(name = "auth")
	private String auth;
	
	protected Member () {
		
	}
	
	public Member (String username, String email, String mobile, String pass, String auth) {
		this.username = username.toLowerCase();;
		this.email = email.toLowerCase();
		this.mobile = mobile;
		this.pass = Base64.getEncoder().encodeToString((pass).getBytes());
		this.auth = auth;	
	}
	
	public Member (String username, String email, String mobile, String pass, String auth, long id) {
		this.username = username.toLowerCase();;
		this.email = email.toLowerCase();
		this.mobile = mobile;
		this.pass = Base64.getEncoder().encodeToString((pass).getBytes());
		this.auth = auth;
		this.id = id;
	}

	public String getUsername() {
		return username.toLowerCase();
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();;
	}

	public String getPass() {
				
		return pass;
	}

	public void setPass(String pass) {
		this.pass = Base64.getEncoder().encodeToString((pass).getBytes());
	}
	
	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}