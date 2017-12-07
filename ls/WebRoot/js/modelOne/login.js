$(function() {
	$.ajaxSetup({
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		complete : function(XMLHttpRequest, textStatus) {
			if (XMLHttpRequest.getResponseHeader("sessionstatus") == "timeOut") {
				  if(XMLHttpRequest.getResponseHeader("loginPath")){
			            alert("由于您过长时间未操作，为保证账户安全，请重新登录!");
			            window.top.location.replace(XMLHttpRequest.getResponseHeader("loginPath"));  
			        }else{  
			            alert("请求超时请重新登陆 !");  
			        }  
			}
		}
	});
	$(document).keypress(function(e) {  
	    // 回车键事件  
	       if(e.which == 13) {  
	    	   document.getElementById("oLogin").click(); 
	       }  
	   });

	$("#oLogin").click(function() {
		
		$("#loginerror").css("display", "none");
		$("#loginerror").empty();
		var email = $("#email").val();
		var password = $("#password").val();
		var code = $("#checkCode").val();
		if (email == "" || password == "") {
			$("#loginerror").css("display", "block");
			$("#loginerror").append("用户名和密码不能为空！");
			
		} else {
			$.ajax({
				type : "POST",
				url : "/ls/login.do",
				data : {
					email : email,
					passWord : password,
					code : code
				},
				dateType : "json",
				success : function(data) {
					if (data == "false") {
						$("#loginerror").css("display", "block");
						$("#loginerror").text("您输入的用户名和密码不匹配！");
					} else if (data == "admin") {
						window.location.href = "html/management.html";
					}
					else if (data == "accountNoApproved") {
						$("#loginerror").css("display", "block");
						$("#loginerror").text("您输入的账号暂未审核！");
					}
					else if (data == "NoOrder") {
						$("#loginerror").css("display", "block");
						$("#loginerror").text("您输入的账号暂无订单！");
					}
					else if (data == "true") {
						window.location.href = "frame.html";
//						editss();
						//大于三次调取验证码的事件
					} else if (data == "code") {
						$("#loginerror").css("display", "block");
						$("#loginerror").text("您输入的用户名和密码不匹配！");
						GetCode();
					}else if (data == "iscode") {
						$("#loginerror").css("display", "block");
						$("#loginerror").text("请输入验证码！");
						GetCode();
					}
					
					else if (data == "codeerror") {
						$("#loginerror").css("display", "block");
						$("#loginerror").text("验证码错误！");
						GetCode();
					}
					else if(data=="codefalse"){
						$("#loginerror").css("display", "block");
						$("#loginerror").text("您输入的用户名和密码不匹配！");
						GetCode();
					}
				}
			});
		}
		
	});
	// 获取验证码

	function GetCode() {
		$("#loginCode").css("display","block");
		$("#codeImg").attr("src", "sendValidataCode.do?t="+new Date().getTime());
	}
	// 看不清，换一张
	$("#changeImg").click(function() {
		GetCode();
	});
	$("#codeImg").click(function() {
		GetCode();
	});

});