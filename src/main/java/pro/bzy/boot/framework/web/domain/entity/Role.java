package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import pro.bzy.boot.framework.web.annoations.FormValid;

/**
 * 角色 
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
@JsonIgnoreProperties({"deleted", "datascope", "flag", "userHas", "gmtCreator", "gmtModifier"})
@ToString(of = {"name"})
@Accessors(chain = true)
@TableName("T_ROLE")
@ApiModel(value="Role", description="角色")
public class Role extends Model<Role> {

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
    
    
    @NotNull(message="角色名不能为空", groups= {FormValid.class})
    @Length(min=1, max=30, message="角色名长度应在1-20个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "角色名", position = 10)
    @TableField("NAME")
    private String name;
    
    
    
    @ApiModelProperty(value = "角色权限 ", position = 15)
    @TableField("ROLE_KEY")
    private String roleKey;
    
    
    
    @Min(value = 1, message="排序的最小值为1", groups= {FormValid.class})
    @ApiModelProperty(value = "显示排序", position = 20)
    @TableField("SORT")
    private Integer sort;
    
    
    
    @ApiModelProperty(value = "0禁用 1启用", position = 25)
    @TableField(value = "IS_FORBIDDEN", fill = FieldFill.INSERT)
    private Integer isForbidden;
    
    
    
    @ApiModelProperty(value = "逻辑删除 0删除 1不删除 ", position = 30)
    @TableLogic
    @TableField(value = "DELETED", fill = FieldFill.INSERT)
    private Integer deleted;
    
    
    
    @ApiModelProperty(value = "角色作用域", position = 35)
    @TableField("DATASCOPE")
    private Integer datascope;
    
    
    
    @ApiModelProperty(value = "角色是否至少被某个用户关联", position = 40)
    @TableField("FLAG")
    private Integer flag;
    
    
    
    @ApiModelProperty(value = "创建人id", position = 45)
    @TableField(value = "GMT_CREATOR", fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "修改人id", position = 50)
    @TableField(value = "GMT_MODIFIER", fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 55)
    @TableField(value = "GMT_CREATETIME", fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @NotNull(message="角色说明不能为空", groups= {FormValid.class})
    @Length(min=1, max=30, message="角色说明长度应在1-20个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "说明", position = 60)
    @TableField("DESCRIP")
    private String descrip;
    
    
    @ApiModelProperty(hidden= true)
    @TableField(exist= false)
    private Boolean userHas;
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}