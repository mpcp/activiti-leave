/**
 * �ļ�����LoginController.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�LoginController
 * ����������¼������
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����1:58:00
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����1:58:00
 * �޸ı�ע��
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
