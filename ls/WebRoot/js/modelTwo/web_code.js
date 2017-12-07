$(function(){
	
	$("#checkerror").text("");
	$("#checkr").keyup(function(){
		var turl=$("#checkr").val();
		var url=/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/;
	 if(!url.test(turl)){$("#checkerror").text("请输入合法的地址(http://***.com)");}
		else{$("#checkerror").text("");}
	});
	$("#checkr").blur(function(){
		var turl=$("#checkr").val();
		var url=/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/;
	 if(!url.test(turl)){$("#checkerror").text("请输入合法的地址(http://***.com)");}
		else{$("#checkerror").text("");}
	});
	$("#checkc").keyup(function(){
		var turl=$("#checkc").val();
		var url=/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/;
	 if(!url.test(turl)){$("#checkconerror").text("请输入合法的地址(http://***.com)");}
		else{$("#checkconerror").text("");}
	});
	$("#checkc").blur(function(){
		var turl=$("#checkc").val();
		var url=/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/;
	 if(!url.test(turl)){$("#checkconerror").text("请输入合法的地址(http://***.com)");}
		else{$("#checkconerror").text("");}
	});
	

$.ajax({
		type:"POST",
		url:"getDomain.do",
		dateType:"json",
		async:false,
		success: function(data){
			$("#hido").attr("value",data);	
		}
})

	$.ajax({
		type:"POST",
		url:"getAccount.do",
		dateType:"json",
		success: function(data){
		//var data="123";
	var codehtml='';
		codehtml+="<script type='text/javascript'>\n";
		/*codehtml+="var xyx = xyx || [];\n";
		codehtml+="xyx.push(['xyx_a',";
		codehtml+="'"+data+"']);\n";
		codehtml+="xyx.push(['xyx_domain',";
		codehtml+="'"+$("#hido").val()+"']);\n";*/
		codehtml+="(function() {\n";
		codehtml+="var xyx_s = document.createElement('script');\n";
		codehtml+="xyx_s.src = '//"+$("#hido").val()+"/r.js?xyx_a="+data+"&xyx_domain="+$("#hido").val()+"&t='+(new Date()).getTime();\n";
		codehtml+="var sc = document.getElementsByTagName('script')[0];\n";
		codehtml+="sc.parentNode.insertBefore(xyx_s, sc);\n";
		codehtml+="})();\n";
		codehtml+="</script>\n";
		$("#vistercode").text(codehtml);
		}
		});
	
	
	//监测到达代码
	$("#checkreach").click(function(){
		var code=$("#vistercode").val();
		var url=$("#checkr").val();
		$.ajax({
				type:"POST",
				url:"InstallationCode.do",
				data: {trackingCode:code,toweb:url,},
				dateType:"json",
				success: function(data) {
					if(data=="false"){
						$("#checkerror").text("代码安放不正确，请修改后重试!");
					}
					if(data=="true"){$("#checkerror").text("代码已安放成功!");}
					if(data=="badweb"){$("#checkerror").text("请检查输入的网址是否正确!");}
				}
		});
	})
	$("#checkcon").click(function(){
	var code=$("#conversioncode").val();
		var url=$("#checkc").val();
		if(code==''){alert("请选择转化点，生成转化代码")}
		else{
			$.ajax({
				type:"POST",
				url:"InstallationCode.do",
				data: {trackingCode:code,toweb:url,},
				dateType:"json",
				success: function(data) {
					if(data=="false"){
						$("#checkconerror").text("代码安放不正确，请修改后重试!");
					}
					if(data=="true"){$("#checkconerror").text("代码已安放成功!");}
					if(data=="badweb"){$("#checkconerror").text("请检查输入的网址是否正确!");
					}
				}
		});
		}
	})
	//复制的函数
	$("#code_copyy").click(function(){
		var e=document.getElementById("conversioncode");//对象是
       	 e.select(); //选择对象
        document.execCommand("Copy"); //执行浏览器复制命令
		});
	//复制的函数
	$("#code_copy").click(function(){
		var e=document.getElementById("vistercode");//对象是
       	 e.select(); //选择对象
        document.execCommand("Copy"); //执行浏览器复制命令
		});
	$(".close").click(function(){
		$("#sc_pointerror").text("");
		$("#pointname").val("");
	});
	$("#web_tit a").click(function(){
		$(this).parent().addClass("ac_li_on");
		$(this).parent().siblings().removeClass("ac_li_on");
		$("#web_tit a").removeClass("co");
		$(this).addClass("co");
		var index=$(this).parent().index();
		$('#web_code div').hide();
		$('#web_code div:eq('+index+')').show();
		$('#foot').show();
	});
	initPoint();
	//初始化转化点列表
	//校验转化点重名
	$("#pointname").keyup(function(){
		var name=$("#pointname").val();
		$.ajax({
				type:"POST",
				url:"validateConvertion.do",
				data: {name:name},
				dateType:"json",
				success: function(data) {
					if(data=="false"){
						$("#sc_pointerror").text("转化点已存在!");
					}
				}
		});
	});
	//增加转化点列表
	$("#addpoint").click(function(){
		var name=$("#pointname").val();
		isname=/^([\u4e00-\u9fa5a-zA-z0-9]{0,10})$/;
		if(name==""){$("#sc_pointerror").text("必填");}
		else if(!isname.test(name))
		{
			$("#sc_pointerror").text("只支持汉字、字母和数字且长度不能超过10位!");
		}
		else{
			$.ajax({
			type:"POST",
			url:"addInversionpoint.do",
			data: {name:name},
			dateType:"json",
			success: function(data){
				if(data=="true")
				{
					$(".close").trigger("click");
					$("#sc_pointerror").text("");
					$("#pointname").val("");
					$("#more").empty();
					initPoint();
				}
			}
			});
		
		}
		
	});
	var onOff=true;
	$("#show").click(function() {
		if (onOff) {
			$(this).text("关闭");
			onOff = false;
			$("#addp").attr('disabled',true); 
		} else {
			$(this).text("管理转化点");
			onOff = true;
			$("#addp").attr('disabled',false);
		}
		$(".chuu").toggle();
		$(".bianji").toggle();
	});
	$("#c").change(function(){
		if($("#c option:selected").text()=='点击触发')
		{
			var ch='';
			ch+="/*将以下代码复制到按钮的点击事件代码中*/\n";
			ch+="var c= new Image();\n";
			ch+="c.src = '//"+$("#hido").val()+"/v?a="+aid+"&v="+vid+"&t='+(new Date()).getTime();\n";
			$("#conversioncode").text(ch);
		}
		else if($("#c option:selected").text()=='页面加载')
		{
			/*var rh='';
			rh+='<script type="text/javascript">\n';
			rh+='var xyx_ri=xyx_ri||[];\n';
			rh+="xyx_ri.push(['xyx_ra','"+aid+"']);\n";
			rh+="xyx_ri.push(['xyx_rv','"+vid+"']);\n";
			rh+="xyx_ri.push['xyx_rd',";
			rh+="'"+$("#hido").val()+"'];\n";
			rh+="(function(){\n";
			rh+="var xyx_si = document.createElement('script');\n";
			rh+="xyx_si.src = '//"+$("#hido").val()+"/v.js?t='+(new Date()).getTime();\n";
			rh+="var rc = document.getElementsByTagName('script')[0];\n";
			rh+="rc.parentNode.insertBefore(xyx_si, rc);\n";
			rh+="})();\n";
			rh+="</script>\n";*/
			var rh='';
			rh+="<img src='//"+$("#hido").val()+"/v?a="+aid+"&v="+vid+"' style='display:none;'/>";
		
			$("#conversioncode").text(rh);
		}
		})
		
});
function initPoint(){
	$.ajax({
			type:"POST",
			url:"findAllInversionpoint.do",
			dateType:"json",
			success: function(data){
				var json = eval('(' + data + ')');
				$.each(json,function(){
				var html="";
				html+="<button class='upload bordercol'  onclick='getcode(this)'  title='";
				html+=this.id;
				html+="'>";
				html+=this.name;
				html+="</button>";
				
				html+="<button class='bianji' onclick='getinput(this)' value='";
				html+=this.id;
				html+="'>";
				html+="<img  src='images/modelTwo/b.gif'/>";
				html+="</button>";
				
				html+="<button class='chuu'  onclick='removes(this)'  value='";
				html+=this.id;
				html+="'>";
				html+="<img  src='images/modelTwo/removeidea.gif'/>";
				html+="</button>";
				$("#more").append(html);
	            });
			}
		});
				}
var aid='',vid='';
function getcode(qwe){
	var id=$(qwe).attr("title");
	$("#c option:eq(0)").prop('selected',true);
	$(qwe).addClass("b").siblings().removeClass("b");
	vid=id;
	$.ajax({
		type:"POST",
		url:"getAccount.do",
		dateType:"json",
		success: function(data){
			getv(data,id);
			aid=data;
			}
		});
	
//	var id=$(qwe).attr("title");
//	$.ajax({
//		type:"POST",
//		url:"productCode.do",
//		data: {id:id},
//		dateType:"json",
//		success: function(data){
//				$("#conversioncode").val(data);
//			}
//		});
	}
function tianjia(eve){
	var name=$(eve).val();
	var id=$(eve).parent().attr("title");
	$.ajax({
			type:"POST",
			url:"validateConvertion.do",
			data: {name:name},
			dateType:"json",
			success: function(data) {
				if(data=="false"){
					alert("转化点已存在")
				}else{
					wscbianji(id,name);
				}
			}
	});
	if(name!=''){
	$(eve).parent().html($(eve).val());
	$(eve).hide();
	}else{$(eve).hide();}}
function removes(even){
	id = $(even).attr("value");
	if(confirm("确定删除吗")){
$.ajax({
	type : "POST",
	url : "deleteInversionpoint.do",
	data : {
		ids : id.toString()
	},
	dateType : "json",
	success : function(data) {
		if (data == "true") {
			$(even).prev().prev().remove();
			$(even).prev().remove();
			$(even).remove();
		}
		else if(data=="false")
			{
				alert("此转化点已产生有效数据，无法移除！");
			}
	}
	});
	}	}
function getinput(ev){
	  var html="";
	  html="<input type='text' class='bianjikuang'  onblur='tianjia(this)'/>";
	  $(ev).prev().append(html);
	}
function wscbianji(id,name){
	$.ajax({
		type:"POST",
		url:"updateInversionpoint.do",
		data: {name:name,
			id:id},
		dateType:"json",
		success: function(data){
			if(data=="true")
			{alert("修改成功");
			}
		}
		});}
function getv(a,v)
{
	var ch='';
	ch+="/*将以下代码复制到按钮的点击事件代码中*/\n";
		ch+="var c =new Image();\n";
		ch+="c.src = '//"+$("#hido").val()+"/v?a="+a+"&v="+v+"&t='+(new Date()).getTime();\n";
		$("#conversioncode").text(ch);
}






