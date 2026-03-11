<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8" />
	<meta content="yes" name="apple-mobile-web-app-capable">  
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
	<meta http-equiv="X-UA-Compatible" content="ie=edge" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="format-detection" content="telephone=no,email=no"/>
	<title>授权登录系统</title>
</head>
<body>
	<div class="wrapper">
		<div class="login_box">
			<input id="error_1" value="1" />
			<div class="item" id="error_show" style="display: none">
			</div>
			<div class="item">
				<i class="icon_account"></i>
				<input class="input_account" type="text" name="username" value="${username!''}" placeholder="请输入账号" />
			</div>
			<div class="item item_psw" style="display: block;">
				<i class="icon_psw"></i>
				<#--<input class="input_psw" type="password" name="password" value="${password!''}" placeholder="请输入密码" />-->
			</div>

			<div class="item">
				<a class="btn_login" href="javascript:void(0);" onclick="oauth2login();">授权并登录</a>
			</div>
			</form>
		</div>
	</div>
	<script src="../../../static/jquery-2.1.4.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		function oauth2login() {
            var errormsg = jQuery("#error_1").val();
            alert(errormsg);
			$("#oauth2form").submit();
        }
        $(document).ready(function(){ // 页面加载完之后，自动执行该方法

        });
	</script>
</body>
</html>