// JavaScript Document
$(function(){
	
	$("#wordexp").jqGrid(
			{
				height : '100%',
				postData : {
					
				},
				datatype : "local",
				colNames : [ '名词', '含义',],
				colModel : [
				{
					name : 'name',
					align : 'center',
					width:50,
					sortable : false,
				},{
					name : 'explain',
					sortable : false,
					align:'',
				}, 
				 ],
				 caption : "报表名词解释",
				viewrecords : true,
				sortname : "name",
				sortorder : "desc",
				autowidth : true,
				multiselect : false,
			});
	var data=[{name:'展示数',explain:'用户访问某互联网页面，该页面上的广告展示数加1'},{name:'点击数',explain:'用户对网页上的广告感兴趣，点击该广告即产生一次点击行为，广告点击数加1'},{name:'到达数',explain:'用户点击页面广告后，会跟随网页链接跳转到广告主的活动页面，到达数加1'},{name:'转化数',explain:'广告主在到达的活动页面自定义转化行为，例如注册成功或者购买成功，当用户产生一次该行为后转化数加1'},{name:'点击率',explain:'点击数／展示数＊100%'},{name:'到达率',explain:'到达数／点击数＊100%'},{name:'转化率',explain:'转化数／到达数＊100%'},{name:'CPM',explain:'Cost Per Mille ，一千次广告展示的成本'},{name:'CPC',explain:'Cost Per Click ，一次广告点击的成本'},{name:'CPL',explain:'Cost Per Landing ，一次广告到达的成本'},{name:'CPA',explain:'Cost Per Action ，一次广告转化的成本'},{name:'到达地址',explain:'广告主进行一次广告活动的网页地址，即广告被点击后跳转到的地址'},{name:'花费',explain:'根据结算方式实时计算出来的广告投放费用'}]
for ( var i = 0; i <= data.length; i++){
	 jQuery("#wordexp").jqGrid('addRowData', i + 1, data[i]);
	  }
})
