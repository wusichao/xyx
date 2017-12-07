// JavaScript Document
$(function(){	

/*$("#getCodes").attr("disabled",true);*/
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
	
//账户信息和验证码是否填写的验证
/*$("#forgetpwd-one").validate({*/
		
  /*  rules: {
      email: {
		  	required: true,
        	email: true,
			maxlength: 40,
		},*/
		/*getcheck:{
			required: true,
			},*/
	 
  /*  },
    messages: {
		 email:{
			 required:"必填",
			email: "格式错误!",
		  	maxlength: "长度不大于40位",
		 },*/
		/* getcheck:{
			required: "必填",
			},*/
   /* }
	});*/
	
//验证邮箱是否存在或者未审核
$("#for-email").blur(function(){
			var foremail=$("#for-email").val();
			if(foremail==""){$("#emailerror").html("必填！");}
			$.ajax({
			type:"POST",
			url:"validataEmailIsApproved.do",
			data: {email:foremail},
			dateType:"json",
			success: function(data) {
				if(data=="true"){$("#emailerror").html("");
				}
				else{$("#emailerror").html("账号不存在或未通过审核！");}
				},
			});
			});
$("#for-email").keyup(function(){
			var foremail=$("#for-email").val();
			if(foremail==""){$("#emailerror").html("必填！");}
			$.ajax({
			type:"POST",
			url:"validataEmailIsApproved.do",
			data: {email:foremail},
			dateType:"json",
			success: function(data) {
				if(data=="true"){$("#emailerror").html("");
				}
				else{$("#emailerror").html("账号不存在或未通过审核！");}
				},
			});
			});			
//点击获取验证码

$("#getCodes").click(function(){
	var foremail=$("#for-email").val();
	if(foremail==""){$("#emailerror").html("必填！");}
	else if($("#emailerror").text()=="")
	{
	
				$.ajax({
			type:"POST",
			url:"sendCode.do",
			data: {email:foremail},
			dateType:"json",
			success: function(data) {
				if(data=="true"){$("#emailerror").text("邮件已发送，请注意查收！");}
				},
			});
			djstime();
	}
	else{$("#emailerror").html("请核实账号！");}
});
function djstime(){
				var i=60;
				var interval=setInterval(function(){
					$("#getCodes").val("剩余"+i+"秒");
					$("#getCodes").attr("disabled",true);
					i--;
					if(i<0){
						$("#getCodes").attr("disabled",false);
						$("#getCodes").val("重新获取");
						clearInterval(interval);	
					}
				},1000);	
			}
//验证验证码是否正确
$("#for-next").click(function(){
			$("#emailerror").text("");
			var getcode=$("#for-getcode").val();
			var foremail=$("#for-email").val();
			//验证码的校验
			$.ajax({
			type:"POST",
			url:"validataCode.do",
			data: {code:getcode},
			dateType:"json",
			success: function(data) {
				if(data=="false"){$("#geterror").text("验证码错误！");}
				if(data=="true"){
				$("#geterror").text("");
				if(foremail==""){$("#emailerror").text("必填！");$("#emailerror").focus();}
			else if(getcode==""){$("#geterror").text("必填！");$("#geterror").focus();}
			
			else if($("#geterror").text()==""&&$("#emailerror").text()==""){
				$(".resetpwd").addClass("ft");
				$(".confirm").removeClass("ft");
				$("#septone").css("display","none");
				$("#septtwo").css("display","block");
			}
				}
				if(data=="null"){$("#geterror").text("必填！");}
				},
			});
			
			});							
//密码是否相同的验证
/*$("#forgetpwd-two").validate({
	success:{},
		
    rules: {
      
	  newpassword: {
        required: true,
		isPassword:true,
      },
      newconfirm_password: {
        required: true,
        equalTo: "#for-innewpwd",
		isPassword:true,
      },
    },
    messages: {
		 
	  newpassword:{
		  required:"必填",
		  isPassword:"6-14位，密码至少由字母和数字组成",
		  
		  },
	 newconfirm_password: {
        required:"必填",
        equalTo: "两次密码输入不一致！",
		isPassword:"6-14位，密码至少由字母和数字组成",
      },
    }
	});	*/
	
	//新密码的校验	
	$("#for-innewpwd").blur(function(){
		var old=$("#for-innewpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(old==""){$("#npe").text("必填")}
		else if(!password.test(old)){$("#npe").text("6-14位，密码至少由字母和数字组成")}
		else{$("#npe").text("");}
	})
	$("#for-innewpwd").keyup(function(){
		var old=$("#for-innewpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(old==""){$("#npe").text("必填")}
		else if(!password.test(old)){$("#npe").text("6-14位，密码至少由字母和数字组成")}
		else{$("#npe").text("");}
	})
	//密码相同的校验
	$("#newconfirm_password").blur(function(){
			var newpwd=$("#for-innewpwd").val();//新密码
		var old=$("#newconfirm_password").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(old==""){$("#ntpe").text("必填")}
		else if(!password.test(old)){$("#ntpe").text("6-14位，密码至少由字母和数字组成")}
		else if(newpwd!=old){$("#ntpe").text("两次密码输入不一致！")}
		else{$("#ntpe").text("");}
	})
	$("#newconfirm_password").keyup(function(){
		var newpwd=$("#for-innewpwd").val();
		var newestpwd=$("#newconfirm_password").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(newpwd==""){$("#ntpe").text("必填")}
		else if(!password.test(newpwd)){$("#ntpe").text("6-14位，密码至少由字母和数字组成")}
		else if(newpwd!=newestpwd){$("#ntpe").text("两次密码输入不一致！")}
		else{$("#ntpe").text("");}
	})
	
	
		
//修改成功，将数据返回后台数据库(返回到登陆页面)
$("#for-save").click(function(){
			var forgetemail=$("#for-email").val();
			var newpwd=$("#for-innewpwd").val();
			var conpwd=$("#newconfirm_password").val();
			if(newpwd==""||conpwd=="")
			{alert("请输入新密码！")}
			
			else if(newpwd==conpwd)
			{
				$.ajax({
			type:"POST",
			url:"resetPassword.do",
			data: {email:forgetemail,password:newpwd},
			dateType:"json",
			success: function(data) {
				if(data=="true"){
				$("#septone").css("display","none");
				$("#septtwo").css("display","none");
				$("#changeSuccess").css("display","block");
					djsatime();
					}
				else{alert("密码修改失败！");}
				},
			});
			}
			});		
		
//取消密码的修改（要修改成转到登陆页面）
/*$("#pre").click(function(){		
			window.location.href="loginUI.do";
		});	*/	

//提示框的显示与隐藏
$("#for-innewpwd").focus(function(){
	$(".exp").css("display","block");
	});

$("#for-innewpwd").blur(function(){
	$(".exp").css("display","none");
	});

function djsatime(){
	var i=5;
	var intervall=setInterval(function(){
		i--;
		$("#time").text(i);
		if(i<1){
			window.location.href="loginUI.do";
			clearInterval(intervall);	
		}
	},1000);	};

});