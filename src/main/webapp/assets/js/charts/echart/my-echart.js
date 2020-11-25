/**
 * =================================
 * 	    图表颜色池1 较鲜艳
 * =================================
 */
colorPool = ['#93b69c', '#deb068', '#c4a3bf', '#eebbcb', '#bce2e8', '#839b5c', '#a59aca', '#d8e698', '#00a3af', '#c099a0', '#a6a5c4'];



/**
 * =================================
 * 	    基础饼图1 默认option
 * =================================
 */
PIE_OPTION_TYPE1 = {
		title: {
	        text: '饼图-类型1-标题',
	        subtext: null,
	        left: 'center'
	    },
	    tooltip: {
	        trigger: 'item',
	        formatter: '{a} <br/>{b} : {c} ({d}%)'
	    },
	    legend: {
	        // orient: 'vertical',
	        bottom: 'bottom',
	        // TODO 需填模块数据
            /*['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
             */
	        data: []
	    },
	    series: [
	        {
	            name: '访问来源',
	            type: 'pie',
	            radius: '55%',
	            center: ['50%', '50%'],
	            // TODO 需填充数值数据
	            /*
	             * {value: 335, name: '直接访问'},
	             * {value: 310, name: '邮件营销'}
	             * ...
	             */
	            data: [],
	            emphasis: {
	                itemStyle: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
}



/**
 * =================================
 * 	    我的echart图表方法
 * =================================
 */
var myEchart = {
		/**
		 * 加载基础饼图1
		 * chartDom：容器dom 只能用js document.getElementById(id) 不能用jq
		 * option：定制化opt
		 */
		pie_1: function(chartDom, option) {
			var myChart = echarts.init(chartDom);
			var opt = cloneOption(PIE_OPTION_TYPE1);
			
			// 标题填充
			if (option.title)    opt.title.text = option.title;
			// 副标题填充
			if (option.subTitle) opt.title.subtext = option.subTitle;
			// legend数据
			if (option.legend_data) opt.legend.data = option.legend_data;
			// series数据
			if (option.series_data) opt.series[0].data = option.series_data;
			
			opt.color = fetchColorFromPool(8);
			myChart.setOption(opt);
		}
}



/**
 * =================================
 * 	    我的echart公共方法 
 * =================================
 */
/**
 * 克隆option
 * @param commonOption 基础默认option
 * @returns
 */
function cloneOption(commonOption) {
	var opt = {};
	$.extend(opt, commonOption);
	return opt;
};

/**
 * 从颜色池中选取颜色
 * @param num
 * @returns
 */
function fetchColorFromPool(num) {
	var colorArr = [];
	for (var index = 0; index < num; index++) {
		var index2 = Math.floor((Math.random() * colorPool.length));
		var curColor = colorPool[index2];
		if (colorArr.indexOf(curColor) == -1)
			colorArr.push( colorPool[index2] );
		else 
			index--;
	}
	return colorArr;
}

/**
 * 将从服务器请求的数据结果加工成图标需要的
 * @param data 原始数据
 * @param nameField 名称字段名
 * @param valueField 值字段名
 * @returns
 */
function handleRequestDataForEchart_1(data, nameField, valueField) {
	if ($.isEmptyObject(data))
		throw "请求结果为空，无法构造图标需要得参数";
	
	var opt = {legendData: null, seriesData: null};
	var legendData = [];
	var seriesData = [];
	if ($.type(data) === "array") {
		var seriesData_item = null;
		for (var i = 0; i < data.length; i++) {
			seriesData_item = {};
			try {
				legendData.push(data[i][nameField]);
				
				seriesData_item.name  = data[i][nameField];
				seriesData_item.value = data[i][valueField];
				seriesData.push(seriesData_item);
			} catch (e) {
				console.error(e);
			}
		}
		opt.legendData = legendData;
		opt.seriesData = seriesData;
	}
	return opt;
}