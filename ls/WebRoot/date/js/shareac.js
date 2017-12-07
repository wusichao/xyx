
$(function(){
	var nowDate = new Date();
		month=nowDate.getMonth()+1;
		day=nowDate.getDate();
		 if(month<10){
                month= '0'+month;
        }
		 if(day<10){
                day= '0'+day;
        }
        timeStr = nowDate.getFullYear() + '/' + month + '/' + day;
		//$("#startDate").val(timeStr);
		$("#endDate").attr("value",timeStr);
		$("#startDate").attr("value",timeStr);
	 $('.startDate').datepicker({
			changeMonth: true,
      		changeYear: true,
			// minDate:0,
            dateFormat: "yy/mm/dd",
            onClose : function(dateText, inst) {
             //  $( "#endDate" ).datepicker( "show" );
            },
			onSelect:function(dateText, inst) {
                $( "#endDate" ).datepicker( "option","minDate",dateText );
            },
        });
		 $(".endDate").datepicker({
            dateFormat: "yy/mm/dd",
			changeMonth: true,
      		changeYear: true,
//            onClose : function(dateText, inst) {
//				var starts=FormatDate($("#startDate").attr("value"));
//				var ends=FormatDate(dateText);
//                if (ends < starts){
//                  $( "#endDate" ).datepicker( "show" );
//				    alert("结束日期不能小于开始日期！");
//					$("#endDate").val($("#startDate").attr("value"));
//                }
//            }
        });
	 	
    });
    function datePickers(){
		
        $(".ui-datepicker-css").css("display","none");
//        getAppCondtion()
    }
/*
    function timeConfig(time){
		//快捷菜单的控制
        var nowDate = new Date();
        timeStr = '一' + nowDate.getFullYear() + '/' + (nowDate.getMonth()+1) + '/' + nowDate.getDate();
        nowDate.setDate(nowDate.getDate()+parseInt(time));
        var endDateStr = nowDate.getFullYear() + '/'+  (nowDate.getMonth()+1) + '/' + nowDate.getDate();
        if(time == -1){
            endDateStr += '一' + endDateStr;
        }else{
            endDateStr += timeStr;
        }
        return endDateStr;
    }*/	
