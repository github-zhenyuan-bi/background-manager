/** 
 * 我的开关组件 
 */
var mySwitch = {
		/*
		 *  初始化开关组件 
		 */
		load: function($slec) {
			var size = $slec.data("size");
			var onChange = $slec.data("chan");
			
			$slec.bootstrapSwitch();
			$slec.bootstrapSwitch("size", size? size : "small");
			$slec.each(function() {
				$(this).bootstrapSwitch("state", $(this).data("state"));
			})
			if (onChange) {
				var changeFunc = eval(onChange);
				$slec.bootstrapSwitch("onSwitchChange", function(event, state) {
					changeFunc(event, state);
				});
			}
		}
}
