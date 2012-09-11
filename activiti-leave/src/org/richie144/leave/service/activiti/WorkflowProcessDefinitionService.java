/**
 * 文件名：WorkflowProcessDefinitionService.java
 *
 * 版本信息：
 * 日期：2012-8-27
 * Copyright 足下 Corporation 2012 
 * 版权所有
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
 * 项目名称：activiti-leave
 * 类名称：WorkflowProcessDefinitionService
 * 类描述：流程定义和部署相关业务
 * 创建人：任欢
 * 创建时间：2012-8-27 上午11:58:11
 * 修改人：任欢
 * 修改时间：2012-8-27 上午11:58:11
 * 修改备注：
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
	 * findProcessDefinition(根据流程实例id得到流程定义对象)
	 * @param   processInstanceId  流程实例的id
	 * @return processDefinition    该流程实例对应的流程定义
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
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
	 * findProcessDefinitionByPDId(根据流程定义id得到流程定义对象)
	 * @param   processDefinitionId
	 * @return    processDefinition  流程定义对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public ProcessDefinition findProcessDefinitionByPDId(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}
	
	/**
	 * 
	 * deployFromClasspath(部署classpath下面的流程定义)
	 * <p>从属性配置文件中获取属性<b>workflow.modules</b>扫描**deployments**</p>
	 * <p>然后从每个**deployments/${module}**查找在属性配置文件中的属性**workflow.module.keys.${submodule}**
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
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
				 * 所有流程
				 */
				deploySingleProcess(loader, loopProcessKey);
			}
		}
	}
	
	/**
	 * 
	 * deploySingleProcess(部署单个流程定义)
	 * @param  loader  流程定义资源加载器
	 * @param  processKey    流程模块名称
	 * @return void
	 * @Exception IOException  找不到路径下的文件
	 * @since  CodingExample　Ver(编码范例查看) 1.1
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
			logger.debug("文件"+classpathResourceUrl+"不存在");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * redeploy(重新部署单个流程定义)
	 * @param   processKey
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public void redeploy(String... processKey){
		deployFromClasspath(processKey);
	}
	
	/**
	 * 
	 * deployAllFromClasspath(重新部署所有流程定义)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public void deployAllFromClasspath() {
		deployFromClasspath();
	}
}
