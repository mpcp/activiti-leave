/**
 * �ļ�����PropertyType.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.dto;

import java.util.Date;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�PropertyType
 * ��������
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����4:55:55
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����4:55:55
 * �޸ı�ע��
 * @version 
 * 
 */
public enum PropertyType {
	
	S(String.class), I(Integer.class), L(Long.class), F(Float.class), N(Double.class), D(Date.class), SD(java.sql.Date.class), B(
			Boolean.class);
	private Class<?> clazz;
	
	PropertyType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getValue() {
		return clazz;
	}
}
