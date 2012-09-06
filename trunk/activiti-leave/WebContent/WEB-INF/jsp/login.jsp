<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/meta.jsp" %>
<%@ include file= "/common/include-base-styles.jsp" %>
<title>activiti演示实例登录首页</title>
</head>
<body>
	<center>
		<c:if test="${not empty param.error}">
			<div><font color="cyan" size="12px">用户名或密码错误</font></div>
		</c:if>
		<c:if test="${not empty param.timeout}">
			<div><font color="cyan" size="12px">您还未登录或您已登陆超时</font></div>
		</c:if>
		<div style="width: 350px">
			<h2>工作流引擎Activiti演示</h2>
			<hr/>
			<form action="../user/logon">
				<table>
					<tr>
						<td align="right">用户名:</td>
						<td width="80"><input id="username" type="text"/></td>
					</tr>
					<tr>
						<td align="right">密&nbsp;&nbsp;&nbsp;&nbsp;码:</td>
						<td width="80"><input id="password" type="password"/></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<button type="submit">登录系统</button>
						</td>
					</tr>
				</table>
			</form>
			<hr />
			<table border="1">
				<caption>用户列表(密码：000000)</caption>
				<tr>
					<th style="text-align: center">用户名</th>
					<th style="text-align: center">角色</th>
				</tr>
				<tr>
					<td>admin</td>
					<td>管理员、用户</td>
				</tr>
				<tr>
					<td>kafeitu</td>
					<td>用户</td>
				</tr>
				<tr>
					<td>hruser</td>
					<td>人事、用户</td>
				</tr>
				<tr>
					<td>leaderuser</td>
					<td>部门经理、用户</td>
				</tr>
			</table>
			<hr />
			<p>
			<b>Activiti学习资料：</b><a target="_blank" href="http://www.kafeitu.me/categories.html#activiti-ref">http://www.kafeitu.me/categories.html#activiti-ref</a>
			</p>
		</div>
	</center>
</body>
</html>