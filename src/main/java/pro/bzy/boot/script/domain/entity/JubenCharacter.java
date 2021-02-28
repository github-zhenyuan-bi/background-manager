package pro.bzy.boot.script.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 剧本的角色 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-10-14
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString(of= {"jubenId", "name"})
@TableName("SC_JUBEN_CHARACTER")
@ApiModel(value="JubenCharacter", description="剧本的角色")
public class JubenCharacter extends Model<JubenCharacter> {

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
    
    
    
    @ApiModelProperty(value = "剧本ID", position = 10)
    @TableField("JUBEN_ID")
    private String jubenId;
    
    
    
    @ApiModelProperty(value = "人物名称", position = 15)
    @TableField("NAME")
    private String name;
    
    
    
    @ApiModelProperty(value = "人物年龄", position = 20)
    @TableField("AGE")
    private Integer age;
    
    
    
    @ApiModelProperty(value = "人物性别", position = 25)
    @TableField("SEX")
    private String sex;
    
    
    
    @ApiModelProperty(value = "人物介绍", position = 30)
    @TableField("INTRO")
    private String intro;
    
    
    
    @ApiModelProperty(value = "人物照片", position = 35)
    @TableField("IMG")
    private String img;
    
    
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}