// JavaScript Document
$(function(){
	
	$(window).resize(function(){
		$("#log_grid").setGridWidth(document.body.clientWidth*0.9);
	});
	
	
	
	$("#uppage").click(function(){
		window.location.href = "activity.html";
	})
	
	var reg = new RegExp("(^|&)" + "id" + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	var lqid=unescape(r[2]);
	Init(lqid);
});
function Init(id)

{
	$("#log_grid").jqGrid(
			{
				mtype : "POST",
				url : 'getOperateLogData.do',
				height : '100%',
				datatype : "json",
				postData : {
					id:id,
				},
				colNames : ['ID', '更新时间', 'IP','XID', '操作类型'],
				colModel : [ {
					name : 'id',
					key : true,
					hidden : true,
				},
				{name:'operateTime',
					key:true,
					formatter : function(value, options, rData){
	            	var timestamp = "";
	            	if(!isNaN(value) &&value){
	            	timestamp = (new Date(parseFloat(value))).format("yyyy-MM-dd hh:mm:ss");
	            	}
	            	return timestamp;
	            	}}, {
					name : 'iP',
					sortable : false,
				},{
					name : 'userId',
					sortable : false,
				}, {
					name : 'operateType',
					height : '40',
					sortable : false,
				} ],
				
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#log_pager',
				viewrecords : true,//是否要显示总记录数
				scrollOffset:0,//滚动条的宽度，默认是18
				sortname : "operateTime",//默认按什么排序
				sortorder : "desc",//升序还是降序
				//height: 'auto' ,
				autowidth : true,//宽度自动
				multiselect : false,//不支持多选
				 subGrid: true,
				 // define the icons in subgrid
				 subGridOptions: {
    "plusicon": "ui-icon-triangle-1-e",
    "minusicon": "ui-icon-triangle-1-s",
    "openicon": "ui-icon-arrowreturn-1-e"
},
	subGridRowExpanded: function(subgrid_id,row_id) {
    var subgrid_table_id, pager_id;//定义下边表格的id和pager_id
    subgrid_table_id = subgrid_id + "_t";
    //pager_id = "p_" + subgrid_table_id;
    $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table>");
    jQuery("#" + subgrid_table_id).jqGrid({
        url:"getOperate.do?id=" + row_id,
	   	//datatype: "local",
		datatype: "json",
        colNames: ['变更项', '变更前', '变更后'],
        colModel: [{
            name: "changeItem",
            index: "changeItem",
            sortable : false,
            width:100,
            
        },
        {
            name: "changeBefore",
            index: "changeBefore",
            sortable : false,
		},
        {
            name: "changeAfter",
            index: "changeAfter",
            sortable : false,
        },
       ],
        rowNum: 20,
        //pager: pager_id,
        sortname: 'changeItem',
        sortorder: "asc",
        height: '100%',
        autowidth : true,//宽度自动
    });
   jQuery("#" + subgrid_table_id).jqGrid('navGrid', "#" + pager_id, {
        edit: false,
        add: false,
        del: false,
		search:false,
    });
	/* var mydatas = [ {changeItem:"花费",changeBefore:"2",changeAfter:"3"},{changeItem:"花费",changeBefore:"2",changeAfter:"3"},{changeItem:"花费",changeBefore:"2",changeAfter:"3"}];
	for ( var i = 0; i <= mydatas.length; i++){ 
	jQuery("#" + subgrid_table_id).jqGrid('addRowData', i + 1, mydatas[i]);
	}*/
}
});

	jQuery("#log_grid").jqGrid('navGrid', '#log_pager', {
		edit : false,
		add : false,
		del : false,
		search:false,
	});
	/* var mydata = [ {id : "192.168.0.11",operateTime : "4",iP : "test",userId:"2",operateType:'55'},{id : "192.168.0.11",operateTime : "3",iP : "test",userId:"2",operateType:'55'}, {id : "192.168.0.11",operateTime : "5",iP : "test",userId:"2",operateType:'55'}, {id : "192.168.0.11",operateTime : "4",iP : "test",userId:"2",operateType:'55'}, {id : "192.168.0.11",operateTime : "3",iP : "test",userId:"2",operateType:'55'}, {id : "192.168.0.11",operateTime : "4",iP : "test",userId:"2",operateType:'55'},{id : "192.168.0.11",operateTime : "7",iP : "test",userId:"2",operateType:'55'}, {id : "192.168.0.11",operateTime : "8",iP : "test",userId:"2",operateType:'55'}, {id : "192.168.0.11",operateTime : "3",iP : "test",userId:"2",operateType:'55'} ];
	  for ( var i = 0; i <= mydata.length; i++)
	  { 
	  jQuery("#log_grid").jqGrid('addRowData', i + 1, mydata[i]);
	  }*/
}

Date.prototype.format = function(format) {    
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), 
        "S" : this.getMilliseconds()  
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