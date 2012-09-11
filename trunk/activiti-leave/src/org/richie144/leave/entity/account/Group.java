/**
 * 文件名：Group.java
 *
 * 版本信息：
 * 日期：2012-8-27
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.entity.account;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：Group
 * 类描述：组与用户是多对多的关系
 * 创建人：任欢
 * 创建时间：2012-8-27 上午11:40:49
 * 修改人：任欢
 * 修改时间：2012-8-27 上午11:40:49
 * 修改备注：
 * @version 
 * 
 */
@Entity
@Table(name="act_id_group")
public class Group implements Serializable {
	
	private static final long serialVersionUID = 10589563254899L;
	
	private String id;
	
	private String name;
	
	private String type;
	
	private List<User> actIdUsers;

	public Group() {
		
	}

	public Group(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "name_")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "type_")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ManyToMany(mappedBy = "actIdGroups" )
	public List<User> getActIdUsers() {
		return actIdUsers;
	}

	public void setActIdUsers(List<User> actIdUsers) {
		this.actIdUsers = actIdUsers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
