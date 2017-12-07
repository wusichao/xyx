// JavaScript Document
$(function(){
		$(window).resize(function(){
$("#rate_grid").setGridWidth(document.body.clientWidth*0.9);
});
	$('.startDate').datepicker({
			changeMonth: true,
      		changeYear: true,
            dateFormat: "yy/mm/dd",
			maxDate:-1 ,
            onClose : function(dateText, inst) {
             //  $( "#endDate" ).datepicker( "show" );
            },
			onSelect:function(dateText, inst) {
                $( "#endDate" ).datepicker( "option","minDate",dateText );
            },
        });
	$(".endDate").datepicker({
            dateFormat: "yy/mm/dd",
			changeMonth: true,
      		changeYear: true,
			 maxDate:-1 ,
            onClose : function(dateText, inst) {
                if (dateText < $("input[name=startDate]").val()){
				    alert("结束日期不能小于开始日期！");
					$("#endDate").val($("input[name=startDate]").val())
                }
            }
        });
	var nowDate = new Date();
	var emonth=nowDate.getMonth()+1;
	
	nowDate.setDate(nowDate.getDate()+parseInt(-1));
	var eday=nowDate.getDate();
	if(emonth<10){emonth= '0'+emonth;}
	if(eday<10){eday= '0'+eday;}
	
 	var timeEnd = nowDate.getFullYear() + '/' + emonth + '/' + eday;
		nowDate.setDate(nowDate.getDate()+parseInt(-7));
	var smonth=nowDate.getMonth()+1;
	var sday=nowDate.getDate();
	if(smonth<10){smonth= '0'+smonth;}
	if(sday<10){sday= '0'+sday;}
	var timeStr = nowDate.getFullYear() + '/' + smonth + '/' + sday;
	$("#startDate").attr("value",timeStr);
	$("#endDate").attr("value",timeEnd);

	PageInit();//初始化表格	
	$('#cannel').selectpicker({noneSelectedText: "请选择渠道",});//选择框的提示信息
	//活动名称的联想输入
	$( "#rate_name" ).autocomplete({
      minLength: 0,
      source:  function(request, response) {
                    $.ajax({
                        url: "getCampaignList.do",
                        dataType: "json",
                        data: {name: request.term},
                        success: function(data) {
                            response($.map(data, function(item) {
                                return { label: item.id, value: item.name };
                            }));
                        }
                    });
                },
      focus: function( event, ui ) {
        $( "#rate_name" ).val( ui.item.value );
        return false;
      },
      select: function( event, ui ) {
        $( "#rate_name" ).val( ui.item.value );
        return false;
      }
    })
    .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
      return $( "<li>" )
    .append( "<a>" + item.value +"</a>" )
    .appendTo( ul );
    };
	//活动编辑框的按键事件初始化渠道
	$("#cannel").next().children().first().click(function(){
		var name=$("#rate_name").val();
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
		$(this).addClass("s");
		var name=$("#rate_name").val();
		var cannel=$("#cannel option:selected").val();
		var sdate=$("#startDate").val();
		var edate=$("#endDate").val();
		if(name==''){alert("请输入活动名称");}
		else if(!cannel){alert("请选择要查询的渠道");}
		else{
			//刷新表格
			$("#rate_grid").jqGrid('setGridParam',{url :'rptImpClick.do',
			postData:{
				name:name,
				channelId:cannel,
				startDate:sdate,
				endDate:edate,
				},page:1}).trigger("reloadGrid");
			}	
	});
	$("#ratexport").click(function(){
		var name=$("#rate_name").val();
		var cannel=$("#cannel option:selected").val();
		var sdate=$("#startDate").val();
		var edate=$("#endDate").val();
		if(name==''){alert("请输入活动名称");}
		else if(!cannel){alert("请选择要查询的渠道");}
		else{
				$("#ratexport").attr("href","rptImpHZExport.do?name="+name+"&channelId="+cannel+"&startDate="+sdate+"&endDate="+edate);
				$("#ratexport")[0].click();
			}
	});
function PageInit(){
	$("#rate_grid").jqGrid({
				mtype : "POST",
				height : '100%',
				postData : {},
				datatype : "json",
				colNames : [ 'id','日期', '展示数','展示人数', '展示IP数', '展示数/人数','展示数/IP数','点击数','点击人数','点击IP数','点击数/人数','点击数/IP数','人群重合度','IP重合度','展示IP频次','展示用户频次'],
				colModel : [ 
				{
					name : 'id',
					hidden:true,
					
				},
				            {name:'day',formatter : function(value, options, rData){
				            	var timestamp = "";
				            	if(!isNaN(value) &&value){
				            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd");
				            	}
				            	return timestamp;
				            	}}, 
				{
					name : 'imp',
					align : 'right',
				},{
					name : 'impUserNum',
					align : 'right',
				}, {
					name : 'impIpNum',
					align : 'right',
				}, {
					name : 'impAveUser',
					align : 'center',
				}, {
					name : 'impAveIp',
					align : 'center',
				}, 
				 {
					name : 'click',
					align : 'right',
				},{
					name : 'clickUserNum',
					align : 'right',	
				},{
					name : 'clickIpNum',
					align : 'right',
				}, {
					name : 'clickAveUser',
					align : 'center',
				}, {
					name : 'clickAveIp',
					align : 'center',
				}, {
					name : 'userContactRatio',
					align : 'center',
				}, {
					name : 'ipContactRatio',
					align : 'center',
				},
				 {
					name : 'ippc',
					align : 'center',
				},
				 {
					name : 'yhpc',
					align : 'center',
				},
				 ],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#rate_pager',
				viewrecords : true,
				sortname : "day",
				sortorder : "desc",
				autowidth : true,
				multiselect : false,
				gridComplete: function() {
			var ids = $("#rate_grid").jqGrid("getDataIDs");
			for (var i = 0; i < ids.length; i++) {
				var getRow = $('#rate_grid').getRowData(ids[i]); //获取当前的数据行
				var id=getRow.id;

				var ip = "<a href='#' style='text-decoration:underline;' onclick='getip("+id+")'>查看</a>";
				var xid = "<a href='#'  style='text-decoration:underline;' onclick='getuserid("+id+")'>查看</a>";
				$("#rate_grid").jqGrid('setRowData', ids[i], {
					ippc: ip,
					yhpc: xid,
				});
			}
		}
			});
	jQuery("#rate_grid").jqGrid('navGrid','#rate_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
	}
Date.prototype.format = function(format) {  
  
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
    return format;  }; 
});
function getip(id){
	var datas=new Array();
	if($("#ippc").modal())
	{
		
		$.ajax({
    		type : "post",
    		url : 'rptImpHZ.do',
    		data : {
				who:1,
    			id : id,
    		},
    		async : false,
    		success : function(data) {
    			var json = eval('(' + data + ')');
    			datas.push(json.hundredsip);
				datas.push(json.fiftyAndHundredip);
				datas.push(json.elevenAndFiftyip);
				datas.push(json.sevenAndTenip);
				datas.push(json.fourAndSixip);
				datas.push(json.twoAndThreeip);
				datas.push(json.oneip);
    			InitBar(datas);
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
    	});
		
	}
	}
function getuserid(id){
	var datas=new Array();
	if($("#yhpc").modal())
	{
		
		$.ajax({
    		type : "post",
    		url : 'rptImpHZ.do',
    		data : {
				who:0,
    			id : id,
    		},
    		async : false,
    		success : function(data) {
    			var json = eval('(' + data + ')');
    			datas.push(json.hundredsuser);
				datas.push(json.fiftyAndHundreduser);
				datas.push(json.elevenAndFiftyuser);
				datas.push(json.sevenAndTenuser);
				datas.push(json.fourAndSixuser);
				datas.push(json.twoAndThreeuser);
				datas.push(json.oneuser);
    			InityhBar(datas);
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
    	});
	}
	}	
function InitBar(datas){
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
        text:'同IP展示频次',
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
            data :['100+次','51-100次','11-50次','7-10次','4-6次','2-3次','1次']
        }
    ],
    series : [
        {
            name:'IP频次',
            type:'bar',
           data:datas,
		   barWidth : 25,
		  // data:[122,33,334,555,66,55,55],
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
                  // 为echarts对象加载数据 
                myChart.setOption(option);
			//	myChart.on(ecConfig.EVENT.CLICK, eConsole);
				window.onresize = myChart.resize;
               
            }
        );
}	
	
function InityhBar(datas){
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
                var myChart = ec.init(document.getElementById('yhbar'),'macarons'); 
               
option = {
    title : {
        text:'同用户展示频次',
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
            data :['100+次','51-100次','11-50次','7-10次','4-6次','2-3次','1次']
        }
    ],
    series : [
        {
            name:'用户频次',
            type:'bar',
           data:datas,
		   barWidth : 25,
		   //data:[122,33,334,555,66,55,55],
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
                  // 为echarts对象加载数据 
                myChart.setOption(option);
			//	myChart.on(ecConfig.EVENT.CLICK, eConsole);
				window.onresize = myChart.resize;
               
            }
        );
}	
	
	
	