/**
 * 文件名：UserController.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
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
 * 项目名称：activiti-leave
 * 类名称：UserController
 * 类描述：用户控制器类
 * 创建人：任欢
 * 创建时间：2012-9-3 下午5:13:17
 * 修改人：任欢
 * 修改时间：2012-9-3 下午5:13:17
 * 修改备注：
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
	 * logon(用户登录系统)
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
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
	 * logout(用户登出)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(RequestUtil.USER);
		return "/login";
	}
}
