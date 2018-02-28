import com.plugin.auto.db.TableUtils;
import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;
import com.plugin.auto.script.java.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {

        DatabaseConfigInfo configInfo = new DatabaseConfigInfo();
        configInfo.setPackagePath("com.plugin.auto.out");
        configInfo.setWriteFilePath("/Users/Jun/Documents/idea_workspace/mybatis_test/src/main/java");
        configInfo.setResourcePath("/Users/Jun/Documents/idea_workspace/mybatis_test/src/main/resources");
        configInfo.setDb("golftest");
        configInfo.setHost("120.27.152.120");
        configInfo.setPort("7561");
        configInfo.setUser("golftest");
        configInfo.setPwd("golftest@bookingtee.com");

        TableUtils tableUtils = new TableUtils(configInfo);
        tableUtils.initTables();

        List<TableInfo> tableInfo = tableUtils.getTables().stream().filter(tableInfo1 -> tableInfo1.getTableName().startsWith("cc_checkin")).collect(Collectors.toList());

        new DtoGenerator(configInfo, tableInfo).startGeneratorList();
        new ModelGenerator(configInfo, tableInfo).startGeneratorList();
        new ServiceGenerator(configInfo, tableInfo).startGeneratorList();
        new MybatisDaoGenerator(configInfo, tableInfo).startGeneratorList();
        new MybatisConfigGenerator(configInfo, tableInfo).generator();
    }
}
