// 表单
var $bulletinForm = $("#bulletin-form");
// 模板选择框对象
var $tempSelect = $bulletinForm.find("#templateId");
// 模板示例dom
var $tempDom = $("#temp-dom");
// 变量填写容器
var $varContainer = $("#var-container");

// 变量填写框缓存
var varDomCache = {};
// 变量填充的content缓存
var tempContentCache = null;



/** ======================================
  	模板选择事件 
=========================================*/
$tempSelect.change(function(e){
	var _this = $(this);
	// 模板展示
	var _content = showTemplateExample(_this.val());
	// 内容变量提取{{xxx}}
	buildVarForm($varContainer, _this.val(), _content);
}); 

function showTemplateExample(templateId) {
	var _temp = $("#bulletin-temp-"+templateId);
	var _fixedTop = _temp.data("ft");
	var _content = _temp.find(".temp-content").html();
	tempContentCache = _content;
	// 模板展示
	$tempDom.empty();
	$tempDom.html(_temp.children().clone());
	// 置顶展示
	if (_fixedTop == '1') {
		$tempDom.addClass("bg-grey-1");
		$tempDom.insertAfter("#top-target");
	} else {
		$tempDom.removeClass("bg-grey-1");
		$tempDom.insertAfter("#org-target");
	}
	return _content
}


/** ======================================
  	构建变量框
=========================================*/
function buildVarForm($container, _templateId, _content) {
	var cachedFormVarDom = varDomCache[_templateId];
	if (cachedFormVarDom && !$.isEmptyObject(cachedFormVarDom)) {
		$container.append(cachedFormVarDom);
	} else {
		var _listVar = getMatchedStrs(_content);
		$container.empty();
		if (_listVar) {
			var formVarDom = '';
			for (var i = 0; i < _listVar.length; i++) {
				const elem = _listVar[i];
				formVarDom += buildVarFormItem(elem);
			}
			$container.append(formVarDom);
			varDomCache[_templateId] = formVarDom;
		}
	}
}
function buildVarFormItem(varName) {
	return '<div class="form-group row">' + 
			    '<label for="example-text-input" class="col-sm-2 col-md-3 col-form-label">'+varName+'</label>' + 
			    '<div class="col-sm-10 col-md-9">' + 
			        '<input class="form-control" type="text" oninput="varChange(this)" name="'+varName+'" placeholder="输入'+varName+'值填充模板变量" autocomplete="off"' + 
			        		'data-parsley-required="true" data-parsley-required-message="'+varName+'不可为空"' + 
			        		'data-parsley-trigger="focusout">' + 
			    '</div>' + 
			'</div>';
}


/** ======================================
  	变量框输入事件
=========================================*/
function varChange(dom) {
	var _varForm = $(dom).parents("form");
	var _varInputs = $varContainer.find("input");
	var _contentDom = _varForm.find(".temp-content");
	var resContent = tempContentCache;
	
	_varInputs.each(function() {
		var _this = $(this);
		var val = _this.val();
		if (val) {
			resContent = resContent.replace("{{"+_this.attr("name")+"}}", _this.val());
		}
	});
	_contentDom.html(resContent);
}


/** ======================================
  	表单初始化
=========================================*/
function formInit() {
	// id重置
	$bulletinForm.find("#timer-task-id").val('');
	// 模板选择重置
	$tempSelect.val("");
	// 变量输入框重置
	$varContainer.empty();
	// 通知示例重置
	$tempDom.html($("#default-temp-dom").children().clone());
	// 通知方式选择重置
	$bulletinForm.find("#sender").val("0"); 
	// 定制 周期 输入框隐藏
	$timerDom.addClass("d-none");
	$periodDom.addClass("d-none");
}


/** ======================================
  	推送通知按钮点击事件
=========================================*/
function sendBtnClickFunc(dom, e) {
	e.preventDefault();
	var flag = $bulletinForm.parsley().validate();
	if (!flag) return false;
	
	var sendMode = $bulletinForm.find("#sender").val();
	var __id = $bulletinForm.find("#timer-task-id").val();
	var _formData = {
			id: __id,
			templateId: $tempSelect.val(),
			content: $tempDom.find(".temp-content").html(),
			sender: sendMode,
			sendMode: sendMode
	}
	
	var url = "/script/bulletin/addRecord";
	if (sendMode == 1 || sendMode == 2) {
		url = "/script/bulletinTimerTask/addRecord";
		if (__id)
			url = "/script/bulletinTimerTask/updateById";
		if (sendMode == 1) {
			var _time = $timerDom.find("input").val();
			_time = _time.replace("T", " ");
			_formData.sendTime = _time;
		} else
			_formData.sendCron = $periodDom.find("select").val();
	}
	fetchPostSwalComfirm(url
			, {
				body: JSON.stringify(_formData)
			}, function(res) {
				mySwal.alertForRes(res, {
					text: "成功",
					onOpen: function() {
						// 表单初始化
						formInit();
						// 关闭右侧侧边栏
						$sideBar.hide();
						// 已推送表格刷新
						$bulletinTable.bootstrapTable('refresh');
						// 定时推送表格刷新
						if (sendMode == 1)
							$bulletinTable3.bootstrapTable('refresh');
						else
							$bulletinTable4.bootstrapTable('refresh');
					}
				});
			});
};


/** ======================================
	推送方式选择改变事件
=========================================*/
var $timerDom = $("#timer-dom");
var $periodDom = $("#period-dom");
$("#sender").change(function(e) {
	var _this = $(this);
	var _val  = _this.val();
	
	sendModeChange(_val);
});
function sendModeChange(_val, timerVal, periodVal) {
	if (_val == 1) {
		$timerDom.removeClass("d-none");
		$periodDom.addClass("d-none");
	} else if (_val == 2) {
		$timerDom.addClass("d-none");
		$periodDom.removeClass("d-none");
	} else {
		$timerDom.addClass("d-none");
		$periodDom.addClass("d-none");
	}
	if (timerVal)
		$timerDom.find("input").val(timerVal);
	if (periodVal)
		$periodDom.find("select").val(periodVal);
}