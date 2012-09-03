/**
 * 文件名：PropertyType.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.dto;

import java.util.Date;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：PropertyType
 * 类描述：
 * 创建人：任欢
 * 创建时间：2012-9-3 下午4:55:55
 * 修改人：任欢
 * 修改时间：2012-9-3 下午4:55:55
 * 修改备注：
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
