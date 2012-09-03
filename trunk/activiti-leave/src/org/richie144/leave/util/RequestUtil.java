/**
 * 文件名：RequestUtil.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.util;

import javax.servlet.http.HttpSession;

import org.richie144.leave.entity.account.User;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：RequestUtil
 * 类描述：关于请求中的辅助类
 * 创建人：任欢
 * 创建时间：2012-9-3 下午2:20:42
 * 修改人：任欢
 * 修改时间：2012-9-3 下午2:20:42
 * 修改备注：
 * @version 
 * 
 */
public class RequestUtil {
	public static final String USER = "user";
	
	/**
	 * 
	 * saveUserToSession(将用户存放在session中)
	 * @param   session
	 * @param  user    当前登录的用户
	 * @return null
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static void saveUserToSession(HttpSession session,User user) {
		session.setAttribute(USER, user);
	}
	/**
	 * 
	 * getUserFromSession(从session中取出用户)
	 * @param   session
	 * @return obj    session中的用户
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static User getUserFromSession(HttpSession session) {
		Object obj = session.getAttribute(USER);
		return obj==null ? null:(User)obj;
	}
}
