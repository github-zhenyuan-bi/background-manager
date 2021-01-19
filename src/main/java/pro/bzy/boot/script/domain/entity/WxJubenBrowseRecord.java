package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
 * VIEW 
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
@TableName("v_wx_juben_browse_record")
@ApiModel(value="WxJubenBrowseRecord", description="VIEW")
public class WxJubenBrowseRecord extends Model<WxJubenBrowseRecord> {

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
    private String id;
    
    
    
    @ApiModelProperty(value = "剧本-名称", position = 10)
    private String jubenName;
    
    
    
    @ApiModelProperty(value = "剧本-封面图地址", position = 15)
    private String coverImg;
    
    
    
    @ApiModelProperty(value = "微信浏览用户id", position = 20)
    private String openid;
    
    
    
    @ApiModelProperty(value = "浏览时间", position = 25)
    private Date browseTime;
    
    
    
    @ApiModelProperty(value = "微信名称", position = 30)
    private String nickname;
    
    
    
    @ApiModelProperty(value = "真实姓名", position = 35)
    private String realname;
    
    
    
    @ApiModelProperty(value = "性别", position = 40)
    private Integer sex;
    
    
    
    @ApiModelProperty(value = "头像", position = 45)
    private String avatarUrl;
    
    
    

    @Override
    protected Serializable pkVal() {
        return null;
    }

}