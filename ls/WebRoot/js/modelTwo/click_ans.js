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
	
	$.ajax({
			type: "POST",
			url: "getCampaignListByAccount.do",
			dateType: "json",
			success: function(data) {
				var json = eval('(' + data + ')');
				$("#click_name").empty();
				$("#click_name").append("<option value=''>请选择活动</option>");
				$.each(json,
				function() {
					$("#click_name").append("<option value='" + this.id + "'>" + this.name + "</option>");
					$("#click_name").selectpicker('refresh');
				});
			}
		});
	
	
	
	
	
	
	var whoes="";
	//选中当前时间
	$(window).resize(function(){
		$("#clic_grid").setGridWidth(document.body.clientWidth*0.9);
	});
	var clickDate = new Date();
	var hours= clickDate.getHours();
	var count=$("#stime option").length;
	for(var i=0;i<count;i++)
	{ 
		if($("#stime ").get(0).options[i].text == hours)
		{
			$("#stime").get(0).options[i].selected = true;
			$("#etime").get(0).options[i].selected = true;
			break;
		}
	}
	//选择框的提示信息
	 $('#click_name').selectpicker({
				noneSelectedText: "请选择活动",
            });
	//选择框的提示信息
	 $('#cannel').selectpicker({
				noneSelectedText: "请选择渠道",
            });
	 $('#stime').selectpicker({
				noneSelectedText: "请选择开始时间点",
            });	
	 $('#etime').selectpicker({
				noneSelectedText: "请选择结束时间点",
            });	
			
	InitBar("点击分析","同一用户",['7次','6次','5次','4次','3次','2次','1次'],[33,157,223,323,564,1099,1800]);
	

	//活动名称的联想输入
/* $( "#click_name" ).autocomplete({
      minLength: 0,
      source:  function(request, response) {
                    $.ajax({
                        url: "getCampaignList.do",
                        dataType: "json",
                        data: {
                            name: request.term
                        },
                        success: function(data) {
                            response($.map(data, function(item) {
                                return { label: item.id, value: item.name };
                            }));
                        }
                    });
                },
      focus: function( event, ui ) {
        $( "#click_name" ).val( ui.item.value );
        return false;
      },
      select: function( event, ui ) {
        $( "#click_name" ).val( ui.item.value );
        return false;
      }
    })
    .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
      return $( "<li>" )
    .append( "<a>" + item.value +"</a>" )
    .appendTo( ul );
    };*/
PageInit();//初始化表格
//活动编辑框的按键事件初始化渠道

	 $( "#click_name" ).change(function(){
	var name=$("#click_name option:selected").text();
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
						$("#cannel").append("<option value=''>请选择渠道</option>");
						$.each(json, function() {
					$("#cannel").append(
							"<option value='" + this.id + "'>" + this.name
									+ "</option>");
									$("#cannel").selectpicker('refresh');
				});
				}
		});
});


$("#search").click(function(){
	whoes=0;
	$(this).addClass("s")
	$("#conaccount").addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	$("#more_btn").show();
	var title="同一用户点击分析";
	var subtit="同一用户";
	ajaxbar(title,subtit,0);
	
		
});


$("#conaccount").click(function(){
	$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var title="同一用户点击分析";
	var subtit="同一用户";
	ajaxbar(title,subtit,0);
	whoes=0;
});
$("#conip").click(function(){
	$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds");
	var title="同一IP点击分析";
	var subtit="同一IP";
	ajaxbar(title,subtit,1);
	whoes=1;
});
$("#clickexport").click(function(){
		var name=$("#click_name option:selected").text();//活动名称
		var nameid=$("#click_name option:selected").val();//活动名称
		var cannel=$("#cannel option:selected").val();
		var date=$("#startDate").val();
		var stime=$("#stime  option:selected").val();
		var etime=$("#etime  option:selected").val();
		if(nameid==''){alert("请输入活动名称");}
		else if(!cannel){alert("请选择要查询的渠道");}
		else{
			$.ajax({
				type : "POST",
				url : "isClickSoLong.do",
				data : {
					name:name,
					channelId:cannel,
					date:date,
					startHour:stime,
					endHour:etime,
				},
				async: false,
				dateType : "json",
				success : function(data) {
					if(data=="false"){
						
						alert("您查询的数据已超过20000条，请做更精细的筛选！（如需查看这些数据，请与我们的运营团队联系）");
					}
					else{	$("#clickexport").attr("href","clickExport.do?name="
							+name+"&channelId="+cannel+"&date="+date+"&startHour="+stime+"&endHour="+etime);
					 $("#clickexport")[0].click();
					}
					}
				
		});
		}
	});
$("#btchide").click(function(){
	$("#addcdata").toggle("slide", {direction : "right"}, 1000);
	})		

function PageInit()
{
	$("#clic_grid").jqGrid({
				mtype : "POST",
				height : '100%',
				postData : {
					
				},
				datatype : "json",
				colNames : [ '时间', '城市','IP', '用户ID', '当前URL', '创意名称',],
				colModel : [ 
				            {name:'clickTime',formatter : function(value, options, rData){
				            	var timestamp = "";
				            	if(!isNaN(value) &&value){
				            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
				            	}
				            	return timestamp;
				            	}}, 
				{
					name : 'city',
					align : '',
					
				},{
					name : 'ip',
					
					align:'',
				}, {
					name : 'userId',
					
				}, {
					name : 'url',
					width:200,
					
				}, {
					name : 'creativeName',
					
				}, 
				 ],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#clic_pager',
				viewrecords : true,
				sortname : "clickTime",
				sortorder : "desc",
				//height: 'auto' ,
				autowidth : true,
				multiselect : false,
				//scrollOffset:5,
			});
	jQuery("#clic_grid").jqGrid('navGrid','#clic_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	$("#btctab_grid").jqGrid({
				mtype : "POST",
				height : '250',
				postData : {	
				},
				datatype : "json",
				colNames : [ '时间', '城市','IP', '用户ID', '当前URL', '创意名称',],
				colModel : [ 
				            {name:'clickTime',width:150,
							formatter : function(value, options, rData){
				            	var timestamp = "";
				            	if(!isNaN(value) &&value){
				            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
				            	}
				            	return timestamp;
				            	}}, 
								
				{
					name : 'city',
					align : 'center',
					width:90,
					
				},{
					name : 'ip',
					width:120,
					align:'center',
				}, {
					name : 'userId',
					width:110,
					align:'center',
				}, {
					name : 'url',
					width:230,
					
				}, {
					name : 'creativeName',
					width:140,
					//align:'center',
				}, 
				 ],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#btctab_pager',
				viewrecords : true,
				sortorder : "desc",
				autoScroll: true,
				shrinkToFit:false, 
				autowidth : true,
				multiselect : false,
				sortname : "clickTime",
			});
	jQuery("#btctab_grid").jqGrid('navGrid','#btctab_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	$( "#btctab_grid" ).closest(".ui-jqgrid-bdiv").css({ 'overflow-y' : 'scroll' });
}
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
function ajaxbar(title,subtit,who){
	var data1=new Array();
	var data2=new Array();

		var name=$("#click_name option:selected").text();
		var nameid=$("#click_name option:selected").val();
		var cannel=$("#cannel option:selected").val();
		var date=$("#startDate").val();
		var stime=$("#stime  option:selected").val();
		var etime=$("#etime  option:selected").val();
		if(nameid==''){alert("请选择活动名称");}
		else if(!cannel){alert("请选择要查询的渠道");}
		else if(parseInt(stime)>parseInt(etime)){alert("开始时刻不能大于结束时刻");}
		else{
			
			$.ajax({
				type : "POST",
				url : "isClickSoLong.do",
				data : {
					name:name,
					channelId:cannel,
					date:date,
					startHour:stime,
					endHour:etime,
				},
				dateType : "json",
				success : function(data) {
					if(data=="false"){
						alert("您查询的数据已超过20000条，请做更精细的筛选！（如需查看这些数据，请与我们的运营团队联系）");
					}
					else{
						//刷新表格
						$("#clic_grid").jqGrid('setGridParam',{url :'getClick_Log.do',
						postData:{
							name:name,
							channelId:cannel,
							date:date,
							startHour:stime,
							endHour:etime,
							
						},page:1}).trigger("reloadGrid");
						//ajax请求显示柱状图
						$.ajax({
				type : "POST",
				url : "clickHZ.do",
				data : {
					who:who,
					name:name,
					channelId:cannel,
					date:date,
					startHour:stime,
					endHour:etime,
				},
				dateType : "json",
				success : function(data) {
						var json = eval('(' + data + ')');
						$.each(json, function() {
							data1.push(this.clickNum+'次');
							data2.push(this.useripNum);
						});	
				InitBar(title,subtit,data1,data2);
						
					}
		});
					
						}
					}
				
		});

		}

} 
//横向柱状图
function InitBar(tit,subtit,data1,data2){
	
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
        text:tit,
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
            data :data1
        }
    ],
    series : [
        {
            name:subtit,
            type:'bar',
           data:data2,
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
	
	showtab(param.name);  
        }
                     
                  // 为echarts对象加载数据 
                myChart.setOption(option);
				myChart.on(ecConfig.EVENT.CLICK, eConsole);
				window.onresize = myChart.resize;
               
            }
        );
}
function showtab(index){	

		var name=$("#click_name option:selected").text();
		var nameid=$("#click_name option:selected").val();
		var cannel=$("#cannel option:selected").val();
		var date=$("#startDate").val();
		var stime=$("#stime  option:selected").val();
		var etime=$("#etime  option:selected").val();
		if(nameid==''){alert("请选择活动名称");}
		/*else if(!cannel){alert("请选择要查询的渠道");}*/
		else if(parseInt(stime)>parseInt(etime)){alert("开始时刻不能大于结束时刻");}
		else{
			
			//$("#addcdata").toggle("slide", {direction : "right"}, 1000);
			$.ajax({
				type : "POST",
				url : "isClickSoLong.do",
				data : {
					name:name,
					channelId:cannel,
					date:date,
					startHour:stime,
					endHour:etime,
				},
				dateType : "json",
				success : function(data) {
					if(data=="false"){
						alert("您查询的数据已超过20000条，请做更精细的筛选！（如需查看这些数据，请与我们的运营团队联系）");
					}
					else{
						//刷新表格
						$("#btctab_grid").jqGrid('setGridParam',{url :'getClick_Log.do',
						postData:{
							who:whoes,
							clickNum:index.substring(0,index.length-1),
							name:name,
							channelId:cannel,
							date:date,
							startHour:stime,
							endHour:etime,
						},page:1}).trigger("reloadGrid");
						$("#cbartab").modal();
						}
					}
		});
		}}
});