/** ======================================
  	周期推送通知事件
=========================================*/
window.operateScriptBulletinEvents3 = {
	// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
	'click .tb-op': function(e, value, row, index) {
		var $btn = $(e.currentTarget);
		var event = $btn.data("event");
		//var dd = $("#user-table").bootstrapTable('getData')
		operateScriptBulletinEvents3[event]? operateScriptBulletinEvents3[event]($btn, value, row, index) : "";
	},
	edit: function($btn, value, row, index) {
		// 模板选择重置
		$bulletinForm.find("#timer-task-id").val(row.id);
		$tempSelect.val(row.templateId);
		var _content = showTemplateExample(row.templateId);
		buildVarForm($varContainer, row.templateId, _content);
		var sendModeVal = 0;
		if (row.sendMode=='周期推送') sendModeVal = 2;
		if (row.sendMode=='延时推送') sendModeVal = 1;
		$bulletinForm.find("#sender").val(sendModeVal);
		sendModeChange(sendModeVal, null, row.sendCron);
		$("#send-bulletin").html("修改延时任务");
		$sideBar.show();
	},
	remove: function($btn, value, row, index) {
		mySwal.delConfirm({
			// 删除请求
			url: "/script/bulletinTimerTask/deleteById?id="+value,
			// 删除成功后的回调方法
			afterConfirm: function(res) {
				$bulletinTable3.bootstrapTable('refresh');
			}
		});
	}
}
var $bulletinTable3 = $("#bulletin-table3");
myBsTable.pageTable($bulletinTable3, {
	url: '/script/bulletinTimerTaskDetail/getDelayPage',
	// 刷新按钮
	columns: [
		{field: 'title', title: '标题'},
		{field: 'sendTime', width: 130, title: '预定推送时间'},
		{field: 'sendResult', title: '推送结果', formatter: function(value, row, index) {
			return value? '<span class="text-muted">'+value+'</span>' : '等待推送';
		}},
		{field: 'id',title: '操作', width: 120, events: operateScriptBulletinEvents3, formatter: function(value, row, index) {
			return 	(row.sendResult? "" : '<a class="tb-op edit" data-event="edit" title="修改" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>') +
					'<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
		}}
	],
	pageSize: 5,
	onClickRow:function (row,$element) {
    },
	// 加载完成方法
	onLoadSuccess: function (data) {
		
	},
});