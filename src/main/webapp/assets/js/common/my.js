function isForbiddenCover(fobid) {
	if ($.type(fobid) === 'number') {
		return (fobid == 1);
	} else if ($.type(fobid) === 'boolean'){
		return fobid? 1 : 0;
	}
}

/**
 * 自动将form表单封装成json对象
 */
$.fn.serializeFormToJson = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};




/**
 * 图片格式判断
 * @param ext
 * @returns
 */
function isAssetTypeAnImage(ext) {
	return [ 'png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff' ]
			.indexOf(ext.toLowerCase()) !== -1;
}
/**
 * 校验图片格式
 * @param fileName
 * @returns
 */
function checkFileTypeImg(fileName) {
	//获取最后一个.的位置
	var index= fileName.lastIndexOf(".");
	//获取后缀
	var ext = fileName.substr(index+1);
	//判断是否是图片
	return isAssetTypeAnImage(ext);
}
/**
 * 重定向到登录页
 * @param code
 * @returns
 */
function redirectToLogin(code) {
	if (code === 600)
		window.location.href = "/login";
}

/** 
 * 正则表达式提取字符串中的内容为数组
 * */
function getMatchedStrs(str) {
	if (!str)
		return null;
    var reg = /\{\{(.+?)\}\}/
    var reg_g = /\{\{(.+?)\}\}/g
    var result = str.match(reg_g)
    var list = [];
    if (result && result.length > 0) 
        for (var i = 0; i < result.length; i++) {
            var item = result[i];
            list.push(item.match(reg)[1])
        }
    return list;
}


function dragEnable(dom) {
    var Drag = dom;
    Drag.onmousedown = function(event) {
        var ev = event || window.event;
        event.stopPropagation();
        var disX = ev.clientX - Drag.offsetLeft;
        var disY = ev.clientY - Drag.offsetTop;
        document.onmousemove = function(event) {
            var ev = event || window.event;
            Drag.style.left = ev.clientX - disX + "px";
            Drag.style.top = ev.clientY - disY + "px";
            Drag.style.cursor = "move";
        };
    };
    Drag.onmouseup = function() {
        document.onmousemove = null;
        this.style.cursor = "default";
    };
};



