	$(function() {
		$(window).resize(function(){
$("#account_grid").setGridWidth(document.body.clientWidth*0.94);
});
		 pageInit();
	 isApproved ='true';
	 $("#changeButton").click(function(){
		 $("#changeButton").addClass("in");
		 $("#changeNoButton").removeClass("in");
		 $("#approved").hide();
		 $("#disapproved").hide();
			 isApproved ='false';
			$("#account_grid").jqGrid('setGridParam',{url : '/ls/approval.do',
				postData:{
					isApproved:isApproved,
				},page:1}).trigger("reloadGrid");
		});
	 $("#changeNoButton").click(function(){
		 		 $("#changeNoButton").addClass("in");
		 
		 $("#changeButton").removeClass("in");
		 $("#approved").show();
		 $("#disapproved").show();
			isApproved ='true';
			$("#account_grid").jqGrid('setGridParam',{url : '/ls/approval.do',
				postData:{
					isApproved:isApproved,
				},page:1}).trigger("reloadGrid");
		});
	 $("#searchButton").click(function(){
			var company_name = $("#company_name");
			$("#account_grid").jqGrid('setGridParam',{url : '/ls/approval.do',
				postData:{
					isApproved:isApproved,
					company_name:company_name.val(),
				},page:1}).trigger("reloadGrid");
		});
	 });
	
function pageInit(Approved) {
			$("#account_grid").jqGrid({
				mtype: "POST",
				url : '/ls/approval.do',
				height: 250,
				postData:{isApproved:'true',
					company_name:''
				},
				datatype : "json",
				colNames : ['编号','公司名称','行业', '公司网址', '营业执照', '联系人','联系方式','账户邮箱','审核状态', '更新时间','操作'],
				colModel : [{
					name : 'id',
					key:true,
					align : 'left',
					hidden :true,
					height:30
				},
				 {
					name : 'company_name',
					sortable : false,
					align : 'left'
						
				}, 
				 {
					name : 'vertical',
					sortable : false,
					align : 'left'
				}, {
					name : 'web_site',
					sortable : false,
					align : 'left'
				}, {
					name : 'license_path',
					sortable : false,
					align : 'center'
				}, {
					name : 'contact',
					sortable : false,
					align : 'left'
				}, {
					name : 'cellphone',
					sortable : false,
					align : 'left'
				},{
					name : 'email',
					sortable : false,
					align : 'left'
				}, 
				{
					name : 'status',
					sortable : false,
					align : 'left',
				}, 
				{name:'last_modified',formatter : function(value, options, rData){
            	var timestamp = "";
            	if(!isNaN(value) &&value){
            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
            	}
            	return timestamp;
            	}},{
					name : 'delete',
					align : 'center',
					sortable : false,
				},
				],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#account_pager',
				viewrecords : true,
				sortname:"last_modified",
				sortorder : "desc",
				caption : "审批列表",
				height:'auto',
				autowidth:true,
				multiselect : true,
			});
			jQuery("#account_grid").jqGrid('navGrid', '#account_pager', {
				edit : false,
				add : false,
				del : false,
				search: false,
			});
			 }
function approved(){
	var rowId=$("#account_grid").jqGrid('getGridParam','selarrrow');
	if (rowId.length==0) {
		alert("请选中节点");
		return
	} 
	var rowData = $("#account_grid").jqGrid('getRowData', rowId);
	$.ajax({
		type : "post",
		url : '/ls/approvalOP.do',
		data : {
			id : rowId.toString(),
			status:'已通过'
		},
		async : false,
		success : function(data) {
			$("#account_grid").trigger("reloadGrid");
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}
function disapproved(){
	var rowId=$("#account_grid").jqGrid('getGridParam','selarrrow');
	if (rowId.length==0) {
		alert("请选中节点");
		return
	} 
	$.ajax({
		type : "post",
		url : '/ls/approvalOP.do',
		data : {
			id : rowId.toString(),
			status:'未通过'
		},
		async : false,
		success : function(data) {
			$("#account_grid").trigger("reloadGrid");
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});}

	function creativeShow(a){
		var src=$(a).children().attr("src");
		var html="<img style='width:100%; height:100%;' src='";
		html+=src;
		html+="'/>";
		$("#im").html(html);
			};
			
			
			
			
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
    			$("#account_grid").trigger("reloadGrid");
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
    	});
    }else{
    }
}			
function deleteAccount(id) {
	if(confirm("确定删除吗")){
				$.ajax({
					type : "post",
					url : '/ls/deleteAccount.do',
					data : {
						id : id,
					},
					async : false,
					success : function(data) {
					$("#account_grid").trigger("reloadGrid");
					},
				error : function(XMLHttpRequest, textStatus, errorThrown) {}
					});}
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
