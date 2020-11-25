package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
 * 日志记录 
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
@TableName("T_LOG")
@ApiModel(value="Log", description="日志记录")
public class Log extends Model<Log> {

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
    
    
    
    @ApiModelProperty(value = "访问者", position = 10)
    @TableField("ACCESSOR")
    private String accessor;
    
    
    
    @ApiModelProperty(value = "访问者IP", position = 15)
    @TableField("ACCESSOR_IP")
    private String accessorIp;
    
    
    
    @ApiModelProperty(value = "访问时间", position = 20)
    @TableField("ACCESS_TIME")
    private Date accessTime;
    
    
    
    @ApiModelProperty(value = "访问模块", position = 25)
    @TableField("ACCESS_MODULE")
    private String accessModule;
    
    
    
    @ApiModelProperty(value = "备用字段1 暂时用作日志类别", position = 30)
    @TableField("FIELD1")
    private String field1;
    
    
    
    @ApiModelProperty(value = "备用字段2", position = 35)
    @TableField("FIELD2")
    private String field2;
    
    
    
    @ApiModelProperty(value = "备用字段3", position = 40)
    @TableField("FIELD3")
    private String field3;
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}