-- 创建短链接记录表
CREATE TABLE "EASYJ_DWZ_LOG"
(
    "ID"               NUMBER (20,0)  NOT NULL,
    "SHORT_URL_CODE"   VARCHAR2 (16)  NOT NULL,
    "LONG_URL"         VARCHAR2 (255) NOT NULL,
    "TERM_OF_VALIDITY" DATE,
    "STATUS"           NUMBER (1,0)   NOT NULL,
    "CREATE_TIME"      DATE           NOT NULL,
    "UPDATE_TIME"      DATE           NOT NULL,
    "VERSION"          NUMBER         NOT NULL,
    PRIMARY KEY ("ID")
);

CREATE UNIQUE INDEX "UDX_SHORT_URL_CODE" ON "DWZ_LOG" ("SHORT_URL_CODE" ASC);
CREATE INDEX "IDX_LONG_URL_STATUS" ON "DWZ_LOG" ("LONG_URL" ASC, "STATUS" ASC);
CREATE INDEX "IDX_TERM_OF_VALIDITY_STATUS" ON "DWZ_LOG" ("TERM_OF_VALIDITY" ASC, "STATUS" ASC);

COMMENT ON TABLE "DWZ_LOG" IS '短链接记录表';

COMMENT ON COLUMN "DWZ_LOG"."ID" IS 'ID';
COMMENT ON COLUMN "DWZ_LOG"."SHORT_URL_CODE" IS '短链接码';
COMMENT ON COLUMN "DWZ_LOG"."LONG_URL" IS '长链接';
COMMENT ON COLUMN "DWZ_LOG"."TERM_OF_VALIDITY" IS '过期时间（为空表示不过期）';
COMMENT ON COLUMN "DWZ_LOG"."STATUS" IS '状态：0=无效,1=有效,2=已过期';
COMMENT ON COLUMN "DWZ_LOG"."CREATE_TIME" IS '数据创建时间';
COMMENT ON COLUMN "DWZ_LOG"."UPDATE_TIME" IS '数据更新时间';
COMMENT ON COLUMN "DWZ_LOG"."VERSION" IS '数据版本号';


-- 创建生成短链接码所需的序列（注意：序列名不要乱改）
CREATE SEQUENCE SEQ_SHORT_URL_CODE -- 序列名
INCREMENT BY 1 -- 每次加1
START WITH 1   -- 从1开始计数
MINVALUE 1     -- 最小值
NOMAXvalue     -- 不设最大值
NOCYCLE        -- 一直累加，不循环
NOCACHE        -- 无缓存
NOORDER        -- 请求无序，能保证唯一就行（可增加性能）。
;