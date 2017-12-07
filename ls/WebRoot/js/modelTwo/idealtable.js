// JavaScript Document
$(function(){
	var oTab=document.getElementById('tab');
	var oTbody=oTab.tBodies[0];
	
	$("#wscbtncancel").click(function(){
		oTbody.innerHTML="";
	$.ajax({
			type:"POST",
			url:"creativeDate.do",
			dateType:"json",
			success: function(data) {
				var json = eval('(' + data + ')');
	
		$.each(json, function() {
			var oTr=document.createElement('tr');
			var oTd=document.createElement('td');
			
			oTd.innerHTML=this.id;
			alert(this.id);
			oTr.appendChild(oTd);
			oTbody.appendChild(oTr);
			
			oTd=document.createElement('td');
			var imghtml='';
			imghtml+="<img onmouseover='imgcorn(this)'style='cursor:pointer' data-toggle='modal'  data-target='#imgphoto' src='";
			imghtml+= json[i].path;
			imghtml+="'/>"
			oTd.appendChild(imghtml);
			oTr.appendChild(oTd);
			oTbody.appendChild(oTr);

			oTd=document.createElement('td');
			oTd.innerHTML=this.name;
			oTr.appendChild(oTd);
			oTbody.appendChild(oTr);
			oTd=document.createElement('td');
			oTd.innerHTML=this.date;
			oTr.appendChild(oTd);
			oTbody.appendChild(oTr);
			
			
			oTd = document.createElement('td');
			var html = "";
					html += "<a href='javascript:;' class='delete' onclick=";
					html += " ' ";
					html += "deleteRow(";
					html += "this";
					html += ");";
					html += " ' ";
					html += ">删除</a>";
					oTd.innerHTML = html;
					oTr.appendChild(oTd);
					oTbody.appendChild(oTr);

		
	}   
        )}
			});
})});

