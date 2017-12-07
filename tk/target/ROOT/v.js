
(function(){
var xyx_iv = "xyx_" + (new Date()).getTime();//自定义全局变量

//获取后面的元素

function type(d) {
        return Object.prototype.toString.call(d) === "[object Array]";
    };
  xyx_ri.getLast = function (f) {
        for (var d = this.length - 1; 0 <= d; d--) {
			
            if (type(this[d])) {//判断是否是数组，如果是数组继续执行
                if (this[d][0] == f) {
                    return this[d][1];
                }
            }
        }
    };
var c = window[xyx_iv] = new Image(); 
	c.onload = (c.onerror = function() {window[xyx_iv] = null;});	
    c.src = '//'+xyx_ri.getLast('xyx_rd')+'/v?a='+encodeURIComponent(xyx_ri.getLast('xyx_ra'))+'&v='+encodeURIComponent(xyx_ri.getLast('xyx_rv'));
	c = null; 
})()