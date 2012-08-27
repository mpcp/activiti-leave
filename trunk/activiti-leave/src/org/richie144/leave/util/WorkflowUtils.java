/**
 * �ļ�����WorkflowUtils.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-8-27
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�WorkflowUtils
 * �����������̸�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����5:07:52
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����5:07:52
 * �޸ı�ע��
 * @version 
 * 
 */
public class WorkflowUtils {
	
	/**
	 * 
	 * parseToZhType(ת�����̽ڵ�����Ϊ����˵��)
	 * @param   type Ӣ������ 
	 * @return  ת������������
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public static String parseToZhType(String type) {
		Map<String, String> types = new HashMap<String, String>();
		types.put("userTask", "�û�����");
		types.put("serviceTask", "ϵͳ����");
		types.put("startEvent", "��ʼ�ڵ�");
		types.put("endEvent", "�����ڵ�");
		types.put("exclusiveGateway", "�����жϽڵ�(ϵͳ�Զ�������������)");
		types.put("inclusiveGateway", "���д�������");
		types.put("callActivity", "������");
		String handled = types.get(type);
		return handled == null ? type :handled;
	}
}
