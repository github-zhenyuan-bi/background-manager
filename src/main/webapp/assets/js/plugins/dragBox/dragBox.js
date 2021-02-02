!function($) {
    "use strict";

    function Pointer(x, y) {
        this.x = x;
        this.y = y;
    }

    function Position(left, top) {
        this.left = left;
        this.top = top;
    }

    const STYLE_OPTION = {
    	style_direction   : "row",
    	style_itemContent : "width: 100%; height: 100%; display: flex; flex-wrap: wrap;",
    	style_positionBox : "margin: 15px; visibility:hidden;",
    	style_item        : "cursor: pointer;",
    	style_itemImg     : "width: 100%; height: 100%; border-radius: 6px;",
    	style_close       : "display: none;width: 20px;height: 20px;top: 0;right: 0;z-index: 9999;position: absolute;text-align: center;font-size: 16px;cursor: pointer;color: aliceblue",
    	style_dragBox_width: '150px',
    	style_dragBox_height: '150px',
    };

    var DragBox = function() {};

    DragBox.prototype.itemContainer = null;
	DragBox.prototype.buildItemContainer = function() {
		this.itemContainer = $('<div class="item_content" style="flex-direction:'+STYLE_OPTION.style_direction+';'+STYLE_OPTION.style_itemContent+'"></div>');
	},
	DragBox.prototype.buildItemPositionBox = function() {
		return $('<div class="position-box" style="width: '+STYLE_OPTION.style_dragBox_width+'; height:'+STYLE_OPTION.style_dragBox_height+';'+STYLE_OPTION.style_positionBox+'"></div>');
	},
	DragBox.prototype.buildItem = function() {
		var $item =  $('<div class="item" style="width: '+STYLE_OPTION.style_dragBox_width+'; height:'+STYLE_OPTION.style_dragBox_height+';'+STYLE_OPTION.style_item+'"></div>');
		$item.mouseenter(function(event) {
			var $cls = $item.find("span");
			$cls.show();
		}).mouseleave(function(event) {
			var $cls = $item.find("span");
			$cls.hide();
		});
		return $item;
	},
	DragBox.prototype.buildImgDom = function(option, item, imgUrl) {
		var $img =  $('<img src="'+imgUrl+'" style="'+STYLE_OPTION.style_itemImg+'">');
		$img.dblclick(function(e){
			e.preventDefault();
		  	if (option.doubleClickImgAction) {
				option.doubleClickImgAction(item, option);
			} else {
				console.log("doubleClickImgAction 事件暂未设定");
			}
		});
		return $img;
	},
	DragBox.prototype.buildCloseDom = function(option, item) {
		var $close =  $('<span style="'+STYLE_OPTION.style_close+'">×</span>');
		$close.click(function(e) {
			e.preventDefault();
			if (option.closeAction) {
				option.closeAction(item, option);
			} else {
				console.log("关闭按钮事件暂未设定");
			}
		});
		return $close;
	},



    DragBox.prototype.initDragImgs = function(option) {
    	var __container = $(option.container);
    	var __imgs      = option.datas;
    	var __width     = option.width;
    	var __height    = option.height;
    	var __direction = option.direction;
    	var __imgUrlField = option.imgUrlField? option.imgUrlField : 'url';

		if ($.isEmptyObject(__container))
    		throw "拖拽盒子容器必须指定 container";
    	if (__width)
    		STYLE_OPTION.style_dragBox_width = __width;
    	if (__height)
			STYLE_OPTION.style_dragBox_height = __height;
		if (__direction)
			STYLE_OPTION.style_direction = __direction;
    	
    	this.buildItemContainer();
    	if (__imgs && __imgs.length > 0) {
    		var $positionBox, $item, $imgDom;
    		for (var i = 0; i < __imgs.length; i++) {
    			const imgUrl = __imgs[i][__imgUrlField];
    			var posBox = this.buildItemPositionBox()
    							.append(this.buildItem()
    								.append(this.buildImgDom(option, __imgs[i], imgUrl))
    								.append(this.buildCloseDom(option, __imgs[i])));
	    		this.itemContainer.append(posBox);
	    	}
    	}
    	__container.append(this.itemContainer);
    	this.enableDrag(option);
    },


    DragBox.prototype.enableDrag = function(option) {
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


    $.DragBox = new DragBox, $.DragBox.Constructor = DragBox
}(window.jQuery);