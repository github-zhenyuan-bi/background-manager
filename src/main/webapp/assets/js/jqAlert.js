(function($) {
	$.extend({
		jqAlert : function(option) {
			let _this = this;
			var settings = {
				type : 'info',
				content : '提示内容',
				autoClose : true
			};
			var $dom = $('.my_alert-wrapper');
			if ($dom.length === 0) {
				$(document.body).append(
						'<div class="my_alert-wrapper"></div>');
			}
			$dom = $('.my_alert-wrapper');
			$.extend(settings, option);
			let box = $('<div class="my_alertBox" animation=""></div>');
			box.addClass('my_alertBox--' + settings.type);
			box.attr("style", settings.style_box? settings.style_box : "");
			let typeIcon = $('<i class="my_alert-icon iconfont"></i>');
			typeIcon.addClass('icon-alert-' + settings.type);
			let contentBox = $('<div class="my_alert-content"></div>');
			contentBox.text(settings.content);
			contentBox.attr("style", settings.style_content? settings.style_content : "");
			let closeIcon = $('<i class="my_alert-closebtn iconfont icon-close"></i>');
			box.append(typeIcon).append(contentBox).append(closeIcon);
			$dom.append(box);
			if (settings.autoClose === true) {
				var time = settings.time | 3 * 1000;
				setTimeout(function() {
					box.remove();
				}, time);
			}
			closeIcon.on('click', function() {
				box.remove();
			});
		},
		jqAlert_small(option) {
			$.extend(option, {
				style_box: "width: 300px;",
				style_content: "width: 280px;"
			});
			$.jqAlert(option);
		}
	});
})(jQuery);