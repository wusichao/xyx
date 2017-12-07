// JavaScript Document

$(function(){
	//$("#ts").popover({trigger: 'hover',placement:'bottom'});
	$(window).resize(function(){
		$("#no_grid").setGridWidth(document.body.clientWidth*0.9);
		$("#simple_grid").setGridWidth(document.body.clientWidth*0.9);
		$("#whole_grid").setGridWidth(document.body.clientWidth*0.9);
	});
	InitSimpleL([11.01,11.02,11.03,11.04,11.05,11.06,11.07],[6778,5675,5667,7645,5327,8770,5130],[6778,4575,5368,5332,4235,7543,5130],[6770,5675,5345,4556,1243,3456,2365],[245,890,236,675,456,654,876],[234,677,775,333,998,330,943],[0.1,0.2,0.4,0.3,0.7,0.05,0.6],[6778,5675,5667,7645,5327,8770,5130],[3666,4575,5368,5332,3235,6543,3556],[2334,3443,3456,4556,1243,3456,2365],[245,890,236,675,456,654,876],[234,677,775,333,998,330,943],[0.1,0.2,0.4,0.3,0.7,0.05,0.6],[0.1,0.2,0.4,0.3,0.7,0.05,0.6]);
//活动名称的联想输入
/* $( "#trend_name" ).autocomplete({
      minLength: 0,
      source:  function(request, response) {
                    $.ajax({
                        url: "getCampaignList.do",
                        dataType: "json",
                        data: {
                            name: request.term,
                        },
                        success: function(data) {
                            response($.map(data, function(item) {
                                return { label: item.monitor_type, value: item.name };
                            }));
                        }
                    });
                },
      focus: function( event, ui ) {
        $( "#trend_name" ).val( ui.item.value );
		 $( "#trend_name" ).attr('title', ui.item.label );
        return false;
      },
      select: function( event, ui ) {
        $( "#trend_name" ).val( ui.item.value );
		$( "#trend_name" ).attr('title', ui.item.label );
        return false;
      }
    })
    .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
      return $( "<li>" )
        .append( "<a>" + item.value +"</a>" )
        .appendTo( ul );
    };*/
	
//点击按钮显示和隐藏渠道和创意			
	var onOff=true;
	$("#show").click(function(){
		if(onOff){$(this).val("<<");
		onOff=false;}
		else{$(this).val(">>");
		onOff=true;}
		$("#more_btn").toggle();
	})	
	//选择框的提示信息
	$('#trend_name').selectpicker({
				noneSelectedText: "请选择活动",
				noneResultsText: '没有找到匹配项',
				
				
            });
	 $('#cannel').selectpicker({
				noneSelectedText: "请选择渠道 (可多选)",
				noneResultsText: '没有找到匹配项',
            });
	 $('#creative').selectpicker({
				noneSelectedText: "请选择创意 (可多选)",
				noneResultsText: '没有找到匹配项',
            });
	//加载活动列表		
	$.ajax({
				type : "POST",
				url : "getCampaignList.do",
				data : {},
				dateType : "json",
				success : function(data) {
						var json = eval('(' + data + ')');
						$("#trend_name").empty();
						
						$("#trend_name").append("<option>自然流量</option>");
						$("#trend_name").append("<option>所有流量</option>");
						$.each(json, function() {
				$("#trend_name").append("<option value="+this.monitor_type+">" + this.name + "</option>");
				$("#trend_name").selectpicker('refresh');
				});
				}
			});		
			
			
			
			
			
//点击渠道获取渠道的下拉列表
$("#trend_name").change(function(){

	var name=$("#trend_name option:selected").text();
	if(name=="所有流量"||name=="自然流量"){name=''}
	$.ajax({
				type : "POST",
				url : "getChannelByCampaign.do",
				data : {
					name : name,
				},
				dateType : "json",
				success : function(data) {
						var json = eval('(' + data + ')');
						$("#cannel").empty();
						$.each(json, function() {
					$("#cannel").append(
							"<option id='"+this.id+"' value='" + this.id + "'>" + this.name
									+ "</option>");
									$("#cannel").selectpicker('refresh');
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
						$("#creative").empty();
						$.each(json, function() {
					$("#creative").append(
							"<option id='"+this.id+"' value='" + this.id + "'>" + this.name
									+ "</option>");
				$('#creative').selectpicker('refresh');
				});
				}
			});
});

	//点击查询按钮，开始查询数据
gridNoInit();
gridSimpleInit();
gridWholeInit();
$("#com_search").click(function(){
		var compaignname=$("#trend_name option:selected").text();//活动名称
		var style=$("#trend_name option:selected").attr("value");//活动类型
		var channel=$("#cannel option:selected");
		var creative=$("#creative option:selected");
		var channelIds="";//渠道id
		var creativeIds="";//创意id
		$.each(channel, function() {
				channelIds+=this.id+',';
			});
		$.each(creative, function() {
				creativeIds+=this.id+',';
			});
		if(channelIds.length>0){
		 channelIds=channelIds.substring(0,channelIds.length - 1);}
		if(creativeIds.length>0){
		 creativeIds=creativeIds.substring(0,creativeIds.length - 1);}
		var start=$("#startDate").val();//开始时间
		var end=$("#endDate").val();//结束时间
		
		if(compaignname=="自然流量"){
			//头部表格
			$("#no_camp").show();
			$("#simple1").hide();
			$("#simple2").hide();
			$("#whole1").hide();
			$("#whole2").hide();
			//折线图
			$("#no_trend").show();
			$("#simple_trend").hide();
			$("#whole_trend").hide();
			//初始化表格
			
			$("#no_comp_grid").show();
			$("#has_simple_grid").hide();
			$("#has_whole_grid").hide();
			
			
			//刷新表格
			$("#no_grid").jqGrid('setGridParam',{url : 'getReportGridBean.do',
			postData:{
				name:"",
				channelIds:channelIds,
				creativeIds:creativeIds,
				startDate:start,
				endDate:end,
			},page:1}).trigger("reloadGrid").trigger("reloadGrid");
			
			
			$.ajax({
				type : "POST",
				url : "getReportData.do",
				data : {
					name:"",
					channelIds : channelIds,
					creativeIds:creativeIds,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var arrxAxis=[],visit=[],visitUser=[],visitIp=[],conv=[],convUser=[],convIp=[];	 
					var json = eval('(' + data + ')');
					if(json[1]==null)
					{
						InitNoL(arrxAxis,visit,visitUser,visitIp,conv,convUser,convIp);	
						$("#no_visit").text("-");
						$("#no_visitUser").text("-");
						$("#no_visitIp").text("-");
						$("#no_conv").text("-");
						$("#no_convUser").text("-");
						$("#no_convIp").text("-");				
					}
					else
					{
						$("#no_visit").text(json[1].visit);
						$("#no_visitUser").text(json[1].visitUser);
						$("#no_visitIp").text(json[1].visitIp);
						$("#no_conv").text(json[1].convertion);
						$("#no_convUser").text(json[1].conversionUser);
						$("#no_convIp").text(json[1].conversionIp);
						
						 $.each(json[2], function() {
							  arrxAxis.push(this.day);
							  visit.push(this.visit);
							  visitUser.push(this.visitUser);
							   visitIp.push(this.visitIp);
							  conv.push(this.convertion);
							  convUser.push(this.conversionUser);
							  convIp.push(this.conversionIp);
								})
				InitNoL(arrxAxis,visit,visitUser,visitIp,conv,convUser,convIp);
				}
				}
	})
			}
		else if(compaignname=='所有流量')
		{
			//头部表格
			$("#no_camp").show();
			$("#simple1").hide();
			$("#simple2").hide();
			$("#whole1").hide();
			$("#whole2").hide();
			//折线图
			$("#no_trend").show();
			$("#simple_trend").hide();
			$("#whole_trend").hide();
			//初始化表格
			
			$("#no_comp_grid").show();
			$("#has_simple_grid").hide();
			$("#has_whole_grid").hide();
			
			
			//刷新表格
			$("#no_grid").jqGrid('setGridParam',{url : 'getReportGridBean.do',
			postData:{
				name:"all",
				channelIds:channelIds,
				creativeIds:creativeIds,
				startDate:start,
				endDate:end,
			},page:1}).trigger("reloadGrid").trigger("reloadGrid");
			
			
			$.ajax({
				type : "POST",
				url : "getReportData.do",
				data : {
					name:"all",
					channelIds : channelIds,
					creativeIds:creativeIds,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var arrxAxis=[],visit=[],visitUser=[],visitIp=[],conv=[],convUser=[],convIp=[];	 
					var json = eval('(' + data + ')');
					if(json[1]==null)
					{
						$("#no_visit").text("-");
						$("#no_visitUser").text("-");
						$("#no_visitIp").text("-");
						$("#no_conv").text("-");
						$("#no_convUser").text("-");
						$("#no_convIp").text("-");	
						
						InitNoL(arrxAxis,visit,visitUser,visitIp,conv,convUser,convIp);						
					}
					else
					{
						$("#no_visit").text(json[1].visit);
						$("#no_visitUser").text(json[1].visitUser);
						$("#no_visitIp").text(json[1].visitIp);
						$("#no_conv").text(json[1].convertion);
						$("#no_convUser").text(json[1].conversionUser);
						$("#no_convIp").text(json[1].conversionIp);
						
						 $.each(json[2], function() {
							  arrxAxis.push(this.day);
							  visit.push(this.visit);
							  visitUser.push(this.visitUser);
							   visitIp.push(this.visitIp);
							  conv.push(this.convertion);
							  convUser.push(this.conversionUser);
							  convIp.push(this.conversionIp);
								})
				InitNoL(arrxAxis,visit,visitUser,visitIp,conv,convUser,convIp);
				}
				}
	})
			}
		//到访分析
		else if(compaignname!=''&&style==0&&compaignname!='自然流量'&&compaignname!='所有流量') 
		{
			$("#no_camp").hide();
			$("#simple1").show();
			$("#simple2").show();
			$("#whole1").hide();
			$("#whole2").hide();
			//折线图
			$("#no_trend").hide();
			$("#simple_trend").show();
			$("#whole_trend").hide();
			//初始化表格
			$("#no_comp_grid").hide();
			$("#has_simple_grid").show();
			$("#has_whole_grid").hide();
			
			
			//刷新表格
			$("#simple_grid").jqGrid('setGridParam',{url : 'getReportGridBean.do',
			postData:{
				name:compaignname,
				channelIds:channelIds,
				creativeIds:creativeIds,
				startDate:start,
				endDate:end,
			},page:1}).trigger("reloadGrid");
			
			$.ajax({
				type : "POST",
				url : "getReportData.do",
				data : {
					name:compaignname,
					channelIds : channelIds,
					creativeIds:creativeIds,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var arrxAxis=[],cost=[],reach=[],reachUser=[],reachIp=[],visit=[],visitUser=[],visitIp=[],conv=[],convUser=[],convIp=[],conversionRate=[],cpl=[],cpa=[];	 
					var json = eval('(' + data + ')');
					if(json[1]==null)
					{
						InitSimpleL(arrxAxis,cost,reach,reachUser,reachIp,visit,visitUser,visitIp,conv,convUser,convIp,conversionRate,cpl,cpa);
						$("#simple1_cost").text("-");
						$("#simple1_reach").text("-");
						$("#simple1_reachtUser").text("-");
						$("#simple1_reachIp").text("-");
						$("#simple1_visit").text("-");
						$("#simple1_visitUser").text("-");
						$("#simple1_visitIp").text("-");
						
						$("#simple2_conv").text("-");
						$("#simple2_convUser").text("-");
						$("#simple2_convIp").text("-");
						
						$("#simple2_convRate").text("-");
						
						$("#simple2_cpl").text("-");
						$("#simple2_cpa").text("-");						
					}
					else
					{
						$("#simple1_cost").text(json[1].cost);
						$("#simple1_reach").text(json[1].reach);
						$("#simple1_reachtUser").text(json[1].reachUser);
						$("#simple1_reachIp").text(json[1].reachIp);
						$("#simple1_visit").text(json[1].visit);
						$("#simple1_visitUser").text(json[1].visitUser);
						$("#simple1_visitIp").text(json[1].visitIp);
						
						$("#simple2_conv").text(json[1].convertion);
						$("#simple2_convUser").text(json[1].conversionUser);
						$("#simple2_convIp").text(json[1].conversionIp);
						
						$("#simple2_convRate").text(json[1].convertionRate);
						
						$("#simple2_cpl").text(json[1].cpl);
						$("#simple2_cpa").text(json[1].cpa);
						
						 $.each(json[2], function() {
							  arrxAxis.push(this.day);
							  cost.push(this.cost);
							  reach.push(this.reach);
							  reachUser.push(this.reachUser);
							  reachIp.push(this.reachIp);
							  visit.push(this.visit);
							  visitUser.push(this.visitUser);
							  visitIp.push(this.visitIp);
							  conv.push(this.convertion);
							  convUser.push(this.conversionUser);
							  convIp.push(this.conversionIp);
							  if(this.convertionRate){
								var convRate=this.convertionRate.substring(0,this.convertionRate.length-1)
							  	conversionRate.push(convRate);
							  }
							  cpl.push(this.cpl);
							  cpa.push(this.cpa);
								})
				InitSimpleL(arrxAxis,cost,reach,reachUser,reachIp,visit,visitUser,visitIp,conv,convUser,convIp,conversionRate,cpl,cpa);
				}
				}
	})	
		}
		//全程分析
		else if(compaignname!=''&&style==1&&compaignname!='自然流量'&&compaignname!='所有流量')
		{
			$("#no_camp").hide();
			$("#simple1").hide();
			$("#simple2").hide();
			$("#whole1").show();
			$("#whole2").show();
			//折线图
			$("#no_trend").hide();
			$("#simple_trend").hide();
			$("#whole_trend").show();
			//初始化表格
			$("#no_comp_grid").hide();
			$("#has_simple_grid").hide();
			$("#has_whole_grid").show();
			
			
			//刷新表格
			$("#whole_grid").jqGrid('setGridParam',{url : 'getReportGridBean.do',
			postData:{
				name:compaignname,
				channelIds:channelIds,
				creativeIds:creativeIds,
				startDate:start,
				endDate:end,
			},page:1}).trigger("reloadGrid");
			
			$.ajax({
				type : "POST",
				url : "getReportData.do",
				data : {
					name:compaignname,
					channelIds : channelIds,
					creativeIds:creativeIds,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var arrxAxis=[],cost=[],imp=[],impUser=[],clicks=[],clickUser=[],reach=[],reachUser=[],visit=[],visitUser=[],conv=[],convUser=[],clickRate=[],reachRate=[],conversionRate=[],cpl=[],cpa=[],cpm=[],cpc=[];	 
					var json = eval('(' + data + ')');
					if(json[1]==null)
					{
						InitWholeL(arrxAxis,cost,imp,impUser,clicks,clickUser,reach,reachUser,visit,visitUser,conv,convUser,clickRate,reachRate,conversionRate,cpm,cpc,cpl,cpa);
						$("#whole1_cost").text("-");
						$("#whole1_imp").text("-");
						$("#whole1_impUser").text("-");
						$("#whole1_click").text("-");
						$("#whole1_clickUser").text("-");
						$("#whole1_reach").text("-");
						$("#whole1_reachUser").text("-");
						$("#whole1_visit").text("-");
						$("#whole1_visitUser").text("-");
						
						$("#whole2_conv").text("-");
						$("#whole2_convUser").text("-");
						
						$("#whole2_clickRate").text("-");
						$("#whole2_reachRate").text("-");
						$("#whole2_convRate").text("-");
						$("#whole2_cpm").text("-");
						$("#whole2_cpc").text("-");							
						$("#whole2_cpl").text("-");
						$("#whole2_cpa").text("-");					
					}
					else
					{
						$("#whole1_cost").text(json[1].cost);
						$("#whole1_imp").text(json[1].imp);
						$("#whole1_impUser").text(json[1].impUser);
						$("#whole1_click").text(json[1].click);
						$("#whole1_clickUser").text(json[1].clickUser);
						$("#whole1_reach").text(json[1].reach);
						$("#whole1_reachUser").text(json[1].reachUser);
						$("#whole1_visit").text(json[1].visit);
						$("#whole1_visitUser").text(json[1].visitUser);
						
						$("#whole2_conv").text(json[1].convertion);
						$("#whole2_convUser").text(json[1].conversionUser);
						
						$("#whole2_clickRate").text(json[1].clickRate);
						$("#whole2_reachRate").text(json[1].reachRate);
						$("#whole2_convRate").text(json[1].convertionRate);
												
						$("#whole2_cpm").text(json[1].cpm);
						$("#whole2_cpc").text(json[1].cpc);
						
						$("#whole2_cpl").text(json[1].cpl);
						$("#whole2_cpa").text(json[1].cpa);
						
						 $.each(json[2], function() {
							  arrxAxis.push(this.day);
							  cost.push(this.cost);
							  imp.push(this.imp);
							  impUser.push(this.impUser);
							  clicks.push(this.click);
							  clickUser.push(this.clickUser);
							  
							  reach.push(this.reach);
							  reachUser.push(this.reachUser);
							 
							  visit.push(this.visit);
							  visitUser.push(this.visitUser);
							  
							  conv.push(this.convertion);
							  convUser.push(this.conversionUser);
							  if(this.clickRate){
								  var cR=this.clickRate.substring(0,this.clickRate.length-1);
							      clickRate.push(cR);
							  }
							   if(this.reachRate){
									var rR=this.reachRate.substring(0,this.reachRate.length-1);
							  		reachRate.push(rR);
							   }
							    if(this.conversionRate){
									var conR=this.conversionRate.substring(0,this.conversionRate.length-1);
							  		conversionRate.push(conR);
								}
							 cpm.push(this.cpm);
							  cpc.push(this.cpc);
							  cpl.push(this.cpl);
							  cpa.push(this.cpa);
								})
				InitWholeL(arrxAxis,cost,imp,impUser,clicks,clickUser,reach,reachUser,visit,visitUser,conv,convUser,clickRate,reachRate,conversionRate,cpm,cpc,cpl,cpa);
				}
				}
	})	
		}
	})
$("#trendexport").click(function(){
		var channel=$("#cannel option:selected");
		var creative=$("#creative option:selected");
		var channelIds="";//渠道id
		var creativeIds="";//创意id
		var compaignname=$("#trend_name option:selected").text();//活动名称
		$.each(channel, function() {
				channelIds+=this.id+'';
			});
		$.each(creative, function() {
				creativeIds+=this.id+'';
			});
		var start=$("#startDate").val();//开始时间
		var end=$("#endDate").val();//结束时间
		
		//初始化grid
		$("#trendexport").attr("href","exportExcel.do?name="
				+compaignname+"&channelIds="+channelIds+"&creativeIds="+creativeIds+"&startDate="+start+"&endDate="+end);
		 $("#trendexport")[0].click();
	});
});
function gridNoInit() {
	$("#no_grid").jqGrid({
				mtype : "POST",
				postData : {},
				datatype : "json",
				colNames : [ '日期', '浏览数', '浏览人数', '浏览IP数', '转化数','转化人数', '转化IP数'],
						colModel : [ {
							name : 'day',
							key : true,
							align : '',
							width:80,
							
						}, {
							name : 'visit',
							align:'right',
							width:80,
						
						}, {
							name : 'visitUser',
							align:'right',
							width:80,
							
						}, {
							name : 'visitIp',
							align:'right',
							width:80,
						
						}, {
							name : 'convertion',
							align:'right',
							width:64,
						
						}, {
							name : 'conversionUser',
							align:'right',
							width:64,
						
						}, {
							name : 'conversionIp',
							align:'right',
							width:64,
						
						},],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#no_pager',
				viewrecords : true,
				sortname : "day",
				sortorder : "desc",
				height: '100%',
				multiselect : false,
				autowidth : true,
				 footerrow : true,
				  userDataOnFooter : true,

			});
	jQuery("#no_grid").jqGrid('navGrid', '#no_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	$("#no_grid").setGridWidth(document.body.clientWidth*0.9);

	}

function gridSimpleInit() {
	$("#simple_grid").jqGrid({
				mtype : "POST",
				postData : {},
				datatype : "json",
				colNames : [ '日期', '花费','到达数', '到达人数', '到达IP数','浏览数', '浏览人数', '浏览IP数', '转化数','转化人数', '转化IP数','转化率','CPL','CPA'],
						colModel : [ {
							name : 'day',
							key : true,
							align : '',
							width:80,
							
						},
						{
							name : 'cost',
							align:'right',
							width:80,
						},
						 {
							name : 'reach',
							align:'right',
							width:80,
						}, {
							name : 'reachUser',
							align:'right',
							width:80,
						}, {
							name : 'reachIp',
							align:'right',
							width:80,
						},
						 {
							name : 'visit',
							align:'right',
							width:80,
						}, {
							name : 'visitUser',
							align:'right',
							width:80,
						}, {
							name : 'visitIp',
							align:'right',
							width:80,
						}, {
							name : 'convertion',
							align:'right',
							width:64,
						}, {
							name : 'conversionUser',
							align:'right',
							width:64,
						}, {
							name : 'conversionIp',
							align:'right',
							width:64,
						},
						 {
							name : 'convertionRate',
							align:'right',
							width:64,
						}, {
							name : 'cpl',
							align:'right',
							width:64,
						},
						{
							name : 'cpa',
							align:'right',
							width:64,
						},],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#simple_pager',
				viewrecords : true,
				sortname : "day",
				sortorder : "desc",
				height: '100%',
				multiselect : false,
				 footerrow : true,
				  userDataOnFooter : true,
			});
	jQuery("#simple_grid").jqGrid('navGrid', '#simple_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	$("#simple_grid").setGridWidth(document.body.clientWidth*0.9);
	}

function gridWholeInit() {
	$("#whole_grid").jqGrid({
				mtype : "POST",
				postData : {},
				datatype : "json",
				colNames : [ '日期', '花费','展示数', '展示人数', '点击数','点击人数', '到达数', '到达人数','浏览数', '浏览人数', '转化数','转化人数', '点击率','到达率','转化率','CPM','CPC','CPL','CPA'],
						colModel : [ {
							name : 'day',
							key : true,
							align : '',
							width:110,
						},
						{
							name : 'cost',
							align:'right',
							width:80,
						},
						 {
							name : 'imp',
							align:'right',
							width:80,
						}, {
							name : 'impUser',
							align:'right',
							width:80,
						}, {
							name : 'click',
							align:'right',
							width:80,
						},
						 {
							name : 'clickUser',
							align:'right',
							width:80,
						}, {
							name : 'reach',
							align:'right',
							width:80,
						}, {
							name : 'reachUser',
							align:'right',
							width:80,
						}, {
							name : 'visit',
							align:'right',
							width:64,
						}, {
							name : 'visitUser',
							align:'right',
							width:64,
						}, {
							name : 'convertion',
							align:'right',
							width:64,
						},
						 {
							name : 'conversionUser',
							align:'right',
							width:50,
						},
						{
							name : 'clickRate',
							align:'right',
							width:64,
						},
						{
							name : 'reachRate',
							align:'right',
							width:64,
						},
						{
							name : 'convertionRate',
							align:'right',
							width:64,
						}, {
							name : 'cpm',
							align:'right',
							width:64,
						},
						{
							name : 'cpc',
							align:'right',
							width:64,
						}, {
							name : 'cpl',
							align:'right',
							width:64,
						},
						{
							name : 'cpa',
							align:'right',
							width:64,
						},],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#whole_pager',
				viewrecords : true,
				sortname : "day",
				sortorder : "desc",
				height: '100%',
				multiselect : false,
				 footerrow : true,
				  userDataOnFooter : true,
			});
	jQuery("#whole_grid").jqGrid('navGrid', '#whole_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	$("#whole_grid").setGridWidth(document.body.clientWidth*0.9);}



function InitNoL(arrxAxis,visit,visitUser,visitIp,conv,convUser,convIp){
		require.config({
            packages: [
                {
                    name: 'echarts',
                    location: 'js/src',      
                    main: 'echarts'
                },
                {
                    name: 'zrender',
                    location: 'js/zrender/src',
                    main: 'zrender'
                }
            ]
        });
	      
        require(
            [
                'echarts',
				'echarts/theme/macarons',
                'echarts/chart/line', 
				
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('no_trend')); 
                 option = {
					 title : {
    },	
    tooltip : {
       trigger: 'axis',
	    /* formatter: function (params,ticket,callback) {
            var res =params[0].name;
            for (var i = 0, l = params.length; i < l; i++) {
                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
            }
            setTimeout(function (){
                // 仅为了模拟异步回调
                callback(ticket, res);
            }, 1)
            return 'loading';
        }*/
    },
    legend: {
		orient: 'horizontal', // 'vertical'
        x:'center', // 'center' | 'left' | {number},
        y: '20px', // 'center' | 'bottom' | {number}
        data:['浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'],
		  selected: {
            '浏览人数' : false,
			'浏览IP数' : false,
			'转化人数' : false,
			'转化IP数' : false,
        },
    },

    toolbox: {
         show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
  
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
			data:arrxAxis,//日期
			
        }
    ],
     yAxis : [
        {
            type : 'value',
            axisLabel : {
            }
        },
        {
            type : 'value',
            axisLabel : {
            }
        }
    ],
   
    series : [
        {
            name:'浏览数',
            type:'line',
			data:visit,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		 {
            name:'浏览人数',
            type:'line',
		   data:visitUser,//浏览人数
		    smooth:true,
		   itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'浏览IP数',
            type:'line',
		   data:visitIp,//浏览IP数
		    smooth:true,
		   itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'转化数',
            type:'line',
			yAxisIndex: 1,
			 smooth:true,
			data:conv,//转化数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'转化人数',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:convUser,//转化人数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
			
        },
		{
            name:'转化IP数',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:convIp,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
    ]
};
                // 为echarts对象加载数据 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
            }
        );}
		
function InitSimpleL(arrxAxis,cost,reach,reachUser,reachIp,visit,visitUser,visitIp,conv,convUser,convIp,conversionRate,cpl,cpa){
		require.config({
            packages: [
                {
                    name: 'echarts',
                    location: 'js/src',      
                    main: 'echarts'
                },
                {
                    name: 'zrender',
                    location: 'js/zrender/src',
                    main: 'zrender'
                }
            ]
        });
	      
        require(
            [
                'echarts',
				'echarts/theme/macarons',
                'echarts/chart/line', 
				
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('simple_trend')); 
                 option = {
					 title : {
    },	
    tooltip : {
       trigger: 'axis',
	    /* formatter: function (params,ticket,callback) {
            var res =params[0].name;
            for (var i = 0, l = params.length; i < l; i++) {
                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
            }
            setTimeout(function (){
                // 仅为了模拟异步回调
                callback(ticket, res);
            }, 1)
            return 'loading';
        }*/
    },
    legend: {
		orient: 'horizontal', // 'vertical'
        x:'center', // 'center' | 'left' | {number},
        y: '20px', // 'center' | 'bottom' | {number}
        data:['花费','到达数','到达人数','到达IP数','浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','转化率','CPL','CPA'],
		  selected: {
			  
            '花费' : false,
			'到达数' : false,
			'到达人数' : false,
			'到达IP数' : false,
			
            '浏览人数' : false,
			'浏览IP数' : false,
			'转化人数' : false,
			'转化IP数' : false,
			'转化率' : false,
			'CPL' : false,	
			'CPA' : false,		
        },
    },

    toolbox: {
         show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
  
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
			data:arrxAxis,//日期
			
        }
    ],
     yAxis : [
        {
            type : 'value',
            axisLabel : {
            }
        },
        {
            type : 'value',
            axisLabel : {
            }
        },
		 {
            type : 'value',
            axisLabel : {
            }
        }
    ],
   
    series : [
	{
            name:'花费',
            type:'line',
			data:cost,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'到达数',
            type:'line',
			data:reach,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'到达人数',
            type:'line',
			data:reachUser,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'到达IP数',
            type:'line',
			data:reachIp,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		 {
            name:'浏览数',
            type:'line',
			data:visit,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		 {
            name:'浏览人数',
            type:'line',
		   data:visitUser,//浏览人数
		    smooth:true,
		   itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'浏览IP数',
            type:'line',
		   data:visitIp,//浏览IP数
		    smooth:true,
		   itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'转化数',
            type:'line',
			 smooth:true,
			 yAxisIndex: 1,
			data:conv,//转化数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'转化人数',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:convUser,//转化人数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
			
        },
		{
            name:'转化IP数',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:convIp,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'转化率',
			smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:conversionRate,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'CPL',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:cpl,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'CPA',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:cpa,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
    ]
};
                // 为echarts对象加载数据 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
            }
        );}



function InitWholeL(arrxAxis,cost,imp,impUser,clicks,clickUser,reach,reachUser,visit,visitUser,conv,convUser,clickRate,reachRate,conversionRate,cpm,cpc,cpl,cpa){
		require.config({
            packages: [
                {
                    name: 'echarts',
                    location: 'js/src',      
                    main: 'echarts'
                },
                {
                    name: 'zrender',
                    location: 'js/zrender/src',
                    main: 'zrender'
                }
            ]
        });
	      
        require(
            [
                'echarts',
				'echarts/theme/macarons',
                'echarts/chart/line', 
				
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('whole_trend')); 
                 option = {
					 title : {
    },	
    tooltip : {
       trigger: 'axis',
	    /* formatter: function (params,ticket,callback) {
            var res =params[0].name;
            for (var i = 0, l = params.length; i < l; i++) {
                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
            }
            setTimeout(function (){
                // 仅为了模拟异步回调
                callback(ticket, res);
            }, 1)
            return 'loading';
        }*/
    },
    legend: {
		orient: 'horizontal', // 'vertical'
        x:'center', // 'center' | 'left' | {number},
        y: '20px', // 'center' | 'bottom' | {number}
        data:['展示数','展示人数','点击数','点击人数','到达数','到达人数','浏览数','浏览人数','转化数','转化人数',
		'点击率','到达率','转化率','CPM','CPC','CPL','CPA'],
		  selected: {
			'展示数' : false,
			'展示人数' : false,
			'点击数' : false,
			
			'点击人数' : false,
			'到达数' : false,
			'到达人数' : false,
			
			'浏览人数' : false,
			
			'转化人数' : false,
			'点击率' : false,
			'到达率' : false,
            '转化率' : false,
			'CPM' : false,
			'CPC' : false,
			'CPL' : false,
			'CPA' : false,
			
        },
    },

    toolbox: {
         show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
			data:arrxAxis,//日期
			
        }
    ],
     yAxis : [
        {
            type : 'value',
            axisLabel : {
            }
        },
        {
            type : 'value',
            axisLabel : {
            }
        }
    ],
   
    series : [
        {
            name:'展示数',
            type:'line',
			data:imp,//浏览数
			 smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		 {
            name:'展示人数',
            type:'line',
		   data:impUser,//浏览人数
		    smooth:true,
		   itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'点击数',
            type:'line',
		   data:clicks,//浏览IP数
		    smooth:true,
		   itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'点击人数',
            type:'line',
			 smooth:true,
			data:clickUser,//转化数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
        {
            name:'到达数',
			 smooth:true,
            type:'line',
			data:reach,//转化人数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
			
        },
		{
            name:'到达人数',
			 smooth:true,
            type:'line',
			//yAxisIndex: 1,
			data:reachUser,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'浏览数',
			 smooth:true,
            type:'line',
			//yAxisIndex: 1,
			data:visit,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'浏览人数',
			 smooth:true,
            type:'line',
			//yAxisIndex: 1,
			data:visitUser,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'转化数',
			 smooth:true,
            type:'line',
			//yAxisIndex: 1,
			data:conv,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'转化人数',
			 smooth:true,
            type:'line',
			//yAxisIndex: 1,
			data:convUser,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'点击率',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:clickRate,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'到达率',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:reachRate,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'转化率',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:conversionRate,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },{
            name:'CPM',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:cpm,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'CPC',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:cpc,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'CPL',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:cpl,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
		{
            name:'CPA',
			 smooth:true,
            type:'line',
			yAxisIndex: 1,
			data:cpa,//转化IP 数
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
        },
    ]
};
                // 为echarts对象加载数据 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
            }
        );
		}	
		
	
		
		