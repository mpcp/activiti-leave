/**
 * �ļ�����WorkflowProcessDefinitionService.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-8-27
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.service.activiti;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;


import org.apache.commons.lang3.ArrayUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�WorkflowProcessDefinitionService
 * �����������̶���Ͳ������ҵ��
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����11:58:11
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����11:58:11
 * �޸ı�ע��
 * @version 
 * 
 */
@Service
public class WorkflowProcessDefinitionService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@javax.annotation.Resource
	private RepositoryService repositoryService;
	
	@javax.annotation.Resource
	private HistoryService historyService;
	
	/**
	 * 
	 * findProcessDefinition(��������ʵ��id�õ����̶������)
	 * @param   processInstanceId  ����ʵ����id
	 * @return processDefinition    ������ʵ����Ӧ�����̶���
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public ProcessDefinition findProcessDefinitionByPIId(String processInstanceId) {
		ProcessDefinition processDefinition = null;
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		processDefinition = findProcessDefinitionByPDId(processDefinitionId);
		return processDefinition;
	}
	
	/**
	 * 
	 * findProcessDefinitionByPDId(�������̶���id�õ����̶������)
	 * @param   processDefinitionId
	 * @return    processDefinition  ���̶������
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public ProcessDefinition findProcessDefinitionByPDId(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}
	
	/**
	 * 
	 * deployFromClasspath(����classpath��������̶���)
	 * <p>�����������ļ��л�ȡ����<b>workflow.modules</b>ɨ��**deployments**</p>
	 * <p>Ȼ���ÿ��**deployments/${module}**���������������ļ��е�����**workflow.module.keys.${submodule}**
	 * TODO(�����������������ʹ�÷��� �C ��ѡ)
	 * TODO(�����������������ע������ �C ��ѡ)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public void deployFromClasspath(String... processKey) {
		ResourceLoader loader = new DefaultResourceLoader();
		String[] processKeys = {"leave","leave-dynamic-from"};
		
		for(String loopProcessKey :processKeys) {
			if(ArrayUtils.isNotEmpty(processKey)) {
				if(ArrayUtils.contains(processKey,loopProcessKey )) {
					logger.debug("hit model of {}",processKey);
					deploySingleProcess(loader, loopProcessKey);
				} else {
					logger.debug("module: {} not equals process key: {}, ignore and continue find next.", loopProcessKey, processKey);
				}
			} else {
				/**
				 * ��������
				 */
				deploySingleProcess(loader, loopProcessKey);
			}
		}
	}
	
	/**
	 * 
	 * deploySingleProcess(���𵥸����̶���)
	 * @param  loader  ���̶�����Դ������
	 * @param  processKey    ����ģ������
	 * @return void
	 * @Exception IOException  �Ҳ���·���µ��ļ�
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	private void deploySingleProcess(ResourceLoader loader,String processKey) {
		String classpathResourceUrl = "classpath:/org/richie144/leave/deployments/"+processKey + ".bar" ;
		logger.debug("read workflow from: {}", classpathResourceUrl);
		Resource resource = loader.getResource(classpathResourceUrl);
		try {
			InputStream is = resource.getInputStream();
			if(is == null) {
				logger.warn("ignore deploy workflow module: {}", classpathResourceUrl);
				
			} else {
				logger.debug("finded workflow module: {}, deploy it!", classpathResourceUrl);
				ZipInputStream zis = new ZipInputStream(is);
				repositoryService.createDeployment().addZipInputStream(zis).deploy();
			}
		} catch (IOException e) {
			logger.debug("�ļ�"+classpathResourceUrl+"������");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * redeploy(���²��𵥸����̶���)
	 * @param   processKey
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public void redeploy(String... processKey){
		deployFromClasspath(processKey);
	}
	
	/**
	 * 
	 * deployAllFromClasspath(���²����������̶���)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	public void deployAllFromClasspath() {
		deployFromClasspath();
	}
}
