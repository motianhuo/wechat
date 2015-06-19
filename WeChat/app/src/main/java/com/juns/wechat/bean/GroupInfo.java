package com.juns.wechat.bean;


//群组
public class GroupInfo {
	private String affiliations;
	private String group_name;
	private String authority;
	private String owner_id;
	private String group_id;
	private String maxusers;
	private String receive;
	private String image_path;
	private String description;
	private String is_common;
	private String is_in;
	private String share_location;
	private String type;
	private String members;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIs_common() {
		return is_common;
	}

	public void setIs_common(String is_common) {
		this.is_common = is_common;
	}

	public String getIs_in() {
		return is_in;
	}

	public void setIs_in(String is_in) {
		this.is_in = is_in;
	}

	public String getShare_location() {
		return share_location;
	}

	public void setShare_location(String share_location) {
		this.share_location = share_location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getMaxusers() {
		return maxusers;
	}

	public void setMaxusers(String maxusers) {
		this.maxusers = maxusers;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

}
