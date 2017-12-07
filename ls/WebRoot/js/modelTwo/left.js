// JavaScript Document
$(function(){
	var lis=$("li").size();
	var arrCon=new Array();
	for(var i=0;i<lis;i++)
	{
		var content=$('span:eq('+i+')').text();
		arrCon.push(content);
	}
	$.ajax({
			type : "POST",
			url : "findFunctionsListByAccount.do",
			dateType : "json",
			success : function(data) {
				var json = eval('(' + data + ')');
					$.each(json,function() {
					for(var j=0;j<arrCon.length;j++)
					{
						if(this.name==arrCon[j])
						{
							$('li:eq('+j+')').show();
						}
					}
				});
				
			}
		});

	$(".left a").click(function(){
		$(this).parent().addClass("ac_on");
		$(this).parent().siblings().removeClass("ac_on");
	})
	$("#list").click(function(){
		var t=window.parent; 
		t.cha(); 
		$("#shku").show();
	})
$("#shku").click(function(){
		var t=window.parent; 
		t.nocha(); 
		$("#shku").hide();
	})
})
function recha(){
	$("#rech").addClass("ac_on");
	$("#rech").siblings().removeClass("ac_on");
}