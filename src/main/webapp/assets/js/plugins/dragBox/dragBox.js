!function($) {
    "use strict";

    // 当前鼠标指针位置对象
    function Pointer(x, y) { this.x = x; this.y = y; }
    // 当前盒子定位对象
    function Position(left, top) { this.left = left; this.top = top; }

    // 常量元素
    var STYLE_OPTION = {
    	style_direction   : "row",
    	style_itemContent : "width: 100%; height: 100%; display: flex; flex-wrap: wrap;",
    	style_positionBox : "margin: 15px; visibility:hidden;",
    	style_item        : "cursor: pointer;",
    	style_itemImg     : "width: 100%; height: 100%; border-radius: 6px;",
    	style_close       : "display: none;width: 20px;height: 20px;top: 0;right: 0;z-index: 9999;position: absolute;text-align: center;font-size: 16px;cursor: pointer;color: aliceblue",
    	style_dragBox_width: '150px',
    	style_dragBox_height: '150px',
    };
    
    
    var style_setting = {
    		itemContainer_css: {
    			"display": "flex",
    			"flex-wrap": "wrap",
    			"flex-direction" : "row",
    			"width": "100%",
    			"height": "100%",
    		},
    		itemPositionBox_css: {
    			"width": "150px",
    			"height": "150px",
    			"margin": "15px", 
    			"visibility": "hidden"
    		},
    		item_css: {
    			"width": "150px",
    			"height": "150px",
    			"cursor": "pointer",
    			"margin": "15px",
    		},
    		img_css: {
    			"width": "100%", 
    			"height": "100%", 
    			"border-radius": "6px",
    		},
    		close_css: {
    			"display": "none",
    			"top": "5px",
    			"right": "5px",
    			"z-index": "9999",
    			"position": "absolute",
    			"text-align": "center",
    			"font-size": "14px",
	    		"cursor": "pointer",
	    		"color": "aliceblue",
	    		"padding": "0 6px",
	    	    "background-color": "rgb(127 126 126 / 40%)",
	    	    "border-radius": "6px",
    		}
    }
    

    // 拖拽盒子对象
    var DragBox = function() {
    	// 拖拽盒子父容器 与其构造方法
		this.itemContainer = null;
		this.option =  {},
		this.data =  [];
    };
    // 拖拽盒子对象原生属性
    DragBox.prototype = {
    		itemContainerBuilder:  function() {
    			this.itemContainer = $('<div class="item_content"></div>');
    			this.itemContainer.css(style_setting.itemContainer_css);
    		},
    		// 每个盒子的初始定位容器
    		itemPositionBoxBuilder: function() {
    			return $('<div class="position-box"></div>').css(style_setting.itemPositionBox_css);
    		},
    		// 盒模型
    		itemBuilder: function() {
    			var $item =  $('<div class="item"></div>').css(style_setting.item_css);
    			$item.mouseenter(function(event) {
    				var $cls = $item.find("span");
    				$cls.show();
    			}).mouseleave(function(event) {
    				var $cls = $item.find("span");
    				$cls.hide();
    			});
    			return $item;
    		},
    		// 盒模型内部的图片内容
    		imgDomBuilder: function(item, imgUrl) {
    			var _this = this;
    			var $img =  $('<img src="'+imgUrl+'">').css(style_setting.img_css);
    			$img.dblclick(function(e){
    				e.preventDefault();
    			  	if (_this.option.doubleClickImgAction) {
    			  		_this.option.doubleClickImgAction(item);
    				} else {
    					console.log("doubleClickImgAction 事件暂未设定");
    				}
    			});
    			return $img;
    		},
    		// 盒模型右上角的关闭按钮
    		closeDomBuilder: function(item) {
    			var _this = this;
    			var $close =  $('<span>×</span>').css(style_setting.close_css);
    			$close.click(function(e) {
    				e.preventDefault();
    				if (_this.option.closeAction) {
    					_this.option.closeAction(item);
    				} else {
    					console.log("关闭按钮事件暂未设定");
    				}
    			});
    			return $close;
    		},
    		// ============================================================================
    		//   【初始化方法】 
    		// ============================================================================
    		init: function(option) {
    			var __container = $(option.container);	// 拖拽盒子的
    	    	
    	    	this.data = option.datas;
    	    	this.wrapper = __container;
    	    	this.option = option;
    	    	
    			this.handleCssSetting();
    			this.itemContainerBuilder();
    			
    			this.appendData(option.datas);
    	    	__container.append(this.itemContainer);
    	    	this.enableDrag(option);
    	    	return this;
    		},
    		appendData: function(datas) {
    			var __imgUrlField = this.option.imgUrlField? this.option.imgUrlField : 'url';	// 图片在json对象中的字段
    			if (datas && datas.length > 0) {
    	    		var $positionBox, $item, $imgDom;
    	    		for (var i = 0; i < datas.length; i++) {
    	    			const imgUrl = datas[i][__imgUrlField];
    	    			var posBox = this.itemPositionBoxBuilder()
    	    							.append(this.itemBuilder()
    	    								.append(this.imgDomBuilder(datas[i], imgUrl))
    	    								.append(this.closeDomBuilder(datas[i])));
    		    		this.itemContainer.append(posBox);
    		    	}
    	    	}
    			return this;
    		},
    		
    		reload: function() {
    			this.clear();
    			this.appendData(this.data).enableDrag(this.option);
    		},
    		
    		clear: function() {
    			this.itemContainer.empty();
    			return this;
    		},
    		
    		handleCssSetting: function() {
    	    	if (this.option.width) {
    	    		style_setting.itemPositionBox_css.width = this.option.width;
        	    	style_setting.item_css.width = this.option.width;
    	    	}
    	    	if (this.option.height) {
    	    		style_setting.itemPositionBox_css.height = this.option.height;
        	    	style_setting.item_css.height =  this.option.height;
    	    	}
    			if (this.option.direction)
    				style_setting.itemContainer_css["flex-direction"] = this.option.direction;
    			
    			return this;
    		},
    		add: function(item) {
    			if (!this.data) this.data = [];
    			this.data.push(item);
    			this.reload();
    			return this;
    		},
    		remove: function(item) {
    			if (item && this.data) {
    				var removeIndex = -1;
    				for (var i = 0; i < this.data.length; i++) {
						const array_element = this.data[i];
						if (item.id == array_element.id) {
							removeIndex = i;
							break;
						}
					}
    				if (removeIndex != -1)
    					this.data.splice(removeIndex, 1);
    			}
    			this.reload();
    			return this;
    		},
    		
    		
    		// ============================================================================
    		//   【拖拽盒子核心实现】 
    		// ============================================================================
    		enableDrag: function(option) {
    	    	var __this = this;
    	    	var dests = __this.itemContainer.find(".item");
    	    	dests.each(function(i) {
    	            //$(".item_content .item").each(function(i) {	
    	            this.init = function() { // 初始化
    	                    this.box = $(this).parent();
    	                    $(this).attr("index", i).css({
    	                        position: "absolute",
    	                        left: this.box.position().left,
    	                        top: this.box.position().top
    	                    }).appendTo(__this.itemContainer);
    	                    this.drag();
    	                },
    	                this.move = function(callback) { // 移动
    	                    $(this).stop(true).animate({
    	                        left: this.box.position().left,
    	                        top: this.box.position().top
    	                    }, 500, function() {
    	                        if (callback) {
    	                            callback.call(this);
    	                        }
    	                    });
    	                },
    	                this.collisionCheck = function() {
    	                    var currentItem = this;
    	                    var direction = null;
    	                    $(this).siblings(".item").each(function() {
    	                    	
    	                        if (
    	                            currentItem.pointer.x > this.box.offset().left &&
    	                            currentItem.pointer.y > this.box.offset().top &&
    	                            (currentItem.pointer.x < this.box.offset().left + this.box.width()) &&
    	                            (currentItem.pointer.y < this.box.offset().top + this.box.height())
    	                        ) {
    	                            // 返回对象和方向
    	                            if (currentItem.box.position().top < this.box.position().top) {
    	                                direction = "down";
    	                            } else if (currentItem.box.position().top > this.box.position().top) {
    	                                direction = "up";
    	                            } else {
    	                                direction = "normal";
    	                            }
    	                            this.swap(currentItem, direction);
    	                        }
    	                    });
    	                },
    	                this.swap = function(currentItem, direction) { // 交换位置
    	                    if (this.moveing) return false;
    	                    var directions = {
    	                        normal: function() {
    	                            var saveBox = this.box;
    	                            this.box = currentItem.box;
    	                            currentItem.box = saveBox;
    	                            this.move();
    	                            $(this).attr("index", this.box.index());
    	                            $(currentItem).attr("index", currentItem.box.index());
    	                        },
    	                        down: function() {
    	                            // 移到上方
    	                            var box = this.box;
    	                            var node = this;
    	                            var startIndex = currentItem.box.index();
    	                            var endIndex = node.box.index();;
    	                            for (var i = endIndex; i > startIndex; i--) {
    	                                var prevNode = __this.itemContainer.find(".item[index=" + (i - 1) + "]")[0];  
    	                                node.box = prevNode.box;
    	                                $(node).attr("index", node.box.index());
    	                                node.move();
    	                                node = prevNode;
    	                            }
    	                            currentItem.box = box;
    	                            $(currentItem).attr("index", box.index());
    	                        },
    	                        up: function() {
    	                            // 移到上方
    	                            var box = this.box;
    	                            var node = this;
    	                            var startIndex = node.box.index();

    	                            var endIndex = currentItem.box.index();;
    	                            for (var i = startIndex; i < endIndex; i++) {
    	                                var nextNode = __this.itemContainer.find(".item[index=" + (i + 1) + "]")[0];  
    	                                node.box = nextNode.box;
    	                                $(node).attr("index", node.box.index());
    	                                node.move();
    	                                node = nextNode;
    	                            }
    	                            currentItem.box = box;
    	                            $(currentItem).attr("index", box.index());
    	                        }
    	                    }
    	                    directions[direction].call(this);
    	                },
    	                this.drag = function() { // 拖拽
    	                    var oldPosition = new Position();
    	                    var oldPointer = new Pointer();
    	                    var isDrag = false;
    	                    var currentItem = null;
    	                    var index = -1;
    	                    var posChange = false;
    	                    $(this).mousedown(function(e) {
    	                        e.preventDefault();
    	                        oldPosition.left = $(this).position().left;
    	                        oldPosition.top = $(this).position().top;
    	                        oldPointer.x = e.clientX;
    	                        oldPointer.y = e.clientY;
    	                        isDrag = true;

    	                        currentItem = this;
    	                        index = $(this).attr("index");

    	                    });
    	                    $(document).mousemove(function(e) {
    	                        var currentPointer = new Pointer(e.clientX, e.clientY);
    	                        if (!isDrag) return false;
    	                        $(currentItem).css({
    	                            "opacity": "0.8",
    	                            "z-index": 999
    	                        });
    	                        var left = currentPointer.x - oldPointer.x + oldPosition.left;
    	                        var top = currentPointer.y - oldPointer.y + oldPosition.top;
    	                        $(currentItem).css({
    	                            left: left,
    	                            top: top
    	                        });
    	                        currentItem.pointer = currentPointer;
    	                        // 开始交换位置

    	                        currentItem.collisionCheck();


    	                    });
    	                    $(document).mouseup(function() {
    	                        if (!isDrag) return false;
    	                        isDrag = false;
    	                        currentItem.move(function() {
    	                            $(this).css({
    	                                "opacity": "1",
    	                                "z-index": 0
    	                            });
    	                            if (index != -1) {
    	                            	var nowIndex = $(this).attr("index");
    	                            	if (nowIndex != index) {
    	                            		if (option.moveAction) {
    	                            			option.moveAction(index, nowIndex, option);
    	                            		} else {
    	                            			console.log("moveAction 未设定")
    	                            		}
    	                            	}
    	                            	index = -1;
    	                            }
    	                        });
    	                    });
    	                }
    	            this.init();
    	        });
    	    },
    		
    };



    $.DragBox = new DragBox, $.DragBox.Constructor = DragBox;
    $.DragBoxBuilder = function(option) {
    	var db = new DragBox();
    	db.init(option);
    	return db;
    }
}(window.jQuery);
