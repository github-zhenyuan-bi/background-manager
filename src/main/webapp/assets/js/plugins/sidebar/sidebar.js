/*
 Template Name: Fonik - Responsive Bootstrap 4 Admin Dashboard
 Author: Themesbrand
 File: Main js
 */

!function($) {
    "use strict";

    var SideBar = function() {};
    SideBar.prototype.dom = null;
    
    var OPTION = {
    		"bgcStyle": "width: 100vw;height: 100vh;top: 0;z-index: 9999;",
    		"bgcColor": "rgba(0, 0, 0, 0.5);",
    		
    		"showTitle": true,
    		"title": "标题",
    		
    		"contentDom": "",
    		"contentUrl": "",
    		
    		"showButton": true,
    		"confirmBtnText": "提交",
    		"confirmBtnStyle": "",
    		"confirmBtnColor": "btn-primary",
    		"confirmBtnAction": null,
    		"cancelBtnText": "取消",
    		"cancelBtnStyle": "",
    		"cancelBtnColor": "btn-secondary",
    		"cancelBtnAction": null,
    };
    
    // ======================================背景遮罩板======================================
    SideBar.prototype.backgroundCover = null,
    SideBar.prototype.initBackgroundCover = function () {
    	this.backgroundCover = $('<div class="bg-cover position-fixed invisible" style="background-color:'+ OPTION.bgcColor + OPTION.bgcStyle +'"></div>');
    },
    
    // ======================================标题======================================
    SideBar.prototype.title = null,
    SideBar.prototype.initTitle = function () {
    	this.title = $('<div style="padding: 5px 0; margin-bottom: 10px;" class="bgc-box-title border-bottom '+(OPTION.showTitle?'':'invisible')+'"><h5>'+OPTION.title+'</h5></div>');
    },
    
    // ======================================内容======================================
    SideBar.prototype.content = null,
    SideBar.prototype.initContent = function () {
    	const __this = this;
    	__this.content = $('<div style="padding: 10px 0;overflow-y: scroll;overflow-x: none;" class="noScrollBar"></div>');
    	if (OPTION.contentUrl && OPTION.contentUrl != "")
    		$.get(contentUrl).done(res=>{__this.content.append(res);}).fail(err=>{console.log(err)});
    	else if (OPTION.contentDom)
    		__this.content.append(OPTION.contentDom);
    },
    
    // ======================================按钮======================================
    SideBar.prototype.button = null,
    SideBar.prototype.$confirmBtn = null,
    SideBar.prototype.$cancelBtn = null,
    SideBar.prototype.initButton = function () {
    	var __this = this;
    	var showBtn = OPTION.showButton? '' : 'invisible';
    	var $btnWrapper = $('<div style="padding: 5px 0; bottom: 0;" class="bgc-box-btns d-flex justify-content-around position-relative '+showBtn+'"></div>');
    	this.$confirmBtn = $('<button class="btn '+OPTION.confirmBtnColor+' w-md" type="button" style="'+OPTION.confirmBtnStyle+'">'+OPTION.confirmBtnText+'</button>')
    	this.$cancelBtn  = $('<button class="btn '+OPTION.cancelBtnColor+' w-md" type="button" style="'+OPTION.cancelBtnStyle+'">'+OPTION.cancelBtnText+'</button>');
    	if (OPTION.confirmBtnAction && $.type(OPTION.confirmBtnAction) === "function")
    		this.$confirmBtn.click(function(e) {OPTION.confirmBtnAction(__this.dom, e)});
    	if (OPTION.cancelBtnAction && $.type(OPTION.cancelBtnAction) === "function")
    		this.$cancelBtn.click(function(e) {OPTION.cancelBtnAction(__this.dom, e)});
    	$btnWrapper.append(this.$confirmBtn).append(this.$cancelBtn);
    	__this.button = $btnWrapper
    },
    
    // ======================================侧边栏部分======================================
    SideBar.prototype.box = '',
    SideBar.prototype.initBox = function () {
    	this.initTitle();
    	this.initContent();
    	this.initButton();
    	const __boxWrapperStyle = "padding: 20px; height: 100vh;min-width: 200px;top: 0;z-index: 99999;background-color: #fff;box-shadow: -1px 0px 15px 1px #767676;border-top-left-radius: 20px;border-bottom-left-radius: 20px;";
    	var $boxWrapper = $('<div class="position-fixed d-flex flex-column" style="'+__boxWrapperStyle+'"></div>');
    	$boxWrapper.append(this.title).append(this.content).append(this.button);
    	this.box = $boxWrapper;
    },
    
    SideBar.prototype.show = function() {
    	this.backgroundCover.removeClass("invisible");
    	this.box.animate({ 
    	    right: '0px'
    	  }, 400);
    },
    SideBar.prototype.hide = function() {
    	this.backgroundCover.addClass("invisible");
    	this.box.animate({ 
    	    right: '-' + (this.box.width()+50) + 'px'
    	  }, 400);
    },
    
    // ======================================初始化对象======================================
    SideBar.prototype.init = function (option) {
    	$.extend(OPTION, option);
    	this.initBackgroundCover();
    	this.initBox();
    	this.dom = $("<div></div>").append(this.backgroundCover).append(this.box).children();
    	$("body").append(this.dom);
    	this.box.css("right", '-' + (this.box.width()+50) + 'px');
    	return this;
    },
    
    
    
    //init
    $.SideBar = new SideBar, $.SideBar.Constructor = SideBar
}(window.jQuery),

//initializing
function ($) {
    "use strict";
}(window.jQuery);
