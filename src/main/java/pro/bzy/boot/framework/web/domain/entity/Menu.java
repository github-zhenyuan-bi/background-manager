package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.annoations.FormValid;
import pro.bzy.boot.framework.web.domain.bean.TreeDomain;

/**
 * 菜单资源 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_MENU")
@ApiModel(value="Menu", description="菜单资源")
public class Menu extends Model<Menu> implements TreeDomain<Menu>{

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
    
    
    
    @ApiModelProperty(value = "父级菜单id", position = 10)
    @TableField("PID")
    private String pid;
    
    
    
    @NotNull(message="菜单名称不能为空", groups= {FormValid.class})
    @Length(min=1, max=20, message="菜单名称长度应在1-20个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "菜单名称", position = 15)
    @TableField("NAME")
    private String name;
    
    
    
    @NotNull(message="菜单url不能为空", groups= {FormValid.class})
    @Length(min=1, max=1000, message="菜单url长度应在1-1000个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "菜单url", position = 20)
    @TableField("URL")
    private String url;
    
    
    
    @ApiModelProperty(value = "菜单图表码", position = 25)
    @TableField("ICON")
    private String icon;
    
    
    
    @ApiModelProperty(value = "菜单显示顺序", position = 30)
    @TableField(value="SORT")
    private Integer sort;
    
    
    
    @NotNull(message="菜单url不能为空", groups= {FormValid.class})
    @Length(min=1, max=1000, message="菜单url长度应在1-1000个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "权限过滤表达式", position = 35)
    @TableField("FILTER_EXP")
    private String filterExp;
    
    
    
    @ApiModelProperty(value = "创建人", position = 40)
    @TableField(value = "GMT_CREATOR", fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "修改人", position = 45)
    @TableField(value = "GMT_MODIFIER", fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 50)
    @TableField(value = "GMT_CREATETIME", fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @ApiModelProperty(value = "逻辑删除 0否1是", position = 55)
    @TableLogic
    @TableField(value = "DELETED", fill = FieldFill.INSERT)
    private Integer deleted;
    
    
    
    @ApiModelProperty(value = "菜单类型", position = 60)
    @TableField(value = "MENU_TYPE")
    private String menuType;
    
    
    
    /** 是否含有子菜单 */
    @TableField(exist = false)
    private Boolean hasChild; 
    
    
    /** 子菜单  */
    @TableField(exist = false)
    private List<Menu> childs;
    
    
    
    /**
     * 获取默认的跟菜单
     * @return
     */
    public static Menu getDefualtRootMenu() {
        return builder().id(SystemConstant.TREE_ROOT_ID).build();
    }
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}