package org.beijingair.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wp_foggy.wp_posts")
public class WordpressBlog {
	
	public WordpressBlog(int id, String url, String title) {
		this.id = id;
		this.url = url;
		this.title = title;
	}
	
	public int getId() {
		return id;
	}

	public WordpressBlog() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="guid")
	private String url;
	
	@Column(name="post_title")
	private String title;

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}
	
	

}
