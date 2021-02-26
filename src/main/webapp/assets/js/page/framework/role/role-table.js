// 角色数据表格对象
var _roleTable = $("#role-table");
// 添加角色表单dom
var _roleAdd   = $("#role-add-container").find(".role-table-addRole");
var _menuAlloc = $("#menu-allocate-container").find(".role-table-addRole");
// 工具栏按钮点击事件
window.operateRoleEvents = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			//var dd = $("#user-table").bootstrapTable('getData')
			operateRoleEvents[event]? operateRoleEvents[event]($btn, value, row, index) : "";
		},
		/*
		 * 添加角色
		 */
		addRole: function($btn) {
			var $addRole = _roleAdd.clone();
			mySwal.loadHtml("添加角色", $addRole, {
				preConfirm: function() {
					var addRoleForm = $("#swal2-content .role-table-addRole");
					var flag = addRoleForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/role/addRecord'
							, {body: JSON.stringify(addRoleForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "新增角色成功",
									onOpen: function() {_roleTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		edit: function($btn, value, row, index) {
			var $addRole = _roleAdd.clone();
			mySwal.loadHtml("编辑角色", $addRole, {
				onOpen: function(dom) {
					for ( var key in row) {
						$(dom).find(".role-table-addRole input[name='"+key+"']").val(row[key]);
					}
				},
				preConfirm: function() {
					var addRoleForm = $("#swal2-content .role-table-addRole");
					var flag = addRoleForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/role/updateById'
							, {body: JSON.stringify(addRoleForm.serializeFormToJson())}
							, function(res) {
								load_${echartId }();
								mySwal.alertForRes(res, {
									text: "编辑角色成功",
									onOpen: function() {_roleTable.bootstrapTable('refresh');}
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
				url: "/framework/role/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					_roleTable.bootstrapTable('refresh');
					load_${echartId }();
				}
			});
		},
		/*
		 * 分配菜单
		 */
		allocatePerms: function($btn, value, row, index) {
			var $addRole = _roleAdd.clone();
			mySwal.loadHtml("分配菜单权限", "/framework/menu/form/menu-allocate?id=" + value, {
				width: 600,
				preConfirm: function() {
					var selcDatas = $('#menu-allocate-table').bootstrapTable('getSelections');
					var menuidArr = [];
					if (selcDatas != null && selcDatas.length > 0) {
    					for (let selcd of selcDatas) {
    						menuidArr.push(selcd.id);
						}
					}
					return fetchPostSwalComfirm('/framework/role/updateRoleWithMenuRelationship'
							, {body: JSON.stringify({roleId: value, menuIds: menuidArr.join(",")})}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "分配菜单资源成功"
								});
							});
				}
			});
		}
}


/*
 * 表格头部工具栏按钮点击事件 映射为工具栏事件
 */
$("#role-table-toolbar button").click(function() {
	var event = $(this).data("event");
	operateRoleEvents[event]? operateRoleEvents[event]($(this)) : "";
}) 


//========== 数据表格加载 ===========\
!function(timeout) {
	setTimeout(() => {
		myBsTable.pageTable(_roleTable, {
			url: '/framework/role/getPage',
			columns: [
				{title: '#', width: 50, formatter: function (value, row, index) {return '<strong><i>'+(index+1)+'</i></strong>';}},
				{field: 'name', title: '角色名称'},
				{field: 'descrip', title: '角色描述'},
				{field: 'sort', title: '排序'},
				/* {field: 'isForbidden',title: '是否禁用', formatter: function(value, row, index) {
					return '<input class="role-forbidden-switch" type="checkbox" data-id="'+row.id+'"' + 
								 ' data-on-text="禁用" data-off-text="可用"' + 
								 ' data-on-color="warning" data-off-color="info"' + 
								 ' data-size="mini" data-handle-width="25"' +
								 ' data-chan="onRoleForbiddenSwitchChange" data-state="'+ isForbiddenCover(value) +'"/>';
				}}, */
				{field: 'id',title: '操作', width: 120, events: operateRoleEvents, formatter: function(value, row, index) {
					return '<a class="tb-op edit" data-event="edit" title="编辑角色信息" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' +
						   '<a class="tb-op allocatePerms" data-event="allocatePerms" title="角色权限分配" href="javascript:void(0);"><i class="fa fa-cogs"></i></a>' + 
						   '<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
				}}
			],
			// 加载完成方法
			onLoadSuccess: function (data) {
				// 初始化开关组件
				mySwitch.load($('.role-forbidden-switch'));
			},
		});
	}, timeout);
}(400);

function onRoleForbiddenSwitchChange(event, state) {
	var $btn = $(event.currentTarget);
	var id = $btn.data("id");
	var stateToNum = isForbiddenCover(state);
	
	$.post("/framework/role/updateById", {id: id, isForbidden: stateToNum}, function(res) {
		mySwal.alertForRes(res, {
			text: '角色状态置为' + (state? '禁用' : '启用'),
			onAfterClose: function() {
				if (res && res.code === 200) {
					_roleTable.bootstrapTable('updateByUniqueId', {
				        id: id,
				        row: {isForbidden: stateToNum}
				    });
					
					mySwitch.load($('.role-forbidden-switch'));
				} else {
					$btn.bootstrapSwitch("state", !state);
				}
			}
		});
	});
}