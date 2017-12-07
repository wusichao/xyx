// JavaScript Document
$(function(){
	var reg = new RegExp("(^|&)" + "id" + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	var id=unescape(r[2]);
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
	
	$("#cuochu").click(function(){
		window.location.href="activity.html";
	})
	//记载渠道及下载表格
	$.ajax({
		type:"POST",
		url:"findChannelByCampaign.do",
		data:{},
		dateType:"json",
		success: function(data) {
			 var json = eval('(' + data + ')');
      $.each(json,function(){
				var html="";
				html+="<a href='javascript:;' class='upload'  onclick='upcode(this)'  title='";
				html+=this.id;
				html+="'>";
				html+="<img src='images/modelTwo/tu.png'/>";
				html+=this.name;
				html+="</a>";
				$("#download").append(html);
	            });
    }
	});

	/*$("#totalup").click(function(){
		$("#xiaz").attr("href","excelDomnload.do?campaignId="+wwwwwid+"&i=3");
		 $("#xiaz")[0].click();
	});*/
	
	//关闭在监听页面
	/*$("#ac_over").click(function(){
		window.location.href="activity.html";
	})*/
		
	//复制的函数
	$("#copyshow").click(function(){
		var e=document.getElementById("ac_lokshow");//对象是
       	 e.select(); //选择对象
        document.execCommand("Copy"); //执行浏览器复制命令
		})
			
		//复制的函数
	$("#copyclick").click(function(){
		var e=document.getElementById("ac_lokclick");//对象是
       	 e.select(); //选择对象
        document.execCommand("Copy"); //执行浏览器复制命令
		})
	$("#copyreach").click(function(){
		var e=document.getElementById("ac_lokreach");//对象是
       	 e.select(); //选择对象
        document.execCommand("Copy"); //执行浏览器复制命令
		})	
	//生成代码	
	$("#product").click(function(){
	var channelid = $("#person_cancel option:selected").attr("id");
	var creativeid = $("#person_ideal option:selected").attr("id");
	if(channelid){
	$.ajax({
		type : "post",
		url : 'trackingCodeData.do',
		data : {
			campaignId:wwwwwid,
			channelId:channelid,
			creativeId:creativeid,
		},
		async : true,
		success : function(data) {
			var json = eval('(' + data + ')');
			
			$("#ac_lokshow").val(json[0]);
			$("#ac_lokclick").val(json[1]);
			$("#ac_lokreach").val(json[2]);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});}else{alert("请选择渠道");};
});
	
	campaignChannel();
	campaignCreative();
//	var wwwwwid;
	//获取活动id
	$.ajax({
		type:"POST",
		url:"getcampaignid.do",
		dateType:"json",
		success: function(data) {
			wwwwwid=data;
			
		}
});
	$.ajax({
		type:"POST",
		url:"findCampaignType.do?campaignId="+id+"",
//		data:{campaignId:wwwwwid},
		dateType:"json",
		async : true,
		success: function(data) {
			
			if(data==0){
				$("#imp").hide();
				$("#cli").hide();
			}
			
		}
});

})
//加载渠道
function campaignChannel(){
	$.ajax({
		type:"POST",
		url:"findChannelByCampaign.do",
		data:{},
		dateType:"json",
		success: function(data) {
			 var json = eval('(' + data + ')');
        var cancelhtml=""; 
        for(var i = 0; i < json.length; i++)
        {
            cancelhtml += '<option id='+json[i].id+'>' + json[i].name + '</option>';
        }
		$("#person_cancel").html(cancelhtml);
    }
	});
};
//加载创意
function campaignCreative(){
	$.ajax({
		type:"POST",
		url:"findCreativeByCampaign.do",
		data:{},
		dateType:"json",
		success: function(data) {
			 var json = eval('(' + data + ')');
			 if(json.length>0){
				  var idealhtml="";
		            for(var i = 0; i < json.length; i++)
		            {
						idealhtml+='<option id='+json[i].id+'>' + json[i].name + '</option>';
		            }
					$("#person_ideal").html(idealhtml);
			 }
			 else{$("#person_ideal").remove();};
    }
	});
};
//下载代码
function upcode(eve){
	var channelid=$(eve).attr("title");	
	$(eve).attr("href","byChannelDownload.do?channelId="+channelid);
	$(eve)[0].click();
}
