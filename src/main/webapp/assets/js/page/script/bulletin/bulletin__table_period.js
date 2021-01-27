/** ======================================
  	周期推送通知事件
=========================================*/
window.operateScriptBulletinEvents4 = {
	// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
	'click .tb-op': function(e, value, row, index) {
		var $btn = $(e.currentTarget);
		var event = $btn.data("event");
		//var dd = $("#user-table").bootstrapTable('getData')
		operateScriptBulletinEvents4[event]? operateScriptBulletinEvents4[event]($btn, value, row, index) : "";
	},
	viewHistory: function($btn, value, row, index) {
		
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
		$("#send-bulletin").html("修改周期任务");
		$sideBar.show();
	},
	startOrStop: function($btn, value, row, index) {
		var bolStartOrStop = row.taskEnbale == '开启';
		var startOrStop = bolStartOrStop? '暂停任务' : '启动任务';
		return mySwal.confirm({
			title: '周期推送任务启动/暂停',
			text:  '任务暂停后将不会再下一次推送时间到达时进行推送，直到重新启动任务',
			confirmButtonText: startOrStop,
			icon: iconArr[1],
			confirmButtonColor: '#007bbb',
			preConfirm: function() {
				var url = "/script/bulletinTimerTask/startOrStop?id="+value+"&startOrStop="+!bolStartOrStop;
				fetchPost(url, {}, function(res) {
					$bulletinTable4.bootstrapTable('refresh');
					mySwal.success(startOrStop + '成功');
				});
			}
		});
	},
	remove: function($btn, value, row, index) {
		mySwal.delConfirm({
			// 删除请求
			url: "/script/bulletinTimerTask/deleteById?id="+value,
			// 删除成功后的回调方法
			afterConfirm: function(res) {
				$bulletinTable4.bootstrapTable('refresh');
			}
		});
	}
}







var $bulletinTable4 = $("#bulletin-table4");
myBsTable.pageTable($bulletinTable4, {
	url: '/script/bulletinTimerTaskDetail/getPeriodPage',
	// 刷新按钮
	columns: [
		{field: 'title', title: '标题'},
		{field: 'nextTimeForCron', width: 130, title: '下次推送时间'},
		{field: 'sendCount', title: '已推送次数', events: operateScriptBulletinEvents4, formatter: function(value, row, index) {
			return '<a class="tb-op viewHistory" data-event="viewHistory" title="查看历史推送" href="javascript:void(0);">'+value+'</a>';
		}},
		{field: 'taskEnbale',  title: '状态'},
		{field: 'id',title: '操作', width: 120, events: operateScriptBulletinEvents4, formatter: function(value, row, index) {
			return  '<a class="tb-op edit" data-event="edit" title="修改" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' +
					(row.taskEnbale == '开启'
							? '<a class="tb-op startOrStop" data-event="startOrStop" title="终止任务" href="javascript:void(0);"><i class="fa fa-ban"></i></a>'
							: '<a class="tb-op startOrStop" data-event="startOrStop" title="开启任务" href="javascript:void(0);"><i class="fa fa-play"></i></a>'
					) +
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