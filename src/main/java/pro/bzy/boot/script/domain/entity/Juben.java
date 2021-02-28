package pro.bzy.boot.script.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 剧本 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString(of= {"name"})
@TableName("SC_JUBEN")
@ApiModel(value="Juben", description="剧本")
public class Juben extends Model<Juben> {

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
    
    
    
    @ApiModelProperty(value = "剧本名称", position = 10)
    @TableField("NAME")
    private String name;
    
    
    
    @ApiModelProperty(value = "剧本类型（1-普通本，2-城市限定，3-独家本）", position = 15)
    @TableField("JB_TYPE")
    private String jbType;
    
    
    
    @ApiModelProperty(value = "剧本介绍", position = 20)
    @TableField("INTRODUCTION")
    private String introduction;
    
    
    
    @ApiModelProperty(value = "剧本封面图片地址", position = 25)
    @TableField("COVER_IMG")
    private String coverImg;
    
    
    
    @ApiModelProperty(value = "剧本难度 按数字递增难度增大", position = 30)
    @TableField("DIFFICULTY")
    private String difficulty;
    
    
    
    @ApiModelProperty(value = "游戏时长", position = 35)
    @TableField("GAME_TIME")
    private BigDecimal gameTime;
    
    
    
    @ApiModelProperty(value = "男玩家人数", position = 40)
    @TableField("GAMER_MALE_COUNT")
    private Integer gamerMaleCount;
    
    
    
    @ApiModelProperty(value = "女玩家人数", position = 45)
    @TableField("GAMER_FEMALE_COUNT")
    private Integer gamerFemaleCount;
    
    
    
    @ApiModelProperty(value = "剧本进价", position = 50)
    @TableField("IMPORT_PRICE")
    private Integer importPrice;
    
    
    
    @ApiModelProperty(value = "剧本售价（每人）", position = 55)
    @TableField("SELL_PRICE")
    private Integer sellPrice;
    
    
    
    @ApiModelProperty(value = "剧本评分", position = 60)
    @TableField("SCORE")
    private BigDecimal score;
    
    
    
    @ApiModelProperty(value = "能否角色反串（0否1是）", position = 65)
    @TableField("ROLE_REVERSAL_ENBALE")
    private String roleReversalEnbale;
    
    
    
    @ApiModelProperty(value = "是否上架（0否1是）", position = 70)
    @TableField("IS_SELL")
    private Integer isSell;
    
    
    
    @ApiModelProperty(value = "创建人", position = 75)
    @TableField(value = "GMT_CREATOR", fill = FieldFill.INSERT)
    private String gmtCreator;
    
    
    
    @ApiModelProperty(value = "修改人", position = 80)
    @TableField(value = "GMT_MODIFIER", fill = FieldFill.UPDATE)
    private String gmtModifier;
    
    
    
    @ApiModelProperty(value = "创建时间", position = 85)
    @TableField(value = "GMT_CREATETIME", fill = FieldFill.INSERT)
    private Date gmtCreatetime;
    
    
    @ApiModelProperty(value = "逻辑删除 0否1是", position = 25)
    @TableLogic
    @TableField(value = "DELETED", fill = FieldFill.INSERT)
    private Integer deleted;

    
    /** 关联的标签列表 */
    @TableField(exist=false)
    private List<Tag> tags;
    
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}