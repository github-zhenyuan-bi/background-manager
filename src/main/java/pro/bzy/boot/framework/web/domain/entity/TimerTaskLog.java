package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pro.bzy.boot.framework.utils.DateUtil;

/**
 *  
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-12-07
 */
 
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_timer_task_log")
@ApiModel(value="TimerTaskLog", description="")
public class TimerTaskLog extends Model<TimerTaskLog> {

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
    
    
    
    @ApiModelProperty(value = "定时任务ID", position = 10)
    private String taskId;
    
    
    
    @ApiModelProperty(value = "任务执行开始时间", position = 15)
    private Date taskStartTime;
    
    
    
    @ApiModelProperty(value = "任务执行结束时间", position = 20)
    private Date taskEndTime;
    
    
    
    @ApiModelProperty(value = "执行消耗时长", position = 25)
    private Integer taskCostTime;
    
    
    
    @ApiModelProperty(value = "执行结果0失败1成功", position = 30)
    private String execSuccess;
    
    
    
    @ApiModelProperty(value = "执行结果说明", position = 35)
    private String execResult;
    
    
    public TimerTaskLog() {}
    public TimerTaskLog(TimerTask timerTask) {
        this.taskId = timerTask.getId();
        this.taskStartTime = DateUtil.getNow();
    }
    
    
    public TimerTaskLog calculateCostTime() {
        Long cost = taskEndTime.getTime() - taskStartTime.getTime();
        setTaskCostTime(Integer.valueOf(cost.toString()));
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}