package pro.bzy.boot.framework.web.domain.entity;

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
 * 用户组 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_USERGROUP")
@ApiModel(value="Usergroup", description="用户组")
public class Usergroup extends Model<Usergroup> {

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
    
    
    
    @ApiModelProperty(value = "用户组名称", position = 10)
    @TableField("NAME")
    private String name;
    
    
    
    @ApiModelProperty(value = "用户组描述", position = 15)
    @TableField("DESCRIP")
    private String descrip;
    
    
    
    @ApiModelProperty(value = "用户组类型", position = 20)
    @TableField("GROUP_TYPE")
    private String groupType;
    
    
    
    @ApiModelProperty(value = "创建人id", position = 25)
    @TableField(value = "GMT_CREATOR", fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "修改人id", position = 30)
    @TableField(value = "GMT_MODIFIER", fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 35)
    @TableField(value = "GMT_CREATETIME", fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}