/**
 * icon枚举类型
 */
const iconArr = ['success', 'info', 'warning', 'error', 'question'];

/**
 * 提示类弹框的基本配置
 */
const SWAL_ALERT_OPTION = {
	icon: iconArr[1],
	title: '提示内容',
	text: '',
	showConfirmButton: false,
	onAfterClose: null,
	onOpen: null,
	showClass: {popup: 'animate__animated animate__zoomIn animate__faster'},
	hideClass: {popup: 'animate__animated animate__zoomOut animate__faster' },
	timer: 2000
}
/**
 * 确认类弹框的基本配置
 */
const SWAL_CONFIRM_OPTION = {
		icon: iconArr[2],
		title: '确认内容',
		text: '',
		showConfirmButton: true,
		confirmButtonText: "确认！",
		confirmButtonColor: '#ce5242',
		showCancelButton: true,
		cancelButtonText: "取消",
		showLoaderOnConfirm: true,
		allowOutsideClick: () => !Swal.isLoading(),
		showClass: {popup: 'animate__animated animate__fadeInDown animate__fast'},
		hideClass: {popup: 'animate__animated animate__fadeOutUp animate__faster' },
		preConfirm: null
}


const SWAL_HTML_OPTION = {
		icon: null,
		title: '弹层页面',
		//target: null,
		showConfirmButton: false,
		confirmButtonText: "提交",
		confirmButtonColor: '#ce5242',
		showLoaderOnConfirm: true,
		showCloseButton: true,
		allowOutsideClick: false,
		showClass: {popup: 'animate__animated animate__fadeInDown animate__fast'},
		hideClass: {popup: 'animate__animated animate__fadeOutUp animate__faster' },
		buttonsStyling: false,
		customClass: {
			  //container: 'container-class',
			  //popup: 'popup-class',
			  //header: 'header-class',
			  title: 'myHtml-title-class',
			  //content: 'content-class',
			  //actions: 'actions-class',
			  confirmButton: 'myHtml-btn-class',
			  //footer: 'footer-class'
		},
		loaderHtml: '',
		preConfirm: null
}


const SWAL_QUEUE_OPTION = {
		showLoaderOnConfirm: true,
		showCloseButton: true,
		showCancelButton: true,
		allowOutsideClick: false,
		showClass: {popup: 'animate__animated animate__fadeInDown animate__fast'},
		hideClass: {popup: 'animate__animated animate__fadeOutUp animate__fast' },
		customClass: {
			  //container: 'container-class',
			  //popup: 'popup-class',
			  //header: 'header-class',
			  title: 'myHtml-title-class',
			  //content: 'content-class',
			  //actions: 'actions-class',
			  confirmButton: 'myHtml-btn-class',
			  //footer: 'footer-class'
		},
		loaderHtml: '',
		preConfirm: null
}


/**
 * 我的sweetAlert弹框
 */
mySwal = {
		/*===============================================
		 * 			alert 系列弹框提示
		 ================================================*/
		alertForRes: function(res, arg1, arg2) {
			if (!res) throw "参数的返回数据为空";
			var option = {
					title: "成功",
					text: '',
					onOpen: null,	
					onAfterClose: null,
					timer: 2000
			}
			if ($.type(arg1) === "object")
				$.extend(option, arg1);
			else if ($.type(arg1) === "function")
				option.onOpen = arg1;
			
			if (res.code === 200) {
				mySwal.success(option);
			} else {
				option.text = res.msg;
				option.timer = 5000;
				mySwal.error(option);
			}
		},
		alert: function(icon, option) {
			var opt = {};
			$.extend(opt, SWAL_ALERT_OPTION);
			opt.icon = icon;
			if (icon == iconArr[3])
				opt.timer = 4000;
			$.extend(opt, option);
			return Swal.fire(opt);
		},
		success: function(arg1, arg2, arg3, arg4) {
			return mySwal.alert(iconArr[0], handleAlertArgs(arguments.length, arg1, arg2, arg3, arg4));
		},
		info: function(arg1, arg2, arg3, arg4) {
			return mySwal.alert(iconArr[1], handleAlertArgs(arguments.length, arg1, arg2, arg3, arg4));
		},
		warning: function(arg1, arg2, arg3, arg4) {
			return mySwal.alert(iconArr[2], handleAlertArgs(arguments.length, arg1, arg2, arg3, arg4));
		},
		error: function(arg1, arg2, arg3, arg4) {
			return mySwal.alert(iconArr[3], handleAlertArgs(arguments.length, arg1, arg2, arg3, arg4));
		},
		question: function(arg1, arg2, arg3, arg4) {
			return mySwal.alert(iconArr[4], handleAlertArgs(arguments.length, arg1, arg2, arg3, arg4));
		},
		
		
		
		/*===============================================
		 * 			confirm 系列确认弹层
		 ================================================*/
		confirm: function(arg1, arg2, arg3, arg4) {
			var opt = {};
			$.extend(opt, SWAL_CONFIRM_OPTION);
			$.extend(opt, handleConfirmArgs(arguments.length, arg1, arg2, arg3, arg4));
			return Swal.fire(opt);
		},
		delConfirm: function(option) {
			return mySwal.confirm({
				title: option.title? option.title : '确认删除?',
				text:  option.text? option.text : '',
				confirmButtonText: option.confirmButtonText? option.confirmButtonText : '删除!',
						
				preConfirm: function() {
					var type = $.type(option);
					var confirmFuc = (type === 'function')? option : option.confirm;
					if (option.url) 
						confirmFuc = function() {
							return fetch(option.url, {method: option.method? option.method : "DELETE",});
						}
					
					return confirmFuc()
					.then((res) => { 
						if (!res.ok) throw new Error(res.statusText);
		            	return res.json().then((res) => {
			            	if (res.code != 200) throw res.msg;
			            	return res
			            });
		            }).catch(error => {
						Swal.showValidationMessage(error)
					});
				}
			}).then((result) => {
				if (result.isConfirmed) {
					mySwal.success('删除成功').then((result) => {
		    			if (option.afterConfirm) {
		    				option.afterConfirm(result);
		    			}
		    		});
				}
    		});
		},
		
		
		
		/*===============================================
		 * 			html 系列页面弹层
		 ================================================*/
		html: function(option) {
			var opt = {};
			$.extend(opt, SWAL_HTML_OPTION);
			$.extend(opt, option);
			return Swal.fire(opt);
		},
		viewHtml: function(title, target, option) {
			option = option? option : {};
			option.title = title;
			if ($.type(target) === "string") {
				option.onOpen = function(dom) {
					__jqGet(target).done(res=> {
						option.html = res;
						mySwal.html(option);
					});
				}
			} else {
				option.html = target;
				return mySwal.html(option);
			}
		},
		loadHtml: function(title, target, option) {
			option = option? option : {};
			option.showConfirmButton = true;
			option.title = title;
			if ($.type(target) === "string") {
				option.html = "加载中...";
				option.onOpen = function(dom) {
					__jqGet(target).done(res=> {
						$(dom).find(".swal2-content #swal2-content").html(res);
					});
//					$.get(target, function(res) {
//						$(dom).find(".swal2-content #swal2-content").html(res);
//					});
				}
				return mySwal.html(option);
			} else {
				option.html = target;
				return mySwal.html(option);
			}
		},
		formHtml: function(title, target, option) {
			option = option? option : {};
			option.showConfirmButton = true;
			option.title = title;
			/*if (option.beforeSubmit) {
				var params = option.beforeSubmit();
				option.url = params.url;
				option.fMethod = params.fMethod;
				option.fBody = params.fBody;
			}
			if (option.url) {
				option.preConfirm = function() {
					return fetch(option.url, {
						method: option.fMethod? option.fMethod : "POST",
						body: option.fBody? option.fBody : null
					}).then((res) => { 
						if (!res.ok) throw new Error(res.statusText);
		            	return res.json().then((res) => {
			            	if (res.code != 200) throw res.msg;
			            	if (option.afterSubmit) 
			            		return option.afterSubmit(res);
			            	return res;
			            });
		            }).catch(error => {
						Swal.showValidationMessage(error)
					});
				}
			}*/
			if ($.type(target) === "string") {
				__jqGet(target).done(res=> {
					option.html = res;
					mySwal.html(option);
				});
//				$.get(target, function(res) {
//					option.html = res;
//					mySwal.html(option);
//				});
			} else {
				option.html = target;
				return mySwal.html(option);
			}
		},
		
		
		/*===============================================
		 * 			queue 系列页面弹层
		 ================================================*/
		queue: function(queueOpt, htmlOptions, handler) {
			var newHtmlOpts = [];
			if (htmlOptions) {
				for (let ho of htmlOptions) {
					var optTemp = {};
					$.extend(optTemp, SWAL_QUEUE_OPTION);
					$.extend(optTemp, ho);
					if (optTemp.url) {
						var url = optTemp.url;
						optTemp.onOpen = function(dom) {
							$(dom).find(".swal2-content #swal2-content").show();
							$.get(url, function(res) {
								$(dom).find(".swal2-content #swal2-content").html(res);
							});
						}
					}
					newHtmlOpts.push(optTemp);
				}
			}
			
			Swal.mixin(queueOpt).queue(newHtmlOpts).then((result) => {
				handler(result);
			})
		}
}



/**
 * 处理提示类弹框层参数
 * @param argCount
 * @param arg1
 * @param arg2
 * @param arg3
 * @param arg4
 * @returns
 */
function handleAlertArgs(argCount, arg1, arg2, arg3, arg4) {
	var opt = {};
	switch (argCount) {
		case 1:
			var type_arg1 = $.type(arg1);
			if (type_arg1 === "string") 
				opt.title = arg1;
			else if (type_arg1 === "object")
				$.extend(opt, arg1);
			break;
		case 2:
			opt.title = arg1;
			var type_arg2 = $.type(arg2);
			if (type_arg2 === "string") 
				opt.text = arg2;
			else if (type_arg2 === "object")
				$.extend(opt, arg2);
			else if (type_arg2 === "function")
				opt.onOpen = arg2;
				//opt.onAfterClose = arg2;
			break;
		default:
			opt.title = arg1;
			opt.text = arg2;
			opt.timer = arg3;
			opt.onOpen = arg4;
			//opt.onAfterClose = arg4;
			break;
	}
	return opt;
}


function dumiaotishi() {
	alert(1)
}
function handleConfirmArgs(argCount, arg1, arg2, arg3, arg4) {
	var opt = {};
	switch (argCount) {
		case 1:
			var type_arg1 = $.type(arg1);
			if (type_arg1 === "object")
				$.extend(opt, arg1);
			else
				throw "确认框只用一个参数是，必须是json格式！";
			break;
		case 2:
			opt.title = arg1;
			var type_arg2 = $.type(arg2);
			if (type_arg2 === "string") 
				opt.text = arg2;
			else if (type_arg2 === "object")
				$.extend(opt, arg2);
			else if (type_arg2 === "function")
				opt.preConfirm = arg2;
			break;
		default: 
			opt.title = arg1;
			opt.text = arg2;
			opt.confirmButtonText = arg3[0];
			opt.cancelButtonText = arg3[1];
			opt.preConfirm = arg4;
			break;
	}
	return opt;
}