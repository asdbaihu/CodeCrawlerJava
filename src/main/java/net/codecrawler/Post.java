package net.codecrawler;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "posts")
public class Post implements Serializable {
	
	private static final long serialVersionUID = -3009157732242241606L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "username")
	private String username;
	@Column(name = "blog")
	private String blog;
	@Column(name = "post")
	private String post;
	@Column(name = "reply")
	private Integer reply;
	@Column(name = "stamp")
	private String stamp;
	
	protected Post () {
		
	}

	public Post(String username, String blog, String post, Integer reply, String stamp) {
		
		this.username = username;
		this.blog = blog;
		this.post = post.replace(">", "").replace("<", "").replace(";", "");;
		this.reply = reply;
		this.stamp = stamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post.replace(">", "").replace("<", "").replace(";", "");;
	}
	
	public Integer getReply() {
		return reply;
	}

	public void setReply(Integer reply) {
		this.reply = reply;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	
}