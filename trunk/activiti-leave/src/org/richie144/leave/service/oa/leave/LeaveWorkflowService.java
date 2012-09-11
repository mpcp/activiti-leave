/**
 * �ļ�����LeaveWorkFlowService.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.service.oa.leave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.richie144.leave.entity.oa.Leave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�LeaveWorkFlowService
 * ���������������ҵ����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����10:23:26
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����10:23:26
 * �޸ı�ע��
 * @version 
 * 
 */
@Component
@Transactional(readOnly = true)
public class LeaveWorkflowService {
	private static Logger log = LoggerFactory.getLogger(LeaveWorkflowService.class);
	
	@Resource
	private LeaveManager leaveManager;
	
	@Resource
	private IdentityService identityService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private HistoryService historyService;
	
	public LeaveManager getLeaveManager() {
		return leaveManager;
	}

	public void setLeaveManager(LeaveManager leaveManager) {
		this.leaveManager = leaveManager;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	/**
	 * 
	 * startWorkflow(��������)
	 * @param  leave ���ʵ��
	 * @param  variables ����
	 * @return processInstance ����ʵ������
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public ProcessInstance startWorkflow(Leave leave,Map<String,Object> variables) {
		ProcessInstance processInstance = null;
		leaveManager.saveLeave(leave);
		log.debug("save entity :{}",leave);
		String businessKey = leave.getId().toString();
		
		/**
		 * ���������������̵���ԱID��������Զ����û�ID���浽activiti:initiator��
		 */
		identityService.setAuthenticatedUserId(leave.getUserId());
		processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variables);
		String processInstanceId = processInstance.getId();
		leave.setProcessInstanceId(processInstanceId);
		log.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { "leave", businessKey,
				processInstanceId, variables });
		return processInstance;
		
	}
	
	/**
	 * 
	 * findToDoTasks(��Ѱ��������)
	 * @param   userId ��ǰ��¼�û���id
	 * @return ��ǰ��¼�˵Ĵ����¼��б�
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public List<Leave> findToDoTasks(String userId) {
		List<Leave> todoTasks = new ArrayList<Leave>();
		List<Task> tasks = new ArrayList<Task>();
		/**
		 * ���ݵ�ǰ�˵�id��ѯ
		 */
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userId).orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		/**
		 * ��ǰ��δǩ�յ�����
		 */
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateUser(userId).orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		//�ϲ�
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		// �������̵�ҵ��ID��ѯʵ�岢����
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
							.singleResult();
			String businessKey = processInstance.getBusinessKey();
			Leave leave = leaveManager.getLeave(new Long(businessKey));
			leave.setTask(task);
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			todoTasks.add(leave);
		}		
		return todoTasks;
	}
	
	/**
	 * 
	 * findRunningProcessInstance(��ȡ�����е�����)
	 * @return results    �����е�����ʵ���б�
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public List<Leave> findRunningProcessInstance() {
		List<Leave> results = new ArrayList<Leave>();
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey("leave").list();
		// ����ҵ��ʵ��
		for (ProcessInstance processInstance : list) {
			String businessKey = processInstance.getBusinessKey();
			Leave leave = leaveManager.getLeave(new Long(businessKey));
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			results.add(leave);
			// ���õ�ǰ������Ϣ
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskCreateTime()
					.desc().listPage(0, 1);
			leave.setTask(tasks.get(0));

		}
		return results;
	}
	
	/**
	 * 
	 * findFinishedProcessInstaces(��ȡ���������)
	 * @return results    ����ɵ����̵��б�
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public List<Leave> findFinishedProcessInstaces() {
		List<Leave> results = new ArrayList<Leave>();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("leave")
				.finished().list();

		// ����ҵ��ʵ��
		for (HistoricProcessInstance historicProcessInstance : list) {
			String businessKey = historicProcessInstance.getBusinessKey();
			Leave leave = leaveManager.getLeave(new Long(businessKey));
			leave.setProcessDefinition(getProcessDefinition(historicProcessInstance.getProcessDefinitionId()));
			leave.setHistoricProcessInstance(historicProcessInstance);
			results.add(leave);
		}
		return results;
	}
	/**
	 *  ��ѯ���̶������
	 * @param processDefinitionId	���̶���ID
	 * @return
	 */
	protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}
	
	
}
