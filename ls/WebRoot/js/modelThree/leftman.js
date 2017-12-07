// JavaScript Document
$(function(){
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
	$("#funnel").addClass("ac_on");
	$("#funnel").siblings().removeClass("ac_on");
}