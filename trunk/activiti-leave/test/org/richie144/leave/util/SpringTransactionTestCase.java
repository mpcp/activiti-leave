/**
 * �ļ�����TestUtils.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-14
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.util;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�TestUtils
 * �����������Եĸ�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-14 ����2:01:10
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-14 ����2:01:10
 * �޸ı�ע��
 * @version 
 * 
 */
public abstract class SpringTransactionTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	@Resource
	protected DataSource datasource;
}
