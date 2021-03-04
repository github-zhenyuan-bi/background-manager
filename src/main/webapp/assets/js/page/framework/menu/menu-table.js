var _menuTable = $('#menu-table');
var _menuAdd   = $("#menu-add-container").find(".menu-table-addMenu");
//初始化操作按钮的方法
window.operateMenuEvents = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			operateMenuEvents[event]? operateMenuEvents[event]($btn, value, row, index) : "";
		},
		addMenu: function($btn) {
			var $addMenu = _menuAdd.clone();
			mySwal.formHtml("添加资源菜单", $addMenu, {
				preConfirm: function() {
					var _addMenuForm = $("#swal2-content .menu-table-addMenu");
					var flag = _addMenuForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/menu/addRecord'
							, {body: JSON.stringify(_addMenuForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "新增菜单资源成功",
									onOpen: function() {_menuTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		edit: function($btn, value, row, index) {
			var $editMenu = _menuAdd.clone();
			mySwal.formHtml("编辑资源菜单", $editMenu, {
				onOpen: function() {
					row.pid = row.pid? row.pid : '-1';
					if (row.pid && row.pid != '-1') {
    					
    					// 窗口打开前 加载可做选择得父菜单
    					var curTableData = _menuTable.bootstrapTable('getData');
    					var rootMenus = $.grep( curTableData, function(item, index){
    						var pid = item.pid? item.pid : '-1';
    						return pid == '-1';
    					});
    					var options = '';
    					for (let item of rootMenus) 
    						options += '<option value="'+item.id+'">'+item.name+'</option>';
						
    					var _editMenuForm = $("#swal2-content .menu-table-addMenu");
    					_editMenuForm.find("select[name='pid']").append(options);
					}
					$("#swal2-content .menu-table-addMenu").find("[name]").each(function(){
	    				var name = this.name;
	    				this.value = row[name];
	    			});
				},
				preConfirm: function() {
				    var _editMenuForm = $("#swal2-content .menu-table-addMenu");
				    var newRow = _editMenuForm.serializeFormToJson();
				    var flag = _editMenuForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/menu/updateById'
							, {body: JSON.stringify(newRow)}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "编辑菜单资源成功",
									onOpen: function() {
										_menuTable.bootstrapTable('refresh');
									}
								});
							}); 
				}
			});
		}, 
		add: function($btn, value, row, index) {
			var $addMenu = _menuAdd.clone();
			mySwal.formHtml("添加子菜单", $addMenu, {
				onOpen: function() {
					var _addMenuForm = $("#swal2-content .menu-table-addMenu");
					_addMenuForm.find("select[name='pid']").html(
							'<option value="'+value+'">'+row.name+'</option>');
				},
				preConfirm: function() {
					var _addMenuForm = $("#swal2-content .menu-table-addMenu");
					var flag = _addMenuForm.parsley().validate();
					if (!flag) return false;
					return fetchPostSwalComfirm('/framework/menu/addRecord'
							, {body: JSON.stringify(_addMenuForm.serializeFormToJson())}
							, function(res) {
								mySwal.alertForRes(res, {
									text: "新增菜单资源成功",
									onOpen: function() {_menuTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		remove: function($btn, value, row, index) {
			mySwal.delConfirm({
				// 删除请求
				url: "/framework/menu/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					_menuTable.bootstrapTable('removeByUniqueId', value)
					//_menuTable.bootstrapTable('refresh');
				}
			});
		},
		perms: function($btn, value, row, index) {
			var $cooperationSideBar = null;
		  	function initSideBar() {
				if ($cooperationSideBar == null) {
					$cooperationSideBar = $.SideBarbuilder({
						title: "菜单资源权限配置",
						//contentUrl: "/wx/wxMiniprogramSetting/page/cooperationFormEdit",
						//contentDom: "/wx/wxMiniprogramSetting/page/cooperationFormEdit",
						confirmBtnAction: function(dom) {
							var $form = dom.find("#menu-perms-form");
							var urlExps = $form.find("input[name='urlExp']");
							var perms   = $form.find("input[name='perm']");
							var datas = [];
							var size = urlExps.length;
							for (var i = 0; i < size; i++) {
								var dataTemp = {urlExp: urlExps[i].value, perm: perms[i].value};
								datas.push(dataTemp);
							}
							fetchPost("/framework/permission/updateMenuPerms", {body: JSON.stringify(datas)}, function(res) {
								console.log(res)
							});
						},
						cancelBtnAction: function(dom) {
							$cooperationSideBar.hide();
						}
					});
				}
			}
		  	initSideBar();
		  	$cooperationSideBar.show(function(sidebar) {
 				$cooperationSideBar.option.contentUrl = "/framework/menu/form/menu-perms?id="+value;
 				sidebar.loadContent();
 			});
		}
		
};

// 表格顶部按钮 点击事件
$("#menu-table-toolbar button").click(function() {
	var event = $(this).data("event");
	operateMenuEvents[event]? operateMenuEvents[event]($(this)) : "";
}) 
	
// 表格初始化
myBsTable.treeTable(_menuTable, {
	url: '/framework/menu/getList',
	// 在哪一列展开树形 按钮
    treeShowField: 'name',
    // 字段
    columns: [
        { field: 'name',  title: '名称', halign: 'left', align: 'left' },
        { field: 'url',  title: '资源名称地址', halign: 'left', align: 'left' },
        { field: 'filterExp',  title: '权限过滤', halign: 'left', align: 'left' },
        { field: 'menuType',  title: '类别' },
        { field: 'icon',  title: '图标样式', formatter:  function(value, row, index) {
        	if (value) {
        		return '<i class="'+value+'"></i>';
        	}
        }},
        { field: 'sort',  title: '排序' },
        { field: 'id', title: '操作', align: 'center', events : operateMenuEvents, formatter:  function(value, row, index) {
			return '<a class="tb-op edit" data-event="edit" title="编辑菜单信息" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' +
				   (!row.pid || row.pid == '-1'
						   ? '<a class="tb-op add" data-event="add" title="添加子菜单" href="javascript:void(0);"><i class="fa fa-plus"></i></a>'
						   : '') + 
				   '<a class="tb-op perms" data-event="perms" title="权限配置" href="javascript:void(0);"><i class="fa fa-link"></i></a>' +
			   	   '<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
		}},
    ],
    onResetView: function(data) {
        _menuTable.treegrid({
            initialState: 'expanded',// 所有节点都折叠 （展开就是 expanded）
            treeColumn: 0,
            expanderExpandedClass: 'fa fa-caret-down',  //图标样式
            expanderCollapsedClass: 'fa fa-caret-right',
            onChange: function() {
                _menuTable.bootstrapTable('resetWidth');
            }
        });
    }
});	