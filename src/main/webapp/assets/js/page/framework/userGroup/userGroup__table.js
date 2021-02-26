//角色数据表格对象
var _userGroupTable = $("#userGroup-table");
// 添加角色表单dom
var _userGroupAdd   = $("#userGroup-add-container").find(".userGroup-table-addUserGroup");


// 工具栏按钮点击事件
window.operateUserGroupEvents = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			//var dd = $("#user-table").bootstrapTable('getData')
			operateUserGroupEvents[event]? operateUserGroupEvents[event]($btn, value, row, index) : "";
		},
		/*
		 * 添加用户组
		 */
		 addUserGroup: function($btn) {
			var $addUserGroup = _userGroupAdd.clone();
			mySwal.loadHtml("添加用户组", $addUserGroup, {
				preConfirm: function() {
					var $userGroupForm = $("#swal2-content .userGroup-table-addUserGroup");
					var flag = $userGroupForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/usergroup/addRecord'
							, {body: JSON.stringify($userGroupForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "新增用户组成功",
									onOpen: function() {_userGroupTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		edit: function($btn, value, row, index) {
			var $addUserGroup = _userGroupAdd.clone();
			mySwal.loadHtml("编辑用户组", $addUserGroup, {
				onOpen: function(dom) {
					for ( var key in row) {
						$(dom).find(".userGroup-table-addUserGroup input[name='"+key+"']").val(row[key]);
					}
				},
				preConfirm: function() {
					var $userGroupForm = $("#swal2-content .userGroup-table-addUserGroup");
					var flag = $userGroupForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/usergroup/updateById?id=' + value
							, {body: JSON.stringify($userGroupForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "编辑用户组成功",
									onOpen: function() {_userGroupTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		/*
		 * 逻辑删除角色
		 */
		remove: function($btn, value, row, index) {
			mySwal.delConfirm({
				// 删除请求
				url: "/framework/usergroup/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					_userGroupTable.bootstrapTable('refresh');
				}
			});
		}
}


/*
 * 表格头部工具栏按钮点击事件 映射为工具栏事件
 */
$("#userGroup-table-toolbar button").click(function() {
	var event = $(this).data("event");
	operateUserGroupEvents[event]? operateUserGroupEvents[event]($(this)) : "";
}) 


//========== 数据表格加载 ===========\
!function(timeout) {
	setTimeout(() => {
		myBsTable.pageTable(_userGroupTable, {
			url: '/framework/usergroup/getPage',
			columns: [
				{title: '#', width: 50, formatter: function (value, row, index) {return '<strong><i>'+(index+1)+'</i></strong>';}},
				{field: 'name', title: '用户组名称'},
				{field: 'descrip', title: '用户组描述'},
				{field: 'groupType', title: '类别'},
				{field: 'id',title: '操作', width: 120, events: operateUserGroupEvents, formatter: function(value, row, index) {
					return '<a class="tb-op edit" data-event="edit" title="编辑用户组信息" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' +
						   '<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
				}}
			],
			// 加载完成方法
			onLoadSuccess: function (data) {
			},
		});
	}, timeout);
}(400);