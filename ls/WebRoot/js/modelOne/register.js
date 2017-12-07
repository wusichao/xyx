// JavaScript Document
$(function(){
	$.ajax({
		type:"POST",
		url:"/ls/registerMakeToken.do",
		data: {},
		dateType:"json",
		success: function(data) {
			$("#token").val(data);
		}
	});
	var xyx = "xyx_" + (new Date()).getTime();//自定义全局变量
	$(".sub").click(function() {
		djstime();
	    if ($("#reform").valid()) {
	        var e = encodeURIComponent($("#email").val());
	        var cn = encodeURIComponent($("#comname").val());
	        var ws = encodeURIComponent($("#comhttp").val());
	        var p = encodeURIComponent($("#cellphone").val());
			//扩展参数的格式"参数一：参数值，参数二：参数值"
	        var t = "p1::" + p +",p3::" + e + ",p10::" + cn+ ",p11::" + ws;

	        var c = window[xyx] = new Image(); //把new Image()赋给一个全局变量长期持有
	        c.onload = (c.onerror = function() {
	            window[xyx] = null;
	        });
	        c.src = '//t.xyxtech.com/v?a=3&v=1&p=' + t;//转化代码安放位置
	        c = null; //释放局部变量c
	        c = window[xyx] = new Image(); 
	        c.src = '//t.xyxtech.com/v?a=29&v=28&p=' + t;//转化代码安放位置
	        c = null; //释放局部变量c
	        c = window[xyx] = new Image(); 
	        c.src = '//t.xyxtech.com/v?a=30&v=29&p=' + t;//转化代码安放位置
	        c = null; //释放局部变量c
	        c = window[xyx] = new Image(); 
	        c.src = '//t.xyxtech.com/v?a=36&v=34&p=' + t;//转化代码安放位置
	        c = null; //释放局部变量c
			
	        $("#reform").submit();
	    }
	})

	var emailw='';
	var reg = new RegExp("(^|&)" + "email" + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null){ emailw=unescape(r[2]);}
	
$.ajax({
			type:"POST",
			url:"/ls/registerData.do",
			data: {email:emailw},
			dateType:"json",
			success: function(data) {
				var json = eval('(' + data + ')');
				$("#email").val(json.email);
				$("#password").val(json.passWord);
				$("#confirm_password").val(json.passWord);
				$("#comname").val(json.company_name);
				$("#cat").val(json.vertical);
				$("#comhttp").val(json.web_site);
				$("#comperson").val(json.contact);
				$("#cellphone").val(json.cellphone);
				$("#imghead").attr("src",json.license_path);
				$("#invite").val(json.invitation_code);	
				
			}
});	

	});
	
	
	
		function djstime(){
				var i=3;
				var interval=setInterval(function(){
					$(".sub").attr("disabled",true);
					i--;
					if(i<0){
					$(".sub").attr("disabled",false);
						clearInterval(interval);	
					}
				},1000);	
			}