/*
 Template Name: Fonik - Responsive Bootstrap 4 Admin Dashboard
 Author: Themesbrand
 File: Main js
 */

!function($) {
    "use strict";

    var SideBar = function() {
    	this.option = null;
    	this.dom = null;
    	this.backgroundCover = null;
    	this.title = null;
    	this.content = null;
    	this.button = null;
    	this.$confirmBtn = null;
    	this.$cancelBtn = null;
    	this.box = '';
    };
    //SideBar.prototype.dom = null;
    //SideBar.prototype.backgroundCover = null,
    
    var OPTION = {
    		"bgcStyle": "width: 100vw;height: 100vh;top: 0;z-index: 9;",
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
    SideBar.prototype.initBackgroundCover = function () {
    	this.backgroundCover = $('<div class="bg-cover position-fixed invisible" style="background-color:'+ this.option.bgcColor + this.option.bgcStyle +'"></div>');
    },
    
    // ======================================标题======================================
    SideBar.prototype.initTitle = function () {
    	this.title = $('<div style="padding: 5px 0; margin-bottom: 10px;" class="bgc-box-title border-bottom '+(this.option.showTitle?'':'invisible')+'"><h5>'+this.option.title+'</h5></div>');
    },
    
    // ======================================内容======================================
    SideBar.prototype.initContent = function () {
    	const __this = this;
    	__this.content = $('<div style="padding: 10px 0;overflow-y: scroll;overflow-x: none;" class="noScrollBar"></div>');
    	if (this.option.contentUrl && this.option.contentUrl != "") {
    		$.get(this.option.contentUrl).done(res=>{
    			__this.content.append(res);
    			if (this.box)
    				this.box.css("right", '-' + (this.box.width()+50) + 'px');
    		}).fail(err=>{console.log(err)});
		} else if (this.option.contentDom) {
    		__this.content.append(this.option.contentDom);
    		if (this.box)
    			this.box.css("right", '-' + (this.box.width()+50) + 'px');
    	}
    },
    SideBar.prototype.loadContent = function (ct) {
    	const __this = this;
    	__this.content.empty();
    	if (ct) {
    		__this.content.append(ct);
    		if (this.box)
    			this.box.css("right", '-' + (this.box.width()+50) + 'px');
    	} else {
    		if (this.option.contentUrl && this.option.contentUrl != "") {
        		$.get(this.option.contentUrl).done(res=>{
        			__this.content.append(res);
        			if (this.box)
        				this.box.css("right", '-' + (this.box.width()+50) + 'px');
        		}).fail(err=>{console.log(err)});
    		} else if (this.option.contentDom) {
        		__this.content.append(this.option.contentDom);
        		if (this.box)
        			this.box.css("right", '-' + (this.box.width()+50) + 'px');
        	}
    	}
    },
    
    // ======================================按钮======================================
    SideBar.prototype.initButton = function () {
    	var __this = this;
    	var showBtn = this.option.showButton? '' : 'invisible';
    	var $btnWrapper = $('<div style="padding: 5px 0; bottom: 0;" class="bgc-box-btns d-flex justify-content-around position-relative '+showBtn+'"></div>');
    	this.$confirmBtn = $('<button class="btn '+this.option.confirmBtnColor+' w-md" type="button" style="'+this.option.confirmBtnStyle+'">'+this.option.confirmBtnText+'</button>')
    	this.$cancelBtn  = $('<button class="btn '+this.option.cancelBtnColor+' w-md" type="button" style="'+this.option.cancelBtnStyle+'">'+this.option.cancelBtnText+'</button>');
    	if (this.option.confirmBtnAction && $.type(this.option.confirmBtnAction) === "function")
    		this.$confirmBtn.click(function(e) {__this.option.confirmBtnAction(__this.dom, e)});
    	if (this.option.cancelBtnAction && $.type(this.option.cancelBtnAction) === "function")
    		this.$cancelBtn.click(function(e) {__this.option.cancelBtnAction(__this.dom, e)});
    	$btnWrapper.append(this.$confirmBtn).append(this.$cancelBtn);
    	__this.button = $btnWrapper
    },
    
    // ======================================侧边栏部分======================================
    SideBar.prototype.initBox = function () {
    	this.initTitle();
    	this.initContent();
    	this.initButton();
    	const __boxWrapperStyle = "padding: 20px; height: 100vh;min-width: 200px;top: 0;z-index: 10;background-color: #fff;box-shadow: -1px 0px 15px 1px #767676;border-top-left-radius: 20px;border-bottom-left-radius: 20px;";
    	var $boxWrapper = $('<div class="position-fixed d-flex flex-column" style="'+__boxWrapperStyle+'"></div>');
    	$boxWrapper.append(this.title).append(this.content).append(this.button);
    	this.box = $boxWrapper;
    },
    
    SideBar.prototype.show = function(func) {
    	if (func && $.type(func) === "function") 
    		func(this);
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
    	var thisOpt = {};
    	$.extend(thisOpt, OPTION, option);
    	this.option = thisOpt;
    	this.initBackgroundCover();
    	this.initBox();
    	this.dom = $("<div></div>").append(this.backgroundCover).append(this.box).children();
    	$("body").append(this.dom);
    	this.box.css("right", '-' + (this.box.width()+50) + 'px');
    	return this;
    },
    
    
    
    //init
    $.SideBar = new SideBar, $.SideBar.Constructor = SideBar
    $.SideBarbuilder = function(option) {
    	var sb = new SideBar();
    	sb.init(option);
    	return sb;
    }
}(window.jQuery),

//initializing
function ($) {
    "use strict";
}(window.jQuery);
