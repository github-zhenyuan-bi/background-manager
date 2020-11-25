package pro.bzy.boot.thirdpart.wechat.wx.domain.entity;

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
 * @since 2020-11-02
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WxUserinfo", description="")
public class WxUserinfo extends Model<WxUserinfo> {

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
    
    
    
    @ApiModelProperty(value = "微信用户唯一识别号", position = 10)
    private String openid;
    
    
    
    @ApiModelProperty(value = "微信名称", position = 15)
    private String nickname;
    
    
    
    @ApiModelProperty(value = "真实姓名", position = 20)
    private String realname;
    
    
    
    @ApiModelProperty(value = "性别", position = 25)
    private Integer sex;
    
    
    
    @ApiModelProperty(value = "生日", position = 30)
    private Date birth;
    
    
    
    @ApiModelProperty(value = "头像", position = 35)
    private String avatarUrl;
    
    
    
    @ApiModelProperty(value = "系统注册时间", position = 40)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}