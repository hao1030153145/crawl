package com.transing.crawl.job.impl;

import com.jeeframework.jeetask.event.exception.JobEventException;
import com.jeeframework.jeetask.event.rdb.impl.JobEventCommonStorageProcessor;
import com.jeeframework.jeetask.event.type.JobExecutionEvent;
import com.jeeframework.util.format.DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;

/**
 * 包: com.transing.crawl.job.impl
 * 源文件:CrawlJobEventCommonStorage.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年09月28日
 */
public class CrawlJobEventCommonStorage extends JobEventCommonStorageProcessor
{

    private static final Logger log = LoggerFactory.getLogger(JobEventCommonStorageProcessor.class);
    private static final String TASK_TABLE_NAME = "t_task";

    protected String getTaskTableName() {
        return "t_task";
    }

    public CrawlJobEventCommonStorage(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    protected void createTaskTable(Connection conn) throws SQLException
    {
        String dbSchema = "CREATE TABLE `"+tableName+"` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `job_name` varchar(100) NOT NULL,\n" +
                "  `state` varchar(20) NOT NULL,\n" +
                "  `progress` int(11) NOT NULL,\n" +
                "  `ip` varchar(100) NOT NULL,\n" +
                "  `param` text, \n" +
                "  `message` varchar(500) NOT NULL,\n" +
                "  `failure_cause` VARCHAR(4000) NULL,\n"+
                "  `create_time` timestamp NULL DEFAULT NULL,\n" +
                "  `start_time` timestamp NULL DEFAULT NULL,\n" +
                "  `complete_time` timestamp NULL DEFAULT NULL,\n" +
                "  `project_id` int(11) DEFAULT NULL,\n" +
                "  `flow_detail_id` int(11) DEFAULT NULL,\n" +
                "  `task_id` int(11) DEFAULT NULL,\n"+
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_task_id_state` (`id`,`state`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
        try (PreparedStatement preparedStatement = conn.prepareStatement(dbSchema)) {
            preparedStatement.execute();
        }

    }

    @Override
    protected boolean insertJobExecutionEvent(JobExecutionEvent jobExecutionEvent) {

        boolean result = false;
        String sql = "INSERT INTO `"+tableName+"` (\n" +
                "\t`job_name`,\n" +
                "\t`state`,\n" +
                "\t`progress`,\n" +
                "\t`ip`,\n" +
                "\t`param`,\n" +
                "\t`message`,\n" +
                "\t`create_time`,\n" +
                "\t`start_time`,\n" +
                "\t`complete_time`,\n" +
                "\t`project_id`,\n" +
                "\t`flow_detail_id`,\n" +
                "\t`task_id`\n" +
                ")\n" +
                "VALUES\n" +
                "\t(\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?" +
                "\t);\n" +
                "\n";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            CrawlSubTask crawlSubTask = (CrawlSubTask) jobExecutionEvent.getTask();
            preparedStatement.setString(1, crawlSubTask.getName());
            preparedStatement.setString(2, jobExecutionEvent.getState().toString());
            preparedStatement.setInt(3, 0);
            preparedStatement.setString(4, jobExecutionEvent.getIp());
            preparedStatement.setString(5,crawlSubTask.getParam());
            preparedStatement.setString(6, "");
            preparedStatement.setTimestamp(7, new Timestamp(jobExecutionEvent.getCreateTime().getTime()));
            try {
                preparedStatement.setTimestamp(8, new Timestamp(
                        DateFormat.parseDate("1971-01-01 00:00:00", DateFormat
                                .DT_YYYY_MM_DD_HHMMSS).getTime()));
            } catch (ParseException e) {
            }
            try {
                preparedStatement.setTimestamp(9, new Timestamp(DateFormat.parseDate("1971-01-01 00:00:00", DateFormat
                        .DT_YYYY_MM_DD_HHMMSS).getTime()));
            } catch (ParseException e) {
            }

            preparedStatement.setLong(10,crawlSubTask.getProjectId());
            preparedStatement.setLong(11,crawlSubTask.getFlowdetailId());
            preparedStatement.setLong(12,crawlSubTask.getTaskId());

            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            Object retId = null;
            if (rs.next())
                retId = rs.getObject(1);
            else
                throw new SQLException("insert or generate keys failed..");

            jobExecutionEvent.setTaskId(Long.valueOf(retId + ""));
            result = true;
        } catch (final SQLException ex) {
            if (!isDuplicateRecord(ex)) {
                // TODO 记录失败直接输出日志,未来可考虑配置化
                log.error(ex.getMessage());
            }
        }
        return result;

    }

    @Override
    protected boolean updateJobExecutionEventWhenFinished(
            JobExecutionEvent jobExecutionEvent)
    {
        boolean result = false;
        String sql = "UPDATE `" + this.tableName + "` SET `ip`=?, `state` = ?,`complete_time` = ?   WHERE id = ?";

        try {
            Connection ex = this.dataSource.getConnection();
            Throwable var5 = null;

            try {
                PreparedStatement preparedStatement = ex.prepareStatement(sql);
                Throwable var7 = null;

                try {
                    preparedStatement.setString(1,jobExecutionEvent.getIp());
                    preparedStatement.setString(2, jobExecutionEvent.getState().toString());
                    preparedStatement.setTimestamp(3, new Timestamp(jobExecutionEvent.getCompleteTime().getTime()));
                    preparedStatement.setLong(4, jobExecutionEvent.getTaskId());
                    preparedStatement.executeUpdate();
                    result = true;
                } catch (Throwable var32) {
                    var7 = var32;
                    throw var32;
                } finally {
                    if(preparedStatement != null) {
                        if(var7 != null) {
                            try {
                                preparedStatement.close();
                            } catch (Throwable var31) {
                                var7.addSuppressed(var31);
                            }
                        } else {
                            preparedStatement.close();
                        }
                    }

                }
            } catch (Throwable var34) {
                var5 = var34;
                throw var34;
            } finally {
                if(ex != null) {
                    if(var5 != null) {
                        try {
                            ex.close();
                        } catch (Throwable var30) {
                            var5.addSuppressed(var30);
                        }
                    } else {
                        ex.close();
                    }
                }

            }

            return result;
        } catch (SQLException var36) {
            throw new JobEventException(var36);
        }
    }

    @Override
    protected boolean updateJobExecutionEventWhenStaging(
            JobExecutionEvent jobExecutionEvent)
    {
        boolean result = false;
        String sql = "UPDATE `" + this.tableName + "` SET `ip`=?, `state` = ?   WHERE id = ?";

        try {
            Connection ex = this.dataSource.getConnection();
            Throwable var5 = null;

            try {
                PreparedStatement preparedStatement = ex.prepareStatement(sql);
                Throwable var7 = null;

                try {
                    preparedStatement.setString(1,jobExecutionEvent.getIp());
                    preparedStatement.setString(2, jobExecutionEvent.getState().toString());
                    preparedStatement.setLong(3, jobExecutionEvent.getTaskId());
                    preparedStatement.executeUpdate();
                    result = true;
                } catch (Throwable var32) {
                    var7 = var32;
                    throw var32;
                } finally {
                    if(preparedStatement != null) {
                        if(var7 != null) {
                            try {
                                preparedStatement.close();
                            } catch (Throwable var31) {
                                var7.addSuppressed(var31);
                            }
                        } else {
                            preparedStatement.close();
                        }
                    }

                }
            } catch (Throwable var34) {
                var5 = var34;
                throw var34;
            } finally {
                if(ex != null) {
                    if(var5 != null) {
                        try {
                            ex.close();
                        } catch (Throwable var30) {
                            var5.addSuppressed(var30);
                        }
                    } else {
                        ex.close();
                    }
                }

            }

            return result;
        } catch (SQLException var36) {
            throw new JobEventException(var36);
        }
    }
}
