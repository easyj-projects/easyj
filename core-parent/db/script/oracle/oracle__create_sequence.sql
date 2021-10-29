CREATE SEQUENCE TEST_SEQ -- 序列名

-- 增量：
INCREMENT BY 1 -- 每次加几个
-- 开始值：
START WITH 1   -- 从1开始计数

-- 最小值：
MINVALUE 1     -- 最小值
-- NOMINvalue  -- 也可设置为没有最小值
-- 最大值：
MAXVALUE 99999 -- 最大值
-- NOMAXvalue  -- 也可设置为没有最大值

-- 循环：
CYCLE          -- 循环到最大值后，从最小值重新开始
-- NOCYCLE     -- 也可设置一直累加，不循环

-- 缓存：
CACHE 10       -- 设置缓存cache个序列，如果系统down掉了或者其它情况将会导致序列不连续
-- NOCACHE     -- 也可设置无缓存

ORDER          -- 按请求顺序生成有序的序列号（用途：时间戳）
-- NOORDER     -- 不按请求顺序生成有序的序列号（用途：主键值）
;
