-- 创建短链接记录表
CREATE TABLE `easyj_dwz_log`
(
    `id`               bigint       NOT NULL           COMMENT 'ID',
    `short_url_code`   varchar(16)  NOT NULL           COMMENT '短链接码',
    `long_url`         varchar(255) NOT NULL           COMMENT '长链接',
    `term_of_validity` datetime     NULL               COMMENT '过期时间（为空表示不过期）',
    `status`           tinyint      NOT NULL DEFAULT 1 COMMENT '状态：0=无效,1=有效,2=已过期',
    `create_time`      datetime     NOT NULL           COMMENT '数据创建时间',
    `update_time`      datetime     NOT NULL           COMMENT '数据更新时间',
    `version`          int          NOT NULL           COMMENT '数据版本号',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UDX_SHORT_URL_CODE` (`short_url_code`) USING BTREE,
    INDEX `IDX_LONG_URL_STATUS` (`long_url`, `status`) USING BTREE,
    INDEX `IDX_TERM_OF_VALIDITY_STATUS` (`term_of_validity`, `status`) USING BTREE
) COMMENT = '短链接记录表';


-- dwz-server使用mysql时，id生成依赖于数据库序列表，创建序列表及其函数的SQL文件位于：/src/script/db/mysql/mysql__create_sequence-table_and_function.sql