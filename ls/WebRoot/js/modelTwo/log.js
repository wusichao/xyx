// JavaScript Document
$(function(){

Init(3);
})
function Init(id)
{
	$("#log_grid").jqGrid(
			{
				mtype : "POST",
				url : 'getDimensionRank.do',
				height : 300,
				postData : {
					id : id,
				},
				datatype : "json",
				colNames : [ '更新时间', 'IP','XID', '操作类型', '变更项', '变更前内容', '变更后内容'],
				colModel : [ {
					name : 'date',
					align : '',
				}, {
					name : 'ip',
					align : '',
				},{
					name : 'xid',
					key : true,
					align:'',
				}, {
					name : 'car',
					height : '40',
					align:'right',
				}, {
					name : 'item',
					align:'right',
				}, {
					name : 'content',
					align:'right',
				}, {
					name : 'content2',

					align:'right',
				}, ],
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : '#log_pager',
				viewrecords : true,
				sortname : "date",
				sortorder : "desc",
				//height: 'auto' ,
				autowidth : true,
				multiselect : false,
			});
	jQuery("#log_grid").jqGrid('navGrid', '#log_pager', {
		edit : false,
		add : false,
		del : false,
		search:false
	});
}