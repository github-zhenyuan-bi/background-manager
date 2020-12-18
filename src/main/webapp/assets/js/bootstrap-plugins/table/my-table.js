window.icons = {
    refresh: 'fa fa-refresh',
    columns: 'ion-md-menu',
    fullscreen: 'ion-md-expand'
 }



/**
 * 公共表格Option
 */
COMMON_TABLE_OPTION = {
		// id字段
		idField: 'id',
		uniqueId: "id",
		// 样式
		classes: "table table-hover",
		// 表格加载完成事件
		onLoadSuccess: function (data) {
			console.log("table load finished");
		},
		// 头部样式颜色
		theadClasses: 'thead-light myTableTheadClass',
		// 头部对其方式
		halign:"center", 
		// 表体对其方式
		align:"center",
		// 图标样式
		icons: "icons",
		// 按钮样式
		buttonsClass: "primary"
}



/**
 * 默认列表表格Option
 */
DEFAULT_LIST_TABLE_OPTIONS = {}
$.extend(DEFAULT_LIST_TABLE_OPTIONS, COMMON_TABLE_OPTION);
$.extend(DEFAULT_LIST_TABLE_OPTIONS, {
	// 表格
	toggle: 'table',
	// 是否分页
	pagination: false,
	pageList: "",
	paginationLoop: false,
	// 后台数据分页
	sidePagination: "server",
	// 请求数据处理器
	responseHandler: function(res) {
		return res.data;
	},
	// 请求参数处理 分页参数变量指定
	queryParams: function(params) {
		/*params.pageNo   = params.offset / params.limit + 1; 
		params.pageSize = params.limit;
		delete params.offset;
		delete params.limit;
		return params;*/
	}
});


/**
 * 默认分页表格Option
 */
DEFAULT_PAGE_TABLE_OPTIONS = {}
$.extend(DEFAULT_PAGE_TABLE_OPTIONS, COMMON_TABLE_OPTION);
$.extend(DEFAULT_PAGE_TABLE_OPTIONS, {
	// 表格
	toggle: 'table',
	// 是否分页
	pagination: true,
	pageList: "",
	paginationLoop: false,
	// 后台数据分页
	sidePagination: "server",
	// 请求数据处理器
	responseHandler: function(res) {
		if (res.code === 200) {
			var pageDatas = {};
			pageDatas.rows = res.data.records;
			pageDatas.total = res.data.total
			return pageDatas;
		} else if (res.code === 600) {
			redirectToLogin(res.code)
		}
	},
	// 请求参数处理 分页参数变量指定
	queryParams: function(params) {
		params.pageNo   = params.offset / params.limit + 1; 
		params.pageSize = params.limit;
		delete params.offset;
		delete params.limit;
		return params;
	}
});


/**
 * 默认树形表格Option
 */
DEFAULT_TREEGRID_TABLE_OPTIONS = {};
$.extend(DEFAULT_TREEGRID_TABLE_OPTIONS, COMMON_TABLE_OPTION);
$.extend(DEFAULT_TREEGRID_TABLE_OPTIONS, {
	// 表格
	toggle: 'table',
	// 展开按钮出现的字段
	treeShowField: 'name',
	// id字段
	idField: 'id',
	// 父id字段
    parentIdField: 'pid',
    // 请求数据处理
    responseHandler: function(res) {
    	if (res.code === 200) { 
    		var dataList = res.data;
        	if (dataList) {
        		for (let item of dataList) {
    				if (item.pid == '-1')
    					item.pid = null;
    			}
        	}
        	return dataList;
    	} else if (res.code === 600) {
    		redirectToLogin(res.code)
		}
	},
    // 异步请求分支数据
    onPostBody: function() {
    	//console.warn('must be override onPostBody');
    }
});



/**
 * 我的table表格组件
 */
var myBsTable = {
		/*
		 * 列表表格
		 * $selc: table表格的dom
		 * options：初始化参数
		 */
		listTable: function($selc, options) {
			var opt = {};
			$.extend(opt, DEFAULT_LIST_TABLE_OPTIONS);
			$.extend(opt, options);
			alignTableHeadAndColumn(opt);
			$selc.bootstrapTable(opt);
		},
		/*
		 * 分页表格
		 * $selc: table表格的dom
		 * options：初始化参数
		 */
		pageTable: function($selc, options) {
			var opt = {};
			$.extend(opt, DEFAULT_PAGE_TABLE_OPTIONS);
			$.extend(opt, options);
			alignTableHeadAndColumn(opt);
			$selc.bootstrapTable(opt);
		},
		/*
		 * 树形表格
		 * $selc: table表格的dom
		 * options：初始化参数
		 */
		treeTable: function($selc, options) {
			var opt = {};
			$.extend(opt, DEFAULT_TREEGRID_TABLE_OPTIONS);
			$.extend(opt, options);
			alignTableHeadAndColumn(opt);
			$selc.bootstrapTable(opt);
		}
}



function alignTableHeadAndColumn(options) {
	var halign = options.halign;
	var align = options.align;
	if (halign || align) {
		var columns = options.columns;
		var rel_halign = halign? halign : null;
		var rel_align  = align? align : null;
		for (let column of columns) {
			column.halign = column.halign? column.halign : rel_halign;
			column.align  = column.align? column.align : rel_align;
		}
	}
}

function headStyle(column){
    return{
        css:{'font-weight':'bold'}
    }
}
