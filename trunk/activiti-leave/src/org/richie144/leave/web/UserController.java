/**
 * �ļ�����UserController.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.richie144.leave.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�UserController
 * ���������û���������
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����5:13:17
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����5:13:17
 * �޸ı�ע��
 * @version 
 * 
 */
public class UserController {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	// Activiti Identify Service
	private IdentityService identityService;
	
	
	
	public IdentityService getIdentityService() {
		return identityService;
	}



	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
	/**
	 * 
	 * logon(�û���¼ϵͳ)
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public String logon(String username,String password,HttpSession session) {
		log.debug("logon request: {username={}, password={}}", username, password);
		boolean hasLogin = identityService.checkPassword(username, password);
		if(hasLogin) {
		User user = identityService.createUserQuery().userId(username).singleResult();
		RequestUtil.saveUserToSession(session, user);
		List<Group> groupList = identityService.createGroupQuery().groupMember(username).list();
		session.setAttribute("groups", groupList);
		String[] groupNames = new String[groupList.size()];
		for (int i = 0; i < groupNames.length; i++) {
			System.out.println(groupList.get(i).getName());
			groupNames[i] = groupList.get(i).getName();
		}

		session.setAttribute("groupNames", ArrayUtils.toString(groupNames));
		return "redirect:/main/index";
		} else {
			return "redirect:/login?error=true";
		}
	}
	
	/**
	 * 
	 * logout(�û��ǳ�)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(RequestUtil.USER);
		return "/login";
	}
}
