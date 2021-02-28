package pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity;

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
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString(of= {"isSubtitle", "ctKey"})
@ApiModel(value="WxMiniprogramSettingCooperationsContent", description="")
public class WxMiniprogramSettingCooperationsContent extends Model<WxMiniprogramSettingCooperationsContent> {

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
    
    
    
    @ApiModelProperty(value = "合作表 cooperation的id", position = 10)
    private String coopId;
    
    
    
    @ApiModelProperty(value = "是否副标题", position = 15)
    @TableField("is_subTitle")
    private Boolean isSubtitle;
    
    
    
    @ApiModelProperty(value = "键", position = 20)
    private String ctKey;
    
    
    
    @ApiModelProperty(value = "值", position = 25)
    private String ctValue;
    
    
    
    @ApiModelProperty(value = "css样式", position = 30)
    private String ctCss;
    
    
    
    @ApiModelProperty(value = "排序", position = 35)
    private Integer sort;
    
    
    
    @ApiModelProperty(value = "创建人", position = 40)
    @TableField(fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 45)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @ApiModelProperty(value = "修改人", position = 50)
    private String gntModifier;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}