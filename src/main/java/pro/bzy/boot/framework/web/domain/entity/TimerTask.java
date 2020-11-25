package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 定时器任务 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_TIMER_TASK")
@ApiModel(value="TimerTask", description="定时器任务")
public class TimerTask extends Model<TimerTask> {

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
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;
    
    
    
    @ApiModelProperty(value = "任务名称", position = 10)
    @TableField("NAME")
    private String name;
    
    
    
    @ApiModelProperty(value = "任务类型", position = 15)
    @TableField("TASK_TYPE")
    private Integer taskType;
    
    
    
    @ApiModelProperty(value = "任务执行时间间隔策略表达式", position = 20)
    @TableField("CORN")
    private String corn;
    
    
    
    @ApiModelProperty(value = "是否启用", position = 25)
    @TableField("ENABLE")
    private Integer enable;
    
    
    
    @ApiModelProperty(value = "定时任务说明", position = 30)
    @TableField("DESCP")
    private String descp;
    
    
    
    @ApiModelProperty(value = "定时任务执行方法名", position = 35)
    @TableField("METHODNAME")
    private String methodname;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}