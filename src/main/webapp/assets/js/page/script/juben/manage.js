// 用户数据表格
var _scriptTable = $("#script-table");
// 表格工具栏事件
window.operateScriptEvents = {
		// e: 事件对象  value: 单元格绑定的字段值  row: 当前行对象  index: 行编号
		'click .tb-op': function(e, value, row, index) {
			var $btn = $(e.currentTarget);
			var event = $btn.data("event");
			//var dd = $("#user-table").bootstrapTable('getData');
			operateScriptEvents[event]? operateScriptEvents[event]($btn, value, row, index) : "";
		},
		/*
		 * 增加剧本
		 */
		addScript: function($btn, value, row, index) {
			mySwal.loadHtml("新增剧本", "/script/juben/form/script-add", {
				width: '60rem',
				onOpen: function(dom) {
					var $scriptForm = $(dom).find(".script-table-addScript");
					$scriptForm.parsley();
				},
				preConfirm: function() {
					var $scriptForm = $("#swal2-content .script-table-addScript");
					// 表单验证
					var formValidRes = $scriptForm.parsley().validate();
					if (!formValidRes) 
						return false;
					// 表单提交
					var formDate = $scriptForm.serializeFormToJson();
					var mCount = parseInt(formDate.gamerMaleCount);
					var fmCount = parseInt(formDate.gamerFemaleCount);
					return fetchPostSwalComfirm("/script/juben/addRecord", {
								body: JSON.stringify(formDate)
							}, function(res) {
								_scriptTable.bootstrapTable('refresh');
								
								var jubenData = res.data;
								operateScriptEvents.allocateTag(null, jubenData.id, jubenData, null, function(){
									operateScriptEvents.charac(null, jubenData.id, jubenData);
								})
							});  
				}
			});
		},
		/*
		 * 编辑剧本
		 */
		edit: function($btn, value, row, index) {
			mySwal.loadHtml("编辑剧本信息", "/script/juben/form/script-add?id="+value, {
				width: '60rem',
				preConfirm: function() {
					var $scriptForm = $("#swal2-content .script-table-addScript");
					// 表单验证
					var formValidRes = $scriptForm.parsley().validate();
					if (!formValidRes) 
						return false;
					// 表单提交
					return fetchPostSwalComfirm("/script/juben/updateById", {
								body: JSON.stringify($scriptForm.serializeFormToJson())
							}, function(res) {
								mySwal.alertForRes(res, {
									text: "编辑剧本成功",
									onOpen: function() {_scriptTable.bootstrapTable('refresh');}
								});
							});
				}
			});
		},
		/*
		 * 人物编辑
		 */
		charac: function($btn, value, row, index) {
			var mCount = parseInt(row.gamerMaleCount);
			var fmCount = parseInt(row.gamerFemaleCount);
			var rwUrl = "/script/juben/form/juben-characters?mCount=" + mCount + "&fmCount=" + fmCount + "&jubenId=" + value;
			mySwal.loadHtml("剧本人物编辑", rwUrl, {
				width: '60rem',
				onOpen: function(dom) {
					$(dom).find(".script-table-addCharacter2").each(function(){   $(this).parsley();   });
				},
				preConfirm: function() {
					// 表单验证
					var pass = true;
					var $scriptForm = $("#swal2-content .script-table-addCharacter2");
					$scriptForm.each(function() {
						var flag = $(this).parsley().validate();
						if (!flag)
							pass = false;
					});
					if (!pass) 
						return false;
					
					// 收集表单数据
					var fromDatas = [];
					$scriptForm.each(function() {
						fromDatas.push($(this).serializeFormToJson());
					});
					// 表单提交
					return fetchPostSwalComfirm("/script/jubenCharacter/saveOrUpdateRecords?jubenId=" + value, {
								body: JSON.stringify(fromDatas)
							}, function(res) {
								mySwal.alertForRes(res, {
									text: "剧本人物编辑成功",
									onOpen: function() {}
								});
							});
				}
		  	});
		},
		allocateTag: function($btn, value, row, index, sucfunc) {
			mySwal.loadHtml("标签", "/script/juben/allocateTag?jubenId="+value, {
				preConfirm: function() {
					var $tagForm = $("#swal2-content .inp-cbx");
					var formData = $tagForm.serializeFormToJson();
					var tagIds = formData.tagId, datas = [];
					if (tagIds && tagIds.length > 0) {
						for (var i = 0; i < tagIds.length; i++) {
							datas.push({
								tagId: tagIds[i],
								jubenId: value,
								sort: i,
							});
						}
					}
					// 表单提交
					return fetchPostSwalComfirm("/script/juben/rebindTagRelationship/"+value, {
								body: JSON.stringify(datas)
							}, function(res) {
								if (sucfunc) {
									sucfunc();
								} else {
									mySwal.alertForRes(res, {
										text: "分配标签成功",
										onOpen: function() {}
									});
								}
							});
				}
			});
		},
		/*
		 * 删除剧本
		 */
		remove: function($btn, value, row, index) {
			mySwal.delConfirm({
				// 删除请求
				url: "/script/juben/deleteById?id="+value,
				// 删除成功后的回调方法
				afterConfirm: function(res) {
					_scriptTable.bootstrapTable('refresh');
				}
			});
		}
}
// 增加剧本按钮点击事件
$("#script-table-toolbar button").click(function() {
	var event = $(this).data("event");
	operateScriptEvents[event]? operateScriptEvents[event]($(this)) : "";
});

// 表格加载
myBsTable.pageTable(_scriptTable, {
	url: '/script/juben/getPage',
	fixedColumns: true,
    fixedNumber: +1,
    fixedRightNumber: +1,
	columns: [
		{title: '#', width: 50, formatter: function (value, row, index) {return '<strong><i>'+(index+1)+'</i></strong>';}},
		{field: 'coverImg', title: '封面', formatter: function(value, row, index) {
			if (value) {
				/* var pIndex = value.lastIndexOf('.');
				var ysimgurl = value.substr(0, pIndex) + '-thumbnail' + value.substr(pIndex); */
				return '<img style="width: auto; height: 80px;" src="'+value+'"></img>'
			}
		}},
		{field: 'name', title: '名称', class: 'mytb-col strong'},
		{field: 'jbType', title: '类型', formatter: function(value, row, index) {
			var text = null;
			if (value == 1) text = '<span class="juben-type-pt">普通本</span>';
			if (value == 2) text = '<span class="juben-type-dj">城市限定</span>';
			if (value == 3) text = '<span class="juben-type-csxd">独家本</span>';
			return text; 
		}},
		{field: 'difficulty', title: '难度', formatter: function(value, row, index) {
			var text = null;
			if (value == 1) text = '<span class="juben-diffc-jd">简单</span>';
			if (value == 2) text = '<span class="juben-diffc-yb">一般</span>';
			if (value == 3) text = '<span class="juben-diffc-kn">困难</span>';
			if (value == 4) text = '<span class="juben-diffc-yh">硬核</span>';
			return text; 
		}},
		{field: 'gamerCount', title: '男/女', formatter: function(value, row, index) {
			return row.gamerMaleCount + '/' + row.gamerFemaleCount;
		}},
		{field: 'gameTime', title: '时长(h)'},
		{field: 'importPrice', title: '进价(元)'},
		{field: 'sellPrice', title: '售价（元/人）'},
		{field: 'roleReversalEnbale', title: '反串', formatter: function(value, row, index) {
			var text = null;
			if (value == 1) text = '允许反串';
			if (value == 0) text = '不允许';
			return text; 
		}},
		{field: 'isSell', title: '上架', formatter: function(value, row, index) {
			var text = null;
			if (value == 1) text = '上架';
			if (value == 0) text = '下架';
			return text; 
		}},
		/* {field: 'isForbidden',title: '是否禁用', formatter: function(value, row, index) {
			return '<input class="role-forbidden-switch" type="checkbox" data-id="'+row.id+'"' + 
						 ' data-on-text="禁用" data-off-text="可用"' + 
						 ' data-on-color="warning" data-off-color="info"' + 
						 ' data-size="mini" data-handle-width="25"' +
						 ' data-chan="onRoleForbiddenSwitchChange" data-state="'+ isForbiddenCover(value) +'"/>';
		}}, */
		{field: 'id',title: '操作', width: 220, events: operateScriptEvents, formatter: function(value, row, index) {
			return '<a class="tb-op edit" data-event="edit" title="编辑角色信息" href="javascript:void(0);"><i class="fa fa-pencil"></i></a>' +
				   '<a class="tb-op charac" data-event="charac" title="人物编辑" href="javascript:void(0);"><i class="fa fa-users"></i></a>' + 
				   '<a class="tb-op allocateTag" data-event="allocateTag" title="标签" href="javascript:void(0);"><i class="fa fa-cogs"></i></a>' + 
				   '<a class="tb-op remove" data-event="remove" title="删除" href="javascript:void(0);"><i class="fa fa-trash-o"></i></a>';
		}}
	],
	// 加载完成方法
	onLoadSuccess: function (data) {
		// 初始化开关组件
		//mySwitch.load($('.role-forbidden-switch'));
	},
});