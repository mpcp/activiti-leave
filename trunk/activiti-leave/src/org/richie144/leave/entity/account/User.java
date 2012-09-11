/**
 * �ļ�����User.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-8-27
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�User
 * ���������û�ʵ���࣬��group�ɶ�Զ�
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����11:38:14
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����11:38:14
 * �޸ı�ע��
 * @version 
 * 
 */
@Entity
@Table(name = "act_id_user")
public class User implements Serializable {
	
	private static final long serialVersionUID = 25886688955L;
	
	private String id;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
	
	private List<Group> actIdGroups;

	public User() {
		
	}

	public User(String id, String email, String firstName, String lastName,
			String password) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	@Id
	@Column(name = "id_")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "email_")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "first_")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "last_")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "pwd_")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ManyToMany
	@JoinTable(name = "act_id_membership",joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "group_id")})
	public List<Group> getActIdGroups() {
		return actIdGroups;
	}

	public void setActIdGroups(List<Group> actIdGroups) {
		this.actIdGroups = actIdGroups;
	}
	
	
	
}
