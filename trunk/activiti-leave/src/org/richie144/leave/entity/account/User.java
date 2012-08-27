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

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�User
 * ��������
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����11:38:14
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����11:38:14
 * �޸ı�ע��
 * @version 
 * 
 */
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Group> getActIdGroups() {
		return actIdGroups;
	}

	public void setActIdGroups(List<Group> actIdGroups) {
		this.actIdGroups = actIdGroups;
	}
	
	
	
}
