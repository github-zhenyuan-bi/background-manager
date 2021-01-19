package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

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
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sc_bulletin_timer_task")
@ApiModel(value="BulletinTimerTask", description="")
public class BulletinTimerTask extends Model<BulletinTimerTask> {

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
    
    
    @ApiModelProperty(value = "id", position = 5)
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    
    
    @NotNull(message="定时任务的模板选择不能为空", groups= {FormValid.class})
    @ApiModelProperty(value = "模板id", position = 10)
    private String templateId;
    
    
    
    @ApiModelProperty(value = "定时推送的实际内容，空则使用模板内容", position = 15)
    private String content;
    
    
    
    @NotNull(message="定时任务的推送方式不能为空", groups= {FormValid.class})
    @ApiModelProperty(value = "推送方式 1 定时推送 2 周期推送", position = 20)
    private String sendMode;
    
    
    
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "定时推送 -- 时间", position = 25)
    private Date sendTime;
    
    
    
    @ApiModelProperty(value = "周期推送 -- 表达式", position = 30)
    private String sendCron;
    
    
    
    @ApiModelProperty(value = "发送结果 0 失败 1 成功", position = 35)
    private String sendResult;
    
    
    
    @ApiModelProperty(value = "定时推送任务是否可用", position = 40)
    private Boolean enable;
    
    
    
    @ApiModelProperty(value = "创建人", position = 45)
    @TableField(fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 50)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @ApiModelProperty(value = "修改人", position = 55)
    @TableField(fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "逻辑删除", position = 60)
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}