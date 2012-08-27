/**
 * �ļ�����WorkflowTraceService.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-8-27
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.service.activiti;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.richie144.leave.util.WorkflowUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�WorkflowTraceService
 * ���������������������Service
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����2:28:35
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����2:28:35
 * �޸ı�ע��
 * @version 
 * 
 */
public class WorkflowTraceService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;
	
	private TaskService taskService;
	
	private IdentityService identityService;
	
	public List<Map<String,Object>> processTrace(String processInstanceId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<Map<String, Object>> activityInfos = null;
		//��ʼִ������
		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();
		
		Object property = PropertyUtils.getProperty(execution, "activityId");
		
		String activityId = "";
		if(property != null){
			activityId = property.toString();
		}
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl)repositoryService;
		
		ProcessDefinitionEntity processDefinition =(ProcessDefinitionEntity)repositoryServiceImpl.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		
		/**
		 * ��õ�ǰ��������нڵ�
		 */
		List<ActivityImpl> activitiList = processDefinition.getActivities();
		
		activityInfos = new ArrayList<Map<String,Object>>();
		for(ActivityImpl activity:activitiList){
			boolean currentActiviti = false;
			String id = activity.getId();
			/**
			 * ��ǰ�ڵ�
			 */
			if(id.equals(activityId)) {
				currentActiviti = true;
			}
			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);
			activityInfos.add(activityImageInfo);
		}
		return activityInfos;
	}
	
	/**
	 * 
	 * packageSingleActivitiInfo(��װ�����Ϣ����������ǰ�ڵ��X��Y���ꡢ������Ϣ���������͡���������)
	 * @param  activity
	 * @param  processInstance
	 * @param  activiti
	 * @return activityInfo
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private Map<String,Object> packageSingleActivitiInfo(ActivityImpl activity,ProcessInstance processInstance,boolean currentActiviti) {
		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		//��������
		setPosition(activity, activityInfo);
		//���ÿ�͸�
		setWidthAndHeight(activity, activityInfo);
		Map<String,Object> props = activity.getProperties();
		vars.put("��������", WorkflowUtils.parseToZhType(props.get("type").toString()));
		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		logger.debug("activityBehavior={}", activityBehavior);
		if(activityBehavior instanceof UserTaskActivityBehavior) {
			Task currentTask = null;
			/*
			 * ��ǰ�ڵ��task
			 */
			if (currentActiviti) {
				currentTask = getCurrentNodeInfo(processInstance);
			}
			
			/**
			 * ��ǰ����ķ����ɫ
			 */
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
			TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
			Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
			if(!candidateGroupIdExpressions.isEmpty()) {
				//����Ĵ����ɫ
				setTaskGroup(vars, candidateGroupIdExpressions);
				// ��ǰ������
				if (currentTask != null) {
					setCurrentTaskAssignee(vars, currentTask);
				}
			}
		}
		
		vars.put("�ڵ�˵��", props.get("documentation"));
		String description = activity.getProcessDefinition().getDescription();
		vars.put("����", description);
		logger.debug("trace variables: {}", vars);
		activityInfo.put("vars", vars);
		return activityInfo;
	}
	
	/**
	 * 
	 * setTaskGroup(�������������Ľ�ɫ)
	 * @param  vars  ������Ϣ����
	 * @param  currentTask    ��ǰ����ڵ�
	 * @return void
	 * @Exception ��
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = null;
		
		for(Expression expression:candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			if(expressionText.startsWith("$")) {
				expressionText = expressionText.replace("${insuranceType}", "life");
			}
			String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("����������ɫ", roles);
	}
	
	/**
	 * 
	 * setCurrentTaskAssignee(���õ�ǰ�����˵���Ϣ)
	 * @param   vars  ������Ϣ����
	 * @param  currentTask    ��ǰ����ڵ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		/**
		 * ��ȡ��ǰ�����ִ����
		 */
		String assignee = currentTask.getAssignee();
		
		if(assignee != null) {
			User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			
			vars.put("��ǰ������", userInfo);
		}
	}
	
	/**
	 * 
	 * getCurrentNodeInfo(��������ʵ����ȡ��ǰ�ڵ���Ϣ)
	 * @param  processInstance ����ִ�е�����ʵ��
	 * @return currentTask    ��ǰ����ڵ�
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private Task getCurrentNodeInfo(ProcessInstance processInstance) {
		Task currentTask = null;
		try {
			String activitiId = (String)PropertyUtils.getProperty(processInstance, "activityId");
			logger.debug("current activity id: {}", activitiId);
			currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId).singleResult();
			logger.debug("current task for processInstance: {}",ToStringBuilder.reflectionToString(currentTask));
		} catch (Exception e) {
			logger.error("can not get property activityId from processInstance: {}", processInstance);
			e.printStackTrace();
		} 
		return currentTask;
	}
	
	/**
	 * 
	 * setPosition(���������λ��)
	 * @param   activity
	 * @param  activityInfo
	 * @return void
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private void setPosition(ActivityImpl activity,Map<String, Object> activityInfo) {
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}
	
	/**
	 * 
	 * setPosition(���ÿ�ȣ��߶�����)
	 * @param   activity
	 * @param  activityInfo
	 * @return void
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
	}
}



















