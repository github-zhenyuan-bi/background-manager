package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 菜单角色关联关系 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_ROLE_MENU")
@ApiModel(value="RoleMenu", description="菜单角色关联关系")
public class RoleMenu extends Model<RoleMenu> {

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
    
    
    @ApiModelProperty(value = "角色id", position = 5)
    @TableField("ROLE_ID")
    private String roleId;
    
    
    
    @ApiModelProperty(value = "菜单id", position = 10)
    @TableField("MENU_ID")
    private String menuId;
    

}