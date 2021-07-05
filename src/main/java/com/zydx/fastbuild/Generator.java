package com.zydx.fastbuild;

import com.alibaba.druid.pool.DruidDataSource;
import com.zydx.fastbuild.utils.GeneratorUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.Map;
@SpringBootApplication
public class Generator {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Generator.class, args);
        DruidDataSource dataSource = new DruidDataSource();
        // mysql链接
        dataSource.setUrl("jdbc:mysql://localhost:3306/note?&serverTimezone=Asia/Shanghai");
        // 用户名密码
        dataSource.getConnection("root","root");
        // 指定库名、表名
        Map<String, Map> columnInfo = GeneratorUtils.getInfo("note",null,dataSource);
        // 指定生成位置(如果该项目在D盘  那么 file在D盘根目录下)
        GeneratorUtils.createFile(columnInfo,"/file");
    }
}
