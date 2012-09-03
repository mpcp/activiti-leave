/**
 * �ļ�����RequestUtil.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.util;

import javax.servlet.http.HttpSession;

import org.richie144.leave.entity.account.User;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�RequestUtil
 * �����������������еĸ�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����2:20:42
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����2:20:42
 * �޸ı�ע��
 * @version 
 * 
 */
public class RequestUtil {
	public static final String USER = "user";
	
	/**
	 * 
	 * saveUserToSession(���û������session��)
	 * @param   session
	 * @param  user    ��ǰ��¼���û�
	 * @return null
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public static void saveUserToSession(HttpSession session,User user) {
		session.setAttribute(USER, user);
	}
	/**
	 * 
	 * getUserFromSession(��session��ȡ���û�)
	 * @param   session
	 * @return obj    session�е��û�
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public static User getUserFromSession(HttpSession session) {
		Object obj = session.getAttribute(USER);
		return obj==null ? null:(User)obj;
	}
}
