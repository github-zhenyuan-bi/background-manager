// 用户数据表格
var _userTable = $("#user-table");
// 用户详情展示页面dom
var _userForm  = $("#user-form-container").find(".user-detail-form");
// 添加用户编辑表单dom
var _userAdd   = $("#user-add-container").find(".user-table-addUser");
// 表格工具栏事件
window.operateUserEvents = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			//var dd = $("#user-table").bootstrapTable('getData');
			operateUserEvents[event]? operateUserEvents[event]($btn, value, row, index) : "";
		},
		/*
		 * 新增系统用户
		 */
		addUser: function($btn) {
			var $addUser = _userAdd.clone();
			var $form;
			mySwal.formHtml("添加用户", $addUser, {
				onOpen: function(dom) {
					$form = $(dom).find(".user-table-addUser");
					$form.parsley();
				},
				preConfirm: function() {
					var flag = $form.parsley().validate();
					if (!flag) return false;
					var _addUserForm = $("#swal2-content .user-table-addUser");
					return fetchPostSwalComfirm("/framework/user/addRecord", 
							{
								body: JSON.stringify(params = {
										username: _addUserForm.find("#username").val(),
										password: hex_md5(_addUserForm.find("#password").val())
								})
							}, function(res) {
								mySwal.alertForRes(res, {
									text: "新增用户角色成功",
									onOpen: function() {$("#user-table").bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		/*
		 * 查看用户详情
		 */
		edit: function($btn, value, row, index) {
			var $form = _userForm.clone();
			mySwal.viewHtml("用户详细信息", $form, {
				width: '25rem',
				onBeforeOpen: function(dom) {
					var dataObj = {};
					$.extend(dataObj, row);
					$.extend(dataObj, row.userInfo);
					for ( var key in dataObj) {
						var kd = dataObj[key]?dataObj[key]:"-";
						if (key === "sex" && kd == 1)
							kd = "男";
						else if (key === "sex" && kd == 2)
							kd = "女";
						$(dom).find("#user-detail-" + key).text(kd);
					}
				}
			});
		},
		/*
		 * 用户分配角色
		 */
		allocateRole: function($btn, value, row, index) {
			mySwal.loadHtml("用户角色分配", "/framework/role/form/role-allocate?id="+value, {
				width: '20rem',
				preConfirm: function() {
					return fetchPostSwalComfirm(
							"/framework/user/allocateRoles?" + $("#roleAllocate-form").serialize(), {}
							, function() {mySwal.success("更新用户角色成功"); });
				}
			});
		},
		/*
		 * 分配用户组
		 */
		allocateGroup: function($btn, value, row, index) {
			mySwal.loadHtml("关联用户组", "/framework/userGroup/form/userGroup-allocate?id="+value, {
				preConfirm: function() {
					var selcDatas = $('#userGroup-allocate-table').bootstrapTable('getSelections');
					var groupidArr = [];
					if (selcDatas != null && selcDatas.length > 0) {
    					for (let selcd of selcDatas) {
    						groupidArr.push(selcd.id);
						}
					}
					return fetchPostSwalComfirm('/framework/user/updateUserWithUsergroupRelationship'
							, {body: JSON.stringify({userId: value, groupIds: groupidArr.join(",")})}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "关联用户组成功"
								});
							});
				}
			});
		},
		/*
		 * 逻辑删除用户
		 */
		remove: function($btn, value, row, index) {
			mySwal.delConfirm({
				// 删除请求
				url: "/framework/user/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					$("#user-table").bootstrapTable('refresh');
				}
			});
		}
	}

// ========== 数据表格加载 ===========
myBsTable.pageTable(_userTable, {
	url: '/framework/user/getPage',
	// 刷新按钮
	search: true,
	searchOnEnterKey: true,
	formatSearch: function () { return '用户名/真实姓名'},
	showRefresh: true,
	columns: [
		{title: '#', width: 50, formatter: function (value, row, index) {return '<strong><i>'+(index+1)+'</i></strong>';}},
		{field: 'username',title: '用户名'},
		{field: 'userInfo.realName',title: '真实姓名'},
		{field: 'userInfo.sex',title: '性别', formatter: function(value, row, index) {
			if (value == 0) return '<i class="fa fa-mars" aria-hidden="true"></i>';
			else if (value == 1) return '<i class="fa fa-venus" aria-hidden="true"></i>';
			else return null;
		}},
		{field: 'gmtCreatetime', title: '创建时间'},
		{field: 'isForbidden',title: '是否禁用', formatter: function(value, row, index) {
			return '<input class="forbidden-switch" type="checkbox" data-id="'+row.id+'"' + 
						 ' data-on-text="禁用" data-off-text="可用"' + 
						 ' data-on-color="warning" data-off-color="info"' + 
						 ' data-size="mini" data-handle-width="25"' +
						 ' data-chan="onForbiddenSwitchChange" data-state="'+ isForbiddenCover(value) +'"/>';
		}},
		{field: 'id',title: '操作', width: 200, events: operateUserEvents, formatter: function(value, row, index) {
			return '<a class="tb-op edit" data-event="edit" title="查看" href="javascript:void(0);"><i class="fa fa-eye"></i></a>' + 
				   '<a class="tb-op allocateRole" data-event="allocateRole" title="分配角色" href="javascript:void(0);"><i class="fa fa-address-card-o"></i></a>' + 
				   '<a class="tb-op allocateGroup" data-event="allocateGroup" title="分配用户组" href="javascript:void(0);"><i class="fa fa-users"></i></a>' + 
				   '<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
		}}
	],
	// 加载完成方法
	onLoadSuccess: function (data) {
		// 初始化开关组件
		mySwitch.load($('.forbidden-switch'));
	},
});




/*
 * 表格头部工具栏按钮点击事件 映射为工具栏事件
 */
$("#user-table-toolbar button").click(function() {
	var event = $(this).data("event");
	operateUserEvents[event]? operateUserEvents[event]($(this)) : "";
}) 




// 禁用开关按钮改变事件
function onForbiddenSwitchChange(event, state) {
	var $btn = $(event.currentTarget);
	var id = $btn.data("id");
	var stateToNum = isForbiddenCover(state);
	
	$.post("/framework/user/updateById", {id: id, isForbidden: stateToNum}, function(res) {
		mySwal.alertForRes(res, {
			text: '用户状态置为' + (state? '禁用' : '启用'),
			onAfterClose: function() {
				if (res && res.code === 200) {
					_userTable.bootstrapTable('updateByUniqueId', {
				        id: id,
				        row: {isForbidden: stateToNum}
				    });
					mySwitch.load($('.forbidden-switch'));
				} else {
					$btn.bootstrapSwitch("state", !state);
				}
			}
		});
	}); 
}