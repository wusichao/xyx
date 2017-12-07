// JavaScript Document
$(function(){
	$('.startDate').datepicker({
		changeMonth: true,
  		changeYear: true,
        dateFormat: "yy/mm/dd",
		minDate: -6,
		maxDate:0 ,
        onClose : function(dateText, inst) {  
        },
		onSelect:function(dateText, inst) {  
        },
    });
var nowDate = new Date();
var emonth=nowDate.getMonth()+1;
var eday=nowDate.getDate();
if(emonth<10){emonth= '0'+emonth;}
if(eday<10){eday= '0'+eday;}
	var timeEnd = nowDate.getFullYear() + '/' + emonth + '/' + eday;
$("#startDate").attr("value",timeEnd);
	$(window).resize(function(){
		$("#visitNum_grid").setGridWidth(document.body.clientWidth*0.9);
	});			

	InitBar([23,57,123,223,564,899,1008]);
	InitPage();								
	InitGird();
	InitnGird();
	var onOffs=true;
	$("#convmore").click(function(){
		var activity=$("#conv_name option:selected").val();//活动名称
		var date=$("#startDate").val();//日期
		var startHour=$("#stime option:selected").val();//开始时间点
		var endHour=$("#etime option:selected").val();//结束时间点
		var conversion=$("#conversion option:selected").val();//转化点ID
		var cannel=$("#cannel option:selected").val();//渠道ID
		var GridName=new Array();
		if(onOffs){
			$.ajax({
				type : "POST",
				url : "cvtHasDataList.do",
				data : {
					date:date,
					startHour:startHour,
					endHour:endHour,
					conversionId:conversion,
					campaignId:activity,
					channelId:cannel,
				},
				async: false,
				dateType : "json",
				success : function(data) {
				var json = eval('(' + data + ')');
				if(json.moneyp)
				{
					jQuery("#conv_grid").jqGrid('showCol', ["moneyp"]);
				}
				if(json.phonep)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "phonep"]);
				}
				if(json.emailp)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "emailp"]);
				}
				if(json.dizhip)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "dizhip"]);
				}
				if(json.companyNamep)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "companyNamep"]);
				}
				if(json.webSitep)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "webSitep"]);
				}
				if(json.peopleNamep)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "peopleNamep"]);
				}
				if(json.notep)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "notep"]);
				}
				if(json.sexp)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "sexp"]);
				}
				if(json.agep)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "agep"]);
				}
				if(json.cityp)
				{
					jQuery("#conv_grid").jqGrid('showCol', [ "cityp"]);
				}
				}
			});
			onOffs=false;
			$(this).text("隐藏")
		}
		else{
			jQuery("#conv_grid").jqGrid('hideCol', [ "moneyp", "phonep" ,"emailp","notep", "dizhip", "companyNamep", "webSitep" ,"peopleNamep", "sexp", "agep", "cityp"]);
			onOffs=true;
			$(this).text("扩展")
		}
	});
	
	//将时间设置为当前时间点
	var clickDate = new Date();
	var hours= clickDate.getHours();
	var count=$("#stime option").length;
	for(var i=0;i<count;i++)
	{ 
		if($("#stime").get(0).options[i].text == hours)
		{
			$("#stime").get(0).options[i].selected = true;
			$("#etime").get(0).options[i].selected = true;
			break;
		}
	}

	//更多细分选择
	var onOff=true;
	$("#show").click(function(){
		if(onOff){$(this).val("<<");
		onOff=false;}
		else{$(this).val(">>");
		onOff=true;}
		$("#more_btn").toggle();
	});	
	
	//选择框的提示信息
	 $('#cannel').selectpicker({
				noneSelectedText: "所有渠道",
				noneResultsText: '没有找到匹配项',
            });
	 $('#conversion').selectpicker({
				noneSelectedText: "所有转化点",
				
            });
	 $('#stime').selectpicker({
				//noneSelectedText: "请选择开始时间点",
            });	
	 $('#etime').selectpicker({
				//noneSelectedText: "请选择结束时间点",
            });	
	$('#conv_name').selectpicker({
		noneSelectedText: "所有活动",
		noneResultsText: '没有找到匹配项',
	});
			
	$.ajax({
		type: "POST",
		url: "getCampaignListByAccount.do",
		dateType: "json",
		success: function(data) {
			var json = eval('(' + data + ')');
			$("#conv_name").empty();
			$("#conv_name").append("<option value=''>所有活动</option>");
			$.each(json,
			function() {
				$("#conv_name").append("<option value='" + this.id + "'>" + this.name + "</option>");
				$('#conv_name').selectpicker('refresh');
			});
		}
	});

	//点击渠道时加载其内容
	$("#conv_name").change(function(){
	
	var activityid=	$("#conv_name option:selected").text();
	//$("#cannel").attr("disabled",false);
		//加载渠道名称
$.ajax({
				type : "POST",
				url : "getChannelByCampaign.do",
				data : {
					name : activityid,
				},
				dateType : "json",
				success : function(data) {
						var json = eval('(' + data + ')');
						$("#cannel").empty();
						$("#cannel").append("<option value=''>所有渠道</option>");
						$.each(json, function() {
					$("#cannel").append(
							"<option value='" + this.id + "'>" + this.name
									+ "</option>");
									$('.selectpicker').selectpicker('refresh');
				});
				}
		});
	})
	//点击查询按钮			
	$("#search").click(function(){
		var data1=new Array();
		var activity=$("#conv_name option:selected").val();//活动名称
		var date=$("#startDate").val();//日期
		var startHour=$("#stime option:selected").val();//开始时间点
		var endHour=$("#etime option:selected").val();//结束时间点
		var conversion=$("#conversion option:selected").val();//转化点ID
		var cannel=$("#cannel option:selected").val();//渠道ID
		 if(parseInt(startHour)>parseInt(endHour)){alert("开始时刻不能大于结束时刻");}
		
		else{
			$.ajax({
				type : "POST",
				url : "isConversionSoLong.do",
				data : {
					date:date,
					startHour:startHour,
					endHour:endHour,
					conversionId:conversion,
					campaignId:activity,
					channelId:cannel,
				},
				dateType : "json",
				success : function(data) {
					if(data=="false"){
						alert("您查询的数据已超过20000条，请做更精细的筛选！（如需查看这些数据，请与我们的运营团队联系）");
					}
					//刷新表格
					else{$("#conv_grid").jqGrid('setGridParam',{url : 'getConversion_Log.do',
						postData:{
							date:date,
							startHour:startHour,
							endHour:endHour,
							conversionId:conversion,
							campaignId:activity,
							channelId:cannel,
						},page:1}).trigger("reloadGrid");
						var ids = $("#conv_grid").jqGrid("getDataIDs");
//						for (var i=0; i<ids.length; i++) {
//							var getRow = $('#conv_grid').getRowData(ids[i]); //获取当前的数据行
//							var id="'"+getRow.userId+"'";
//							var visit=getRow.userId;
//							var ip = "<a href='#' style='text-decoration:underline;' class='oncolor' onclick=getvisit("+id+")>"+visit+"</a>";
//							$("#conv_grid").jqGrid('setRowData', ids[i], {userId: ip,});
//						}	
					}
					//柱状图加载数据
					$.ajax({
					type : "POST",
					url : "cvtHZ.do",
					data : {
					date:date,
					startHour:startHour,
					endHour:endHour,
					conversionId:conversion,
					campaignId:activity,
					channelId:cannel,
					},
					dateType : "json",
					success : function(data) {
						var json = eval('(' + data + ')');
						$.each(json, function() {
							data1.push(this.cvtNum);
						});	
					InitBar(data1);	
					}
					});
					}
		});
		
		}	});
	//点击导出按钮
	$("#convexport").click(function(){
		 var activity=$("#conv_name option:selected").val();//活动名称
		var date=$("#startDate").val();//日期
		var startHour=$("#stime option:selected").val();//开始时间点
		var endHour=$("#etime option:selected").val();//结束时间点
		var conversion=$("#conversion option:selected").val();//转化点ID
		 var channel=$("#cannel option:selected").val();//渠道ID
		if(!activity){activity='';}
		if(!channel){channel='';}
		$.ajax({
			type : "POST",
			url : "isConversionSoLong.do",
			data : {
				date:date,
				startHour:startHour,
				endHour:endHour,
				conversionId:conversion,
				campaignId:activity,
				channelId:channel,
			},
			async: false,
			dateType : "json",
			success : function(data) {
				if(data=="false"){
					
					alert("您查询的数据已超过20000条，请做更精细的筛选！（如需查看这些数据，请与我们的运营团队联系）");
				}
				else{	$("#convexport").attr("href","conversionExport.do?date="
						+date+"&startHour="+startHour+"&endHour="+endHour+"&conversionId="+conversion+"&campaignId="+activity+"&channelId="+channel);
				 $("#convexport")[0].click();
				}
				}
	});	
	});	
	//用户分析的导出
	$("#perexport").click(function() {
		var userId=$("#uId").val();//userId
		var activity = $("#activity_name option:selected").val(); //活动ID
		var date = $("#startDate").val(); //日期
		var channel = $("#cannel option:selected").val(); //渠道ID
		if (!activity) {
			activity = '';
		}
		if (!channel) {
			channel = '';
		}
		$("#perexport").attr("href", "visitInfoExport.do?date=" + date + "&campaignId=" + activity + "&channelId=" + channel + "&userId=" + userId);
		$("#perexport")[0].click();

	});	
	$("#uppage").click(function(){
		$("#rea_per").hide();
		$("#conv").show();
	})	
});


//初始化表格
function InitGird()
{
	
	
	$("#conv_grid").jqGrid(
			{
				mtype : "POST",
				height : '100%',
				postData : {	
				},
				
				datatype : "json",
				colNames : [ '时间', '城市','IP', '用户ID', '转化点', '到达时间','到转间隔','渠道','活动','创意','来源URL','参数一','参数二','参数三','参数四','参数五','参数六','参数七','参数八','参数九','参数十','参数十一'],
				colModel : [  {name:'conversionTime',
					key:true,
					width:140,
					formatter : function(value, options, rData){
	            	var timestamp = "";
	            	if(!isNaN(value) &&value){
	            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
	            	}
	            	return timestamp;
	            	}},  {
					name : 'city',
					width:50,
					align : 'center',
				},{
					name : 'ip',
					width:100,
					align:'',
					
				}, {
					name : 'userId',
					width:95,
					align:'',
				}, {
					name : 'conversionName',
					//hidden : true,
					width:80,
					align:'',
				}, {
					name:'clickTime',
					width:150,
				formatter : function(value, options, rData){
	            	var timestamp = "";
	            	if(!isNaN(value) &&value){
	            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
	            	}
	            	return timestamp;
	            	}},  {
					name : 'gap',
					align:'center',
					width:80,
				},
				 {
					name : 'channelName',
					align:'',
					width:60,
				}, 
				
				 {
					name : 'campaignName',
					width:100,
					align:'',
				},
				 {
					name : 'creativeName',
					width:60,
					align:'',
				},
				 {
					name : 'url',
					align:'',
					width:136,
				},
				 {
					name : 'phonep',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'peopleNamep',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'emailp',
					hidden:true,
					align:'',
				},
				 {
					name : 'notep',
					hidden:true,
					align:'',
				},
				 {
					name : 'sexp',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'agep',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'cityp',
					hidden:true,
					align:'',
				},
				 {
					name : 'dizhip',
					hidden:true,
					align:'',
				},
				 {
					name : 'moneyp',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'companyNamep',
					hidden:true,
					align:'',
				},
				 {
					name : 'webSitep',
					
					hidden:true,
					align:'',
				}
			
],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#conv_pager',
				/*gridComplete: function() {
				
				
				var ids = $("#conv_grid").jqGrid("getDataIDs");
			
				for (var i=0; i<ids.length; i++) {
				var getRow = $('#conv_grid').getRowData(ids[i]); //获取当前的数据行
				var id="'"+getRow.userId+"'";
				var visit=getRow.userId;
				var ip = "<a href='#' style='text-decoration:underline;' class='oncolor' onclick=getvisit("+id+")>"+visit+"</a>";
				
				$("#conv_grid").jqGrid('setRowData', ids[i], {
					userId: ip,
				});
			}
		},*/
				viewrecords : true,
				sortname : "conversionTime",
				sortorder : "desc",
				autoScroll: true,
				shrinkToFit:false, 
				autowidth : true,
				multiselect : false,	
			});
	jQuery("#conv_grid").jqGrid('navGrid', '#conv_pager', {
		edit : false,
		add : false,
		del : false,
		search:false,
		
		
	});
	
	
	
				
				
				
		
	
	
	
	
	
	
	
	
	
/*	var mydata=[{conversionTime:'1',city:'北京',ip:'129.123.123.344',userId:'sdwe221',conversionName:'购买',clickTime:'00:00:00',gap:'00:00:00',channelName:'232',campaignName:'http://www.www',creativeName:'sasda'},{conversionTime:'1',city:'北京',ip:'129.123.123.344',userId:'sdwe221',conversionName:'购买',clickTime:'00:00:00',gap:'00:00:00',channelName:'232',campaignName:'http://www.www',creativeName:'sasda'}];
	 for ( var i = 0; i <= mydata.length; i++)
	  { 
	  jQuery("#conv_grid").jqGrid('addRowData', i + 1, mydata[i]);
	  }*/
	  
	  
	  
  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
$( "#conv_grid" ).closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'scroll' });	
	$("#bttab_grid").jqGrid(
			{
				mtype : "POST",
				height : '300',
				postData : {
				},
				datatype : "json",
				colNames : [ '时间', '城市','IP', '用户ID', '转化点名称', '到达时间','到转间隔','渠道','来源URL','活动名称','创意名称','参数一','参数二','参数三','参数四','参数五','参数六','参数七','参数八','参数九','参数十','参数十一'],
			
				colModel : [  {name:'conversionTime',
					key:true,
					width:150,
					formatter : function(value, options, rData){
	            	var timestamp = "";
	            	if(!isNaN(value) &&value){
	            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
	            	}
	            	return timestamp;
	            	}},  {
					name : 'city',
					width:60,
					align : 'center',
				},{
					name : 'ip',
					width:105,
					align:'',
					
				}, {
					name : 'userId',
					width:105,
					align:'',
				}, {
					name : 'conversionName',
					//hidden : true,
					width:100,
					align:'',
				}, {
					name:'clickTime',
					width:150,
				formatter : function(value, options, rData){
	            	var timestamp = "";
	            	if(!isNaN(value) &&value){
	            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
	            	}
	            	return timestamp;
	            	}},  {
					name : 'gap',
					align:'center',
					width:80,
				},
				 {
					name : 'channelName',
					align:'',
					width:60,
				}, 
				 {
					name : 'url',
					align:'',
					width:90,
				},
				 {
					name : 'campaignName',
					width:120,
					align:'',
				},
				 {
					name : 'creativeName',
					width:90,
					align:'',
				},
				
				 {
					name : 'phonep',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'peopleNamep',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'emailp',
					hidden:true,
					align:'',
				},
				 {
					name : 'notep',
					hidden:true,
					align:'',
				},
				 {
					name : 'sexp',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'agep',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'cityp',
					hidden:true,
					align:'',
				},
				 {
					name : 'dizhip',
					hidden:true,
					align:'',
				},
				 {
					name : 'moneyp',
					hidden:true,
					align:'',
				}
				,
				 {
					name : 'companyNamep',
					hidden:true,
					align:'',
				},
				 {
					name : 'webSitep',
					
					hidden:true,
					align:'',
				}
],

				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#bttab_pager',
				viewrecords : true,
				width:'860',
				sortname : "conversionTime",
				sortorder : "desc",
				autoScroll: true,
				shrinkToFit:false, 
				multiselect : false,
			
			});
	jQuery("#bttab_grid").jqGrid('navGrid', '#bttab_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
$( "#bttab_grid" ).closest(".ui-jqgrid-bdiv").css({ 'overflow-y' : 'scroll' });	
$( "#bttab_grid" ).closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'scroll' });			
}

function InitPage(){
	//加载转化点
	$.ajax({
		type : "POST",
		url : "findAllInversionpoint.do",
		data : {},
		dateType : "json",
		success : function(data) {
			var json = eval('(' + data + ')');
			$("#conversion").empty();
			$("#conversion").append("<option value=''>所有转化点</option>");
			$.each(json, function() {
			$("#conversion").append("<option value='" + this.id + "'>" + this.name+"</option>");
			$('.selectpicker').selectpicker('refresh');
			});
		}
	});
}
//表格时间的格式化
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
        "q+" : Math.floor((this.getMonth() + 3) / 3), 
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
    return format;  }; 
	
//横向柱状图
function InitBar(data1){
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
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('bar'),'macarons'); 
option = {
    title : {
        text:"到转间隔分析",
        subtext: '点击柱状图可查看具体明细',
      
		x:'center',
    },
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
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
            type : 'value',
            boundaryGap : [0, 0.08]
        }
    ],
    yAxis : [
        {
            type : 'category',
            data :['7~15天','3~7天','1~3天','1小时~1天','5分钟~1小时','1~5分钟','0~1分钟'],
        }
    ],
    series : [
        {
            name:"转化量",
            type:'bar',
           data:data1,
		    barWidth : 25,
           itemStyle: {
               normal: {
                   color: function(params) {
                       // build a color map as your need.
                	   var colorList = [
                                        '#90a7d1','#da70d6','#32cd32','#6495ed',
                                         '#FE8463','#9BCA63','#87cefa','#F3A43B','#60C0DD',
                                         '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                                      ];

                       return colorList[params.dataIndex]
                   },
				}
			  }

        },
    ]
};
var ecConfig = require('echarts/config');
function eConsole(param) {
	
	var index=7-param.dataIndex;
	showtab(index);  
        }
                  // 为echarts对象加载数据 
                myChart.setOption(option); 
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
				window.onresize = myChart.resize;
               
            }
        );}
function showtab(cate){
		var activity=$("#conv_name option:selected").val();//活动名称
		var date=$("#startDate").val();//日期
		var startHour=$("#stime option:selected").val();//开始时间点
		var endHour=$("#etime option:selected").val();//结束时间点
		var conversion=$("#conversion option:selected").val();//转化点ID
		var cannel=$("#cannel option:selected").val();//渠道ID
		 if(parseInt(startHour)>parseInt(endHour)){alert("开始时刻不能大于结束时刻");}
		else{
			$.ajax({
				type : "POST",
				url : "isConversionSoLong.do",
				data : {
					cate:cate,
					date:date,
					startHour:startHour,
					endHour:endHour,
					conversionId:conversion,
					campaignId:activity,
					channelId:cannel,
				},
				dateType : "json",
				success : function(data) {
					if(data=="false"){
						alert("您查询的数据已超过20000条，请做更精细的筛选！（如需查看这些数据，请与我们的运营团队联系）");
					}
					//刷新表格
					else{$("#bttab_grid").jqGrid('setGridParam',{url : 'getConversion_Log.do',
						postData:{
							date:date,
							startHour:startHour,
							endHour:endHour,
							conversionId:conversion,
							campaignId:activity,
							channelId:cannel,
							gap:cate,
						},page:1}).trigger("reloadGrid");
					
					$.ajax({
			type : "POST",
			url : "cvtHasDataList.do",
			data : {
				date:date,
				startHour:startHour,
				endHour:endHour,
				conversionId:conversion,
				campaignId:activity,
				channelId:cannel,
	
			},
			async: false,
			dateType : "json",
			success : function(data) {
				var json = eval('(' + data + ')');
				if(json.moneyp)
				{
					jQuery("#bttab_grid").jqGrid('showCol', ["moneyp"]);
				}
				if(json.phonep)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "phonep"]);
				}
				if(json.emailp)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "emailp"]);
				}
				if(json.dizhip)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "dizhip"]);
				}
				if(json.companyNamep)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "companyNamep"]);
				}
				if(json.webSitep)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "webSitep"]);
				}
				if(json.peopleNamep)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "peopleNamep"]);
				}
				if(json.notep)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "notep"]);
				}
				if(json.sexp)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "sexp"]);
				}
				if(json.agep)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "agep"]);
				}
				if(json.cityp)
				{
					jQuery("#bttab_grid").jqGrid('showCol', [ "cityp"]);
				}
				}
			
	});
	$("#bartab").modal();}
					}
				
		});
		
		}	
}	
function InitnGird() {	
	$("#visitNum_grid").jqGrid({
		mtype: "POST",
		height: '100%',
		postData: {
			},
		datatype: "json",
		colNames: ['时间','城市','IP','用户ID','渠道','活动','创意','当前URL','来源URL'],
		colModel: [
		{
			name: 'visitTime',
			key: true,
			width: 170,
			formatter: function(value, options, rData) {
				var timestamp = "";
				if (!isNaN(value) && value) {
					timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
				}
				return timestamp;
			}
		},
		{
			name: 'city',
			width: 70,
			align: 'left',
		},
		{
			name: 'ip',
			width: 130,
			align: 'left',

		},
		{
			name: 'userId',
			width: 110,
			hidden:true,
			align: 'left',
		},
		{
			name: 'channelName',
			align: 'left',
			width: 110,
		},
		{
			name: 'campaignName',
			align: 'left',
			width: 110,
		},
		{
			name: 'creativeName',
			align: 'left',
			width: 90,
		},
		{
			name: 'nowUrl',
			align: 'left',
			width: 300,
		},
		{
			name: 'url',
			align: 'left',
			
		
		},
		],
		rowNum: 10,
		rowList: [10, 20, 30],
		pager: '#visitNum_pager',
		viewrecords: true,
		sortname: "visitTime",
		sortorder: "desc",
		autoScroll: true,
		autowidth: true,
		
		//caption : "访问次数明细表",
		
		multiselect: false,
	});
	jQuery("#visitNum_grid").jqGrid('navGrid', '#visitNum_pager', {
		edit: false,
		add: false,
		del: false,
		search: false
	});
	$("#visitNum_grid").setGridWidth(document.body.clientWidth*0.9);
}	
function getvisit(userId){
	$("#uId").val(userId);
	
	var campaignId = $("#activity_name").val();
	var channelId = $("#cannel").val();
	var date = $("#startDate").val();
	$("#rea_per").show();
	$("#conv").hide();
	
	$("#visitNum_grid").jqGrid('setGridParam', {
		url: 'visitInfo.do',
		postData: {
			campaignId: campaignId,
			channelId: channelId,
			date: date,
			userId:userId,
					},
		page: 1
		}).trigger("reloadGrid");
	
	
	$.ajax({
    		type : "post",
    		url : 'visitDateInfo.do',
    		data : {
    			userId:userId,
				date:date,
				campaignId:campaignId,
				channelId:channelId,
    		},
    		async : false,
    		success : function(data) {
    			var datas=new Array(),dates=new Array();
    			var json = eval('(' + data + ')');
				$.each(json, function() {
					var timestamp = "";
					if (!isNaN(this.visitTime) && this.visitTime) {
						timestamp = (new Date(parseFloat(this.visitTime))).format("yyyy-MM-dd");
					}
					
    			dates.push(timestamp);//日期
				datas.push(this.visitNum);//访问量
					})
    			InitLBar(dates,datas);
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
    	});
}
function InitLBar(dates,datas){
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
			'echarts/chart/bar',	
		],
	function (ec) {
		var myChart = ec.init(document.getElementById('bars'),'macarons'); 
		option = {
			title : {
				text:'访问次数明细',
				x:'center',
    				},
			tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
    },
			toolbox: {
        show : true,
        feature : {
            saveAsImage : {show: true}
        }
    },
			yAxis : [
        {
            type : 'value',
            boundaryGap : [0, 0.08]
        }
    ],
			xAxis : [
        {
            type : 'category',
            data :dates,
        }
    ],
			series : [
        {
            name:'访问次数',
            type:'bar',
			 barWidth : 25,
           data:datas,
		
           itemStyle: {
               normal: {
                   color: function(params) {
                	   var colorList = [
                                        '#90a7d1','#da70d6','#32cd32','#6495ed',
                                         '#FE8463','#9BCA63','#87cefa','#F3A43B','#60C0DD',
                                         '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                                      ];
                       return colorList[params.dataIndex]
                   },
				}
			  }
        },
    ]
		};      
	myChart.setOption(option);
	window.onresize = myChart.resize;  
	}
	);}	
