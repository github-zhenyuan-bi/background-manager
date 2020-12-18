package pro.bzy.boot.framework.web.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.annoations.FormValid;

/**
 * 系统常量表 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_CONSTANT")
@ApiModel(value="Constant", description="系统常量表")
public class Constant extends Model<Constant> {

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
    
    
    @ApiModelProperty(value = "id主键", position = 5)
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    
    
    @NotNull(message="常量类型不能为空", groups= {FormValid.class})
    @Length(min=1, max=30, message="常量类型长度应在1-30个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "常量类型", position = 10)
    private String constType;
    
    
    
    @NotNull(message="常量key不能为空", groups= {FormValid.class})
    @Length(min=1, max=30, message="常量key长度应在1-30个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "常量key", position = 15)
    private String constKey;
    
    
    
    @Length(max=1000, message="常量类型长度应在0-1000个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "常量value", position = 20)
    private String constValue;
    
    
    
    @NotNull(message="常量说明不能为空", groups= {FormValid.class})
    @Length(min=1, max=30, message="常量说明长度应在1-30个字符", groups = {FormValid.class})
    @ApiModelProperty(value = "常量说明", position = 23)
    private String descp;
    
    
    @ApiModelProperty(value = "标签", position = 25)
    private String tag;
    
    
    
    @ApiModelProperty(value = "创建人", position = 30)
    @TableField(fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 35)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    
    @ApiModelProperty(value = "修改人", position = 40)
    @TableField(fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    /**
     * 将常量集合转为key-value的map
     * @param constants
     * @return
     */
    public static Map<String, String> toMap(List<Constant> constants) {
        return CollectionUtil.isEmpty(constants)
                ? Maps.newHashMap()
                : CollectionUtil.collToMap(constants, 
                        constant -> constant.getConstKey(), 
                        constant -> constant.getConstValue());
    }
    

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}