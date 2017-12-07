
$(function(){	
	var emails='';
	var regs = new RegExp("(^|&)" + "email" + "=([^&]*)(&|$)", "i");
	var rs = window.location.search.substr(1).match(regs);
	if(rs!=null){ emails=unescape(rs[2]);}

	if(emails==""){
	    $("#reform").validate({
	    rules: {
	      email: {
			  	required: true,
	        	email: true,
				maxlength: 40,
				remote:"validataEmail.do"
			},
	       passWord: {
	        required: true,
			isPassword:true,
	      },
	      confirm_password: {
	        required: true,
	        equalTo: "#password",
			isPassword:true,
	      },
		  company_name:{
			required:true,
		  maxlength: 60,
		  iscomName:true,
		  remote:"validataCompanyname.do",
		  },
		  
		 vertical:"required",
		  web_site:{
			  required:true,
			  url:true,
			  maxlength: 60,
			  remote:"validataWebsite.do",
			  },
			  contact:{
				required:true,
			  maxlength: 40,
			  isName:true,
			  },
			  cellphone:{
			  required:true,
			  isPhone:true,
			  },
			  license_pic:{
				   required:true,
			 
				  },
				  invitation_code:{
				 
					  //remote:"isHasInvitationCode.do",
				   remote:"isInvitationCode.do",
				  },
	    },
	    messages: {
			 email:{
				 required:"必填",
				email: "格式错误!正确格式(XXX@XX.XX)",
			  	maxlength: "长度不大于40位",
				remote:"用户名已注册！"
			 },
	      passWord: {
	        required: "必填",
			isPassword:"6-14位，密码至少由字母和数字组成",
	      },
	      confirm_password: {
	        required: "必填",
	        equalTo: "两次密码输入不一致",
			rangelength:"6-14位，密码至少由字母和数字组成"
	      },
		  company_name:{
			  required:"必填",
			  remote:"公司名已注册！",
			  maxlength:"长度不大于60位",
			  iscomName:"仅支持字母、汉字和括号",
			  },
			  vertical:"请选择",
		  web_site:{
			  required:"必填",
			  url:"格式错误!(http://***.com)",
			  maxlength:"长度不大于60位",
			  remote:"网址已注册！",
			  },
			  contact:{
				  required:"必填",
			  maxlength:"长度不大于60位",
			  isName:"仅支持字母、汉字和点",
			  },
			  cellphone:{
				  required:"必填",
			  isPhone:"格式错误!(010-000000或13/15/18的手机号)"

					  },
					  license_pic:{
					required:"请上传营业执照",
					  },
					  invitation_code:{
				// remote:"邀请码无效",//邀请码已经被使用，请与我们的运营人员联系并重新获取
				   remote:"邀请码错误或已被使用，若无误请与我们的销售人员联系",
				  },
	    }
		});
			}
			
	else{
		$("#reform").validate({
	    rules: {
	      email: {
			  	required: true,
	        	email: true,
				maxlength: 40,
				remote:"/ls/validataEmail.do"
			},
	       passWord: {
	        required: true,
			isPassword:true,
	      },
	      confirm_password: {
	        required: true,
	        equalTo: "#password",
			isPassword:true,
	      },
		  company_name:{
			required:true,
		  maxlength: 60,
		  iscomName:true,
		  remote:"/ls/validataCompanyname.do",
		  },
		  
		 vertical:"required",
		  web_site:{
			  required:true,
			 url:true,
			  maxlength: 60,
			  remote:"/ls/validataWebsite.do",
			  },
			  contact:{
				required:true,
			  maxlength: 40,
			  isName:true,
			  },
			  cellphone:{
			  required:true,
			  isPhone:true,
			  },
			  invitation_code:{
				  //	remote:"/ls/isHasInvitationCode.do",
				   remote:"/ls/isInvitationCode.do",
				  },
	    },
	    messages: {
			 email:{
				 required:"必填",
				email: "格式错误!正确格式(XXX@XX.XX)",
			  	maxlength: "长度不大于40位",
				remote:"用户名已注册！"
			 },
	      passWord: {
	        required: "必填",
			isPassword:"6-14位，密码至少由字母和数字组成",
	      },
	      confirm_password: {
	        required: "必填",
	        equalTo: "两次密码输入不一致",
			rangelength:"6-14位，密码至少由字母和数字组成"
	      },
		  company_name:{
			  required:"必填",
			  remote:"公司名已注册！",
			  maxlength:"长度不大于60位",
			  iscomName:"仅支持字母、汉字和括号",
			  },
			  vertical:"请选择",
		  web_site:{
			  required:"必填",
			  url:"格式错误!(http://***.com)",
			  maxlength:"长度不大于60位",
			  remote:"网址已注册！",
			  },
			  contact:{
				  required:"必填",
			  maxlength:"长度不大于60位",
			  isName:"仅支持字母、汉字和点",
			  },
			  cellphone:{
				  required:"必填",
			  isPhone:"格式错误!(010-000000或以13、15、18开头的手机号)"

					  },
					  invitation_code:{
				// remote:"邀请码无效",//邀请码已经被使用，请与我们的运营人员联系并重新获取
				   remote:"邀请码错误或已被使用，若无误请与我们的销售人员联系",
				  },
			
	    }
		});

	}

});
	//                 img.style.marginLeft = rect.left+'px';
     //           img.style.marginTop = rect.top+'px';
      //图片上传预览 
        function previewImage(file)
        {
        	var img=document.getElementById('imghead');
			img.src="";
          var MAXWIDTH  = 260; 
          var MAXHEIGHT = 150;
          var div = document.getElementById('preview');
          if (file.files && file.files[0])
          {
              div.innerHTML ='<img id=imghead>';
              var img = document.getElementById('imghead');
              img.onload = function(){
               /* var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                img.width  =  rect.width;
                img.height =  rect.height;*/
				img.width  =  260;
                img.height =  150;

              };
              var reader = new FileReader();
              reader.onload = function(evt){img.src = evt.target.result;};
              reader.readAsDataURL(file.files[0]);
          }
          else //兼容IE
          {
            var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
            file.select();
            var src = document.selection.createRange().text;
            div.innerHTML = '<img id=imghead>';
            var img = document.getElementById('imghead');
            img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
            div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
          }
        }
        function clacImgZoomParam( maxWidth, maxHeight, width, height ){
            var param = {top:0, left:0, width:width, height:height};
            if( width>maxWidth || height>maxHeight )
            {
                rateWidth = width / maxWidth;
                rateHeight = height / maxHeight;
                
                if( rateWidth > rateHeight )
                {
                    param.width =  maxWidth;
                    param.height = Math.round(height / rateWidth);
                }else
                {
                    param.width = Math.round(width / rateHeight);
                    param.height = maxHeight;
                }
            }
            
            param.left = Math.round((maxWidth - param.width) / 2);
            param.top = Math.round((maxHeight - param.height) / 2);
            return param;
        }
		
			

//});


