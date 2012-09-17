<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Activiti演示系统</title>
<%@ include file="/common/global.jsp" %>
<script type="text/javascript" charset="UTF-8" language="javascript">
 /* 	var notLogon = ${empty user};
	if(notLogon) {
		location.href = '${ctx}/login?timeout=true';
	} */
</script>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/include-base-styles.jsp" %>
<script src='${ctx }/resources/js/main/main-frame.js' type="text/javascript"></script>
</head>
<body>
	<div id ="topPanel">
		<div>
			<table id = "topTable" style="padding: 0px;margin: 0px;margin-top: -5px" width="100%">
				<tr>
					<td>
						<img  src="${ctx}/resources/images/logo.png">
					</td>
					<td>
						<span>Activiti<br/>演示系统</span>
					</td>
					<td>
       					<div style="float:right; color: #fff;font-size: 12px;margin-top: 2px">
		        			<div>
		        				<label for="username">欢迎：</label>
		        				<span title="角色：${groupNames }">${user.firstName } ${user.lastName }/${user.id }</span>
		        			</div>
		        			<div style="text-align: right;">
		        				<a id="chang-theme" href="#">切换风格</a>
		       					<a href="#" id="loginOut">安全退出</a>
		        			</div>
		        		</div>
       				</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>