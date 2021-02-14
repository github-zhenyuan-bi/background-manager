!function($) {

  'use strict';

  var console = window.console || { log: function () {} };


  	/*=================================================*/
  	/*         【构建图片上传裁剪模态框dom】                         */
  	/*=================================================*/
  	function buildAvatarModal(option) {
  		if (!option.id)
  			throw "必须指定图片裁剪组件的唯一id属性";
  		
  		var $existedModal = $("#"+option.id);
  		if ($existedModal.length > 0) {
  			$existedModal.remove();
  		}
  			
	    var title = option.title? option.title : '图片上传';
	    var fileLabel = option.fileLabel? option.fileLabel : '选择本地图片';
	    var sumitBtnText = option.sumitBtnText? option.sumitBtnText : '上传';
	    var imgInputName = option.imgInputName? option.imgInputName : 'img';
    
	    return '<div class="container" id="'+option.id+'">' +
	              '<!-- Cropping modal -->' +
	              '<div class="modal fade avatar-modal" style="z-index: 9999999;" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">' +
	                '<div class="modal-dialog modal-lg">' +
	                  '<div class="modal-content">' +
	                    '<form class="avatar-form" action="" enctype="multipart/form-data" method="post">' +
	                      '<div class="modal-header">' +
	                        '<h4 class="modal-title" id="avatar-modal-label" style="margin-top: 0">'+title+'</h4>' +
	                        '<button class="close" data-dismiss="modal" type="button">×</button>' +
	                      '</div>' +
	                      '<div class="modal-body">' +
	                        '<div class="avatar-body">' +
	
	                          '<!-- Upload image and data -->' +
	                          '<div class="avatar-upload">' +
	                            '<input class="avatar-src" name="avatar_src" type="hidden">' +
	                            '<input class="avatar-data" name="avatar_data" type="hidden">' +
	                            '<div class="form-group">' +
		                          '<label for="avatarInput">'+fileLabel+'</label>' +
		                          '<input type="file" class="form-control-file avatar-input" name="'+imgInputName+'">' +
		                        '</div>' +
	                            //'<label for="avatarInput">'+fileLabel+'</label>' +
	                            //'<input class="avatar-input btn btn-link" id="avatarInput" name="avatar_file" type="file">' +
	                          '</div>' +
	
	                          '<!-- Crop and preview -->' +
	                          '<div class="row">' +
	                            '<div class="col-md-9">' +
	                              '<div class="avatar-wrapper"></div>' +
	                            '</div>' +
	                            '<div class="col-md-3">' +
	                              '<div class="avatar-preview preview-lg"></div>' +
	                              '<div class="avatar-preview preview-md"></div>' +
	                              '<div class="avatar-preview preview-sm"></div>' +
	                            '</div>' +
	                          '</div>' +
	
	                          '<div class="row avatar-btns">' +
	                            '<!-- <div class="col-md-9">' +
	                              '<div class="btn-group">' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="-90" type="button" title="Rotate -90 degrees">Rotate Left</button>' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="-15" type="button">-15deg</button>' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="-30" type="button">-30deg</button>' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="-45" type="button">-45deg</button>' +
	                              '</div>' +
	                              '<div class="btn-group">' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="90" type="button" title="Rotate 90 degrees">Rotate Right</button>' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="15" type="button">15deg</button>' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="30" type="button">30deg</button>' +
	                                '<button class="btn btn-primary" data-method="rotate" data-option="45" type="button">45deg</button>' +
	                              '</div>' +
	                            '</div> -->' +
	                            '<div class="col">' +
	                              '<button class="btn btn-primary btn-block avatar-save" type="submit">'+sumitBtnText+'</button>' +
	                            '</div>' +
	                          '</div>' +
	                        '</div>' +
	                      '</div>' +
	                      '<!-- <div class="modal-footer">' +
	                        '<button class="btn btn-default" data-dismiss="modal" type="button">Close</button>' +
	                      '</div> -->' +
	                    '</form>' +
	                  '</div>' +
	                '</div>' +
	              '</div><!-- /.modal -->' +
	
	              '<!-- Loading state -->' +
	              '<div class="loading" aria-label="Loading" role="img" tabindex="-1"></div>' +
	            '</div>';
	  }
  



  // ============================================================
  //                       【构造对象】
  // ============================================================
  function CropAvatar() {
	  this.$container = null;
      this.option = null;
      this.$avatarView  = null;
      this.$avatar      = null;
      this.$avatarModal = null;
      this.$loading     = null;
      this.$avatarForm  = null;
      this.$avatarUpload = null;
      this.$avatarSrc   = null;
      this.$avatarData  = null;
      this.$avatarInput = null;
      this.$avatarSave  = null;
      this.$avatarBtns  = null;
      this.$avatarWrapper = null;
      this.$avatarPreview = null;
      this.support.datauri = null;
      
      // ============================================================
      //                       【监听器】
      // ============================================================
      this.addListener = function (option) {
        this.$avatarView.on('click', $.proxy(this.click, this));
        this.$avatarInput.on('change', $.proxy(this.change, this));
        this.$avatarForm.on('submit', $.proxy(this.submit, this));
        this.$avatarBtns.on('click', $.proxy(this.rotate, this));
      };
      // ============================================================
      //                       【提交事件】
      // ============================================================
      this.submit = function () {
        if (this.option.submit) {
          this.option.submit(new FormData(this.$avatarForm[0]), this);
          return false;
        } else {
          if (!this.$avatarSrc.val() && !this.$avatarInput.val()) {
            return false;
          }

          if (this.support.formData) {
            this.ajaxUpload();
            return false;
          }
        }
      };
  }




  CropAvatar.prototype = {
    constructor: CropAvatar,

    support: {
      fileList: !!$('<input type="file">').prop('files'),
      blobURLs: !!window.URL && URL.createObjectURL,
      formData: !!window.FormData
    },


    // ============================================================
    //                       【初始化】
    // ============================================================
    init: function (option) {
      $("body").append(buildAvatarModal(option));
      
      this.$container = $("#" + option.id);
      this.option = option;

      this.$avatarView  = $(this.option.trigger);
      this.$avatar      = this.$avatarView.find('img');
      this.$avatarModal = this.$container.find('.avatar-modal');
      this.$loading     = this.$container.find('.loading');

      this.$avatarForm  = this.$avatarModal.find('.avatar-form');
      this.$avatarUpload = this.$avatarForm.find('.avatar-upload');
      this.$avatarSrc   = this.$avatarForm.find('.avatar-src');
      this.$avatarData  = this.$avatarForm.find('.avatar-data');
      this.$avatarInput = this.$avatarForm.find('.avatar-input');
      this.$avatarSave  = this.$avatarForm.find('.avatar-save');
      this.$avatarBtns  = this.$avatarForm.find('.avatar-btns');

      this.$avatarWrapper = this.$avatarModal.find('.avatar-wrapper');
      this.$avatarPreview = this.$avatarModal.find('.avatar-preview');

      this.support.datauri = this.support.fileList && this.support.blobURLs;

      if (!this.support.formData) {
        this.initIframe();
      }
      if (option.dragEnable)
    	  dragEnable(this.$container.find('.modal-dialog')[0])

      this.initTooltip();
      this.initModal();
      this.addListener(option);
      return this;
    },


    // ============================================================
    //                       【提示】
    // ============================================================
    initTooltip: function () {
      /*this.$avatarView.tooltip({
        placement: 'bottom'
      });*/
    },

    // ============================================================
    //                       【模态框】
    // ============================================================
    initModal: function () {
      this.$avatarModal.modal({
        show: false
      });
    },

    // ============================================================
    //                       【预览模块】
    // ============================================================
    initPreview: function () {
      /*var url = this.$avatar.attr('src');
      this.$avatarPreview.empty().html('<img src="' + url + '">');*/
    },

    // ============================================================
    //                       【iframe】
    // ============================================================
    initIframe: function () {
      var target = 'upload-iframe-' + (new Date()).getTime(),
          $iframe = $('<iframe>').attr({
            name: target,
            src: ''
          }),
          _this = this;

      // Ready ifrmae
      $iframe.one('load', function () {

        // respond response
        $iframe.on('load', function () {
          var data;

          try {
            data = $(this).contents().find('body').text();
          } catch (e) {
            console.log(e.message);
          }

          if (data) {
            try {
              data = $.parseJSON(data);
            } catch (e) {
              console.log(e.message);
            }

            _this.submitDone(data);
          } else {
            _this.submitFail('Image upload failed!');
          }

          _this.submitEnd();

        });
      });

      this.$iframe = $iframe;
      this.$avatarForm.attr('target', target).after($iframe.hide());
    },









    // ============================================================
    //                       【点击事件 模态框展示】
    // ============================================================
    click: function () {
      this.$avatarModal.modal('show');
      this.initPreview();
    },

    // ============================================================
    //                       【图片改变事件】
    // ============================================================
    change: function () {
      var files,
          file;

      if (this.support.datauri) {
        files = this.$avatarInput.prop('files');
        if (files.length > 0) {
          file = files[0];
          if (this.isImageFile(file)) {
            if (this.url) {
              URL.revokeObjectURL(this.url); // Revoke the old one
            }

            this.url = URL.createObjectURL(file);
            this.startCropper();
          }
        }
      } else {
        file = this.$avatarInput.val();

        if (this.isImageFile(file)) {
          this.syncUpload();
        }
      }
    },


    rotate: function (e) {
      var data;

      if (this.active) {
        data = $(e.target).data();

        if (data.method) {
          this.$img.cropper(data.method, data.option);
        }
      }
    },

    isImageFile: function (file) {
      if (file.type) {
        return /^image\/\w+$/.test(file.type);
      } else {
        return /\.(jpg|jpeg|png|gif)$/.test(file);
      }
    },

    startCropper: function () {
      var _this = this;

      if (this.active) {
        this.$img.cropper('replace', this.url);
      } else {
        this.$img = $('<img src="' + this.url + '">');
        this.$avatarWrapper.empty().html(this.$img);
        var __cropp = this.$img.cropper({
          aspectRatio: this.option.aspectRatio | null,
          preview: this.option.preview? this.option.preview : this.$avatarPreview.selector,
          strict: true,
          crop: function (data) {
            var json = [
                  '{"x":' + data.x,
                  '"y":' + data.y,
                  '"height":' + data.height,
                  '"width":' + data.width,
                  '"rotate":' + data.rotate + '}'
                ].join();
            _this.$avatarData.val(json);
          }
        });
        this.active = true;
      }
    },

    stopCropper: function () {
      if (this.active) {
        this.$img.cropper('destroy');
        this.$img.remove();
        this.active = false;
      }
    },

    ajaxUpload: function () {
      console.log("default submit method");
      return false;
      /*var url = this.$avatarForm.attr('action'),
          data = new FormData(this.$avatarForm[0]),
          _this = this;

      $.ajax(url, {
        type: 'post',
        data: data,
        dataType: 'json',
        processData: false,
        contentType: false,

        beforeSend: function () {
          _this.submitStart();
        },

        success: function (data) {
          _this.submitDone(data);
        },

        error: function (XMLHttpRequest, textStatus, errorThrown) {
          _this.submitFail(textStatus || errorThrown);
        },

        complete: function () {
          _this.submitEnd();
        }
      });*/
    },

    syncUpload: function () {
      this.$avatarSave.click();
    },

    submitStart: function () {
      this.$loading.fadeIn();
    },

    submitDone: function (data) {
      if ($.isPlainObject(data) && data.state === 200) {
        if (data.result) {
          this.url = data.result;

          if (this.support.datauri || this.uploaded) {
            this.uploaded = false;
            this.cropDone();
          } else {
            this.uploaded = true;
            this.$avatarSrc.val(this.url);
            this.startCropper();
          }

          this.$avatarInput.val('');
        } else if (data.message) {
          this.alert(data.message);
        }
      } else {
        this.alert('Failed to response');
      }
    },

    submitFail: function (msg) {
      this.alert(msg);
    },

    submitEnd: function () {
      this.$loading.fadeOut();
    },

    cropDone: function () {
      this.$avatarForm.get(0).reset();
      this.$avatar.attr('src', this.url);
      this.stopCropper();
      this.$avatarModal.modal('hide');
    },

    alert: function (msg) {
      var $alert = [
            '<div class="alert alert-danger avater-alert">',
              '<button type="button" class="close" data-dismiss="alert">&times;</button>',
              msg,
            '</div>'
          ].join('');

      this.$avatarUpload.after($alert);
    }
  };



  $.CropAvatar = new CropAvatar, $.CropAvatar.Constructor = CropAvatar;
  $.CropAvatarBuilder = function(option) {
	  var ca = new CropAvatar();
	  ca.init(option);
	  return ca;
  }
}(window.jQuery);
