package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
@TableName("sc_recharge_card_tags")
@ApiModel(value="RechargeCardTags", description="")
public class RechargeCardTags extends Model<RechargeCardTags> {

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
    
    
    
    @ApiModelProperty(value = "充值卡id", position = 10)
    private String rechargeCardId;
    
    
    
    @ApiModelProperty(value = "名称前缀", position = 15)
    private String namePrefix;
    
    
    
    @ApiModelProperty(value = "名称", position = 20)
    private String name;
    
    
    
    @ApiModelProperty(value = "创建人", position = 25)
    @TableField(fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 30)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @ApiModelProperty(value = "修改时间", position = 35)
    @TableField(fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}