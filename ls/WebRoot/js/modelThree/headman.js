// JavaScript Document
$(function(){
	$("#codeexport").click(function(){
		$("#codeexport").attr("href","/ls/productInvitationCode.do");
		$("#codeexport")[0].click();
	});
		$.ajax({
			type:"POST",
			url:"/ls/approvedCompanynameList.do",
			dateType:"json",
			success: function(data) {
				var json = eval('(' + data + ')');
            var html="";
            for(var i = 0; i < json.length; i++)
            {
                html += '<option title='+json[i].id+'>' + json[i].company_name + '</option>';
            }
          $('#totalidentity').append(html) ;
        }
			});
})

function changeAccount(){
		var company= $("#totalidentity option:selected").text();
		var status=$("#totalidentity option:selected").attr("title");
		if(status==0)
		{
			if(confirm("您切换的账户无有效订单，是否继续切换？"))
			{
				$.ajax({
			type : "post",
			url : '/ls/cutAccount.do',
			data : {
				company_name : company,
			},
			async : false,
			success : function(data) {
				if(data=="true"){
					window.top.location.href = "/ls/frame.html";
				}else{ alert("切换失败");};
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			},});
			}
		}
		else
		{
		$.ajax({
			type : "post",
			url : '/ls/cutAccount.do',
			data : {
				company_name : company,
			},
			async : false,
			success : function(data) {
				if(data=="true"){
					window.top.location.href = "/ls/frame.html";
				}else{ alert("切换失败");};
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			},});
		}
	};