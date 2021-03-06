var _constantForm  = $("#constant-add-container").find(".constant-table-addConstant");
	
window.operateConstantEvents = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			//var dd = $("#user-table").bootstrapTable('getData')
			operateConstantEvents[event]? operateConstantEvents[event]($btn, value, row, index) : "";
		},
		addConstant: function($btn) {
			var $addConstant = _constantForm.clone();
			mySwal.formHtml("添加系统常量", $addConstant, {
				preConfirm: function() {
					var _addConstantForm = $("#swal2-content .constant-table-addConstant");
					var flag = _addConstantForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/constant/addRecord'
							, {body: JSON.stringify(_addConstantForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "新增常量配置成功",
									onOpen: function() {_constantTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		edit: function($btn, value, row, index) {
			var $addConstant = _constantForm.clone();
			mySwal.formHtml("编辑系统常量", $addConstant, {
				onOpen: function() {
					$("#swal2-content .constant-table-addConstant").find("[name]").each(function(){
	    				var name = this.name;
	    				this.value = row[name];
	    			});
				},
				preConfirm: function() {
					var _addConstantForm = $("#swal2-content .constant-table-addConstant");
					var flag = _addConstantForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/constant/updateById'
							, {body: JSON.stringify(_addConstantForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "新增常量配置成功",
									onOpen: function() {_constantTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		remove: function($btn, value, row, index) {
			mySwal.delConfirm({
				// 删除请求
				url: "/framework/constant/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					_constantTable.bootstrapTable('refresh');
				}
			});
		}
}

$("#constant-table-toolbar button").click(function() {
	var event = $(this).data("event");
	operateConstantEvents[event]? operateConstantEvents[event]($(this)) : "";
}) 

var _constantTable = $("#constant-table");
myBsTable.pageTable(_constantTable, {
	url: '/framework/constant/getPage',
	search: true,
	searchOnEnterKey: true,
	formatSearch: function () { return '类型/键'},
	showRefresh: true,
	columns: [
		{title: '#', width: 50, formatter: function (value, row, index) {return '<strong><i>'+(index+1)+'</i></strong>';}},
		{field: 'constType', title: '常量类型'},
		{field: 'constKey', title: '常量键'},
		{field: 'constValue', title: '常量值'},
		{field: 'descp', title: '常量说明'},
		{field: 'id',title: '操作', width: 120, events: operateConstantEvents, formatter: function(value, row, index) {
			return '<a class="tb-op edit" data-event="edit" title="编辑常量配置" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' + 
				   '<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
		}}
	],
	// 加载完成方法
	onLoadSuccess: function (data) {
		// 初始化开关组件
		// mySwitch.load($('.role-forbidden-switch'));
	},
});