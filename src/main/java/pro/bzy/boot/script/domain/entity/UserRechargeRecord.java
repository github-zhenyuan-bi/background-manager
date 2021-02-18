package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
 *  
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2021-02-17
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sc_user_recharge_record")
@ApiModel(value="UserRechargeRecord", description="")
public class UserRechargeRecord extends Model<UserRechargeRecord> {

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
    
    
    
    @ApiModelProperty(value = "用户id", position = 10)
    private String userId;
    
    
    
    @ApiModelProperty(value = "充值卡id", position = 15)
    private String rechargeCardId;
    
    
    
    @ApiModelProperty(value = "充值时间", position = 20)
    private Date rechargeTime;
    
    
    
    @ApiModelProperty(value = "充值消费", position = 25)
    private Float rechargeCost;
    
    
    

    @Override
    protected Serializable pkVal() {
        return null;
    }

}