package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pro.bzy.boot.framework.web.annoations.FormValid;

/**
 * 用户 
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
@JsonIgnoreProperties({"deleted", "gmtCreator", "gmtModifier"})
@TableName("T_USER")
@ApiModel(value="User", description="用户")
public class User extends Model<User> {

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
    
    
    @NotNull(message="用户名不能为空", groups= {FormValid.class})
    @Length(min=1, max=30, message="用户名长度应在1-20个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "登录账号", position = 10)
    @TableField("USERNAME")
    private String username;
    
    
    @NotNull(message="用户密码不能为空", groups= {FormValid.class})
    @Length(min=6, max=32, message="密码长度应在6-32个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "登录密码", position = 15)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("PASSWORD")
    private String password;
    
    
    
    @ApiModelProperty(value = "禁用 0否 1是", position = 20)
    @TableField(value = "IS_FORBIDDEN", fill = FieldFill.INSERT)
    private Integer isForbidden;
    
    
    
    @ApiModelProperty(value = "逻辑删除 0否1是", position = 25)
    @TableLogic
    @TableField(value = "DELETED", fill = FieldFill.INSERT)
    private Integer deleted;
    
    
    
    @ApiModelProperty(value = "创建人id", position = 30)
    @TableField(value = "GMT_CREATOR", fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "修改人id", position = 35)
    @TableField(value = "GMT_MODIFIER", fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 40)
    @TableField(value = "GMT_CREATETIME", fill = FieldFill.INSERT)
    private Date gmtCreatetime;


    @ApiModelProperty(value = "头像地址", position = 45)
    @TableField(value = "AVATAR")
    private String avatar;
    
    
    /**
     * 关联的用户基本信息
     */
    @ApiModelProperty(hidden= true)
    @TableField(exist= false)
    private UserInfo userInfo;
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}