package com.wechat;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * @date: 2023/07/06
 * @description: 代码自动生成
 */
public class TableGenerator {

    public static void main(String[] args) {
        autoGenerator();
    }

    public static void autoGenerator() {
        FastAutoGenerator.create("jdbc:mysql://124.222.0.109/member_manage?useSSL=false&serverTimezone=Asia/Shanghai", "root", "Chenye123")
                .globalConfig(builder -> {
                    // 设置作者
                    builder.author("")
                            // 覆盖已生成文件
                            .fileOverride()
                            // 指定输出目录
                            .outputDir("D:\\project_idea\\member\\member-dao\\src\\main\\java\\com\\wechat\\generator")
                    ;
                })
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent("")
                            // 设置父包模块名
                            .moduleName("")
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.mapper, "D:\\project_idea\\member\\member-dao\\src\\main\\java\\com\\wechat\\mapper\\xml"))

                    ;
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude("materials", "check_in_records", "basic_info")
                            .entityBuilder().enableLombok().addTableFills(new Column("create_time", FieldFill.INSERT)).enableTableFieldAnnotation()
                            .mapperBuilder().enableBaseResultMap().enableBaseColumnList()
                    ;
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
//                .templateConfig(builder -> {
//                    builder.controller(null)
//                            .service(null)
//                            .serviceImpl(null);
//                })
                .execute();
    }

}
