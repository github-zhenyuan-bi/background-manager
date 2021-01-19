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



/**
 * 右侧侧边栏
 *  */
var $bgcover = $(".bg-cover");	
var $bgcBox = $(".bgc-box");	
var _coverWidth = $bgcover.width();
var _boxWidth = $bgcBox.width();
$bgcBox.css("right", '-' + (_boxWidth+10) + 'px');

$(".open-r-tb").click(function(e) {
	openRightToolBar();
});
function openRightToolBar() {
	$bgcBox.animate({ 
	    right: 0
	  }, 400);
	$bgcover.animate({ 
	    right: 0
	  }, 10);
}

$(".close-r-tb").click(function(e) {
	closeRightToolBar();
});
function closeRightToolBar() {
	$bgcover.animate({ 
	    right: '-' + _coverWidth + 'px'
	  }, 10);
	$bgcBox.animate({ 
	    right: '-' + (_boxWidth+10) + 'px'
	  }, 400);
}


$(window).resize(function(){
	_coverWidth = $bgcover.width();
	_boxWidth = $bgcBox.width();
});