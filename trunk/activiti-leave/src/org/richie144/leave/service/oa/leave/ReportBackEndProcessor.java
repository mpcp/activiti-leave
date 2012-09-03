/**
 * �ļ�����ReportBackEndProcessor.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.service.oa.leave;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.richie144.leave.entity.oa.Leave;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�ReportBackEndProcessor
 * �����������ٺ�Ĵ�����,��������ʱ��
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����9:47:27
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����9:47:27
 * �޸ı�ע��
 * @version 
 * 
 */
public class ReportBackEndProcessor implements TaskListener {

	private LeaveManager leaveManager;
	private RuntimeService runtimeService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		Leave leave = leaveManager.getLeave(new Long(processInstance.getBusinessKey()));
		Object realityStartTime = delegateTask.getVariable("realityStartTime");
		leave.setRealityStartTime((Date) realityStartTime);
		Object realityEndTime = delegateTask.getVariable("realityEndTime");
		leave.setRealityEndTime((Date) realityEndTime);
		
		leaveManager.saveLeave(leave);

	}

}
