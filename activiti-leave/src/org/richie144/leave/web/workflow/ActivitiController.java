/**
 * 文件名：ActivitiController.java
 *
 * 版本信息：
 * 日期：2012-9-13
 * Copyright 足下 Corporation 2012 
 * 版权所有
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
 * 项目名称：activiti-leave
 * 类名称：ActivitiController
 * 类描述：activiti流程相关业务
 * 创建人：任欢
 * 创建时间：2012-9-13 下午5:09:53
 * 修改人：任欢
 * 修改时间：2012-9-13 下午5:09:53
 * 修改备注：
 * @version 
 * 
 */
@Controller
@RequestMapping("/workflow")
public class ActivitiController {
	/**
	 * 日志记录
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
	 * processList(流程定义列表)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("/process-list")
	public ModelAndView processList() {
		ModelAndView mav = new ModelAndView("workflow/process-list");
		/**
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 */
		List<Object[]> objects = new ArrayList<Object[]>();
		/**
		 * 得到流程定义的列表
		 */
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		for(ProcessDefinition processDefinition :processDefinitionList) {
			/**
			 * 得到每个已经部署流程的id
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
	 * deployAll(部署全部流程)
	 * @return String    部署后重定向到流程定义的列表
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("/redeploy/all")
	public String deployAll() {
		workflowProcessDefinitionService.deployAllFromClasspath();
		return "redirect:/workflow/process-list";
	}
	
	/**
	 * 
	 * loadByDeployment(通过部署流程的id和资源名称读取资源并将资源文件内容显示到页面上)
	 * @param   已部署的流程的id，
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
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
	 * 读取资源，通过流程ID
	 * @param resourceType			资源类型(xml|image)
	 * @param processInstanceId		流程实例ID	
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
	 * delete(删除部署的流程.级联删除流程id)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("/process/delete")
	public String delete(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return "redirect:/workflow/process-list";
	}
	
	/**
	 * 
	 * traceProcess(输出流程跟踪信息)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("process/trace")
	@ResponseBody
	public List<Map<String,Object>> traceProcess(String processInstanceId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<Map<String,Object>> activityinfos = workflowTraceService.processTrace(processInstanceId);
		return activityinfos;
	}
	
	/**
	 * 
	 * deploy(部署流程定义)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("/deploy")
	public String deploy(MultipartFile file) {
		/**
		 * 得到文件的名字(不包含全路径)
		 */
		String fileName = file.getOriginalFilename();
		System.err.println("客户端上传的文件是:" + fileName);
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
				 * bpmn扩展名特殊处理，转换为bpmn20.xml
				 */
				String baseName = FilenameUtils.getBaseName(fileName);
				repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", inputStream).deploy();
			} else {
				throw new ActivitiException("no support file type of " + extension);
			}
		} catch (IOException e) {
			LOGGER.debug("获取文件的输入流出错");
			
			e.printStackTrace();
		}
		return "redirect:/workflow/process-list";
	}
}
