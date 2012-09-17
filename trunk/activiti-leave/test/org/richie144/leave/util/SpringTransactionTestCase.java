/**
 * 文件名：TestUtils.java
 *
 * 版本信息：
 * 日期：2012-9-14
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.util;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：TestUtils
 * 类描述：测试的辅助类
 * 创建人：任欢
 * 创建时间：2012-9-14 下午2:01:10
 * 修改人：任欢
 * 修改时间：2012-9-14 下午2:01:10
 * 修改备注：
 * @version 
 * 
 */
public abstract class SpringTransactionTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	@Resource
	protected DataSource datasource;
}
