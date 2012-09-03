/**
 * 文件名：LoginController.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：LoginController
 * 类描述：登录控制器
 * 创建人：任欢
 * 创建时间：2012-9-3 下午1:58:00
 * 修改人：任欢
 * 修改时间：2012-9-3 下午1:58:00
 * 修改备注：
 * @version 
 * 
 */
@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}
