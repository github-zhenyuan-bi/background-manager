function handleAjaxFail(err) {
	console.log(err)
	var errInfoJson = err.responseJSON;
	// token认证异常 跳转登录页重新认证
	if (errInfoJson && errInfoJson.code === 600) {
		
		window.location.href = "/login";
	}
}
function __jqAjax(opt) {
	return $.ajax(opt).fail(err=>{handleAjaxFail(err)});;
}

// 发送ajax请求
function __jqGet(url, data) {
	return $.get(url, data).fail(err=>{handleAjaxFail(err)});
}

//发送post请求
function __jqPost(url, data) {
	return $.post(url, data).fail(err=>{handleAjaxFail(err)});
}

//发送load请求
function __jqLoad($dom, url) {
	return __jqGet(url).done(res=>{
		$dom.append(res);
	}).fail(err=>{handleAjaxFail(err)});;
}

