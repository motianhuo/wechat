package com.juns.wechat.bean;

//订阅号
public class PublicMsgInfo {
	private String id;
	private String time;
	private String content;
	private String msg_ID;
	private String msg_num;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsg_ID() {
		return msg_ID;
	}

	public void setMsg_ID(String msg_ID) {
		this.msg_ID = msg_ID;
	}

}
