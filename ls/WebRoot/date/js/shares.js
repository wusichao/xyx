

/* 时间插件 使用 */

          
      
(function($){
    $.setStartTime = function(){
        $('.startDate').datepicker({
			 changeMonth: true,
      		changeYear: true,
            dateFormat: "yy/mm/dd",
            minDate: -30,
			maxDate: 0,
        });
    };
   /* $.setEndTime = function(){
        $(".endDate").datepicker({
            dateFormat: "yy/mm/dd",
			 changeMonth: true,
      		changeYear: true,
           // maxDate: "+d",
			//defaultDate : new Date(),
            onClose : function(dateText, inst) {
                if (dateText < $("input[name=startDate]").val()){
                  $( "#endDate" ).datepicker( "show" );
				    alert("结束日期不能小于开始日期！");
					$("#endDate").val($("input[name=startDate]").val())
                }
            }
        });
    };*/
 /*   $.date = function(){
        $('.date').datepicker(
            $.extend({showMonthAfterYear:true}, $.datepicker.regional['zh-CN'],
                {'showAnim':'','dateFormat':'yy/mm/dd','changeMonth':'true','changeYear':'true',
                    'showButtonPanel':'true'}
            ));
    };*/
    $.datepickerjQ = function(){
       $(".ui-datepicker-time").on("click",function(){
           $(".ui-datepicker-css").css("display","block")
        });
        $(".ui-kydtype li").on("click",function(){
            $(".ui-kydtype li").removeClass("on").filter($(this)).addClass("on");
//            getAppCondtion();
        });
        $(".ui-datepicker-quick input").on("click",function(){
            var thisAlt = $(this).attr("alt");
            var dateList = timeConfig(thisAlt);
            $(".ui-datepicker-time").val(dateList);
            $(".ui-datepicker-css").css("display","none");
			 $("#ui-datepicker-div").css("display","none")
//            getAppCondtion()
        });
        $(".ui-close-date").on("click",function(){
            $(".ui-datepicker-css").css("display","none")
			 $("#ui-datepicker-div").css("display","none")
			//inst.dpDiv.css({"display":"none"})
        });
		 $(".startDate").on("click",function(){
            $(".endDate").attr("disabled",false);
        });
	
    }
	
})(jQuery);

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
        $.setStartTime();
       // $.setEndTime();
        $.datepickerjQ();
		
        var nowDate = new Date();
        timeStr = nowDate.getFullYear() + '/' + month + '/' + day;
        nowDate.setDate(nowDate.getDate()+parseInt(-1));
        var endDateStr = nowDate.getFullYear() + '/'+  (nowDate.getMonth()+1) + '/' + nowDate.getDate();
		//$(".ui-datepicker-time").attr("value",endDateStr +"—"+ timeStr)
		$("#startDate").attr("value",timeStr)
		//$("#endDate").attr("value",timeStr)
    });

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

    function datePickers(){
		//自定义菜单
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
       // var dateList = startDate +'一'+ endDate;
        //$(".ui-datepicker-time").val(dateList);
        $(".ui-datepicker-css").css("display","none");
//        getAppCondtion()
    }