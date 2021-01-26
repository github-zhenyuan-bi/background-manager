package pro.bzy.boot.framework.config.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.PropertiesUtil;

/**
 * 数据库代码逆向生成
 * @author zhenyuan.bi
 */
@Slf4j
public class MybatisPlusCodeGen {

    public static void main(String[] args) {
        //frameworkCodeGen();
        scriptCodeGen();
        //wxCodeGen();
    }
    
    public static void wxCodeGen() {
        Map<String, String> db = PropertiesUtil.props;
        String parentPackage = "pro.bzy.boot.thirdpart.wechat.miniprogram";
        String viewPath = "";
        String module = "wx";
        Map<String, Object> extraArgs = Maps.newHashMap();
        extraArgs.put("module", module);
        //String[] tables = {"WX_USERINFO"};
        //String[] tables = {"WX_MINIPROGRAM_LOG"};
        //String[] tables = {"V_WX_JUBEN_BROWSE_RECORD"};
        String[] tables = {
                "V_WX_JUBEN_BROWSE_RECORD",
                "WX_MINIPROGRAM_SETTING_INDEXIMG", 
                "WX_MINIPROGRAM_SETTING_SERVERMODULE",
                "WX_MINIPROGRAM_SETTING_COOPERATION", 
                "WX_MINIPROGRAM_SETTING_COOPERATIONS_CONTENT"};
        codeGen(db, parentPackage, viewPath, module, extraArgs, tables);
    }
    
    public static void scriptCodeGen() {
        Map<String, String> db = PropertiesUtil.props;
        String parentPackage = "pro.bzy.boot.script";
        String viewPath = "";
        String module = "script";
        Map<String, Object> extraArgs = Maps.newHashMap();
        extraArgs.put("module", module);
        //String[] tables = {"SC_JUBEN", "SC_TAG", "SC_JUBEN_TAG"};
        //String[] tables = {"SC_JUBEN_CHARACTER"};
        //String[] tables = {"T_USER"};
        //String[] tables = {"SC_BULLETIN_TEMPLATE", "SC_BULLETIN", "SC_BULLETIN_TIMER_TASK"};
        String[] tables = {"V_BULLETIN_TIMER_TASK_DETAIL"};
        //String[] tables = {"SC_JUBEN_BROWSE_LOG"};
        //String[] tables = {"V_WX_JUBEN_BROWSE_RECORD"};
        codeGen(db, parentPackage, viewPath, module, extraArgs, tables);
    }
    
    public static void frameworkCodeGen() {
        Map<String, String> db = PropertiesUtil.props;
        String parentPackage = "pro.bzy.boot.framework.web";
        String viewPath = "";
        String module = "framework";
        Map<String, Object> extraArgs = Maps.newHashMap();
        extraArgs.put("module", module);
        /*String[] tables = {"T_USER", "T_USER_INFO", "T_USER_ROLE",
                            "T_ROLE", "T_ROLE_MENU", "T_ROLE_PERMISSION",
                            "T_PERMISSION", "T_MENU", "T_TIME_TASK"};*/
        //String[] tables = {"T_CONSTANT"};
        //String[] tables = {"T_TIMER_TASK"};
        //String[] tables = {"T_USERGROUP", "T_USER_USERGROUP"};
        String[] tables = {"T_TIMER_TASK_LOG"};
        codeGen(db, parentPackage, viewPath, module, extraArgs, tables);
    }
    
    /**
     * 逆向生成
     */
    public static final void codeGen(
            Map<String, String> db, // 数据库连接信息 可以使用PropertiesUtil读取的内容
            String parentPackage, // 生成文件存放的父包名 com.aaa.bbb
            String viewPath, // 自定义生成文件 - 视图文件 路径
            String module, // 所属模块名 - 会在Controller的RequestMapping等地方用到
            Map<String, Object> extraArgs, // 扩展的自定义变量 在模板中使用
            String... tables // 需要生成的表名 大写
            ) {
        
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        
        // 全局配置 基本按默认来就行 有需要再改
        mpg.setGlobalConfig(GC.defaultGlobalConfig());
        log.debug("【全局配置完成】 {}", mpg.getGlobalConfig());
        
        // 数据源配置
        mpg.setDataSource(
                new DataSourceConfig()
                    .setUrl("jdbc:mysql://47.114.142.152:3506/script_dev?useUnicode=true&characterEncoding=utf8&useSSL=false")
                    .setDriverName("com.mysql.jdbc.Driver")
                    .setUsername("root")
                    .setPassword("AliMysql123456"));
//                    .setUrl(db.get("spring.datasource.url").toString())
//                    .setDriverName(db.get("spring.datasource.driverClassName").toString())
//                    .setUsername(db.get("spring.datasource.username").toString())
//                    .setPassword(db.get("spring.datasource.password").toString()));
        log.debug("【数据库配置完成】 {}", mpg.getDataSource());
        
        // 生成后的文件存放包配置 默认的 domain.entity mapper service seriveImpl controller 
        // 四个包下 注释得都是默认的
        mpg.setPackageInfo(
                new PackageConfig()
                    .setParent(parentPackage)
                    .setEntity("domain.entity")
                    /*.setMapper("mapper")
                    .setService("service")
                    .setServiceImpl("service.impl")
                    .setController("controller")*/);
        log.debug("【包信息配置完成】 {}", mpg.getPackageInfo());
        
        
        // 注入自定义变量配置，可以在 模板文件中 中使用 cfg.变量名 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(extraArgs);
            }
        };
        cfg.setMap(extraArgs);
        // 自定义输出文件
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>(3);
        /*focList.add(new FileOutConfig("/templates/tablePage.html.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String bean = tableInfo.getEntityName().toLowerCase();
                return viewPath + module + File.separator + 
                            bean + "/tablePage.html";
            }
        });*/
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        log.debug("【自定义输出文件配置完成】 {}", mpg.getCfg());
        
        // 策略设定
        mpg.setStrategy(
                new StrategyConfig()
                    // 类命名规则
                    .setNaming(NamingStrategy.underline_to_camel)
                    // 字段命名规则
                    .setColumnNaming(NamingStrategy.underline_to_camel)
                    // 是否使用lombok
                    .setEntityLombokModel(true)     
                    // 逻辑删除字段名称
                    .setLogicDeleteFieldName("DELETED")                             
                    // 表名通用前缀 设置此策略 则生成的类名前将不带这个前缀
                    .setTablePrefix("t_","v_","sys_","pd_", "sc_")   
                    // 选择那些表逆向生成 
                    .setInclude(tables)   
                    .setControllerMappingHyphenStyle(true)
                    // 自动填充字段
                    .setTableFillList(Arrays.asList(                                
                        new TableFill("gmt_creator", FieldFill.INSERT), 
                        new TableFill("gmt_modifier", FieldFill.UPDATE),
                        new TableFill("gmt_createtime", FieldFill.INSERT),
                        new TableFill("deleted", FieldFill.INSERT),
                        new TableFill("is_forbidden", FieldFill.INSERT))));
        log.debug("【策略配置完成】 {}", mpg.getStrategy());
        
        // 自定义模板文件配置 不配置的话就是用默认的
        final String templatePath = "templates/mybatisCodeGen/";
        mpg.setTemplate(
                new TemplateConfig()
                    .setEntity(templatePath + "entity.java")
                    .setMapper(templatePath + "mapper.java")
                    .setService(templatePath + "service.java")
                    .setServiceImpl(templatePath + "serviceImpl.java")
                    .setController(templatePath + "Controller.java"));
        log.debug("【自定义模板文件配置完成】 {}", mpg.getTemplate());
        
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        log.debug("【模板引擎配置完成】 {}", mpg.getTemplateEngine());
        mpg.execute();
    }
    
    
    
    /**
     * 类说明    ：逆向工程全局配置
     * 创建时间：下午4:32:19下午4:32:19
     * 创建人    ：zhenyuan.bi
     */
    @Data
    public static class GC {
        private String author = "zhenyuan.bi";
        private String serviceName = "%sService";
        private String serviceImplName = "%sServiceImpl";
        private String mapperName = "%sMapper";
        private String mapperXmlName = "%sMapper";
        
        private boolean open = true;
        private boolean fileOverride = false;
        private boolean activeRecord = true;
        private boolean enableCache = true;
        private IdType idtype = IdType.ASSIGN_UUID;
        
        private GC() {}
        public static GlobalConfig defaultGlobalConfig() {
            GC mgc = new GC();
            GlobalConfig gc = new GlobalConfig();
            String projectPath = System.getProperty("user.dir");
            gc.setOutputDir(projectPath + "/src/main/java");
            gc.setAuthor(mgc.author);
            gc.setOpen(mgc.open);
            gc.setServiceName(mgc.serviceName);
            gc.setServiceImplName(mgc.serviceImplName);
            gc.setMapperName(mgc.mapperName);
            gc.setXmlName(mgc.mapperXmlName);
            gc.setFileOverride(mgc.fileOverride);
            gc.setActiveRecord(mgc.activeRecord);
            gc.setIdType(mgc.idtype);
            gc.setEnableCache(mgc.enableCache);
            gc.setDateType(DateType.ONLY_DATE);
            return gc;
        }
    }
    
}
