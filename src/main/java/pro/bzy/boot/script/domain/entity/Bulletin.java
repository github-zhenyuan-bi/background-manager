package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pro.bzy.boot.framework.web.annoations.FormValid;

/**
 *  
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-12-14
 */
 
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sc_bulletin")
@ApiModel(value="Bulletin", description="")
public class Bulletin extends Model<Bulletin> {

	/*
		可以手动在部分字段上额外加入以下注解增加验证功能 （import javax.validation.constraints）
		min max value 为注解校验时比较的值
		message 为校验错误时的异常信息
		groups 为自定义场景下才校验该字段
		
		@Length(min=1, max=30, message="", groups = {})
		@NotNull(message="", groups= {})
		@Max(value = 9999, message="", groups= {})
    	@Min(value = 1, message="", groups= {})
	*/
	
	
    private static final long serialVersionUID = 1L;
    
    public Bulletin() {}
    /**
     * 使用模板构造一个通知对象
     * @param template
     */
    public Bulletin(BulletinTemplate template) {
        this.templateId = template.getId();
        this.iconPath = template.getIconPath();
        this.title    = template.getTitle();
        this.titleExtend = template.getTitleExtend();
        this.isFixedTop  = template.getIsFixedTop();
        this.theme    = template.getTheme();
    }
    
    
    
    
    @ApiModelProperty(value = "id", position = 5)
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    
    
    @NotNull(message="通知推送必须使用模板", groups= {FormValid.class})
    @ApiModelProperty(value = "使用模板ID", position = 10)
    private String templateId;
    
    
    
    @ApiModelProperty(value = "图标地址", position = 15)
    private String iconPath;
    
    
    
    @ApiModelProperty(value = "标题", position = 20)
    private String title;
    
    
    
    @ApiModelProperty(value = "标题标签", position = 25)
    private String titleExtend;
    
    
    
    @ApiModelProperty(value = "内容", position = 30)
    private String content;
    
    
    
    @ApiModelProperty(value = "通知主题", position = 35)
    private String theme;
    
    
    
    @ApiModelProperty(value = "是否置顶", position = 40)
    private Boolean isFixedTop;
    
    
    
    @ApiModelProperty(value = "发送方式 1手推 2 系统推送", position = 45)
    private String sendWay;
    
    
    
    @ApiModelProperty(value = "发送时间", position = 50)
    private Date sendTime;
    
    
    
    @ApiModelProperty(value = "发送者 send_way =1 则为系统用户 =2 则为定时任务id", position = 55)
    private String sender;
    
    
    
    @ApiModelProperty(value = "逻辑删除", position = 60)
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}