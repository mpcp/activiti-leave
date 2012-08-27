/**
 * 文件名：LeaveDao.java
 *
 * 版本信息：
 * 日期：2012-8-27
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.dao;

import org.richie144.leave.entity.oa.Leave;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：LeaveDao
 * 类描述：请假实体管理接口
 * 创建人：任欢
 * 创建时间：2012-8-27 上午11:50:54
 * 修改人：任欢
 * 修改时间：2012-8-27 上午11:50:54
 * 修改备注：
 * @version 
 * 
 */
public interface LeaveDao extends CrudRepository<Leave, Long> {
	
}
