package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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

/**
 * VIEW 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2021-01-25
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("v_bulletin_timer_task_detail")
@ApiModel(value="BulletinTimerTaskDetail", description="VIEW")
public class BulletinTimerTaskDetail extends Model<BulletinTimerTaskDetail> {

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
    private String id;
    
    
    
    @ApiModelProperty(value = "定时推送的实际内容，空则使用模板内容", position = 10)
    private String content;
    
    
    
    @ApiModelProperty(value = "", position = 15)
    @TableField("sendMode")
    private String sendMode;
    
    
    
    @ApiModelProperty(value = "", position = 20)
    private String taskEnbale;
    
    
    
    @ApiModelProperty(value = "", position = 25)
    private String sendResult;
    
    
    
    @ApiModelProperty(value = "定时推送 -- 时间", position = 30)
    private Date sendTime;
    
    
    
    @ApiModelProperty(value = "周期推送 -- 表达式", position = 35)
    private String sendCron;
    
    
    
    @ApiModelProperty(value = "id", position = 40)
    private String templateId;
    
    
    
    @ApiModelProperty(value = "标题", position = 45)
    private String title;
    
    
    
    @ApiModelProperty(value = "", position = 50)
    private Long sendCount;
    
    
    @TableField(exist=false)
    private Date nextTimeForCron;
    

    @Override
    protected Serializable pkVal() {
        return null;
    }

}