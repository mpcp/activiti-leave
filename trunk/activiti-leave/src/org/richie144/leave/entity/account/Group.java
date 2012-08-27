/**
 * �ļ�����Group.java
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
 * �����ƣ�Group
 * ��������
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����11:40:49
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����11:40:49
 * �޸ı�ע��
 * @version 
 * 
 */
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
