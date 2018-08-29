package com.plugin.auto.common;

/**
 * 文件存在后的写操作类型
 */
public enum WriteFileType {
    OLD,//只保留旧文件
    NEW,//只保留新文件
    BOTH_NEW,//保留2者，以新文件作为最终生成的文件，旧文件作为备份
    BOTH_OLD //保留2者，以旧文件作为最终生成的文件，新文件作为备份
}
