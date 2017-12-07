// JavaScript Document
$(function(){
	$.ajax({
		type:"POST",
		url:"findAccountFunction.do",
		dateType:"json",
		success: function(data) {
			$("#Ordername").val(data);
			}
		});	
	var nowDate = new Date();
	var year=nowDate.getFullYear();
	var	month=nowDate.getMonth()+1;
	var	day=nowDate.getDate();
		 if(month<10){
                month= '0'+month;
        }
		 if(day<10){
                day= '0'+day;
        }
	var date=year+"-"+month+"-"+day;
	$.ajax({
	type:"POST",
	url:"findServiceLife.do",
	dateType:"json",
	success: function(data) {
		var indexs=data.indexOf("至");
		var str=data.substring(indexs+1,data.length);
		var cha=daysBetween(date,str);
		if(cha<=7) 
		{$("#Orderlimit").addClass("con");
		$("#Orderlimit").val(data);
		} 
		else{$("#Orderlimit").val(data);} 
		
		}
	});

	//初始化页面
	//加载账户信息
	$.ajax({
			type:"POST",
			url:"updateAccountUI.do",
			dateType:"json",
			success: function(data) {
				var json = eval('(' + data + ')');
				$("#myname").val(json.email);
				$("#mycontact").val(json.contact);
				$("#myphone").val(json.cellphone);
				$("#mycompany").val(json.company_name);
				$("#myindustry").val(json.vertical);
				},
			});
	//加载回溯期
	$.ajax({
			type:"POST",
			url:"attributionCycleData.do",
			//data: {password:password},
			dateType:"json",
			success: function(data) {
				$("#CycleData").val(data);
				},
			});
	//我的账户和设置的切换
	$("#mymain li").click(function(){
		$(this).addClass('on').siblings().removeClass('on');
		var index = $(this).index();
		$("#mymain a").removeClass('fontcol');
		$(this).children().addClass('fontcol');
		$('.content div').hide();
		$('.content div:eq('+index+')').show();
	});
	//联系人的验证
	$("#mycontact").keyup(function(){
		var con=$("#mycontact").val();
		var name = /^([\u4e00-\u9fa5a-zA-z\.]{0,60})$/;
		if(con==""){$("#mycontacte").text("必填")}
		else if(con.length>40){$("#mycontacte").text("长度不能大于40")}
		else if(!name.test(con)){$("#mycontacte").text("仅支持字母、汉字和点")}
		else{$("#mycontacte").text("")}
	})
	$("#mycontact").blur(function(){
		var con=$("#mycontact").val();
		var name = /^([\u4e00-\u9fa5a-zA-z\.]{0,60})$/;
		if(con==""){$("#mycontacte").text("必填")}
		else if(con.length>40){$("#mycontacte").text("长度不能大于40")}
		else if(!name.test(con)){$("#mycontacte").text("仅支持字母、汉字和点")}
		else{$("#mycontacte").text("")}
	})
	//联系电话的验证
	$("#myphone").keyup(function(){
		var myphone=$("#myphone").val();
		var mobiles = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
    	var tels = /^\d{3,4}-?\d{7,9}$/; 
		if(myphone==""){$("#myphonee").text("必填")}
		else if(!mobiles.test(myphone)|!tels.test(myphone)){$("#myphonee").text("格式错误!(010-12341234或以13、15、18开头的手机号")}
		else{$("#myphonee").text("")}
	})
	$("#myphone").blur(function(){
		var myphone=$("#myphone").val();
		var mobiles = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
    	var tels = /^\d{3,4}-?\d{7,9}$/; 
		if(myphone==""){$("#myphonee").text("必填")}
		else if(!mobiles.test(myphone)|!tels.test(myphone)){$("#myphonee").text("格式错误!(010-12341234或以13、15、18开头的手机号")}
		else{$("#myphonee").text("")}
	})
	//旧密码的验证
	$("#oldpwd").keyup(function(){
		var old=$("#oldpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(old==""){$("#pwderror").text("必填")}
		else if(!password.test(old)){$("#pwderror").text("6-14位，密码至少由字母和数字组成")}
		else{$("#pwderror").text("");}
	})
	$("#oldpwd").blur(function(){
		var old=$("#oldpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(old==""){$("#pwderror").text("必填")}
		else if(!password.test(old)){$("#pwderror").text("6-14位，密码至少由字母和数字组成")}
		else{$("#pwderror").text("");}
	})
	//新密码的验证
	$("#newerpwd").keyup(function(){
		var newp=$("#newerpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(newp==""){$("#newpe").text("必填")}
		else if(!password.test(newp)){$("#newpe").text("6-14位，密码至少由字母和数字组成")}
		else{$("#newpe").text("");}
	})
	$("#newerpwd").blur(function(){
		var newp=$("#newerpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(newp==""){$("#newpe").text("必填")}
		else if(!password.test(newp)){$("#newpe").text("6-14位，密码至少由字母和数字组成")}
		else{$("#newpe").text("");}
	})
	//密码一致性的验证
	$("#newestpwd").keyup(function(){
		var newp=$("#newestpwd").val();
		var news=$("#newerpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(newp==""){$("#cnewpe").text("必填")}
		else if(!password.test(newp)){$("#cnewpe").text("6-14位，密码至少由字母和数字组成")}
		else if(newp!=news){$("#cnewpe").text("两次密码输入不一致")}
		else{$("#cnewpe").text("");}
	})
	$("#newestpwd").blur(function(){
		var newp=$("#newestpwd").val();
		var news=$("#newerpwd").val();
		var password =/^(?=.*?[a-zA-Z])(?=.*?[0-6])[!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~A-Za-z0-9]{6,14}$/;
		if(newp==""){$("#cnewpe").text("必填")}
		else if(!password.test(newp)){$("#cnewpe").text("6-14位，密码至少由字母和数字组成")}
		else if(newp!=news){$("#cnewpe").text("两次密码输入不一致")}
		else{$("#cnewpe").text("");}
	})
	//账户信息保存到数据库
	$("#myMgsSave").click(function(){
				var ec=$("#mycontacte").text();
				var ep=$("#myphonee").text();
				var contact=$("#mycontact").val();
				var cellphone=$("#myphone").val();
				var vertical=$("#myindustry").val();
				if(ec!=''||ep!=''){alert("请正确填写信息")}
				else{
				$.ajax({
						type:"POST",
						url:"updateAccount.do",
						data: {contact:contact,cellphone:cellphone,vertical:vertical},
						dateType:"json",
						success: function(data) {
							if(data=="true"){alert("账户信息保存成功")}
							if(data=="false"){alert("保存失败，请核对信息")}
						},
				});
					}
	})
	//将修改的密码保存到数据库中
	$("#savePwd").click(function(){
		var oldpwd=$("#oldpwd").val();
		var newpwd=$("#newerpwd").val();
		var newp=$("#newestpwd").val()
		var cnewpe=$("#cnewpe").text()
		var newpe=$("#newpe").text()
		var pwderror=$("#pwderror").text()
		if(oldpwd==''||newpwd==''||newp==''){alert("请填写密码信息")}
		else if(cnewpe==''&&newpe==''&&pwderror==''){
					$.ajax({
				type:"POST",
				url:"updatePasswordValidada.do",
				data: {oldPassword:oldpwd,password:newpwd},
				dateType:"json",
				success: function(data) {
					if(data=="true"){alert("密码修改成功！");
					}
					else{$("#pwderror").text("旧密码输入有误！");};
					},
				});
				}
		else{alert("请正确填写信息")}
		});
	
	//旧密码编辑框获取焦点以后错误提示信息消失
	$("#oldpwd").focus(function(){
		$("#pwderror").text("");
	})
	//将回溯期保存到数据库
	$("#saveCycleData").click(function(){
	var re = /^\+?[1-9][0-9]*$/;
	var cycledata=$("#CycleData").val();
if(cycledata==""){alert("请输入回溯期")}
else if(cycledata>15||cycledata<1){alert("请输入1~15的整数")}
else if(!re.test(cycledata)){alert("请输入整数")}
else
{
			$.ajax({
		type:"POST",
		url:"updataAttributionCycle.do",
		data: {attributionCycle:cycledata},
		dateType:"json",
		success: function(data) {
			if(data=="true"){alert("修改成功");}
			else{alert("修改失败");}
			},
		});
}
});
	//我的账户取消按钮
	$("#myremgs").click(function(){
		$.ajax({
			type:"POST",
			url:"updateAccountUI.do",
			dateType:"json",
			success: function(data) {
				var json = eval('(' + data + ')');
				$("#myname").val(json.email);
				$("#mycontact").val(json.contact);
				$("#myphone").val(json.cellphone);
				$("#mycompany").val(json.company_name);
				$("#myindustry").val(json.vertical);
				},
			});
		})
	//回溯期取消按钮
		$("#myrecyc").click(function(){
			$.ajax({
				type:"POST",
				url:"attributionCycleData.do",
				dateType:"json",
				success: function(data) {
					$("#CycleData").val(data);
					},
				});
			});
	//修改密码取消
	$("#myrepwd").click(function(){
		$("#oldpwd").val("");
		$("#newerpwd").val("");
		$("#newestpwd").val("");
		$("#pwderror").text("");
		});
});
function daysBetween(DateOne,DateTwo) 
{  
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-')); 
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1); 
    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));
    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-')); 
    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1); 
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));
    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
    return Math.abs(cha); 
}
