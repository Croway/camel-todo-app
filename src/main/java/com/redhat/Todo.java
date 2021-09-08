package com.redhat;

import java.util.UUID;

public class Todo {

	private String id;
	private String _id;
	private String title;
	private boolean completed;
	private int order;
	private String url;

	public Todo() {
	}

	public Todo(String id, String _id, String title, boolean completed, int order, String url) {
		this.id = id;
		this._id = _id;
		this.title = title;
		this.completed = completed;
		this.order = order;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
