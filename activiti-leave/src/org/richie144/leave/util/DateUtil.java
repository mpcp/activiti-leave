/**
 * 文件名：DateUtil.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：DateUtil
 * 类描述：日期装换的工具类
 * 创建人：任欢
 * 创建时间：2012-9-3 下午4:08:18
 * 修改人：任欢
 * 修改时间：2012-9-3 下午4:08:18
 * 修改备注：
 * @version 
 * 
 */
public class DateUtil implements Converter {
	private static final Logger log = Logger.getLogger(DateUtil.class);
	private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String MONTH_PATTERN = "yyyy-MM";
	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class type, Object value) {
		Object result = null;
		if(type == Date.class) {
			try {
				result = convertToDate(value);
			} catch (ParseException e) {
				
				e.printStackTrace();
			} 
		} else if (type == String.class) {
			result = convertToString(value);
		}
		return result;
	}
	
	private Date convertToDate(Object value) throws ParseException {
		Date result = null;
		if(value instanceof String) {
			result = DateUtils.parseDate((String) value,DATETIME_PATTERN,DATETIME_PATTERN_NO_SECOND,DATE_PATTERN,MONTH_PATTERN);
			if (result == null && StringUtils.isNotEmpty((String) value)) {
				try {
					result = new Date(new Long((String) value).longValue());
				} catch (NumberFormatException e) {
					log.error("Converting from milliseconds to Date fails!");
					e.printStackTrace();
				}
			}
		} else if(value instanceof Object[]) {
			Object[] array = (Object[]) value;
			if ((array != null) && (array.length >= 1)) {
				value = array[0];
				result = convertToDate(value);
			}
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			result = (Date) value;
		}
		return result;
	}
	
	private String convertToString(Object value) {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
		if(value instanceof Date) {
			result = sdf.format(value);
		}
		return result;
		
	}
}
