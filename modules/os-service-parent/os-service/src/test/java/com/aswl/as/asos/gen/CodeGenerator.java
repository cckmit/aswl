package com.aswl.as.asos.gen;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) throws Exception {


        //System.out.println(addOneRoot("A99"));
        /*
        //"AES"
        String s=new String(Base64.getEncoder().encode("123456".getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(s.getBytes(), "AES"), new IvParameterSpec(s.getBytes()));
        byte[] arr=cipher.doFinal(s.getBytes(StandardCharsets.UTF_8));
        System.out.println("得出的结果为==="+new String(arr,StandardCharsets.UTF_8));
        System.out.println(new String(Base64.getEncoder().encode("123456".getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8));
        */


        //System.out.println(encrypt("123456","1234567887654321","1234567887654321"));

        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) throws Exception {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = "C:/SOFE/code/asms/trunk/modules/os-service-parent/os-service";//System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("hfx");
        gc.setOpen(false);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://localhost:3306/as-os?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
//        dsc.setUrl("jdbc:mysql://localhost:3306/as-ibrs?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        dsc.setUrl("jdbc:mysql://localhost:3306/as-user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("asos");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.aswl.as.asos.modules");//.test
        mpg.setPackageInfo(pc);

        // 自定义配置 自定义参数传递过去
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        //String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 生成的service和Controller模板太空了，需要添加些常用的函数
        //这里使用FreeMarker,所以直接拉了生成器的ftl模板出来改，需要其它引擎的拿其它文件
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //代码生成器很多内置的函数和字段都private，需要修改源码，放开对应的字段

        // templateConfig.setEntity("templates/entity2.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setController("templates/controller.java");

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        strategy.setSuperControllerClass("com.aswl.as.asos.modules.sys.controller.AbstractController");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");




        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        //mpg.setTemplateEngine(new VelocityTemplateEngine());



        mpg.execute();
    }

    /**
     * content: 加密内容
     * slatKey: 加密的盐，16位字符串
     * vectorKey: 加密的向量，16位字符串
     */
    public static String encrypt(String content, String slatKey, String vectorKey) throws Exception {
        //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }


    public static String addOneRoot(String testStr){

        // 获取字符串最后一位+1,该函数专门改一下，不单A99，还可以W09,不然到了A100的时候，当A10匹配权限的时候，很可能like '%A10'，就会查到A100的数据
        //本函数只用来创建机构的第一层

        //暂时只拿3位，，后续可以改一下拿多位
        String arr="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //前一位必定是字母，后两位是数字

        char first=testStr.charAt(0);

        //根据不是数字的字符拆分字符串
        String[] strs = testStr.split("[^0-9]");
        //取出最后一组数字
        String numStr = strs[strs.length-1];
        //如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常

        if(numStr.equals("99"))
        {
            if(first=='Z')
            {
                throw new NumberFormatException();
            }
            else
            {
                return arr.charAt(arr.indexOf(first)+1)+"01";
            }
        }
        else
        {
            if(numStr != null && numStr.length()>0){
                //取出字符串的长度
                int n = numStr.length();
                //将该数字加1
                int num = Integer.parseInt(numStr)+1;
                String added = String.valueOf(num);
                n = Math.min(n, added.length());
                //拼接字符串
                return testStr.subSequence(0, testStr.length()-n)+added;
            }else{
                throw new NumberFormatException();
            }
        }
    }


}