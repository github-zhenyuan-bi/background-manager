

function myFetch(url, option, sucFuc, errFuc) {
	option = option? option : {};
	var data = option.body;
	if (option.method === "POST") {
		return __jqAjax({
			url: url,
			type: "POST",
			data: data,
			headers: option.headers
		}).done(res=> {
			if (res.code != 200) throw res;
			if (sucFuc && $.type(sucFuc) === 'function') 
        		sucFuc(res)
		});
	} else {
		return __jqGet(url).done(res=> {
			if (res.code != 200) throw res;
			if (sucFuc && $.type(sucFuc) === 'function') 
        		sucFuc(res)
		});
	}
	
	
//	return fetch(url, option).then((res) => { 
//		if (!res.ok) throw new Error(res.statusText);
//    	return res.json().then((res2) => {
//        	if (res2.code != 200) throw res2;
//        	
//        	if (sucFuc && $.type(sucFuc) === 'function') 
//        		sucFuc(res2)
//        });
//    }).catch(error => {  
//    	console.error(error)
//    	redirectToLogin(error.code);
//    	if (errFuc && $.type(errFuc) === 'function')
//    		errFuc(error.msg)
//    	else
//    		throw error;
//    }); 
}


function fetchPost(url, option, sucFuc, errFuc) {
	var opt = {};
	$.extend(opt, option);
	opt.method = "POST";
	opt.headers = {'Content-Type': 'application/json'};
	return myFetch(url, opt, sucFuc, errFuc);
}

function fetchPostSwalComfirm(url, option, sucFuc) {
	return fetchPost(url, option, sucFuc, function(error) {
		Swal.showValidationMessage(error);
	})
}