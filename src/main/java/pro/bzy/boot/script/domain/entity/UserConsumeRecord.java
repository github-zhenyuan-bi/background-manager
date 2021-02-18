package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("sc_user_consume_record")
@ApiModel(value="UserConsumeRecord", description="")
public class UserConsumeRecord extends Model<UserConsumeRecord> {

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
    
    
    
    @ApiModelProperty(value = "用户id", position = 10)
    private String userId;
    
    
    
    @ApiModelProperty(value = "消费商品类型", position = 15)
    private String consumeGoodsType;
    
    
    
    @ApiModelProperty(value = "消费商品id", position = 20)
    private String consumeGoodsId;
    
    
    
    @ApiModelProperty(value = "实付消费值", position = 25)
    private Float consumeSum;
    
    
    
    @ApiModelProperty(value = "应付消费值", position = 30)
    private Float originalPrice;
    
    
    
    @ApiModelProperty(value = "消费时间", position = 35)
    private Date consumeTime;
    
    
    
    @ApiModelProperty(value = "说明", position = 40)
    private String instructions;
    
    
    
    @ApiModelProperty(value = "订单状态", position = 45)
    private Integer orderStatus;
    
    
    
    @ApiModelProperty(value = "预留字段1", position = 50)
    private String filed1;
    
    
    
    @ApiModelProperty(value = "预留字段2", position = 55)
    private String filed2;
    
    
    
    @ApiModelProperty(value = "预留字段3", position = 60)
    private String filed3;
    
    
    
    @ApiModelProperty(value = "预留字段4", position = 65)
    private Integer filed4;
    
    
    
    @ApiModelProperty(value = "预留字段5", position = 70)
    private Float filed5;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}