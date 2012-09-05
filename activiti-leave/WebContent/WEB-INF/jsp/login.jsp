<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
			<form action="user/logon">
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
		</div>
	</center>
</body>
</html>