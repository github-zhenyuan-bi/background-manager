// 侧边栏
var $sideBar = $.SideBar.init({
	title: "通知公告推送",
	contentDom: $("#bulletin-form"),
	confirmBtnAction: sendBtnClickFunc,
	cancelBtnAction: function(dom) {
		$sideBar.hide();
		formInit();
	}
});

// 表格上方推送按钮
$("#bulletin-table2-toolbar button#sendBullet").click(function(e) {
	formInit();
	$sideBar.show();
});




/** ======================================
  	已推送通知事件
=========================================*/
window.operateScriptBulletinEvents2 = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			//var dd = $("#user-table").bootstrapTable('getData')
			operateScriptBulletinEvents2[event]? operateScriptBulletinEvents2[event]($btn, value, row, index) : "";
		},
		remove: function($btn, value, row, index) {
			mySwal.delConfirm({
				// 删除请求
				url: "/script/bulletin/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					$bulletinTable.bootstrapTable('refresh');
				}
			});
		},
		edit: function($btn, value, row, index) {
			var url = "/script/bulletin/content/form/bulletin-content-edit" + 
					  "?id=" + row.id + 
					  "&templateId=" + row.templateId;
			return mySwal.formHtml("修改公告内容", url, {
				width: 600,
				onOpen: function(dom) {
					var $ceForm = $(dom).find(".bulletin-content-edit-form");
					var _templateContent = $ceForm.find("#edit-content-temp").html();
					var _varsContainer = $ceForm.find("#template-vars-container");
					var _templateId = _varsContainer.data("tid");
					tempContentCache = _templateContent;
					buildVarForm(_varsContainer, _templateId, _templateContent);
				},
				preConfirm: function() {
					var _form = $("#swal2-content .bulletin-content-edit-form");
					var _flag = _form.parsley().validate();
					if (!_flag) return false;
					
					var _formContent = _form.find("#edit-content-temp").html();
					return fetchPostSwalComfirm('/script/bulletin/updateById?id=' + row.id + "&content=" + _formContent
							, {}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "修改公告内容成功",
									onOpen: function() {$bulletinTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		fixedTopOp: function($btn, value, row, index) {
			var isFixedUp = row.isFixedTop? '取消置顶' : '置顶';
			return mySwal.confirm({
				title: '通知公告置顶操作',
				text:  '置顶后该条公告会会固定在列表最上端，否则按照默认推送时间排序。',
				confirmButtonText: isFixedUp,
				icon: iconArr[1],
				confirmButtonColor: '#007bbb',
				preConfirm: function() {
					var url = "/script/bulletin/updateFixedTopStatus?bulletinId="+row.id+"&isFixedTop="+!row.isFixedTop;
					fetchPost(url, {}, function(res) {
						$bulletinTable.bootstrapTable('refresh');
						mySwal.success(isFixedUp + '成功');
					});
				}
			});
		}
}
/** ======================================
       表格加载 
=========================================*/
var $bulletinTable = $("#bulletin-table2");
myBsTable.pageTable($bulletinTable, {
	url: '/script/bulletin/getPage',
	// 刷新按钮
	columns: [
		{title: '#', width: 50, formatter: function (value, row, index) {return '<strong><i>'+(index+1)+'</i></strong>';}},
		{field: 'iconPath',title: '图标', formatter: function(value, row, index) {
			return '<img src="'+value+'" class="box-wh-40 ">';
		}},
		{field: 'title', width: 120, title: '标题'},
		{field: 'titleExtend', title: '标签', formatter: function(value, row, index) {
			return '<span class="badge badge-danger">'+value+'</span>';
		}},
		{field: 'content', width: 400, title: '通知内容'},
		{field: 'theme',title: '主题类型', formatter: function(value, row, index) {
			return value;
		}},
		{field: 'isFixedTop',title: '是否置顶', formatter: function(value, row, index) {
			return value? '置顶' : '-';
		}},
		{field: 'sendTime', title: '推送时间'},
		{field: 'id',title: '操作', width: 120, events: operateScriptBulletinEvents2, formatter: function(value, row, index) {
			var fixedTopIcon = row.isFixedTop? "fa-window-minimize" : "fa-deaf";
			return  '<a class="tb-op edit" data-event="edit" title="编辑" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' + 
					'<a class="tb-op fixedTopOp" data-event="fixedTopOp" title="置顶操作" href="javascript:void(0);"><i class="fa '+fixedTopIcon+'"></i></a>' + 
					'<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
		}}
	],
	onClickRow:function (row,$element) {
    },
	// 加载完成方法
	onLoadSuccess: function (data) {
		
	},
});