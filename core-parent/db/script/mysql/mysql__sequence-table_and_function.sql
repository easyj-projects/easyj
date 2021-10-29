-- 建表语句
CREATE TABLE `sys_sequence`
(
    `name`            varchar(50) NOT NULL           COMMENT '序列的名字，唯一',
    `current_value`   bigint      NOT NULL           COMMENT '当前的值',
    `increment_value` tinyint     NOT NULL DEFAULT 1 COMMENT '步长，默认为1',
    PRIMARY KEY (`name`) USING BTREE
) COMMENT = '公共的序列表';


-- 获取当前序列值的函数
CREATE FUNCTION func_currval (seq_name varchar(50))
    RETURNS bigint
    DETERMINISTIC
BEGIN
    DECLARE val bigint;

    SET val = -99999999;

    SELECT `current_value` INTO val
      FROM `sys_sequence`
     WHERE `name` = seq_name;

    -- 支持自动创建序列（注意，自动创建的序列`increment_value`为1，如想要其他值，请事先手动添加数据）
    IF val = -99999999 THEN
        SET val = 1;
        INSERT INTO `sys_sequence` (`name`,   `current_value`, `increment_value`)
                            VALUES (seq_name,  val,             1);
    END IF;

    RETURN val;
END;


-- 获取下个序列值的函数
CREATE FUNCTION func_nextval (seq_name varchar(50))
    RETURNS bigint
    DETERMINISTIC
BEGIN
    UPDATE `sys_sequence`
       SET `current_value` = `current_value` + `increment_value`
     WHERE `name` = seq_name;

    RETURN func_currval(seq_name);
END;


-- 设置序列值的函数（起到纠正当前值的作用）
CREATE FUNCTION func_setval (seq_name varchar(50), val bigint)
    RETURNS bigint
    DETERMINISTIC
BEGIN
    DECLARE previousVal bigint;

    SET previousVal = func_currval(seq_name);

    IF previousVal != val THEN
        UPDATE `sys_sequence`
           SET `current_value` = val
         WHERE `name` = seq_name;
    END IF;

    return previousVal;
END;