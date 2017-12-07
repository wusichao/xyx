// JavaScript Document
$(function() {
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
	//加载活动名称
	$.ajax({
			type: "POST",
			url: "getCampaignListByAccount.do",
			dateType: "json",
			success: function(data) {
				var json = eval('(' + data + ')');
				$("#activity_name").empty();
				$("#activity_name").append("<option value=''>所有活动</option>");
				$.each(json,
				function() {
					$("#activity_name").append("<option value='" + this.id + "'>" + this.name + "</option>");
					$('.selectpicker').selectpicker('refresh');
				});
			}
		});
	//活动名称改变时加载渠道列表	
	$("#activity_name").change(function() {
		var activityid = $("#activity_name option:selected").text();
		//加载渠道名称
		$.ajax({
			type: "POST",
			url: "getChannelByCampaign.do",
			data: {
				name: activityid,
			},
			dateType: "json",
			success: function(data) {
				var json = eval('(' + data + ')');
				$("#cannel").empty();
				$("#cannel").append("<option value=''>所有渠道</option>");
				$.each(json,
				function() {
					$("#cannel").append("<option value='" + this.id + "'>" + this.name + "</option>");
					$("#cannel").selectpicker('refresh');
				});
			}
		});
	});
		
	
		$(window).resize(function(){
$("#reach_grid").setGridWidth(document.body.clientWidth*0.9);
$("#visitNum_grid").setGridWidth(document.body.clientWidth*0.9);
});

	InitGird();
	InitnGird();
	//将时间设置为当前时间点
	var clickDate = new Date();
	var hours = clickDate.getHours();
	var count = $("#stime option").length;
	for (var i = 0; i < count; i++) {
		if ($("#stime ").get(0).options[i].text == hours) {
			$("#stime").get(0).options[i].selected = true;
			$("#etime").get(0).options[i].selected = true;
			break;
		}
	}

	//更多细分选择
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

	//选择框的提示信息
	$('#cannel').selectpicker({
		noneSelectedText: "所有渠道",
		noneResultsText: '没有找到匹配项',
	});

	$('#stime').selectpicker({
		//noneSelectedText: "请选择开始时间点",
	});
	$('#etime').selectpicker({
		//noneSelectedText: "请选择结束时间点",
	});
	$('#activity_name').selectpicker({
		noneSelectedText: "所有活动",
		noneResultsText: '没有找到匹配项',
	});
	
	//点击查询按钮			
	$("#search").click(function() {
		$(this).addClass("s");
	var name = $("#activity_name option:selected").val();
	var cannel = $("#cannel option:selected").val();
	var date = $("#startDate").val();
	var stime = $("#stime  option:selected").val();
	var etime = $("#etime  option:selected").val();

	if (parseInt(stime) > parseInt(etime)) {
		alert("开始时刻不能大于结束时刻");
	} else {
		//刷新表格
		$("#reach_grid").jqGrid('setGridParam', {
			url: 'visitLog.do',
			postData: {
				campaignId: name,
				channelId: cannel,
				date: date,
				startHour: stime,
				endHour: etime,
						},
			page: 1
			}).trigger("reloadGrid");
	}
	});
	//点击导出按钮
	$("#reachexport").click(function() {
		var activity = $("#activity_name option:selected").val(); //活动名称
		var date = $("#startDate").val(); //日期
		var startHour = $("#stime option:selected").val(); //开始时间
		var endHour = $("#etime option:selected").val(); //结束时间
		var channel = $("#cannel option:selected").val(); //渠道ID
		if (!activity) {
			activity = '';
		}
		if (!channel) {
			channel = '';
		}
		if (parseInt(startHour) > parseInt(endHour)) {
		alert("开始时刻不能大于结束时刻");
	}else{

					$("#reachexport").attr("href", "visitExport.do?date=" + date + "&startHour=" + startHour + "&endHour=" + endHour + "&campaignId=" + activity + "&channelId=" + channel);
					$("#reachexport")[0].click();
		}

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
function InitGird() {	
	$("#reach_grid").jqGrid({
		mtype: "POST",
		height: '100%',
		postData: {},
		datatype: "json",
		colNames: ['最新访问时间','城市','IP','用户ID','停留时长(当天)','访问次数(近7天)','当前URL','来源URL'],
		colModel: [
		
		{
			name: 'visitTime',
			key: true,
			width: 100,
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
			width: 30,
			align: 'left',
		},{
			name: 'ip',
			width: 80,
			align: 'left',

		},
		{
			name: 'userId',
			width: 70,
			align: 'left',
		},
		{
			name: 'stopTime',
			align: 'left',
			width: 60,
		},
		{
			name: 'visitNum',
			align: 'center',
			width: 60,
		},
		{
			name: 'nowUrl',
			align: 'left',
			width: 230,
		},
		{
			name: 'url',
			align: 'left',
			hidden:true,
			width: 200,
		},
		],
		rowNum: 10,
		rowList: [10, 20, 30],
		pager: '#reach_pager',
		viewrecords: true,
		sortname: "visitTime",
		sortorder: "desc",
		autoScroll: true,
		autowidth: true,
		multiselect: false,
		gridComplete: function() {
			var id;
			var ids = $("#reach_grid").jqGrid("getDataIDs");
			for (var i = 0; i < ids.length; i++) {
				var getRow = $('#reach_grid').getRowData(ids[i]); //获取当前的数据行
				id="'"+getRow.userId+"'";
				
				var visit=getRow.visitNum;
				var ip = "<a href='#' style='text-decoration:underline;' class='oncolor' onclick=getvisit("+id+")>"+visit+"</a>";
				$("#reach_grid").jqGrid('setRowData', ids[i], {
					visitNum: ip,
				});
			}
		}
	});
	jQuery("#reach_grid").jqGrid('navGrid', '#reach_pager', {
		edit: false,
		add: false,
		del: false,
		search: false
	});
	/*var mydata=[{visitTime:'1',city:'北京',ip:'129.123.123.344',userId:'sdwe221',stopTime:'00:00:00',visitNum:'232',nowUrl:'http://www.www',url:'sasda'},{visitTime:'1',city:'北京',ip:'129.123.123.344',userId:'sdwe221',stopTime:'00:00:00',visitNum:'232',nowUrl:'http://www.www',url:'sasda'}];
	 for ( var i = 0; i <= mydata.length; i++)
	  { 
	  jQuery("#reach_grid").jqGrid('addRowData', i + 1, mydata[i]);
	  }*/
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
			width: 140,
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
			width: 40,
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
			width: 80,
		},
		{
			name: 'campaignName',
			align: 'left',
			width: 110,
		},
		{
			name: 'creativeName',
			align: 'left',
			width: 100,
		},
		{
			name: 'nowUrl',
			align: 'left',
			width: 250,
		},
		{
			name: 'url',
			align: 'left',
			width: 150,
			//hidden:true,
		
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
    			InitBar(dates,datas);
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
    	});
}	
		
	

function InitBar(dates,datas){
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
		var myChart = ec.init(document.getElementById('bar'),'macarons'); 
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
	);
}	
//表格时间的格式化
Date.prototype.format = function(format) {
	/* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */
	var o = {
		"M+": this.getMonth() + 1,
		// month  
		"d+": this.getDate(),
		// day  
		"h+": this.getHours(),
		// hour  
		"m+": this.getMinutes(),
		// minute  
		"s+": this.getSeconds(),
		// second  
		"q+": Math.floor((this.getMonth() + 3) / 3),
		// quarter  
		"S": this.getMilliseconds()
		// millisecond  
	};

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}

	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};
