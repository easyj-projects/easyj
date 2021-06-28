package org.easyj.spring.boot.autoconfigure.office;

import org.easyj.spring.boot.autoconfigure.EasyjStarterConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * EasyJ-Office-Excel配置类
 *
 * @author wangliang181230
 */
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = EasyjStarterConstant.EASYJ_OFFICE_EXCEL_PREFIX)
public class EasyjOfficeExcelProperties {
}
