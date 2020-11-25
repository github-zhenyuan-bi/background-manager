package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户基本信息表 
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
@TableName("T_USER_INFO")
@ApiModel(value="UserInfo", description="用户基本信息表")
public class UserInfo extends Model<UserInfo> {

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
    
    
    @ApiModelProperty(value = "id 与t_user 一致", position = 5)
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;
    
    
    
    @ApiModelProperty(value = "真实姓名", position = 10)
    @TableField("REAL_NAME")
    private String realName;
    
    
    
    @ApiModelProperty(value = "年龄", position = 15)
    @TableField("AGE")
    private Integer age;
    
    
    
    @ApiModelProperty(value = "性别 0未知 1男 2女", position = 20)
    @TableField("SEX")
    private Integer sex;
    
    
    
    @ApiModelProperty(value = "生日", position = 25)
    @TableField("BIRTH")
    private Date birth;
    
    
    
    @ApiModelProperty(value = "手机号", position = 30)
    @TableField("PHONE")
    private String phone;
    
    
    
    @ApiModelProperty(value = "qq号", position = 35)
    @TableField("QQ")
    private String qq;
    
    
    
    @ApiModelProperty(value = "微信号", position = 40)
    @TableField("WECHAT")
    private String wechat;
    
    
    
    @ApiModelProperty(value = "支付宝账号", position = 45)
    @TableField("ZFB")
    private String zfb;
    
    
    
    @ApiModelProperty(value = "邮箱", position = 50)
    @TableField("EMAIL")
    private String email;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}