package com.zydx.fastbuild.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GeneratorUtils {

    public static Map<String, Map> getInfo(String dbName, String tableName, DataSource dataSource) throws SQLException {
        // 创建连接
        Connection con = null;
        PreparedStatement pstemt = null;
        ResultSet rs = null;
        //sql
        Map<String, Map> columnsInfo = new HashMap<>();
        String sql = "select column_name,data_type,column_comment,table_name from information_schema.columns where table_schema='" + dbName + "'";
        if (StringUtils.hasText(tableName)) {
            sql += " and table_name='" + tableName + "'";
        }
        try {
            con = dataSource.getConnection();
            pstemt = con.prepareStatement(sql);
            rs = pstemt.executeQuery();
            while (rs.next()) {
                ArrayList tempList = new ArrayList();
                String columnName = rs.getString(1);
                String jdbcType = rs.getString(2);
                String comment = rs.getString(3);
                String db_tableName = rs.getString(4);
                String key = MySqlTypeToJavaTypeUtils.changeToJavaFiled(db_tableName.replace("tb_", ""));
                Map<String, String> columnMap = new HashMap<>();
                if (jdbcType.equalsIgnoreCase("int")) {
                    columnMap.put("jdbcType", "Integer");
                } else if (jdbcType.equalsIgnoreCase("datetime")) {
                    columnMap.put("jdbcType", "timestamp");
                } else {
                    columnMap.put("jdbcType", jdbcType);
                }
                columnMap.put("comment", comment);
                columnMap.put("property", MySqlTypeToJavaTypeUtils.changeToJavaFiled(columnName));
                columnMap.put("javaType", MySqlTypeToJavaTypeUtils.jdbcTypeToJavaType(jdbcType));
//                //设置注解类型
//                if (columnName.equalsIgnoreCase("id")) {
//                    bi.setIdType(ci.getJavaType());
//                    bi.setIdJdbcType(ci.getJdbcType());
//                }

                columnMap.put("columnName", columnName);
                columnMap.put("jdbcType", jdbcType);
                columnMap.put("columnComment", comment);
                tempList.add(columnMap);
                HashMap temp = (HashMap) columnsInfo.get(key);
                if (temp == null || temp.isEmpty()) {
                    Map cis = new HashMap();
                    cis.put("dbTableName", db_tableName);
                    cis.put("cis", tempList);
                    columnsInfo.put(key, cis);
                } else {
                    ArrayList list = (ArrayList) temp.get("cis");
                    list.addAll(tempList);
                    temp.put("cis", list);
                    columnsInfo.put(key, temp);
                }
            }
            System.out.println(columnsInfo.keySet().toArray());
            // 完成后关闭
            rs.close();
            pstemt.close();
            con.close();
            if (columnsInfo.isEmpty()) {
                throw new RuntimeException("未能读取到表或表中的字段。请检查链接url，数据库账户，数据库密码，查询的数据名、是否正确。");
            }
            return columnsInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("自动生成实体类错误：" + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException se2) {
            }
            // 关闭资源
            try {
                if (pstemt != null) pstemt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (con != null) con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void createFile(Map<String, Map> dataModel, String filePath) {
        String[] fileType = {"entity", "mapper", "dto", "service", "serviceImpl", "controller"};
        // 通过FreeMarker的Confuguration读取相应的模板文件
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        // 设置模板路径
        configuration.setClassForTemplateLoading(GeneratorUtils.class, "/templates");
        // 设置默认字体
        configuration.setDefaultEncoding("utf-8");
        for (Map.Entry entrySet : dataModel.entrySet()) {
            for (int i = 0; i < fileType.length; i++) {
                // 获取模板
                try {
                    String templateName = fileType[i] + ".ftl";
                    Template template = configuration.getTemplate(templateName);
                    String key = entrySet.getKey().toString();
                    HashMap<String, Object> body = (HashMap<String, Object>) entrySet.getValue();
                    String camelKey = String.valueOf(key.charAt(0)).toUpperCase() + key.substring(1);
                    body.put("entityName", camelKey);
                    String localFilePath = filePath + File.separatorChar + fileType[i] + File.separatorChar + key + File.separatorChar + camelKey + messageStr(templateName);
                    File file = new File(localFilePath);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                    }
                    //设置输出流
                    FileWriter out = new FileWriter(file);
                    //模板输出静态文件
                    template.process(body, out);
                } catch (Exception e) {

                }
            }
        }
    }

    public static String messageStr(String name) {
        if (name.equals("entity.ftl")) {
            name = ".java";
        } else if (name.equals("mapper.ftl")) {
            name = "Mapper.java";
        } else if (name.equals("service.ftl")) {
            name = "Service.java";
        } else if (name.equals("serviceImpl.ftl")) {
            name = "ServiceImpl.java";
        } else if (name.equals("controller.ftl")) {
            name = "Controller.java";
        } else if (name.equals("dto.ftl")) {
            name = "DTO.java";
        }
        return name;
    }
}

