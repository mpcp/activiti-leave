/**
 * �ļ�����LeaveDao.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-8-27
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.dao;

import org.richie144.leave.entity.oa.Leave;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�LeaveDao
 * �����������ʵ�����ӿ�
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����11:50:54
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����11:50:54
 * �޸ı�ע��
 * @version 
 * 
 */
public interface LeaveDao extends CrudRepository<Leave, Long> {
	
}
