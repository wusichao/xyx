// JavaScript Document
$(function() {
	$(window).resize(function(){
$("#activity_grid").setGridWidth(document.body.clientWidth*0.96);
});
	$("#jd").hover(function(){
		$("#sm").toggle();
	})
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
	var conversion=new Array();
	listInit();
	// 初始化转化点
	$.ajax({
	type : "POST",
	url : "findAllInversionpoint.do",
	dateType : "json",
	success : function(data) {
		conversion=data;
		var json = eval('(' + data + ')');
		$.each(json,
				function() {
					var html = "";
					html += "<label class='ac_checkbox'>";
					html += "<input onclick='che()' id=gx" + this.id
							+ " type='checkbox' name='spam' value="
							+ this.id + ">";
					html += this.name;
					html += '</label>';
					$('#trans').append(html);
				});
	}});
	bigcampaignId = "";
	pageInit();//初始化表格
	//表格的查询操作
	$("#searchButton").click(function() {
		var status = $("#sousuo option:selected").val();
		var name = $("#ac_search").val();
		$("#activity_grid").jqGrid('setGridParam', {
			url : 'finAllCampaign.do',
			postData : {
				status : status,
				name : name,
			},
			page : 1
		}).trigger("reloadGrid");
	});
	// 点击增加活动按钮
	$(".add").click(function() {
		
		/*if(conversion.length==2){alert("您还未添加转化点，无法新建活动。请先到[网站代码]->[转化监测代码]添加转化点!");}
		else{*/
		listInit();
	$("#e").text("");
	$("#timee").text("");
	$("#acmoneye").text("");
	$("#acme").text("");
	$("#timee").text("");
	$("#acurle").text("");
	$("#nosele").text("");
	//$("#poe").text("");
		$("#activitytitle li:first").text("新增活动");// 将标题名称改
		$("#ac_index").hide();
		$("#addactivity").toggle("slide", {
			direction : "right"
		}, 1000);
		$("#acname").val("");
	
		$("#startDate").val("");
		$("#endDate").val("");
		$("#acmoney").val("");
		$("#acurl").val("");
		$("#acdan").val("");
		$("#selectedway").empty();
		$(":checkbox").attr("checked", false);
		$("#actc option:eq(0)").prop("selected",true);
		$("#tab tbody").html("");
		$("#morefont").html("");
		bigcampaignId = "";
		//tableToGrid("#tab");
		$.ajax({
			type : "POST",
			url : "addCampaignDataUI.do",
			data : {
			},
			dateType : "json",
			success : function(data) {
				
			}
		});	
		//}

	});
	$("#actc").change(function(){
		var actc=$(this).val();
		if(actc=="1")
		{
		
		$("#acmethod").append("<option>CPM</option>");
		$("#acmethod").append("<option>CPC</option>");
		$("#acmethod").val("CPC");
		
		}
		else if(actc=="0"){
			$("#acmethod").empty();
			$("#acmethod").append("<option>CPL</option>");
			$("#acmethod").append("<option>CPA</option>")
			
		}
		})
	
	//渠道管理按钮(清空编辑框和错误提示框 )
	$("#ac_addtrend").click(function(){
		$("#wayname").val("");
		$("#sc_wayerror").text("");
	});
	$("#acname").keyup(function(){
		var acname=$("#acname").val();
		var name = /^([\u4e00-\u9fa5a-zA-z0-9()]{0,60})$/;
		if(acname==''){$("#e").text("必填");}
		else if(!name.test(acname)){$("#e").text("支持字母、汉字、数字和括号");}
		else if(acname.length>60){$("#e").text("长度不大于60");}
		else{$("#e").text("");}
	});
	$("#acname").blur(function(){
		var acname=$("#acname").val();
		var name = /^([\u4e00-\u9fa5a-zA-z0-9()]{0,60})$/;
		if(acname==''){$("#e").text("必填");}
		else if(!name.test(acname)){$("#e").text("支持字母、汉字、数字和括号");}
		else if(acname.length>60){$("#e").text("长度不大于60");}
		else{$("#e").text("");}
	});
	//活动名称的校验
	$("#acname").change(function() {
		var name = $("#acname").val();
		$.ajax({
			type : "POST",
			url : "validateCampaign.do",
			data : {
				name : name
			},
			dateType : "json",
			success : function(data) {
				if (data == "false") {
					$("#e").text("活动名称已存在!");
				}
			}
		});
	});
	$("#acmoney").keyup(function(){
		var money=$("#acmoney").val();
		var num=/^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/;
		if(money==''){$("#acmoneye").text("必填");}
		else if(!num.test(money)){$("#acmoneye").text("请输入合法的数字（整数，小数）");}
		else if(money<=0){$("#acmoneye").text("请输入大于0的数字");}
		else{$("#acmoneye").text("");}
	});
	$("#acmoney").blur(function(){
		var money=$("#acmoney").val();
		var num=/^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/;
		if(money==''){$("#acmoneye").text("必填");}
		else if(!num.test(money)){$("#acmoneye").text("请输入合法的数字（整数，小数）");}
		else if(money<=0){$("#acmoneye").text("请输入大于0的数字");}
		else{$("#acmoneye").text("");}
	});	
	
	$("#acdan").keyup(function(){
		var money=$("#acdan").val();
		var num=/^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/;
		if(money==''){$("#acme").text("必填");}
		else if(!num.test(money)){$("#acme").text("请输入合法的数字（整数，小数）");}
		else if(money<=0){$("#acme").text("请输入大于0的数字");}
		else{$("#acme").text("");}
	});
	$("#acdan").blur(function(){
		var money=$("#acdan").val();
		var num=/^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/;
		if(money==''){$("#acme").text("必填");}
		else if(!num.test(money)){$("#acme").text("请输入合法的数字（整数，小数）");}
		else if(money<=0){$("#acme").text("请输入大于0的数字");}
		else{$("#acme").text("");}
	});
	$("#acurl").keyup(function(){
		var turl=$("#acurl").val();
		var url=/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/;
		if(turl==""){$("#acurle").text("必填");}
		else if(!url.test(turl)){$("#acurle").text("请输入合法的地址(http://***.com)");}
		else if(turl.length>100){$("#acurle").text("长度不大于100");}
		else{$("#acurle").text("");}
	});
	$("#acurl").blur(function(){
		var turl=$("#acurl").val();
		var url=/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/;
		if(turl==""){$("#acurle").text("必填");}
		else if(!url.test(turl)){$("#acurle").text("请输入合法的地址(http://***.com)");}
		else if(turl.length>100){$("#acurle").text("长度不大于100");}
		else{$("#acurle").text("");}
	});
	// 点击保存
	$("#ac_set_save").click(
			function() {
				var id = new Array();
				var oTab = document.getElementById('tab');
				var oTbody = oTab.tBodies[0];
				var imgs = oTab.getElementsByTagName("img");
				for (var i = 0; i < oTbody.rows.length; i++) {
					id[i] = oTbody.rows[i].cells[0].innerText;
				}
				var ids=id.toString();
				var url;
				if (bigcampaignId) {
					url = "updateCampaign.do";
				} else {
					url = "addCampaign.do";
				}
				// 将数据回传到后台
				var acname = $("#acname").val();
				var selectedway = "";
				
				var starttime = $("#startDate").val();
				var endtime = $("#endDate").val();
				var monitor_type=$("#actc option:selected").val();
				var acmoney = $("#acmoney").val();
				/*if(monitor_type=='0'){var day=daysBetween(starttime,endtime);
				var dan=((acmoney/(parseInt(day)+1))).toFixed(2);
				$("#acdan").val(dan);$("#acmethod option:eq(4)").prop("selected",true);}*/
				
				var acmethod = $("#acmethod").val();
				var acurl = $("#acurl").val();
				var acdan = $("#acdan").val();
				var convstr=document.getElementsByName("spam");
				var conval="";
				
				for (i=0;i<convstr.length;i++)
				{
  					if(convstr[i].checked == true)
  					{
  						 conval+=convstr[i].value+",";
  					}
				}
				
				if (conval.length > 0) {
					conval = conval.substring(0,
							conval.length - 1);
				}
				$('#selectedway option').each(function() {
					selectedway += $.trim($(this).val()) + ",";
				});
				var selectedId='';
				if (selectedway.length > 0) {
					selectedId = selectedway.substring(0,
							selectedway.length - 1);
				}
				var radiobj=$('input:checkbox[name=spam]:checked').clone(true);
				if(acname==''){$("#e").text("必填"); $("#acname").focus();}
				//if(radiobj.size()==0){$("#poe").addClass("error");$("#poe").text("必选"); $("#acname").focus();}
				if(acmoney==''){$("#acmoneye").text("必填");$("#acname").focus();}
				if(starttime==''||endtime==''){$("#timee").text("必填");$("#acname").focus();}
				if(acdan==''){$("#acme").text("必填");$("#acname").focus();}
				if(acurl==''){$("#acurle").text("必填");$("#acname").focus();}
				var ob=$('#selectedway option').clone(true);
				if(ob.size()==0){
					$("#nosele").text("必选");$("#acname").focus();
				}
				
				
				if($("#e").text()==""&&$("#acmoneye").text()==""&&$("#acme").text()==""&&$("#acurle").text()==""&&$("#nosele").text()==""/*&&$("#poe").text()==""*/)
				{
					var gapmgs='';
					var urlmgs='';
					var gap=daysBetween(endtime,starttime);
					
					if(gap<=3)
					{
					 gapmgs='您选择的活动时间范围小于3天，系统统计的数据可能会缺少,';
					}
				
					$.ajax({
						type : "POST",
						url : 'getWebSite.do',
						data : {
							target_url : acurl,
						},
						dateType : "json",
						async: false,
						success : function(data) {
							if(data=='false')
							{
								urlmgs='您的活动地址与您的注册网址不一致,';
							}
							else
							{
							}
						}
						
					});
					
					if(gapmgs!=""||urlmgs!=""){
						if(confirm(gapmgs+urlmgs+"您要继续提交吗？"))
						{
							$.ajax({
						type : "POST",
						url : url,
						data : {
							monitor_type:monitor_type,
							creativeString : ids,
							name:acname,
							id : bigcampaignId,
							begin_date : starttime,
							end_date : endtime,
							cost : acmoney,
							unit_price : acdan,
							cost_type : acmethod,
							target_url : acurl,
							channelIds : selectedId,
							inversionpointIds : conval
						},
						dateType : "json",
						success : function(data) {
									$("#addactivity").hide("slide", {
										direction : "right"
									}, 1000);
									$("#ac_index").show();
									$("#activity_grid").jqGrid('setGridParam',{url : 'finAllCampaign.do',
										postData:{
										},page:1}).trigger("reloadGrid");
									}
					});
						}
						else
						{
						}
					}
					else
					{
						$.ajax({
						type : "POST",
						url : url,
						data : {
							monitor_type:monitor_type,
							creativeString : ids,
							name:acname,
							id : bigcampaignId,
							begin_date : starttime,
							end_date : endtime,
							cost : acmoney,
							unit_price : acdan,
							cost_type : acmethod,
							target_url : acurl,
							channelIds : selectedId,
							inversionpointIds : conval,
						},
						dateType : "json",
						success : function(data) {
									$("#addactivity").hide("slide", {
										direction : "right"
									}, 1000);
									$("#ac_index").show();
									$("#activity_grid").jqGrid('setGridParam',{url : 'finAllCampaign.do',
										postData:{
										},page:1}).trigger("reloadGrid");

									}
						
					});
					}
					
					
				}
				else {alert("请完善信息！");}
			});
	
	// 点击退出增加活动按钮小错号
	$("#ac_exit").click(function() {
		$("#addactivity").toggle("slide", {
			direction : "right"
		}, 1000);
		$("#ac_index").show();
	});

	// 关闭在建立活动页面(取消按钮)
	$("#ac_set_reset").click(function() {
		$("#addactivity").toggle("slide", {
			direction : "right"
		}, 1000);
		$("#ac_index").show();
	});
	// 左边数据传到后边
	$("#ac_way_chose").click(function() {
		var $obj = $('#selectway option:selected').clone(true);
		if ($obj.size() == 0) {
			alert("请至少选择一条!");
		}
		else{
		$('#selectedway').append($obj);
		$('#selectway option:selected').remove();
		$("#nosele").text("");
		}
	});
	// 右边渠道清除并传回到左边
	$("#ac_way_remove").click(function() {
		
		var arr=[];
		var j=0;
		var $obj = $('#selectedway option:selected').clone(true);
	  var str = $("#selectedway option:selected").map(function(){
		  return $(this).attr("indexs");
		  }).get().join(",");
		  arr = str.split(",");
		for(var i=0;i<arr.length;i++)
		{
			if(arr[i]==1)
			{
				j++;
			}
			
		}
		if ($obj.size() == 0) {
			alert("请至少选择一条!");
		}
		else if(j>0){
			alert("已选中的渠道已产生有效数据，无法移除！");
			
		}
		else{
		$('#selectway').append($obj);
		$('#selectedway option:selected').remove();
		}
	});
	// 添加渠道按钮
	$("#addway").click(function() {
		$("#sc_wayerror").text("");
		var newname = $("#wayname").val();
		isname = /^([\u4e00-\u9fa5a-zA-z0-9()]{0,60})$/;
		if (newname == "") {
			$("#sc_wayerror").text("必填!");
		} else if (!isname.test(newname)) {
			$("#sc_wayerror").text("只支持汉字、字母、数字和括号!");
		} else {
			// 将数据回传到后台
			$.ajax({
				type : "POST",
				url : "addChannel.do",
				data : {
					name : newname
				},
				dateType : "json",
				success : function(data) {
					if(data=="false"){
						$("#sc_wayerror").text("渠道名称已存在!");

					}else{
						var json = eval('(' + data + ')');
						$("#selectway").append(
								"<option id='wsc"+this.id+"' value='" + json.id + "'>" + json.name
										+ "</option>");
						$("#moreremove").append(
								"<option value='" + json.id + "'>" + json.name
										+ "</option>");
						$("#wayname").val("");
						$(".close").trigger("click");
					}	
				}
			});
		}

	});
	// 删除渠道按钮
	$("#removecancel").click(function() {
		$("#sc_wayerror").text("");
		var shuzu = [];
		var $obj = $('#moreremove option:selected');
		for (var i = 0; i < $obj.length; i++) {
			shuzu.push($obj[i].value);
		}
		if ($obj.size() == 0) {
			alert("请至少选择一条!");
		}
		else{
			$.ajax({
				type : "post",
				url : 'deleteChannel.do',
				data : {
					ids : shuzu.toString(),
				},
				async : false,
				success : function(data) {
					if (data == "true") {
						$obj.remove();
						$("#wsc" + this.id + "").remove();
						$(".close").trigger("click");
					}
					else if(data=="false")
						{
							$("#sc_wayerror").text("此渠道已被关联，无法删除");
						}
				}

			});
			
		}
		
	});
	
	$("#addfont").click(function(){
		var fontname=$("#fontname").val();
		isname=/^([\u4e00-\u9fa5a-zA-z0-9]{0,10})$/;
		if(fontname==""){$("#fonterr").text("必填");}
		
		else if(!isname.test(fontname))
		{
			$("#fonterr").text("只支持汉字、字母和数字且长度不能超过10位!");
		}
		else{
			initIft(fontname);
		}
		
	
	});
	$("#two").click(function(){
		$("#fonterr").text("");
		$("#fontname").val("");
	})
	var onOf=true;
	$("#mft").click(function() {
		$(".chuu").toggle();
		$(".bianji").toggle();
		if (onOf) {
			$(this).text("关闭");
			onOf = false;
			$("#two").attr('disabled',true);
			return false; 
			
		} else {
			$(this).text("删除文字创意");
			onOf = true;
			$("#two").attr('disabled',false);
			return false; 
		}
	
	});
	
});
//初始化表格
function pageInit() {
	$("#activity_grid").jqGrid(
			{
				mtype : "POST",
				url : 'finAllCampaign.do',
				height : '100%',

				postData : {
					status : '',
					name : ''
				},
				datatype : "json",
				colNames : [ '编号', '名称', '活动周期', '花费(元)', '结算方式',
						'活动地址', '状态','活动模式',  '更新时间','操作','操作日志' ],
				colModel : [ {
					name : 'id',
					key : true,
					align : 'left',
					hidden : true,
				}, {
					name : 'name',
					align : 'left',
					width : '150',
				}, {
					name : 'activity_cycle',
					align : 'left',
						width : '150',
				}, {
					name : 'cost',
					align : 'right',
					width : '60',
				}, {
					name : 'cost_type',
					align : 'center',
					width : '70',
				}, {
					name : 'target_url',
					align : 'left',
					width : '150',
				}, {
					name : 'status',
					align : 'center',
					sortable : false,
					width : '50',
				},{
					name : 'monitorType',
					align : 'center',
					sortable : false,
					width : '50',
				},
				 {name:'last_modified',
					key:true,
					formatter : function(value, options, rData){
	            	var timestamp = "";
	            	if(!isNaN(value) &&value){
	            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
	            	}
	            	return timestamp;
	            	}},{
					name : 'operation',
					align : 'left',
					width : '120',
					sortable : false,
				}, {
					name : 'operateLog',
					align : 'center',
					sortable : false,
					width : '50',
				}, ],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				scrollOffset:0,
				pager : '#activity_pager',
				viewrecords : true,
				sortname : "last_modified",
				sortorder : "desc",
				caption : "活动列表",
				autowidth : true,
				multiselect : false,
			});
	jQuery("#activity_grid").jqGrid('navGrid', '#activity_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
}
//初始化渠道列表和转化点
	function listInit() {
		// 初始化渠道列表
		$.ajax({
			type : "POST",
			url : "findAllChannel.do",
			dateType : "json",
			success : function(data) {
				var json = eval('(' + data + ')');
				$("#selectway").empty();
				$("#moreremove").empty();
				$.each(json, function() {
					$("#selectway").append(
							"<option  id='wsc"+this.id+"' value='"+this.id+"'>"+ this.name+"</option>");
					$("#moreremove").append(
							"<option value='" + this.id + "'>" + this.name
									+ "</option>");
				});
			}
		});
	}
function fn(id){
    if(confirm("确定删除吗")){
    	// 删除
    	$.ajax({
    		type : "post",
    		url : 'deleteCampaign.do',
    		data : {
    			ids : id.toString(),
    		},
    		async : false,
    		success : function(data) {
    			$("#activity_grid").trigger("reloadGrid");
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
    	});
    }else{
    }
}
function deleteActivity(id) {
	
	$.ajax({
		type : "post",
		url : 'isCampaignData.do',
		data : {
			ids : id.toString(),
		},
		async : false,
		success : function(data) {
			if(data=="false")
			{
				if(confirm("已产生有效数据，确认删除吗？")){
				// 删除
				$.ajax({
					type : "post",
					url : 'falseDeleteCampaign.do',
					data : {
						ids : id.toString(),
					},
					async : false,
					success : function(data) {
					$("#activity_grid").trigger("reloadGrid");
					},
				error : function(XMLHttpRequest, textStatus, errorThrown) {}
					});
				 }else{ }
			}
			else if(data=='true')
			{
				//删除确认框
				fn(id);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}
// 监测代码
function TrackingCode(id) {
	window.location.href = "getcodeUI.do?id=" + id + "&t="
			+ new Date().getTime();
};
// 效果
function effect(id) {
	var t=window.parent.frames["left"]; 
	t.recha();
	window.location.href = "conversion_funnel.html?id="+id;
};
// 编辑		
function edit(id) {
	$("#e").text("");
	$("#acmoneye").text("");
	$("#acme").text("");
	$("#acurle").text("");
	$("#nosele").text("");
	$("#timee").text("");
	//$("#poe").text("");
	$("#acname").val("");
	$("#startDate").val("");
	$("#endDate").val("");
	$("#acmoney").val("");
	$("#acmethod").val("");
	$("#acurl").val("");
	$("#acdan").val("");
	$("#selectedway").empty();
	$("input[name=spam]").attr("checked", false);
	$("#tab tbody").html("");
	$("#morefont").html("");
	$("#actc").val("");
	bigcampaignId = id;
	var oTab = document.getElementById('tab');
	var oTbody = oTab.tBodies[0];
	$.ajax({
		type : "post",
		url : 'addCampaignData.do',
		data : {
			id : id
		},
		async : true,
		success : function(data) {
			var json = eval('(' + data + ')');
			$.each(json[1], function() {
				$("#gx" + this.id + "").prop("checked",true);
			});
			var start = new Date();
			start.setTime(json[0].begin_date);
			ys = start.getFullYear();
			ms = start.getMonth() + 1;
			ss = start.getDate();
			start = ys + "/" + ms + "/" + ss;
			
			var end = new Date();
			end.setTime(json[0].end_date);
			ye = end.getFullYear();
			me = end.getMonth() + 1;
			se = end.getDate();
			end = ye + "/" + me + "/" + se;
			$("#startDate").val(start);
			$("#endDate").val(end);
			$("#endDate").datepicker( "option","minDate",start );

			$("#acname").val(json[0].name);
			$("#acmoney").val(json[0].cost);
			
			$("#acurl").val(json[0].target_url);
			$("#acdan").val(json[0].unit_price);
			$("#actc").val(json[0].monitor_type);
			if(json[0].monitor_type=="1"){
				$("#acmethod").append("<option>CPM</option>");
				$("#acmethod").append("<option>CPC</option>");
				}
			else if(json[0].monitor_type=="0"){
				$("#acmethod").empty();
				$("#acmethod").append("<option>CPL</option>");
				$("#acmethod").append("<option>CPA</option>");
				}
			$("#acmethod").val(json[0].cost_type);
			
			$.each(json[2], function() {
				var html="<option value='";
				html+=this.id;
				html+=" '";
				html+="indexs= '";
				html+=this.isData;
				html+=" '";
				html+=">";
				html+=this.name;
				html+="</option>";
				$("#selectedway").append(html);
			});
			$("#selectway").empty();
			$.each(json[4], function() {
				$("#selectway").append(
						"<option value='" + this.id + "'>" + this.name
								+ "</option>");
			});



			$.each(json[3], function() {
				var oTr=document.createElement('tr');
				
				var oTd=document.createElement('td');
				oTd.innerHTML=this.id;
				oTr.appendChild(oTd);
				oTbody.appendChild(oTr);
				
				oTd=document.createElement('td');
				
				var imghtml='';
				if(this.path=='null'){imghtml='——';}
				
				else{
				imghtml+="<img onmouseover='imgcorn(this)'style='cursor:pointer' data-toggle='modal'  data-target='#imgphoto' src='";
				imghtml+= this.path;
				imghtml+="'/>"}
				oTd.innerHTML=imghtml;
				oTr.appendChild(oTd);
				oTbody.appendChild(oTr);
				
				oTd=document.createElement('td');
				oTd.innerHTML=this.name;
				oTr.appendChild(oTd);
				oTbody.appendChild(oTr);
				
				oTd=document.createElement('td');
				var start = new Date();
				start.setTime(this.creation);
				ys = start.getFullYear();
				ms = start.getMonth() + 1;
				ss = start.getDate();
				hh=start.getHours();
				ff=start.getMinutes();
				mm=start.getSeconds();
				start = ys + "/" + ms + "/" + ss+ "/" + hh + ":" + ff+":"+mm;

				oTd.innerHTML=start;
				oTr.appendChild(oTd);
				oTbody.appendChild(oTr);
				
				var oTd=document.createElement('td');
				if(this.path=='null'){
				oTd.innerHTML="文字";
				}
				else{oTd.innerHTML="图片";}
				
				oTr.appendChild(oTd);
				oTbody.appendChild(oTr);
				
				
				oTd = document.createElement('td');
				var html = "";
						html += "<a href='javascript:;' class='delete' onclick=";
						html += " ' ";
						html += "deleteRow(";
						html += "this";
						html += ");";
						html += " ' ";
						html += ">删除</a>";
						oTd.innerHTML = html;
						oTr.appendChild(oTd);
						
						oTbody.appendChild(oTr);
			});
			$("#ac_index").css("display", "none");// 有表格的页面不可见
			$("#activitytitle").css("display", "block");// 标题可见
			$("#addactivity").css("display", "block");// 编辑活动页面可见
			$("#activitytitle li:first").text("编辑活动");// 将标题名称改

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		},
	});
}
function startLoad() {
	var saveurl = ""; // 将图片存放的信息存入数据库中
	var url = "uploadCreative.do"; // 处理上传的servlet
	var sizeLimit = "500 KB";// 文件的大小 注意: 中间要有空格
	var types = "*.jpg;*.jpeg;*.gif"; // 注意是 " ; " 分割
	var typesdesc = "web iamge file"; // 这里可以自定义
	var uploadLimit = 20; // 上传文件的个数
	
	initSwfupload(saveurl, url, sizeLimit, types, typesdesc, uploadLimit);
}
function deleteRow(r) {
	var id = r.parentNode.parentNode.cells[0].innerText;
    	// 删除
		if(confirm("确定删除吗")){
    		$.ajax({
		type : "post",
		url : "deleteCreativeInC.do",
		data : {
			id : id
		},
		async : false,
		success : function(data) {
			
			if(data=='true')
			{	
			var i = r.parentNode.parentNode.rowIndex;
			document.getElementById('tab').deleteRow(i);	
			}
			else if(data=="false")
			{
				alert("此创意已产生有效数据，无法移除！");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		},
	});
		}
	}

function che(){
	//$("#poe").text("");
}
function actionlog(id) {
	window.location.href = "action_log.html?id="+id;
};

Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()  
        // millisecond  
    };  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
};
function imgcorn(eve){
	var src=$(eve).attr("src");
		var html="<img style='width:100%; height:100%;' src='";
		html+=src;
		html+="'/>";;
		$("#creativephoto").html(html);
}
function daysBetween(DateOne,DateTwo) 
{  
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('/')); 
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('/')+1); 
    var OneYear = DateOne.substring(0,DateOne.indexOf ('/'));
    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('/')); 
    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('/')+1); 
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('/'));
    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
    return Math.abs(cha); 
}
//文字创意的清空和增加
function initIft(fontname){
	
	$.ajax({
			type:"POST",
			url:"addCreativeNoPath.do",
			dateType:"json",
			data:{name:fontname},
			success: function(data){
				if(data=='false'){
					$("#fonterr").text("名称已存在，请重新输入!");
					$("#fontname").focus();
					}
				else{	
				var json = eval('(' + data + ')');
				 
				//var newTr = document.getElementById('tab').insertRow(1);
				var oTab = document.getElementById('tab');
				var oTbody = oTab.tBodies[0];
				var newTr = oTbody.insertRow(0);
				//添加两列
				var newTd0 = newTr.insertCell();
				var newTd1 = newTr.insertCell();
				var newTd2 = newTr.insertCell();
				var newTd3 = newTr.insertCell();
				var newTd4 = newTr.insertCell();
				var newTd5 = newTr.insertCell();
				//设置列内容和属性
				newTd0.innerHTML = json.id;

				newTd1.innerText= "——";
				newTd2.innerText=json.name;
				var start = new Date();
					start.setTime(json.creation);
					ys = start.getFullYear();
					ms = start.getMonth() + 1;
					ss = start.getDate();
					hh=start.getHours();
					ff=start.getMinutes();
					mm=start.getSeconds();
					start = ys + "/" + ms + "/" + ss+ "/" + hh + ":" + ff+":"+mm;
				newTd3.innerText= start;
				newTd4.innerText= '文字';
				var html = "";
							html += "<a href='javascript:;' class='delete' onclick=";
							html += " ' ";
							html += "deleteRow(";
							html += "this";
							html += ");";
							html += " ' ";
							html += ">删除</a>";
				newTd5.innerHTML=html;

				
				$(".close").trigger("click");
				$("#fonterr").text("");
				$("#fontname").val("");
	           
				}
			}
		});

				}

//小画笔的点击事件，往前边label中添加一个编辑框
/*function getinput(ev){
	  var html="";
	  html="<input type='text' class='bianjikuang'  onblur='tianjia(this)'/>";
	  $(ev).prev().append(html);
	  return false;
	}*/
	
//小删除按钮事件，监测是否产生有效数据	
/*function removes(even){
	id = $(even).attr("value");
	if(confirm("确定删除吗")){
$.ajax({
	type : "POST",
	url : "deleteCreativeInC.do",
	data : {
		id : id.toString()
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
				alert("此名称已产生有效数据，无法移除！");
			}
	}
	});
	}	}	*/

//编辑框失去焦点事件，如果有内容就替换，没有内容就直接删除
/*function tianjia(eve){
	var name=$(eve).val();
	var id=$(eve).parent().attr("title");
	
	$.ajax({
			type:"POST",
			url:"validateConvertion.do",
			data: {name:name},
			dateType:"json",
			success: function(data) {
				if(data=="false"){
					alert("名称已存在")
				}else{
			//将修改后的数据添加到数据库，并提示修改成功		
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
		});
	
	
				}
			}
	});
	
	if(name!=''){
	$(eve).parent().html($(eve).val());
	$(eve).hide();
	}else{$(eve).hide();}
	}
	*/
