// JavaScript Document

$(function(){
	$('#rank_name').selectpicker({
				noneSelectedText: "请选择活动",
				noneResultsText: '没有找到匹配项',
            });
	$("#qu").popover({trigger: 'hover',placement:'top'});
	$("#chuang").popover({trigger: 'hover',placement:'top'});				
	$(window).resize(function(){
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		$("#cityrank_grid").setGridWidth(document.body.clientWidth*0.9);
		$("#hourrank_grid").setGridWidth(document.body.clientWidth*0.9);
	});
	channelgridInit();
	creativegridInit();
	citygridInit();
	hourgridInit();
	weidu="";
	InitPie("转化流量分布",['渠道一','渠道二','渠道三','渠道四','渠道五'],[{value:996,name:'渠道一'},{value:456,name:'渠道二'},{value:301,name:'渠道三'},{value:223,name:'渠道四'},{value:158,name:'渠道五'}]);
	
	
		//加载活动列表		
	$.ajax({
				type : "POST",
				url : "getCampaignList.do",
				data : {},
				dateType : "json",
				success : function(data) {
						var json = eval('(' + data + ')');
						$("#rank_name").empty();
						
						$("#rank_name").append("<option value=''>自然流量</option>");
						$.each(json, function() {
				$("#rank_name").append("<option value="+this.monitor_type+">" + this.name + "</option>");
				$("#rank_name").selectpicker('refresh');
				});
				}
			});	
	
	
	
	//渠道按钮时默认显示渠道花费饼状图
	$("#rank_channel").click(function(){
		defaultClPie();
	})
	//渠道花费按钮
	$("#rank_ch_cost").click(function(){
		$("#rank_ch_cost").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");//小图标变色
		var name=$("#rank_name option:selected").text();
		if(name=="自然流量"){name=""}
		var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendChannel=new Array();
	var arrSeriesCost=new Array();
	if(name=="")
	{
		
		alert("请选择活动名称！");
	}
	else
	{
		if(style=='0')
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		
		$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="渠道花费分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesCost.push({value:this.cost,name:this.dimension});
				});
				
				arrSeriesCost.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesCost, function() {
						arrLegendChannel.push(this.name);
					})
				InitPie(title,arrLegendChannel,arrSeriesCost);
				},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
			});
	}
	})
	//渠道展示按钮
	$("#rank_ch_imp").click(function(){
		$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendImp=new Array();
	var arrSeriesImp=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
			if(style=='0')
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="渠道展示流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesImp.push({value:this.imp,name:this.dimension});
					
				});
						arrSeriesImp.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesImp, function() {
						arrLegendImp.push(this.name);
					})
				InitPie(title,arrLegendImp,arrSeriesImp);
				}
			});
			}
	});
	//渠道点击占比
	$("#rank_ch_click").click(function(){
		$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendClick=new Array();
	var arrSeriesClick=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
			if(style=='0')
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="渠道点击流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesClick.push({value:this.click,name:this.dimension});
					
				});
				
				 arrSeriesClick.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesClick, function() {
						arrLegendClick.push(this.name);
					})
				
				InitPie(title,arrLegendClick,arrSeriesClick);
				}
			});
			}
	})
	//渠道到达占比
	$("#rank_ch_reach").click(function(){
		$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendReach=new Array();
	var arrSeriesReach=new Array();
	if(name=="")
	{
		alert("请选择活动名称");
	}
	else
	{
			if(style=='0')
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="渠道到达流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesReach.push({value:this.reach,name:this.dimension});
					
				});
				 arrSeriesReach.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesReach, function() {
						arrLegendReach.push(this.name);
					})
				
				InitPie(title,arrLegendReach,arrSeriesReach);
				}
			});
			}
	})
	//渠道浏览占比
	$("#rank_ch_visit").click(function(){
		defaultClPie();
	/*
	$("#rank_ch_visit").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendVisit=new Array();
	var arrSeriesVisit=new Array();
	if(name=="")
	{
		alert("请选择活动名称");
	}
	else
	{
			if(style=='0')
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="渠道浏览流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesVisit.push({value:this.visit,name:this.dimension});
					
				});
				 arrSeriesVisit.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesVisit, function() {
						arrLegendVisit.push(this.name);
					})
				
				InitPie(title,arrLegendVisit,arrSeriesVisit);
				}
			});
			}
	*/})
	//渠道转化占比
	$("#rank_ch_conversion").click(function(){
	
	
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendConversion=new Array();
	var arrSeriesConversion=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
			if(style=='0'&&name!="")
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1'&&name!="")
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					$("#rank_ch").show().siblings().hide();//渠道五中排名的小图标
					$("#rank_ch_conversion").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
				
					var title="渠道转化流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesConversion.push({value:this.convertion,name:this.dimension});
					
				});
				
				arrSeriesConversion.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesConversion, function() {
						arrLegendConversion.push(this.name);
					})
				InitPie(title,arrLegendConversion,arrSeriesConversion);
				}
			});
			}
	
	})
	//点击创意按钮时默认显示创意花费饼状图
	$("#rank_cretive").click(function(){
		
		defaultCePie();
	})
	//创意花费饼状图
	$("#rank_cr_cost").click(function(){
	$("#rank_cr_cost").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");//小图标变色
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendCreative=new Array();
	var arrSeriesCost=new Array();
	if(name=="")
	{
		
		alert("请选择活动名称！");
	}
	else
	{
		if(style=='0')
		{//到访分析
		creativeF5(name,'创意' ,start,end);
		jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate" , "cpm","cpc"]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			creativeF5(name,'创意' ,start,end);
			jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'创意',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					
					
					
					var title="创意花费分布";
					var json = eval('(' + data + ')');
					$.each(json, function() {
					arrSeriesCost.push({value:this.cost,name:this.dimension});
					});
					arrSeriesCost.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesCost, function() {
						arrLegendCreative.push(this.name);
					})
					InitPie(title,arrLegendCreative,arrSeriesCost);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
			});
	}
	})
	//创意展示饼状图
	$("#rank_cr_imp").click(function(){
	$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendCeImp=new Array();
	var arrSeriessImp=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
		if(style=='0')
		{//到访分析
		creativeF5(name,'创意' ,start,end);
		jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate" , "cpm","cpc"]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			creativeF5(name,'创意' ,start,end);
			jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'创意',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					
					var title="创意展示流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriessImp.push({value:this.imp,name:this.dimension});
					
				});
				arrSeriessImp.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriessImp, function() {
						arrLegendCeImp.push(this.name);
					})
				InitPie(title,arrLegendCeImp,arrSeriessImp);
				}
			});
		}
	})
	//创意点击占比
	$("#rank_cr_click").click(function(){
		$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendCeClick=new Array();
	var arrSeriessClick=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
		if(style=='0')
		{//到访分析
		creativeF5(name,'创意' ,start,end);
		jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate" , "cpm","cpc"]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			creativeF5(name,'创意' ,start,end);
			jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'创意',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="创意点击流量分布";
					var json = eval('(' + data + ')');
					$.each(json, function() {
					arrSeriessClick.push({value:this.click,name:this.dimension});
				});
				arrSeriessClick.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriessClick, function() {
						arrLegendCeClick.push(this.name);
					})
				
				InitPie(title,arrLegendCeClick,arrSeriessClick);
				}
			});
	}
	})
	//创意到达占比
	$("#rank_cr_reach").click(function(){
		$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendCeReach=new Array();
	var arrSeriessReach=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
		if(style=='0')
		{
			//到访分析
		creativeF5(name,'创意' ,start,end);
		jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate" , "cpm","cpc"]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			creativeF5(name,'创意' ,start,end);
			jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'创意',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var title="创意到达流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriessReach.push({value:this.reach,name:this.dimension});
				});
				
				arrSeriessReach.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriessReach, function() {
						arrLegendCeReach.push(this.name);
					})
				InitPie(title,arrLegendCeReach,arrSeriessReach);
				}
			});
	}
	})
	//创意浏览占比
	$("#rank_cr_visit").click(function(){
		defaultCePie();
		})	
	//创意转化占比
	$("#rank_cr_conversion").click(function(){
	
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendCeCn=new Array();
	var arrSeriessConversion=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
		
		if(style=='0'&&name!="")
		{//到访分析
		creativeF5(name,'创意' ,start,end);
		jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate" , "cpm","cpc"]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1'&&name!="")
		{
			//全程分析
			creativeF5(name,'创意' ,start,end);
			jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'创意',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					$("#rank_cr_conversion").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
					var title="创意转化流量分布";
					//创意五中排名的小图标
						var json = eval('(' + data + ')');
						$.each(json, function() {
				
					arrSeriessConversion.push({value:this.convertion,name:this.dimension});
				});
				arrSeriessConversion.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriessConversion, function() {
						arrLegendCeCn.push(this.name);
					})
				InitPie(title,arrLegendCeCn,arrSeriessConversion);
				}
			});
	}

	
	})	

//点击地域按钮默认显示省份地图
	$("#rank_territory").click(function(){
		defaultMap();
	})
	//点击省份按钮
	$("#rank_pro").click(function(){
		defaultMap();
	})
	//点击城市按钮
	$("#rank_city").click(function(){
	$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
		$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'geo',
					name:name,
					startDate:start,
					endDate:end,
					level:'city',
				},
				dateType : "json",
				success : function(data) {
					var totalArr=[],totalLen=[],arrx=[],arrv=[],arrvu=[],arrvi=[],arrc=[],arrcu=[],arrci=[],arrr=[],arrru=[],arrri=[],arrcost=[],arrcRate=[],cpm=[],cpc=[],cpl=[],cpa=[],arri=[],arriu=[],arrcl=[],arrclu=[],arrclRate=[];arrrRate=[];
					var json = eval('(' + data + ')');
					
					if(name==""){
						totalLen=['浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'];
						var j=json.length;
						if(json.length>10){j=10;}
						for(var i=0;i<j;i++)
						{	
						//arrx.push(json[i].city);
						arrv.push({value:json[i].visit,name:json[i].city});
						arrvu.push({value:json[i].visitUser,name:json[i].city});
						arrvi.push({value:json[i].visitIp,name:json[i].city});
						arrc.push({value:json[i].convertion,name:json[i].city});
						arrcu.push({value:json[i].conversionUser,name:json[i].city});
						arrci.push({value:json[i].conversionIp,name:json[i].city});
						}
							 arrv.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrv, function() {
						arrx.push(this.name);
					})
					totalArr.push(arrv,arrvu,arrvi,arrc,arrcu,arrci);
					InitBar(arrx,totalLen,totalArr);
					}
					else if(name!=''&&style==0)
					{
						//到访分析
						
						totalLen=['花费','到达数','到达人数','到达IP数','浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数']
						var j=json.length;
						if(json.length>10){j=10;}
							
						for(var i=0;i<j;i++)
						{	
						//arrx.push(json[i].city);
						
						arrcost.push({value:json[i].cost,name:json[i].city});
						
						arrr.push({value:json[i].reach,name:json[i].city});
						arrru.push({value:json[i].reachUser,name:json[i].city});
						arrri.push({value:json[i].reachIp,name:json[i].city});
						
						arrv.push({value:json[i].visit,name:json[i].city});
						arrvu.push({value:json[i].visitUser,name:json[i].city});
						arrvi.push({value:json[i].visitIp,name:json[i].city});
						
						arrc.push({value:json[i].convertion,name:json[i].city});
						arrcu.push({value:json[i].conversionUser,name:json[i].city});
						arrci.push({value:json[i].conversionIp,name:json[i].city});
						
						/*if(json[i].convertionRate){
							var convertionRate=json[i].convertionRate.substring(0,json[i].convertionRate.length-1);
							arrcRate.push({value:convertionRate,name:json[i].city});
						}*/
						//cpl.push({value:json[i].cpl,name:json[i].city});
						//cpa.push({value:json[i].cpa,name:json[i].city});
						}
								 arrv.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrv, function() {
						arrx.push(this.name);
					})
		//'花费','到达数','到达人数','到达IP数','浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'
					totalArr.push(arrcost,arrr,arrru,arrri,arrv,arrvu,arrvi,arrc,arrcu,arrci);
					InitBar(arrx,totalLen,totalArr);
						
					
					}
					else if(name!=""&&style==1){
						//全程分析
						
						totalLen=['花费','展示数','点击数','到达数','浏览数','转化数','点击率','到达率','转化率','CPM','CPC','CPL','CPA']
						var j=json.length;
						if(json.length>10){j=10;}
							
						for(var i=0;i<j;i++)
						{	
						//arrx.push(json[i].city);
						
						arrcost.push({value:json[i].cost,name:json[i].city});
						
						arri.push({value:json[i].imp,name:json[i].city});
						//arriu.push({value:json[i].impUser,name:json[i].city});
						
						arrcl.push({value:json[i].click,name:json[i].city});
						//arrclu.push({value:json[i].clickUser,name:json[i].city});
						
						arrr.push({value:json[i].reach,name:json[i].city});
						//arrru.push({value:json[i].reachUser,name:json[i].city});
						
						
						arrv.push({value:json[i].visit,name:json[i].city});
						//arrvu.push({value:json[i].visitUser,name:json[i].city});
						
						
						arrc.push({value:json[i].convertion,name:json[i].city});
						//arrcu.push({value:json[i].conversionUser,name:json[i].city});
						var clickRate,reachRate,conversionRate;
						if(json[i].clickRate){
							 clickRate=json[i].clickRate.substring(0,json[i].clickRate.length-1);
						}
						arrclRate.push({value:clickRate,name:json[i].city});
						if(json[i].reachRate){
							 reachRate=json[i].reachRate.substring(0,json[i].reachRate.length-1);
						}
						arrrRate.push({value:reachRate,name:json[i].city});
						if(json[i].convertionRate){
							 conversionRate=json[i].convertionRate.substring(0,json[i].convertionRate.length-1);
						}
						arrcRate.push({value:conversionRate,name:json[i].city});
						cpm.push({value:json[i].cpm,name:json[i].city});
						cpc.push({value:json[i].cpc,name:json[i].city});
						cpl.push({value:json[i].cpl,name:json[i].city});
						cpa.push({value:json[i].cpa,name:json[i].city});
						}
								 arrv.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrv, function() {
						arrx.push(this.name);
					})
		//'花费','展示数','点击数','到达数','浏览数','转化数','点击率','到达率','转化率','CPM','CPC','CPL','CPA'
					totalArr.push(arrcost,arri,arrcl,arrr,arrv,arrc,arrclRate,arrrRate,arrcRate,cpm,cpc,cpl,cpa);
					InitBar(arrx,totalLen,totalArr);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
			});
	})
	
	//点击时段排序
	$("#rank_time").click(function(){	
		//hourgridInit();	
	$(this).addClass("obtn_selected").siblings().removeClass("obtn_selected");
	$("#lbtn span").hide();
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();

	if(name==""){
		//显示没有活动的几列并刷新表格
		hourF5(name,'时段' ,start,end);
		jQuery("#hourrank_grid").jqGrid('showCol', [ "dimension","visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp"]);
		jQuery("#hourrank_grid").jqGrid('hideCol', [ "cost","imp","impUser","click","clickUser", "reach" ,"reachUser", "reachIp", "clickRate", "reachRate" ,"convertionRate","cpm","cpc", "cpl","cpa"]);
		
		$("#hourrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
	else if(name!=''&&style==0){
		//到访分析
		hourF5(name,'时段' ,start,end);
		jQuery("#hourrank_grid").jqGrid('showCol', [ "dimension" ,"cost","reach" ,"reachUser", "reachIp","visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","convertionRate", "cpl","cpa"]);
		jQuery("#hourrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc",]);
		
		$("#hourrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
	else if(name!=''&&style==1){
		//全程分析
		hourF5(name,'时段' ,start,end);
		jQuery("#hourrank_grid").jqGrid('showCol', [ "dimension" ,"cost","imp","impUser","click","clickUser","reach" ,"reachUser","visit", "visitUser", "convertion" ,"conversionUser","clickRate", "reachRate", "convertionRate","cpm","cpc", "cpl","cpa"]);
		jQuery("#hourrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp"]);
		
		$("#hourrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}	

$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'时段',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					var totalArr=[],totalLen=[],arrx=[],arrv=[],arrvu=[],arrvi=[],arrc=[],arrcu=[],arrci=[],arrr=[],arrru=[],arrri=[],arrcost=[],arrcRate=[],cpm=[],cpc=[],cpl=[],cpa=[],arri=[],arriu=[],arrcl=[],arrclu=[],arrclRate=[];arrrRate=[];
					var json = eval('(' + data + ')');
					
					if(name==''){
						totalLen=['浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'];
						$.each(json, function() {	
						arrx.push(this.dimension||0);//时段
						arrv.push(this.visit||0);
						arrvu.push(this.visitUser||0);
						arrvi.push(this.visitIp||0);
						arrc.push(this.convertion||0);
						arrcu.push(this.conversionUser||0);
						arrci.push(this.conversionIp||0);
						});
						
					totalArr.push(arrv,arrvu,arrvi,arrc,arrcu,arrci);
					InitLine(arrx,totalLen,totalArr);
						
					}
					else if(name!=""&&style==0)
					{
						//到访分析
						
						totalLen=['花费','到达数','到达人数','到达IP数','浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','转化率','CPL','CPA'];
						$.each(json, function() {
					
						arrx.push(this.dimension||0);
						
						arrcost.push(this.cost||0);
						
						arrr.push(this.reach);
						arrru.push(this.reachUser||0);
						arrri.push(this.reachIp||0);
						
						arrv.push(this.visit||0);
						arrvu.push(this.visitUser||0);
						arrvi.push(this.visitIp||0);
						
						arrc.push(this.convertion||0);
						arrcu.push(this.conversionUser||0);
						arrci.push(this.conversionIp||0);
						var conversionRate;
						if(this.convertionRate){
							 conversionRate=this.convertionRate.substring(0,this.convertionRate.length-1);	
						}
						arrcRate.push(conversionRate||0);
						cpl.push(this.cpl);
						cpa.push(this.cpa);
						});
		
					totalArr.push(arrcost,arrr,arrru,arrri,arrv,arrvu,arrvi,arrc,arrcu,arrci,arrcRate,cpl,cpa);
					InitLine(arrx,totalLen,totalArr);
						
					
					}
					else if(name!=""&&style==1){
						//全程分析
						
						totalLen=['花费','展示数','点击数','到达数','浏览数','转化数','点击率','到达率','转化率','CPM','CPC','CPL','CPA'];
						$.each(json, function() {
						arrx.push(this.dimension);
						
						arrcost.push(this.cost||0);
						
						arri.push(this.imp||0);
						//arriu.push(this.impUser||0);
						
						arrcl.push(this.click||0);
						//arrclu.push(this.clickUser||0);
						
						arrr.push(this.reach||0);
						//arrru.push(this.reachUser||0);
						
						
						arrv.push(this.visit||0);
						//arrvu.push(this.visitUser||0);
						
						
						arrc.push(this.convertion||0);
						//arrcu.push(this.conversionUser||0);
						var clickRate,reachRate,conversionRate;
						if(this.clickRate){
							 clickRate=this.clickRate.substring(0,this.clickRate.length-1);
							
						}
						arrclRate.push(clickRate||0);
						if(this.reachRate){
							 reachRate=this.reachRate.substring(0,this.reachRate.length-1);
							
						}
						arrrRate.push(reachRate||0);
						if(this.convertionRate){
							 conversionRate=this.convertionRate.substring(0,this.convertionRate.length-1);
							
						}
						arrcRate.push(conversionRate||0);
						cpm.push(this.cpm);
						cpc.push(this.cpc);
						cpl.push(this.cpl);
						cpa.push(this.cpa);
						});
		
					totalArr.push(arrcost,arri,arrcl,arrr,arrv,arrc,arrclRate,arrrRate,arrcRate,cpm,cpc,cpl,cpa);
					InitLine(arrx,totalLen,totalArr);
					}
				}
			});
	});
	$("#rankexport").click(function(){
		var name=$("#rank_name option:selected").text();
		if(name=="自然流量"){name=""}
		var start=$("#startDate").val();//开始时间
		var end=$("#endDate").val();//结束时间
		//初始化grid
		$("#rankexport").attr("href","rankExport.do?name="
				+name+"&Dimension="+weidu+"&startDate="+start+"&endDate="+end);
		 $("#rankexport")[0].click();
	});

})

//饼状图初始化
function InitPie(tit,arrlegend,arrserise){
	$("#pie").show().siblings().hide();
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
  // 使用
        require(
            [
                'echarts',
                'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
				'echarts/chart/funnel'
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('pie')); 
               
            option = {
    title : {
        text:tit,
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 20,
		y:20,
        data:arrlegend,
    },
    toolbox: {
        show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    series : [
        {
            name:'分布占比',
            type:'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:arrserise,
            itemStyle:{ 
                normal:{ 
                    label:{ 
                       show: true, 
                       formatter: '{b} : {c} ({d}%)' 
                    }, 
                    labelLine :{show:true}
                } 
            } 
}
    ]
};
                    // 为echarts对象加载数据 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
               
            }
        );
}

//省份地图初始化
function Initmap(secost,maxs){
	$("#map").show().siblings().hide();
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
  // 使用
        require(
            [
                'echarts',
                'echarts/chart/map',
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('map')); 
option = {
    title : {
        text:'维度排名-省份',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
      formatter:function (params,ticket,callback) {
        if(params.data.value!="-"){
			var res=params.name+'<br/>'
			if(params.data.cost){
			res+='花费:';
			res+=params.data.cost+'元<br/>';
			}
			if(params.data.imp){
			res+='展示数:';
			res+=params.data.imp+'<br/>';
			}
			if(params.data.impUser)
			{
			res+='展示人数:';
			res+=params.data.impUser+'<br/>';
			}
			if(params.data.clicks)
			{
			res+='点击数:';
			res+=params.data.clicks+'<br/>';
			}
			if(params.data.clickUser)
			{
			res+='点击人数:';
			res+=params.data.clickUser+'<br/>';
			}
			if(params.data.reach)
			{			
			res+='到达数:';
			res+=params.data.reach+'<br/>';
			}
			if(params.data.reachUser)
			{			
			res+='到达人数:';
			res+=params.data.reachUser+'<br/>';
			}
			if(params.data.reachIp)
			{			
			res+='到达IP数:';
			res+=params.data.reachIp+'<br/>';
			}
			if(params.data.visit)
			{			
			res+='浏览数:';
			res+=params.data.visit+'<br/>';
			}
			if(params.data.visitUser)
			{			
			res+='浏览人数:';
			res+=params.data.visitUser+'<br/>';
			}
			if(params.data.visitIp)
			{			
			res+='浏览IP数:';
			res+=params.data.visitIp+'<br/>';
			}						
			if(params.data.convertion)
			{
			res+='转化:';
			res+=params.data.convertion+'<br/>';
			}
			
			
			if(params.data.conversionUser)
			{
			res+='转化人数:';
			res+=params.data.conversionUser+'<br/>';
			}
			if(params.data.conversionIp)
			{
			res+='转化IP数:';
			res+=params.data.conversionIp+'<br/>';
			}
			/*if(params.data.clickRate)
			{
			res+='点击率:';
			res+=params.data.clickRate+'%<br/>';
			}
			if(params.data.reachRate)
			{
			res+='到达率:';
			res+=params.data.reachRate+'%<br/>';
			}
			if(params.data.conversionRate)
			{
			res+='转化率:';
			res+=params.data.conversionRate+'%<br/>';
			}*/
			if(params.data.cpm)
			{
			res+='CPM:';
			res+=params.data.cpm+'元<br/>';
			}
			if(params.data.cpc)
			{
			res+='CPC:';
			res+=params.data.cpc+'元<br/>';
			}
			if(params.data.cpl)
			{
			res+='CPL:';
			res+=params.data.cpl+'元<br/>';
			}
			if(params.data.cpa)
			{
			res+='CPA:';
			res+=params.data.cpa+'元<br/>';
			}
           }
        else{ res=params.name}                
          setTimeout(function (){callback(ticket, res);}, 1);
          },
    },
    dataRange: {
        min: 0,
        max: maxs,
        x: 'left',
        y: 'bottom',
        text:['高','低'],           // 文本，默认为数值文本
        calculable : true
    },
    toolbox: {
        show: true,
        orient : 'vertical',
        x: 'left',
        y: 'top',
        feature : {
            saveAsImage : {show: true}
        }
    },
    roamController: {
        show: true,
        x: 'right',
        mapTypeControl: {
            'china': true
        }
    },
    series : [
			{
            name: '省份维度',
            type: 'map',
            mapType: 'china',
            itemStyle:{
                normal:{label:{show:true}},
                emphasis:{label:{show:true}}
            },
            data:secost,
        }
    ]
}; 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
            });
	}


//城市花费的柱状图
function InitBar(arrx,totalLen,totalArr){
	$("#bar").show().show().siblings().hide();
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
  // 使用
        require(
            [
                'echarts',
				'echarts/theme/macarons',
                'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
				'echarts/chart/line',
				
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('bar'),'macarons'); 
               
option = {
	title : {
        text:"维度排名-城市(Top10)",
        x:'center'
    },
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data:totalLen,
		
		x:'center',
		y:'30px',
    },
    toolbox: {
        show : true,
        x: 'right',
        y: 'top',
        feature : {
            saveAsImage : {show: true}
        }
    },
  
    xAxis : [
        {
            type : 'category',
            data :arrx,
        }
    ],
    yAxis : [
        {
            type : 'value'
        },
		 {
            type : 'value',
          
        }
    ],
	series:function(){
     var serie=[];
    for( var i=0;i<totalLen.length;i++){
		
		if(totalLen[i]=="点击率"||totalLen[i]=="到达率"||totalLen[i]=="转化率"||totalLen[i]=="CPM"||totalLen[i]=="CPC"||totalLen[i]=="CPL"||totalLen[i]=="CPA"){
			var item={
            name:totalLen[i],
			yAxisIndex: 1,
            type:'line',
			
			
            data:totalArr[i],
        }
		}
		else
		{
		
		
      var item={
            name:totalLen[i],
            type:'bar',
            data:totalArr[i],
        }
		}
      serie.push(item);
   }
   return serie;
}()
};  

var a=['浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'];

var b=['花费','到达数','到达人数','到达IP数','浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'];

var c=['花费','展示数','点击数','到达数','浏览数','转化数','点击率','到达率','转化率','CPM','CPC','CPL','CPA'];
if(totalLen.toString()==a.toString())
option.legend.selected = {
"浏览人数":false,
"浏览IP数":false,
"转化人数":false,
"转化IP数":false,};
else if(totalLen.toString()==b.toString())
option.legend.selected = {
"花费":false,
"到达数":false,
"到达人数":false,
"到达IP数":false,
"浏览人数":false,
"浏览IP数":false,
"转化人数":false,
"转化IP数":false,};
else if(totalLen.toString()==c.toString())
option.legend.selected = {
"花费":false,	
"展示数":false,
"点击数":false,
"到达数":false,

"点击率":false,
"到达率":false,
"转化率":false,
"CPM":false,
"CPC":false,
"CPL":false,
"CPA":false,
};

                  // 为echarts对象加载数据 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
               
            }
        );
		
}
		
//时间段花费的折线图	
function InitLine(arrx,totalLen,totalArr){	
		$("#line").show().siblings().hide();
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
  // 使用
        require(
            [
                'echarts',
				'echarts/theme/macarons',
                'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
				'echarts/chart/line',//需要加载line模块
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('line')); 
               
option = {
    title: {
        text: '24小时整体分布',
		x:'center'
		
    },
	 color: [
       '#ff6600','#b6a2de','#5ab1ef','#ffb980','#d87a80',
        '#2aff00','#e5cf0d','#97b552','#95706d','#dc69aa',
        '#fff000','#c1977b','#ff0900','#fb8627','#c05050',
        '#59678c','#c9ab00','#7eb00a','#6f5553','#c14089'
    ],
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:totalLen,
		x:'center',
		y:30,
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
    },
     toolbox: {
        show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: arrx,
    },
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
	series:function(){
     var serie=[];
    for( var i=0;i<totalLen.length;i++){
		if(totalLen[i]=="点击率"||totalLen[i]=="到达率"||totalLen[i]=="转化率"||totalLen[i]=="CPM"||totalLen[i]=="CPC"||totalLen[i]=="CPL"||totalLen[i]=="CPA"){
			var item={
            name:totalLen[i],
			yAxisIndex: 1,
            type:'line',
			smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
            data:totalArr[i],
        }
		}
		else{
      var item={
            name:totalLen[i],
            type:'line',
			smooth:true,
			itemStyle: {normal: {areaStyle: {type: 'default'}}},
            data:totalArr[i],
        }
		}
      serie.push(item);
   }
   return serie;
}()
};

var a=['浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数'];

var b=['花费','到达数','到达人数','到达IP数','浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','转化率','CPL','CPA'];

var c=['花费','展示数','点击数','到达数','浏览数','转化数','点击率','到达率','转化率','CPM','CPC','CPL','CPA'];
if(totalLen.toString()==a.toString())
option.legend.selected = {
"浏览人数":false,
"浏览IP数":false,
"转化人数":false,
"转化IP数":false,};
else if(totalLen.toString()==b.toString())
option.legend.selected = {
"花费":false,
"到达数":false,
"到达人数":false,
"到达IP数":false,
"浏览人数":false,
"浏览IP数":false,
"转化人数":false,
"转化IP数":false,
"转化率":false,
"CPL":false,
"CPA":false
};
else if(totalLen.toString()==c.toString())
option.legend.selected = {
"花费":false,	
"展示数":false,
"点击数":false,
"到达数":false,

"点击率":false,
"到达率":false,
"转化率":false,
"CPM":false,
"CPC":false,
"CPL":false,
"CPA":false};

                  // 为echarts对象加载数据 
                myChart.setOption(option); 
				window.onresize = myChart.resize;
               
            }
        );		
}
//点击渠道时默认显示的函数		
function defaultClPie(){
	
	
	
	$("#rank_channel").addClass("obtn_selected").siblings().removeClass("obtn_selected");//渠道按钮变色
	$("#rank_ch_visit").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendVisit=new Array();
	var arrSeriesVisit=new Array();
	if(name=="")
	{
		alert("请选择活动名称");
	}
	else
	{
			if(style=='0')
		{//到访分析
		channelF5(name,'渠道' ,start,end) ;
		jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc"]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			channelF5(name,'渠道' ,start,end) ;
			jQuery("#channelrank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#channelrank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'渠道',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					$("#rank_ch").show().siblings().hide();//渠道五中排名的小图标
					$("#rank_ch_visit").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
					var title="渠道浏览流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriesVisit.push({value:this.visit,name:this.dimension});
					
				});
				 arrSeriesVisit.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriesVisit, function() {
						arrLegendVisit.push(this.name);
					})
				
				InitPie(title,arrLegendVisit,arrSeriesVisit);
				}
			});
			}
	
	
	
	
	
	
	
	
	}		
//点击创意时默认显示的函数		
function defaultCePie(){
	$("#rank_cretive").addClass("obtn_selected").siblings().removeClass("obtn_selected");//创意按钮变色
	
	
	$("#rank_cr_visit").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrLegendCeVisit=new Array();
	var arrSeriessVisit=new Array();
	if(name=="")
	{
		alert("请选择活动名称！");
	}
	else
	{
	if(style=='0')
		{//到访分析
		creativeF5(name,'创意' ,start,end);
		jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" , "reach" ,"reachUser", "reachIp", "visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","conversionRate","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate" , "cpm","cpc"]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
		else if(style=='1')
		{
			//全程分析
			creativeF5(name,'创意' ,start,end);
			jQuery("#creativerank_grid").jqGrid('showCol', [ "dimension", "cost" ,"imp","impUser","click","clickUser", "reach" ,"reachUser", "visit", "visitUser","convertion" ,"conversionUser","clickRate", "reachRate", "conversionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#creativerank_grid").jqGrid('hideCol', [ "reachIp", "visitIp","conversionIp",]);
		
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'创意',
					name:name,
					startDate:start,
					endDate:end,
				},
				dateType : "json",
				success : function(data) {
					$("#rank_cr").show().siblings().hide();
					var title="创意浏览流量分布";
						var json = eval('(' + data + ')');
						$.each(json, function() {
					arrSeriessVisit.push({value:this.visit,name:this.dimension});
				});
				
				arrSeriessVisit.sort(function(a,b){
            				return b.value-a.value});
					$.each(arrSeriessVisit, function() {
						arrLegendCeVisit.push(this.name);
					})
				InitPie(title,arrLegendCeVisit,arrSeriessVisit);
				}
			});
	}
	

	}				
//点击地域是默认显示省份地图	
function defaultMap(){			
	$("#rank_territory").addClass("obtn_selected").siblings().removeClass("obtn_selected");//创意按钮变色
	var name=$("#rank_name option:selected").text();
	if(name=="自然流量"){name=""}
	var style=$("#rank_name option:selected").attr("value");
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var arrSeImp,arrSeClick,arrSeReach,arrSeVisit,arrSeCon,arrSe;
	if(name==""){
		//显示没有活动的几列并刷新表格
		cityF5(name,'geo',start,end);
		jQuery("#cityrank_grid").jqGrid('showCol', [ "dimension", "city" ,"visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp"]);
		jQuery("#cityrank_grid").jqGrid('hideCol', [ "cost","imp","impUser","click","clickUser", "reach" ,"reachUser", "reachIp", "clickRate", "reachRate" ,"convertionRate","cpm","cpc", "cpl","cpa"]);
		
		$("#cityrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
	else if(name!=''&&style==0){
		//到访分析
		cityF5(name,'geo',start,end);
		jQuery("#cityrank_grid").jqGrid('showCol', [ "dimension", "city" ,"cost","reach" ,"reachUser", "reachIp","visit", "visitUser", "visitIp", "convertion" ,"conversionUser", "conversionIp","convertionRate", "cpl","cpa"]);
		jQuery("#cityrank_grid").jqGrid('hideCol', [ "imp","impUser","click","clickUser","clickRate", "reachRate","cpm","cpc",]);
		
		$("#cityrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}
	else if(name!=''&&style==1){
		//全程分析
		cityF5(name,'geo',start,end);
		jQuery("#cityrank_grid").jqGrid('showCol', [ "dimension", "city" ,"cost","imp","impUser","click","clickUser","reach" ,"reachUser","visit", "visitUser", "convertion" ,"conversionUser","clickRate", "reachRate", "convertionRate","cpm","cpc","cpl","cpa"]);
		jQuery("#cityrank_grid").jqGrid('hideCol', [  "reachIp", "visitIp","conversionIp",]);
		
		$("#cityrank_grid").setGridWidth(document.body.clientWidth*0.9);
		}	
	//显示地图
	$.ajax({
				type : "POST",
				url : "getDimensionRank.do",
				data : {
					Dimension:'geo',
					name:name,
					startDate:start,
					endDate:end,
					level:'',
				},
				dateType : "json",
				success : function(data) {
					
					$("#rank_lo").show().siblings().hide();//创意五中排名的小图标
					$("#rank_pro").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");//小图标变色
					var  arri=new Array();var sumarri=new Array();
					
					var json = eval('(' + data + ')');
					if(json=='')
					{
					$("#rank_city").click();
					$("#rank_pro").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
					}
					else{
					$.each(json, function() {
						
						var imp,impUser,clicks,clickUser,reach,reachUser,reachIp,visit,visitUser,visitIp,convertion,conversionUser,conversionIp,cpl,cpa,cost,cpm,cpc/*,clickRate,reachRate,conversionRate*/;
						
						/*if(this.clickRate){
							clickRate=this.clickRate.substring(0,this.clickRate.length-1)
						}
						if(this.reachRate){
							reachRate=this.reachRate.substring(0,this.reachRate.length-1)
						}
						if(this.convertionRate){
							conversionRate=this.convertionRate.substring(0,this.convertionRate.length-1)
						}*/
						if(this.imp){imp=this.imp}
						if(this.impUser){impUser=this.impUser}
						if(this.click){clicks=this.click}
						if(this.clickUser){clickUser=this.clickUser}
						if(this.reach){reach=this.reach}
						if(this.reachUser){reachUser=this.reachUser}
						if(this.reachIp){reachIp=this.reachIp}
						if(this.visit){visit=this.visit}
						if(this.visitUser){visitUser=this.visitUser}
						if(this.visitIp){visitIp=this.visitIp}
						if(this.convertion){convertion=this.convertion}
						if(this.conversionUser){conversionUser=this.conversionUser}
						if(this.conversionIp){conversionIp=this.conversionIp}
						if(this.cost){cost=this.cost}
						if(this.cpm){cpm=this.cpm}
						if(this.cpc){cpc=this.cpc}
						if(this.cpl){cpl=this.cpl}
						if(this.cpa){cpa=this.cpa}
		
						arri.push({value:visit,name:this.province,imp:imp,impUser:impUser,clicks:clicks,clickUser:clickUser,reach:reach,reachUser:reachUser,reachIp:reachIp,visit:visit,visitUser:visitUser,visitIp:visitIp,convertion:convertion,conversionUser:conversionUser,conversionIp:conversionIp,cpm:cpm,cpc:cpc,cpl:cpl,cpa:cpa,cost:cost});
						
						sumarri.push({imp:this.imp,impUser:this.impUser,clicks:this.click,clickUser:this.clickUser,reach:this.reach,reachUser:this.reachUser,reachIp:this.reachIp,visit:this.visit,visitUser:this.visitUser,visitIp:this.visitIp,convertion:this.convertion,conversionUser:this.conversionUser,conversionIp:this.conversionIp,cost:this.cost});
						
					});
					
					sumarri.sort(function(a,b){return b.visit-a.visit});
					var sum=20000,i=0,iu=0,c=0,cu=0,r=0,ru=0,v=0,vu=0,con=0,conu=0,costu=0;
					if(sumarri[0].imp){i=sumarri[0].imp}
					if(sumarri[0].impUser){iu=sumarri[0].impUser}
					if(sumarri[0].clicks){c=sumarri[0].clicks}
					if(sumarri[0].clickUser){cu=sumarri[0].clickUser}
					if(sumarri[0].reach){r=sumarri[0].reach}
					if(sumarri[0].reachUser){ru=sumarri[0].reachUser}
					if(sumarri[0].visit){v=sumarri[0].visit}
					if(sumarri[0].visitUser){vu=sumarri[0].visitUser}
					if(sumarri[0].convertion){con=sumarri[0].convertion}
					if(sumarri[0].conversionUser){conu=sumarri[0].conversionUser}
					if(sumarri[0].cost){costu=sumarri[0].cost}
					//sum=parseInt(i)+parseInt(iu)+parseInt(c)+parseInt(cu)+parseInt(r)+parseInt(ru)+parseInt(v)+parseInt(vu)+parseInt(con)+parseInt(conu)+parseInt(costu);
					sum=sumarri[0].visit;
					Initmap(arri,sum);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
			});
	}	


function channelgridInit(name,Dimension ,startDate,endDate) {
	$("#channelrank_grid").jqGrid(
			{
				mtype : "POST",
				url : 'getDimensionRank.do',
				postData : {
					name : name,
					Dimension:Dimension,
						startDate:startDate,
						endDate:endDate,
				},
				datatype : "json",
				colNames : [ '渠道','花费(元)', '展示数', '展示人数', '点击数','点击人数', '到达数',
						'到达人数', '到达IP数', '浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','点击率','到达率','转化率','CPM','CPC','CPL','CPA'],
						colModel : [ {
							name : 'dimension',
							align : '',
							width:85,
						},
						{
							name : 'cost',
							key : true,
							width:85,
							align:'right',
						}, {
							name : 'imp',
							width:85,
							align:'right',
						}, {
							name : 'impUser',
							align:'right',
							width:85,
						}, {
							name : 'click',
							align:'right',
							width:66,
						},  {
							name : 'clickUser',
							align:'right',
							width:66,
						}, {
							name : 'reach',
							align:'right',
							width:66,
						}, {
							name : 'reachUser',
							align:'right',
							width:66,
						}, {
							name : 'reachIp',
							align:'right',
							width:66,
						}, {
							name : 'visit',
							align:'right',
							width:66,
						},
						 {
							name : 'visitUser',
							align:'right',
							width:62,
						},
						 {
							name : 'visitIp',
							align:'right',
							width:62,
						},
						 {
							name : 'convertion',
							align:'right',
							width:62,
						},
						 {
							name : 'conversionUser',
							align:'right',
							width:62,
						},
						 {
							name : 'conversionIp',
							align:'right',
							width:62,
						},
						 {
							name : 'clickRate',
							align:'right',
							width:62,
						},
						 {
							name : 'reachRate',
							align:'right',
							width:62,
						},
						 {
							name : 'convertionRate',
							align:'right',
							width:62,
						},{
							name : 'cpm',
							align:'right',
							width:62,
						},
						 {
							name : 'cpc',
							align:'right',
							width:62,
						},
						 {
							name : 'cpl',
							align:'right',
							width:62,
						},
						 {
							name : 'cpa',
							align:'right',
							width:62,
						}],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#channelrank_pager',
				viewrecords : true,
				sortname : "visit",
				sortorder : "desc",
				height: '100%',
				multiselect : false,
				autowidth : true,
				 footerrow : true,
				  userDataOnFooter : true,
			});
	jQuery("#channelrank_grid").jqGrid('navGrid', '#channelrank_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
  $("#channelrank_grid").setGridWidth(document.body.clientWidth*0.9);
}

function channelF5(name,Dimension,start,end){
	$("#channel_grid").show();
	$("#creative_grid").hide();
	$("#city_grid").hide();
	$("#hour_grid").hide();
	weidu=Dimension;
	$("#channelrank_grid").jqGrid('setGridParam',{url : 'getDimensionRank.do',
		postData:{
			name:name,
			Dimension:Dimension,
			startDate:start,
			endDate:end,
			level:'city',
		},page:1}).trigger("reloadGrid");
}

//创意表格
function creativegridInit(name,Dimension ,startDate,endDate) {
	$("#creativerank_grid").jqGrid(
			{
				mtype : "POST",
				url : 'getDimensionRank.do',
				postData : {
					name : name,
					Dimension:Dimension,
						startDate:startDate,
						endDate:endDate,
				},
				datatype : "json",
				colNames : [ '创意','花费(元)', '展示数', '展示人数', '点击数','点击人数', '到达数',
						'到达人数', '到达IP数', '浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','点击率','到达率','转化率','CPM','CPC','CPL','CPA'],
						colModel : [ {
							name : 'dimension',
							align : '',
							width:85,
						},
						{
							name : 'cost',
							key : true,
							width:85,
							align:'right',
						}, {
							name : 'imp',
							width:85,
							align:'right',
						}, {
							name : 'impUser',
							align:'right',
							width:85,
						}, {
							name : 'click',
							align:'right',
							width:66,
						},  {
							name : 'clickUser',
							align:'right',
							width:66,
						}, {
							name : 'reach',
							align:'right',
							width:66,
						}, {
							name : 'reachUser',
							align:'right',
							width:66,
						}, {
							name : 'reachIp',
							align:'right',
							width:66,
						}, {
							name : 'visit',
							align:'right',
							width:66,
						},
						 {
							name : 'visitUser',
							align:'right',
							width:62,
						},
						 {
							name : 'visitIp',
							align:'right',
							width:62,
						},
						 {
							name : 'convertion',
							align:'right',
							width:62,
						},
						 {
							name : 'conversionUser',
							align:'right',
							width:62,
						},
						 {
							name : 'conversionIp',
							align:'right',
							width:62,
						},
						 {
							name : 'clickRate',
							align:'right',
							width:62,
						},
						 {
							name : 'reachRate',
							align:'right',
							width:62,
						},
						 {
							name : 'convertionRate',
							align:'right',
							width:62,
						}, {
							name : 'cpm',
							align:'right',
							width:62,
						},
						 {
							name : 'cpc',
							align:'right',
							width:62,
						},
						 {
							name : 'cpl',
							align:'right',
							width:62,
						},
						 {
							name : 'cpa',
							align:'right',
							width:62,
						}],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#creativerank_pager',
				viewrecords : true,
				sortname : "visit",
				sortorder : "desc",
				height: 'auto' ,
				
				multiselect : false,
				autowidth : true,
				 footerrow : true,
				  userDataOnFooter : true,
			});
	jQuery("#creativerank_grid").jqGrid('navGrid', '#creativerank_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
		$("#creativerank_grid").setGridWidth(document.body.clientWidth*0.9);
		
}

function creativeF5(name,Dimension,start,end){
	$("#creative_grid").show();
	$("#channel_grid").hide();
	$("#city_grid").hide();
	$("#hour_grid").hide();
	weidu=Dimension;
	$("#creativerank_grid").jqGrid('setGridParam',{url : 'getDimensionRank.do',
		postData:{
			name:name,
			Dimension:Dimension,
			startDate:start,
			endDate:end,
			level:'city',
		},page:1}).trigger("reloadGrid");
}


//地域表格

function citygridInit(name,Dimension ,startDate,endDate) {
	$("#cityrank_grid").jqGrid(
			{
				mtype : "POST",
				url : 'getDimensionRank.do',
				postData : {
					name : name,
					Dimension:Dimension,
						startDate:startDate,
						endDate:endDate,
				},
				datatype : "json",
				colNames : [ '省份','城市','花费(元)', '展示数', '展示人数', '点击数','点击人数', '到达数',
						'到达人数', '到达IP数', '浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','点击率','到达率','转化率','CPM','CPC','CPL','CPA' ],
						colModel : [ {
							name : 'dimension',
							align : '',
							width:'80',
							hidden:true,
						},
						{
							name : 'city',
							align : '',
							width:'80',
							hidden:true,
						},

						{
							name : 'cost',
							key : true,
							align:'right',
							width:'80',
							hidden:true,
						}, {
							name : 'imp',
							hidden:true,
							align:'right',
							width:'80',
						},
						 {
							name : 'impUser',
							hidden:true,
							align:'right',
							width:'80',
						}, {
							name : 'click',
							align:'right',
							width:'70',
							hidden:true,
						},
						{
							name : 'clickUser',
							align:'right',
							width:'70',
							hidden:true,
						},
						{
							name : 'reach',
							align:'right',
							width:'70',
							hidden:true,
						},
						 {
							name : 'reachUser',
							align:'right',
							width:'70',
							hidden:true,
						}, {
							name : 'reachIp',
							align:'right',
							width:'70',
							hidden:true,
						}, {
							name : 'visit',
							align:'right',
							width:'60',
							hidden:true,
						},
						 {
							name : 'visitUser',
							align:'right',
							width:'60',
							hidden:true,
						},
						 {
							name : 'visitIp',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'convertion',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'conversionUser',
							align:'right',
							width:'60',
							hidden:true,
						},
						{
							name : 'conversionIp',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'clickRate',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'reachRate',
							hidden:true,
							align:'right',
							width:'60',
						}, {
							name : 'convertionRate',
							align:'right',
							width:'60',
							hidden:true,
						},{
							name : 'cpm',
							hidden:true,
							align:'right',
							width:'59',
						},
						 {
							hidden:true,
							name : 'cpc',
							align:'right',
							width:'59',
						},
						
						 {
							name : 'cpl',
							hidden:true,
							align:'right',
							width:'59',
						},
						 {
							hidden:true,
							name : 'cpa',
							align:'right',
							width:'59',
						}],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#cityrank_pager',
				viewrecords : true,
				sortname : "visit",
				sortorder : "desc",
				height: 'auto' ,
				
				multiselect : false,
				autowidth : true,
				 footerrow : true,
				  userDataOnFooter : true,
			});
	jQuery("#cityrank_grid").jqGrid('navGrid', '#cityrank_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});

	$("#cityrank_grid").setGridWidth(document.body.clientWidth*0.9);
	
}

function cityF5(name,Dimension,start,end){
	$("#city_grid").show();
	$("#channel_grid").hide();
	$("#creative_grid").hide();
	$("#hour_grid").hide();
	weidu=Dimension;
	$("#cityrank_grid").jqGrid('setGridParam',{url : 'getDimensionRank.do',
		postData:{
			name:name,
			Dimension:Dimension,
			startDate:start,
			endDate:end,
			level:'city',
		},page:1}).trigger("reloadGrid");
}


//时段

function hourgridInit() {
	$("#hourrank_grid").jqGrid(
			{
				mtype : "POST",
				postData : {},
				datatype : "json",
				colNames : [ '时段','花费(元)', '展示数', '展示人数', '点击数','点击人数', '到达数',
						'到达人数', '到达IP数', '浏览数','浏览人数','浏览IP数','转化数','转化人数','转化IP数','点击率','到达率','转化率','CPM','CPC','CPL','CPA' ],
				colModel : [
						{
							name : 'dimension',
							align : '',
							width:'80',
							hidden:true,
							
						},
						{
							name : 'cost',
							key : true,
							align:'right',
							width:'80',
							hidden:true,
						}, {
							name : 'imp',
							hidden:true,
							align:'right',
							width:'80',
						},
						 {
							name : 'impUser',
							hidden:true,
							align:'right',
							width:'80',
						}, {
							name : 'click',
							align:'right',
							width:'70',
							hidden:true,
						},
						{
							name : 'clickUser',
							align:'right',
							width:'70',
							hidden:true,
						},
						{
							name : 'reach',
							align:'right',
							width:'70',
							hidden:true,
						},
						 {
							name : 'reachUser',
							align:'right',
							width:'70',
							hidden:true,
						}, {
							name : 'reachIp',
							align:'right',
							width:'70',
							hidden:true,
						}, {
							name : 'visit',
							align:'right',
							width:'60',
							hidden:true,
						},
						 {
							name : 'visitUser',
							align:'right',
							width:'60',
							hidden:true,
						},
						 {
							name : 'visitIp',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'convertion',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'conversionUser',
							align:'right',
							width:'60',
							hidden:true,
						},
						{
							name : 'conversionIp',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'clickRate',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'reachRate',
							hidden:true,
							align:'right',
							width:'60',
						}, {
							name : 'convertionRate',
							align:'right',
							width:'60',
							hidden:true,
						}, {
							name : 'cpm',
							hidden:true,
							align:'right',
							width:'59',
						},
						 {
							hidden:true,
							name : 'cpc',
							align:'right',
							width:'59',
						},
						
						 {
							name : 'cpl',
							hidden:true,
							align:'right',
							width:'59',
						},
						 {
							hidden:true,
							name : 'cpa',
							align:'right',
							width:'59',
						}],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#hourrank_pager',
				viewrecords : true,
				sortname : "hour",
				sortorder : "desc",
				height: '100%' ,
				multiselect : false,
				autowidth : true,
				 footerrow : true,
				  userDataOnFooter : true,
			});
	jQuery("#hourrank_grid").jqGrid('navGrid', '#hourrank_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	$("#hourrank_grid").setGridWidth(document.body.clientWidth*0.9);
}

function hourF5(name,Dimension,start,end){
	$("#hour_grid").show();
	$("#channel_grid").hide();
	$("#city_grid").hide();
	$("#creative_grid").hide();
	weidu=Dimension;
	$("#hourrank_grid").jqGrid('setGridParam',{url : 'getDimensionRank.do',
		postData:{
			name:name,
			Dimension:Dimension,
			startDate:start,
			endDate:end,
			level:'city',
		},page:1}).trigger("reloadGrid");
}
