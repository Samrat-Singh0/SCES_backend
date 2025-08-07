package com.example.mainBase.loader;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    runRoleScript();
    runPermissionScript();
    runSemesterScript();
  }

  public void runRoleScript() throws SQLException {
    Integer count = jdbcTemplate.queryForObject("select count(*) from role WHERE role_type='SUPER_ADMIN'", Integer.class);
    if (count == null || count == 0) {
      if(jdbcTemplate.getDataSource() != null) {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource("role-data.sql"));
      }
    }
  }

  public void runPermissionScript() throws SQLException {
    Integer count = jdbcTemplate.queryForObject("select count(*) from permission", Integer.class);
    if (count == null || count == 0) {
      if(jdbcTemplate.getDataSource() != null) {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource("permission-data.sql"));
      }
    }
  }

  public void runSemesterScript() throws SQLException {
    Integer count =  jdbcTemplate.queryForObject("select count(*) from semester", Integer.class);
    if(count == null || count == 0) {
      if(jdbcTemplate.getDataSource() != null) {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource("semester-data.sql"));
      }
    }
  }
}
