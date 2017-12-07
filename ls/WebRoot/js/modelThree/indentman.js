$(function() {
	pageInit();
			$(window).resize(function(){
$("#indent_grid").setGridWidth(document.body.clientWidth*0.94);
});
	$("#adindent").click(function() {
		$("#user_name").empty();
		$("#pruduct_name").empty();
		$('.selectpicker').selectpicker('refresh');
		$("#err").text("");
		var nowDate = new Date();
		var month=nowDate.getMonth()+1;
		var day=nowDate.getDate();
		 if(month<10){
                month= '0'+month;
        }
		 if(day<10){
                day= '0'+day;
        }
       var timeStr = nowDate.getFullYear() + '/' + month + '/' + day;
		$("#endDate").val(timeStr);
		$("#startDate").val(timeStr);

	});
	//客户名称的联想输入
	/*$("#indent_name").autocomplete({
	minLength : 0,
	source : function(request, response) {
		$.ajax({
			url : "approvedCompanynameList.do",
			dataType : "json",
			data : {
				name : request.term
			},
			success : function(data) {
				response($.map(data, function(item) {
					return {
						label : item.id,
						value : item.name
					};
				}));
			}
		});
	},
	focus : function(event, ui) {
		$("#indent_name").val(ui.item.value);
		return false;
	},
	select : function(event, ui) {
		$("#indent_name").val(ui.item.value);
		return false;
	}
	}).data("ui-autocomplete")._renderItem = function(ul, item) {
	return $("<li>").append("<a>" + item.value + "</a>").appendTo(ul);
	};*/
	$('#user_name').selectpicker({
		noneSelectedText : "请选择客户名称",
	});
	$('#pruduct_name').selectpicker({
		noneSelectedText : "请选择增加的功能名称",
	});
	$('#indent_style').selectpicker({
		noneSelectedText : "请选择订单类型",
	});
	$.ajax({
		type : "POST",
		url : "/ls/findFunctionsList.do",
		dateType : "json",
		success : function(data) {
			var json = eval('(' + data + ')');
			$("#pruduct_name").empty();
			$.each(json, function() {
				if(this.name=='标准功能'){$("#biaozhun").val(this.name); $("#biaozhun").attr('title',this.id);}
				else{
				$("#pruduct_name").append(
						"<option value='" + this.id + "'>"
								+ this.name + "</option>");
				$('#pruduct_name').selectpicker('refresh');
				}
			});
		}
	});
	$("#searchButton").click(function() {
		var indentname = $("#indent_name").val();
		 var status = $("#status").val();
		$("#indent_grid").jqGrid('setGridParam', {
			url : '/ls/findAllOrder.do',
			postData : {
				name : indentname,
				status : status,
			},
			page : 1
		}).trigger("reloadGrid");

	});
	$("#user_name").next().children().first().click(
			function() {
				$.ajax({
					type : "POST",
					url : '/ls/approvedCompanynameList.do',
					dateType : "json",
					success : function(data) {
						var json = eval('(' + data + ')');
						$("#user_name").empty();
						$.each(json, function() {
							$("#user_name").append(
									"<option>" + this.company_name
											+ "</option>");
							$('#user_name').selectpicker('refresh');
						});
					}
				});
			});
	$("#pruduct_name").next().children().first().click(
			function() {
				$.ajax({
					type : "POST",
					url : "/ls/findFunctionsList.do",
					dateType : "json",
					success : function(data) {
						var json = eval('(' + data + ')');
						$("#pruduct_name").empty();
						$.each(json, function() {
							if(this.name=='标准功能'){$("#biaozhun").val(this.name); $("#biaozhun").attr('title',this.id);}
							else{
							$("#pruduct_name").append(
									"<option value='" + this.id + "'>"
											+ this.name + "</option>");
							$('#pruduct_name').selectpicker('refresh');
							}
						});
					}
				});
			});

	$("#addok").click(function() {
		var user_name = $("#user_name").val();
		var pruduct_name = $("#pruduct_name option:selected");
		var indent_style = $("#indent_style option:selected").text();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var functions = "";
		var functionser=$("#biaozhun").attr("title");
		$.each(pruduct_name, function() {
			functions += this.value + ',';
		});

		if (functions.length > 0) {
			
			functions = functions.substring(0, functions.length - 1);
			functionser+=',';
			functionser+=functions;
		}
		
		if (!user_name) {
			$("#err").text("请选择客户名称");
		} 
		else {

			$.ajax({
				type : "POST",
				url : "/ls/addOrder.do",
				data : {
					companyName : user_name,
					functions : functionser,
					begin_date : startDate,
					end_date : endDate,
					orderType : indent_style,
				},
				dateType : "json",
				success : function(data) {
					if (data == 'false') {
						$("#err").text("客户已有订单，不能重复添加！");
					} else {
						$(".close").trigger("click");
						$("#indent_grid").jqGrid('setGridParam', {
							url : '/ls/findAllOrder.do',
							postData : {},
							page : 1
						}).trigger("reloadGrid");
					}

				}
			});
		}

	});
	$("#reout").click(function() {
		$(".close").trigger("click");
	})
});

function pageInit() {
	 var status = $("#status").val();
	$("#indent_grid").jqGrid({
		mtype : "POST",
		url : '/ls/findAllOrder.do',
		postData : {status : status,},
		datatype : "json",
		colNames : [  '编号', '客户名称', '产品名称', '使用期限', '订单类型','生成时间' ],
		colModel : [ {
			name : 'orderNumber',
			key : true,
			sortable : false,
			align : 'left',
		}, {
			name : 'companyName',
			align : 'left',
			sortable : false,
		}, {
			name : 'functionsName',
			align : 'left',
			sortable : false,
		}, {
			name : 'serviceLife',
			sortable : false,
			align : 'left'
		}, {
			name : 'orderType',
			sortable : false,
			align : 'center'
		}, 
		 {name:'creation',formatter : function(value, options, rData){
         	var timestamp = "";
         	if(!isNaN(value) &&value){
         	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
         	}
         	return timestamp;
         	}},],
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : '#indent_pager',
		viewrecords : true,
		sortname : "creation",
		sortorder : "desc",
		caption : "订单管理",
		height : 'auto',
		autowidth : true,
		multiselect : false,
	});
	jQuery("#indent_grid").jqGrid('navGrid', '#indent_pager', {
		edit : false,
		add : false,
		del : false,
		search : false,
	});
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
