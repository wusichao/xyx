// JavaScript Document

$(function() {
	$('#con_name').selectpicker({
		noneSelectedText: "请选择活动",
		noneResultsText: '没有找到匹配项',
    });
	$('#wsccannel').selectpicker({
		noneSelectedText : "请选择渠道 (可多选)",
		noneResultsText: '没有找到匹配项',
	});
	$('#creatives').selectpicker({
		noneSelectedText : "请选择创意 (可多选)",
		noneResultsText: '没有找到匹配项',
	});		
	
		

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
	$.ajax({
		type : "POST",
		url : "getCampaignList.do",
		async:false,
		data : {},
		dateType : "json",
		success : function(data) {
				var json = eval('(' + data + ')');
				$("#con_name").empty();
				$("#con_name").append("<option>自然流量</option>");
				$("#con_name").append("<option>所有流量</option>");
				$.each(json, function() {
		$("#con_name").append("<option value="+this.monitor_type+">" + this.name + "</option>");
		$("#con_name").selectpicker('refresh');
		});
		}
	});
	$( "#fun" ).show( "blind",1500);
	var reg = new RegExp("(^|&)" + "id" + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	var lqid=unescape(r[2]);
	
	if(lqid!='0'){
		$.ajax({
			url : "getCampaignById.do",
			dataType : "json",
			
			data : {
				id : lqid
			},
			success : function(data) {
				
				
				
				
				var o=$("#con_name option").length;
				
				for(var c=0; c<o; c++)
				{
					if($("#con_name option:eq("+c+")").text() == data.name){
						$("#con_name option:eq("+c+")").prop("selected",true);
						break;
					}
				}
				$.ajax({
				type : "POST",
				url : "getChannelByCampaign.do",
				data : {
					name : name,
				},
				dateType : "json",
				success : function(data) {
					var json = eval('(' + data + ')');
					$("#wsccannel").empty();
					$.each(json, function() {
						$("#wsccannel").append(
								"<option id='" + this.id + "' value='"
										+ this.id + "'>" + this.name
										+ "</option>");
						$('.selectpicker').selectpicker('refresh');
					});
				}
			});
				$.ajax({
				type : "POST",
				url : "getCreativeByCampaign.do",
				data : {
					name : name,
				},
				dateType : "json",
				success : function(data) {
					var json = eval('(' + data + ')');
					$("#creatives").empty();
					$.each(json, function() {
						$("#creatives").append(
								"<option id='" + this.id + "' value='"
										+ this.id + "'>" + this.name
										+ "</option>");
						$('.selectpicker').selectpicker('refresh');
					});
				}
			});	
			}
		});
	}
	// 第一个表格控制第二个表格
	$("#cdc").click(function() {
		$("#ftpo").show().siblings().hide();
		$(this).addClass("selecteds").siblings().removeClass("selecteds");
	});
	$("#cdr").click(function() {
		$("#ftpt").show().siblings().hide();
		$(this).addClass("selecteds").siblings().removeClass("selecteds");
	});
	$("#cdcr").click(function() {
		$("#ftpth").show().siblings().hide();
		$(this).addClass("selecteds").siblings().removeClass("selecteds");
	});
	$("#impclick").click(function() {
		$(".ft1p").show().siblings().hide();
	});
	$("#clickclick").click(function() {
		$(".ft1p").show().siblings().hide();
	});
	$("#reachclick").click(function() {
		$(".ft2p").show().siblings().hide();
	});
	$("#conversionclick").click(function() {
		$(".ft3p").show().siblings().hide();
	});

	// 点击按钮显示和隐藏渠道和创意
	var onOff = true;
	$("#show").click(function() {
		if (onOff) {
			$(this).val("<<");
			onOff = false;
		} else {
			$(this).val(">>");
			onOff = true;
		}
		$("#more_btn").toggle();
	});

	// 点击按钮加载渠道列表
	$("#con_name").change(function(){
		var name = $("#con_name option:selected").text();
		$.ajax({
					type : "POST",
					url : "getChannelByCampaign.do",
					data : {
						name : name,
					},
					dateType : "json",
					success : function(data) {
						var json = eval('(' + data + ')');
						$("#wsccannel").empty();
						$.each(json, function() {
							$("#wsccannel").append(
									"<option id='" + this.id + "' value='"
											+ this.id + "'>" + this.name
											+ "</option>");
							$('.selectpicker').selectpicker('refresh');
						});
					}
				});
	
		$.ajax({
					type : "POST",
					url : "getCreativeByCampaign.do",
					data : {
						name : name,
					},
					dateType : "json",
					success : function(data) {
						var json = eval('(' + data + ')');
						$("#creatives").empty();
						$.each(json, function() {
							$("#creatives").append(
									"<option id='" + this.id + "' value='"
											+ this.id + "'>" + this.name
											+ "</option>");
							$('.selectpicker').selectpicker('refresh');
						});
					}
				});
	});
	// 点击查询按钮，开始查询数据
	$("#com_search").click(function() {
		var channel = $("#wsccannel option:selected");
		var creative = $("#creatives option:selected");
		var channelIds = "";// 渠道id
		var creativeIds = "";// 创意id
		var compaignname = $("#con_name option:selected").text();// 活动名称
		if(compaignname=="自然流量"){compaignname=""}
		if(compaignname=="所有流量"){compaignname="all"}
		$.each(channel, function() {
			channelIds += this.id + ',';
		});
		$.each(creative, function() {
			creativeIds += this.id + ',';
		});
		var channelId = '';
		var creativeId = '';
		if (channelIds.length > 0) {
		channelId = channelIds.substring(0,channelIds.length - 1);
		}
		if (creativeIds.length > 0) {
		creativeId = creativeIds.substring(0,creativeIds.length - 1);
		}

		var start = $("#startDate").val();// 开始时间
		var end = $("#endDate").val();// 结束时间
		$.ajax({
			type : "POST",
			url : "getReportData.do",
			data : {
				name : compaignname,
				channelIds : channelId,
				creativeIds : creativeId,
				startDate : start,
				endDate : end,
				},
			dateType : "json",
			success : function(data) {
				var arrlose = new Array();
				var json = eval('(' + data + ')');
				if (json[1] == null) {
					$("#com_statu").hide();
					$("#com_style").hide();
					$("#com_dat").text('-');// 活动周期
					// 第一个表
					$("#com_cost").text('-');// 花费
					$("#clickrate").text('-');
					$("#arrivalrate").text('-');
					$("#conversionrate").text('-');
					// 点击率的表
					$("#cti").text('-');
					$("#ctcl").text('-');
					$("#ctcr").text('-');
					$("#ctclr").text('-');
					// 到达率的表
					$("#ctrc").text('-');
					$("#ctrr").text('-');
					$("#ctrra").text('-');
					$("#ctrlra").text('-');
					// 转化率的表
					$("#ctcrea").text('-');
					$("#ctcre").text('-');
					$("#ctcrer").text('-');
					$("#ctcrlr").text('-');
					// 漏斗图上数据
					$("#fi").text('-');
					$("#cl").text('-');
					$("#fr").text('-');
					$("#fc").text('-');
					} else {
						if(compaignname==""||compaignname=="all"){
						$("#com_statu").hide();
						$("#com_style").hide();
						}
						else
							{$("#com_statu").show();
							$("#com_style").show();}
						
						$("#com_statu").text(json[0].status);// 活动状态
						$("#com_style").text(json[0].cost_type+ "计费");// 计费方式
						$("#com_dat").text(json[0].activity_cycle);// 活动周期
						//78 0 0 0
						if(json[1].imp!=0)
						{
							//点击整体流失率
							var cllose=(((json[1].imp-json[1].click)/json[1].imp)*100).toFixed(2);
							arrlose.push(cllose+'%');
							//到达整体流失率
							var rlose=(((json[1].imp-json[1].reach)/json[1].imp)*100).toFixed(2);
							arrlose.push(rlose+'%');
							//转化整体流失率
							var cclose=(((json[1].imp-json[1].convertion)/json[1].imp)*100).toFixed(2);
							arrlose.push(cclose+'%');
						}
						//0 50 4 0
						else if(json[1].imp==0&&json[1].click!=0)
						{
							arrlose[0]='-';
							//到达整体流失率
							var rlose=(((json[1].click-json[1].reach)/json[1].click)*100).toFixed(2);
							arrlose.push(rlose+'%');
							//转化整体流失率
							var cclose=(((json[1].click-json[1].convertion)/json[1].click)*100).toFixed(2);
							arrlose.push(cclose+'%');
						}
						//0 0 8 0
						else if(json[1].imp==0&&json[1].click==0&&json[1].reach!=0)
						{
							arrlose[0]='-';arrlose[1]='-';
							
							//转化整体流失率
							var cclose=(((json[1].reach-json[1].convertion)/json[1].reach)*100).toFixed(2);
							arrlose.push(cclose+'%');
						}
						else if(json[1].imp==0&&json[1].click==0&&json[1].reach==0)
						{
							arrlose[0]="-";arrlose[1]='-';arrlose[2]='-';
						}
						// 第一个表
						$("#com_cost").text(json[1].cost);// 花费
						$("#clickrate").text(json[1].clickRate);
						$("#arrivalrate").text(json[1].reachRate);
						$("#conversionrate").text(json[1].convertionRate);
						// 点击率的表
						$("#cti").text(json[1].imp);
						$("#ctcl").text(json[1].click);
						$("#ctcr").text(json[1].clickRate);
						$("#ctclr").text(arrlose[0]);
						//到达率的表
						$("#ctrc").text(json[1].click);
						$("#ctrr").text(json[1].reach);
						$("#ctrra").text(json[1].reachRate);
						$("#ctrlra").text(arrlose[1]);
						//转化率的表
						$("#ctcrea").text(json[1].reach);
						$("#ctcre").text(json[1].convertion);
						$("#ctcrer").text(json[1].convertionRate);
						$("#ctcrlr").text(arrlose[2]);
						//漏斗图上数据
						$("#fi").text(json[1].imp);
						$("#cl").text(json[1].click);
						$("#fr").text(json[1].reach);
						$("#fc").text(json[1].convertion);
					}
				}
			});
	});
});
