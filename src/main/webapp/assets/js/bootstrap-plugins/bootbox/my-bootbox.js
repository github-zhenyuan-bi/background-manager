BOOTBOX_DEFAULT_OPTIONS = {
		// 标题
		title: 'unset box title',
		// 内容文本
		message: '<p><i class="fa fa-spin fa-spinner"></i> Loading...</p>',
		// 下方按钮
		buttons: null,
		// 默认垂直居中
		centerVertical: true
}

BOOTBOX_DEFAULT_OPTIONS_FORM = {
		onShown: function(e) {
	        // TODO 表单弹窗已完成事件 
	    },
		buttons: {
	        cancel: {
	            label: "<i class='fa fa-times'></i> 取消",
	            className: 'myBtn-cacel-1'
	        },
	        ok: {
	            label: "<i class='fa fa-check'></i> 提交",
	            className: 'myBtn-ok-1',
	            callback: function(){
	                // TODO 这里的提交方法需要被覆盖
	            }
	        }
	    }
}

var myBBox = {
		boxHtml: function(options) {
			var curOption = buildOptions(BOOTBOX_DEFAULT_OPTIONS, options);
			
			var dialog = bootbox.dialog(curOption); 
			
			dialog.init(function(){
				$.get(curOption.url, function(res) {
					dialog.find('.bootbox-body').html(res);
				});
			});
			return dialog;
		},
		boxForm: function(options) {
			if (!options.submitBtnFuc)
				throw "提交按钮事件必须被重写";
			
			var curOption = buildOptions(BOOTBOX_DEFAULT_OPTIONS_FORM, options);
			curOption.buttons.ok.callback = curOption.submitBtnFuc;
			delete curOption.submitBtnFuc;
			
			return myBBox.boxHtml(curOption);
		}
}


/** 克隆一个默认参数的副本 */
function buildOptions(baseopt, options) {
	var curOption = {};
	$.extend(curOption, baseopt);
	if (options)
		$.extend(curOption, options);
	return curOption;
}