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
@TableName("sc_recharge_card")
@ApiModel(value="RechargeCard", description="")
public class RechargeCard extends Model<RechargeCard> {

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
    
    
    
    @ApiModelProperty(value = "充值卡名称", position = 10)
    private String name;
    
    
    
    @ApiModelProperty(value = "图片uri", position = 15)
    private String imageUri;
    
    
    
    @ApiModelProperty(value = "卡原价", position = 20)
    private Integer originalPrice;
    
    
    
    @ApiModelProperty(value = "卡折扣价", position = 25)
    private Integer discountPrice;
    
    
    
    @ApiModelProperty(value = "卡类型", position = 30)
    private String cardType;
    
    
    
    @ApiModelProperty(value = "卡价值", position = 35)
    private Integer cardValue;
    
    
    
    @ApiModelProperty(value = "有效期开始时间", position = 40)
    private Date validityStarttime;
    
    
    
    @ApiModelProperty(value = "有效期结束时间", position = 45)
    private Date validityEndtime;
    
    
    
    @ApiModelProperty(value = "卡使用说明", position = 50)
    private String instructions;
    
    
    
    @ApiModelProperty(value = "卡是否上架", position = 55)
    private Boolean enabled;
    
    
    
    @ApiModelProperty(value = "创建人", position = 60)
    @TableField(fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 65)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @ApiModelProperty(value = "修改人", position = 70)
    @TableField(fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "逻辑删除", position = 75)
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}