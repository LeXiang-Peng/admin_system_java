package com.plx.admin_system.config;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 *
 * code generator
 * @author plx
 * @since 2024-01-12
 *
 */
public class CodeGenerator {
    public static void main(String[] args) {
        Generator();
    }
    private static void Generator(){
        //user.dir	用户当前工作目录
        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/admin_system?serverTimezone=GMT%2b8&characterEncoding=utf-8&useSSL=false", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("plx") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .enableSpringdoc() // 开启 SpringDoc 模式
                            .outputDir(projectPath+"\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))
                .packageConfig(builder -> {
                    builder.parent("com.plx.admin_system") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            //对应 com.example.module_name
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath+"\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("admin")// 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .controllerBuilder().enableHyphenStyle() //设置驼峰命名
                            .entityBuilder().enableLombok().enableFileOverride()  //entity 包开启 lombok 和覆盖
                            .controllerBuilder().enableFileOverride().enableRestStyle() // controller 包开启覆盖 开启RestController模式
                            .mapperBuilder().enableFileOverride() // mapper 包开启覆盖
                            .serviceBuilder().enableFileOverride(); // service 包开启覆盖

                })// 使用Freemarker引擎模板，默认的是Velocity引擎模板
                //.templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
