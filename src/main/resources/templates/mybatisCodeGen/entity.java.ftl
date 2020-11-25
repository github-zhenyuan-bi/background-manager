package ${package.Entity};

<#-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ import开始 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ -->
<#list table.importPackages as pkg>
import ${pkg};
</#list>

<#-- swagger2 -->
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

<#-- lombok -->
<#if entityLombokModel>
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ import结束 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ -->

/**
 * ${table.comment!} 
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/entity.java.ftl
 *
 * @author ${author}
 * @since ${date}
 */
 
<#-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ class注解开始 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ -->
<#-- lombok -->
<#if entityLombokModel>
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
@Accessors(chain = true)
</#if>
<#-- mybatis tableName -->
<#if table.convert>
@TableName("${table.name}")
</#if>
<#-- mybatis swagger -->
@ApiModel(value="${entity}", description="${table.comment!}")
<#-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ class注解结束 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ -->
public class ${entity} extends Model<${entity}> {

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
    
    
<#-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  字段定义开始 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ -->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  字段注解开始 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ -->
	<#-- swagger2 -->
    @ApiModelProperty(value = "${field.comment!}", position = ${(field_index+1) * 5})
    <#if field.keyFlag>
        <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif idType??>
    @TableId(value = "${field.name}", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("${field.name}")
        </#if>
        <#-- 普通字段 -->
        <#-- 逻辑删除注解 -->
    <#elseif (logicDeleteFieldName!"") == field.name >
    @TableLogic
    @TableField(value = "${field.name}", fill = FieldFill.INSERT)
    	<#-- 需自动填充字段   ----->
    <#elseif field.fill??>
        <#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("${field.name}")
    </#if>
    <#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
    <#-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 字段注解结束 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ -->
    private ${field.propertyType} ${field.propertyName};
    
    
    
</#list>
<#-- ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 字段定义结束 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ -->


<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

    <#if entityBuilderModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if entityBuilderModel>
        return this;
        </#if>
    }
    </#list>
</#if>

<#if activeRecord>
    @Override
    protected Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

</#if>
}