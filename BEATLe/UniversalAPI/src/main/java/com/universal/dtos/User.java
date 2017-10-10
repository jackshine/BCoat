package com.universal.dtos;

public class User {
	//http://jsonplaceholder.typicode.com/posts/1
	/*{
		  "userId": 1,
		  "id": 1,
		  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
		  "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
		}*/
	
	private int userId;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	private int id;
	private String title;
	private String body;
	
	
}
