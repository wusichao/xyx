// JavaScript Document
$(function() {
	$('.startDate').datepicker({
			changeMonth: true,
      		changeYear: true,
            dateFormat: "yy/mm/dd",
			/*minDate: -6,*/
			maxDate:0 ,
            onClose : function(dateText, inst) {  
            },
			onSelect:function(dateText, inst) {  
            },
        });
	var nowDate = new Date();
	var emonth=nowDate.getMonth()+1;
	var eday=nowDate.getDate();
	if(emonth<10){emonth='0'+emonth;}
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
				$("#activity_name").append("<option value=''>请选择活动</option>");
				$.each(json,
				function() {
					$("#activity_name").append("<option value='" + this.id + "'>" + this.name + "</option>");
					$("#activity_name").selectpicker('refresh');
				});
			}
		});
	//活动名称改变时加载渠道列表	
	$("#activity_name").change(function() {
		var name = $("#activity_name option:selected").text();
		//加载渠道名称
		$.ajax({
			type: "POST",
			url: "getChannelByCampaign.do",
			data: {
				name: name,
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
		//加载创意
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
						$("#creative").append("<option value=''>所有创意</option>");
						$.each(json, function() {
							$("#creative").append(
									"<option id='" + this.id + "' value='"
											+ this.id + "'>" + this.name
											+ "</option>");
							$('#creative').selectpicker('refresh');
						});
					}
				});
	});
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

	$('#activity_name').selectpicker({
		noneSelectedText: "请选择活动",
		noneResultsText: '没有找到匹配项',
	});
	$('#creative').selectpicker({
		noneSelectedText: "所有创意",
		noneResultsText: '没有找到匹配项',
	});
	//点击查询按钮			
	$("#search").click(function() {
	$(this).addClass("s");
	$(".mo").show();
	var campaignId = $("#activity_name option:selected").val();
	var channelId = $("#cannel option:selected").val();
	var date = $("#startDate").val();
	var creativeId = $("#creative option:selected").val();

	if (campaignId=="") {
		alert("请选择活动");
	} else {
		getInfo("同一用户",1,campaignId,channelId,creativeId,date)
		}
	});
	$("#ip").click(function(){
	$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds")
	var campaignId = $("#activity_name option:selected").val();
	var channelId = $("#cannel option:selected").val();
	var date = $("#startDate").val();
	var creativeId = $("#creative option:selected").val();

	if (campaignId=="") {
		alert("请选择活动");
	} else {
		getInfo("同一IP",0,campaignId,channelId,creativeId,date)
		}
	
	})
	$("#user").click(function(){
	$(this).addClass("obtn_selecteds").siblings().removeClass("obtn_selecteds")
	var campaignId = $("#activity_name option:selected").val();
	var channelId = $("#cannel option:selected").val();
	var date = $("#startDate").val();
	var creativeId = $("#creative option:selected").val();

	if (campaignId=="") {
		alert("请选择活动");
	} else {
		getInfo("同一用户",1,campaignId,channelId,creativeId,date)
		}
	
	})
});
InitBar("同一用户",[45,500,1234,2345,3454,4565,5675,6875,10332,19567],['10次','9次','8次','7次','6次','5次','4次','3次','2次','1次']);
function InitBar(title,impNum,impArea){
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
				text:'展示分析',
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
            data :impArea,
        }
    ],
			series : [
        {
            name:title,
            type:'bar',
           data:impNum,
		   barWidth : 25,
           itemStyle: {
               normal: {
                   color: function(params) {
                	   var colorList = [
                	                    '#FE8463','#9BCA63','#87cefa','#F3A43B','#60C0DD',
                                        '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0',
										 '#90a7d1','#90a7d1','#da70d6','#32cd32','#32cd32','#6495ed',
                                        '#FE8463','#9BCA63','#87cefa','#F3A43B','#60C0DD',
                                        '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0', 
										 '#90a7d1','#da70d6','#da70d6','#32cd32', '#6495ed',

                                      ];
                       return colorList[params.dataIndex];
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
function getInfo(title,who,campaignId,channelId,creativeId,date){
	
		$.ajax({
			type: "POST",
			url: "impInfo.do",
			data:{
				who:who,
				campaignId:campaignId,
				date:date,
				channelId:channelId,
				creativeId:creativeId,
			},
			dateType: "json",
			success: function(data) {
			var json = eval('(' + data + ')');
			var impNum=[],impArea=[];
			$.each(json, function() {
				impNum.push(this.num);
				impArea.push(this.impNum+'次');
			})
			InitBar(title,impNum,impArea);
			}
		});
	
}