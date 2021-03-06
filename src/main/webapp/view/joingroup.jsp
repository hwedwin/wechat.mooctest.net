<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language="java" import="com.mooctest.weixin.pojo.JSApiTicket,java.util.*,com.mooctest.weixin.util.JsSDKSign,java.io.*,com.mooctest.weixin.manager.WitestManager" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String openid=(String)request.getAttribute("openid");
%>
<head>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>加入群组</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="http://weixin.yoby123.cn/weui/style/weui.css"/>
<link rel="stylesheet" href="http://weixin.yoby123.cn/weui/style/weui2.css"/>
<link rel="stylesheet" href="http://weixin.yoby123.cn/weui/style/weui3.css"/>
<script src="http://weixin.yoby123.cn/weui/zepto.min.js"></script>
<style type="text/css">
#main h1, #main p {
	text-align: center;/*  */
}
.myform-row {
	padding: 5px;
}
</style>
</head>

<body>

	<div id="container">
		<div id="div1">
			<div style="float: right">
				<span style="color: #000; text-align: right;"></span>
			</div>
			<div style="clear: both; color: #000;"></div>
		</div>


		<div id="div2">
			<div id="main">
				<h1>加入群组</h1>

				<form name="form" id="join" style="width:100%;" method="post" action="q/group/join">
					<div class="weui_cells weui_cells_form">
						<input type="hidden" value="<%=openid%>" name="openid">
						<div class="weui_cell">
							<div class="weui_cell_hd"><label class="weui_label">群主姓名</label></div>
							<div class="weui_cell_bd weui_cell_primary">
								<input class="weui_input" type="text" required placeholder="请输入群主姓名" id="managerName" name="managerName">
							</div>
						</div>
						<div class="weui_cell">
							<div class="weui_cell_hd"><label class="weui_label">群组编号</label></div>
							<div class="weui_cell_bd weui_cell_primary">
								<input class="weui_input" type="password" required placeholder="请输入群组编号" id="groupId" name="groupId">
							</div>
						</div>
						<div class="myform-row">
							<a href="javascript:;" class="weui_btn weui_btn_primary" id="sd2">加入群组</a>	
						</div>
					</div>
				</form>


			</div>
		</div>
	</div>
<script type="text/javascript">
$(document).on("click", "#sd2", function() {
    $.confirm("您确定要加入此群组?", "确认加入?", function() {
      var form=document.getElementById("join");
      form.submit();
    }, function() {
      //取消操作
    });
  });

</script>

</body>

</html>