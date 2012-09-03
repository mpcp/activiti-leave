/**
 * �ļ�����MainController.java
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
 * �����ƣ�MainController
 * ����������ҳ������
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����2:00:28
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����2:00:28
 * �޸ı�ע��
 * @version 
 * 
 */
@Controller
@RequestMapping("/main")
public class MainController {
	@RequestMapping(value = "/index")
	public String index() {
		return "/main/index";
	}
	
	@RequestMapping(value = "/welcome")
	public String welcome() {
		return "/main/welcome";
	}
}
