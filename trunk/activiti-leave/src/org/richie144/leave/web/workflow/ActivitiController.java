/**
 * �ļ�����ActivitiController.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-13
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.web.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.richie144.leave.service.activiti.WorkflowProcessDefinitionService;
import org.richie144.leave.service.activiti.WorkflowTraceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�ActivitiController
 * ��������activiti�������ҵ��
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-13 ����5:09:53
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-13 ����5:09:53
 * �޸ı�ע��
 * @version 
 * 
 */
@Controller
@RequestMapping("/workflow")
public class ActivitiController {
	/**
	 * ��־��¼
	 */
	private static  final Logger LOGGER =  LoggerFactory.getLogger(ActivitiController.class);
	
	//@Resource
	private WorkflowProcessDefinitionService workflowProcessDefinitionService;
	
	//@Resource
	private RepositoryService repositoryService;
	
	//@Resource
	private RuntimeService runtimeService;
	
	//@Resource
	private WorkflowTraceService workflowTraceService;
	
	/**
	 * 
	 * processList(���̶����б�)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("/process-list")
	public ModelAndView processList() {
		ModelAndView mav = new ModelAndView("workflow/process-list");
		/**
		 * ������������һ����ProcessDefinition�����̶��壩��һ����Deployment�����̲���
		 */
		List<Object[]> objects = new ArrayList<Object[]>();
		/**
		 * �õ����̶�����б�
		 */
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		for(ProcessDefinition processDefinition :processDefinitionList) {
			/**
			 * �õ�ÿ���Ѿ��������̵�id
			 */
			String deploymentId = processDefinition.getDeploymentId();
			
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			objects.add(new Object[]{deployment,processDefinition});
			
		}
		mav.addObject("objects", objects);
		return mav;
	}
	
	/**
	 * 
	 * deployAll(����ȫ������)
	 * @return String    ������ض������̶�����б�
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("/redeploy/all")
	public String deployAll() {
		workflowProcessDefinitionService.deployAllFromClasspath();
		return "redirect:/workflow/process-list";
	}
	
	/**
	 * 
	 * loadByDeployment(ͨ���������̵�id����Դ���ƶ�ȡ��Դ������Դ�ļ�������ʾ��ҳ����)
	 * @param   �Ѳ�������̵�id��
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("/resource/deployment")
	public void loadByDeployment(String deploymentId,String resourceName,HttpServletResponse response) throws IOException {
		InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		byte[] b =new byte[1024];
		int hasRead = 0;
		while((hasRead=inputStream.read()) != -1) {
			response.getOutputStream().write(b, 0, hasRead);
		}
	}
	
	/**
	 * ��ȡ��Դ��ͨ������ID
	 * @param resourceType			��Դ����(xml|image)
	 * @param processInstanceId		����ʵ��ID	
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/resource/process-instance")
	public void loadByProcessInstance(String resourceType,String processInstanceId,HttpServletResponse response) throws IOException {
		InputStream inputStream = null;
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		
		String resourceName = "";
		if(resourceType.equals("image")) {
			resourceName = singleResult.getDiagramResourceName();
		} else if(resourceType.equals("xml")){
			resourceName = singleResult.getResourceName();
		}
		
		inputStream = repositoryService.getResourceAsStream(singleResult.getDeploymentId(), resourceName);
		byte[] b =new byte[1024];
		int hasRead = 0;
		while((hasRead=inputStream.read()) != -1) {
			response.getOutputStream().write(b, 0, hasRead);
		}
	}
	
	/**
	 * 
	 * delete(ɾ�����������.����ɾ������id)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("/process/delete")
	public String delete(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return "redirect:/workflow/process-list";
	}
	
	/**
	 * 
	 * traceProcess(������̸�����Ϣ)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("process/trace")
	@ResponseBody
	public List<Map<String,Object>> traceProcess(String processInstanceId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<Map<String,Object>> activityinfos = workflowTraceService.processTrace(processInstanceId);
		return activityinfos;
	}
	
	/**
	 * 
	 * deploy(�������̶���)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("/deploy")
	public String deploy(MultipartFile file) {
		/**
		 * �õ��ļ�������(������ȫ·��)
		 */
		String fileName = file.getOriginalFilename();
		System.err.println("�ͻ����ϴ����ļ���:" + fileName);
		try {
			InputStream inputStream = file.getInputStream();
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(inputStream);
				repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				repositoryService.createDeployment().addInputStream(fileName, inputStream).deploy();
			} else if (extension.indexOf("bpmn20.xml") != -1) {
				repositoryService.createDeployment().addInputStream(fileName, inputStream).deploy();
			}  else if (extension.equals("bpmn")) {
				/*
				 * bpmn��չ�����⴦��ת��Ϊbpmn20.xml
				 */
				String baseName = FilenameUtils.getBaseName(fileName);
				repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", inputStream).deploy();
			} else {
				throw new ActivitiException("no support file type of " + extension);
			}
		} catch (IOException e) {
			LOGGER.debug("��ȡ�ļ�������������");
			
			e.printStackTrace();
		}
		return "redirect:/workflow/process-list";
	}
}
