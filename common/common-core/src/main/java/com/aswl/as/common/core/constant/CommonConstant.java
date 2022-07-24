package com.aswl.as.common.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公用常量
 *
 * @author aswl.com
 * @date 2018-08-23 12:00
 */
public class CommonConstant {

    /**
     * 正常
     */
    public static final Integer STATUS_NORMAL = 0;

    /**
     * 异常
     */
    public static final String STATUS_EXCEPTION = "1";

    /**
     * 锁定
     */
    public static final String STATUS_LOCK = "9";

    /**
     * jwt签名
     */
    public static final String SIGN_KEY = "IC";

    /**
     * 页码
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 分页大小
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序
     */
    public static final String SORT = "sort";

    /**
     * 排序方向
     */
    public static final String ORDER = "order";

    /**
     * 默认页数
     */
    public static final String PAGE_NUM_DEFAULT = "1";

    /**
     * 默认分页大小
     */
    public static final String PAGE_SIZE_DEFAULT = "10";

    /**
     * 默认排序
     */
    public static final String PAGE_SORT_DEFAULT = "create_date";

    /**
     * 默认排序方向
     */
    public static final String PAGE_ORDER_DEFAULT = " desc";

    /**
     * 排序方向-降序
     */
    public static final String PAGE_ORDER_DESC = "descending";

    /**
     * 排序方向-升序
     */
    public static final String PAGE_ORDER_ASC = "ascending";

    /**
     * 正常状态
     */
    public static final Integer DEL_FLAG_NORMAL = 0;

    /**
     * 删除状态
     */
    public static final Integer DEL_FLAG_DEL = 1;

    /**
     * 路由配置在Redis中的key
     */
    public static final String ROUTE_KEY = "_ROUTE_KEY";

    /**
     * 菜单标识
     */
    public static final String MENU = "0";

    /**
     * token请求头名称
     */
    public static final String REQ_HEADER = "Authorization";

    /**
     * token分割符
     */
    public static final String TOKEN_SPLIT = "Bearer ";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 默认排序
     */
    public static final String DEFAULT_SORT = "30";

    /**
     * utf-8
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 阿里
     */
    public static final String ALIYUN_SMS = "aliyun_sms";

    /**
     * 参数校验失败
     */
    public static final String IllEGAL_ARGUMENT = "参数校验失败！";

    /**
     * 保存code的前缀
     */
    public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

    /**
     * 验证码长度
     */
    public static final String CODE_SIZE = "6";

    /**
     * Bearer
     */
    public static final String AUTHORIZATION_BEARER = "Bearer ";

    /**
     * 密码类型
     */
    public static final String GRANT_TYPE_PASSWORD = "password";

    /**
     * 手机号类型
     */
    public static final String GRANT_TYPE_MOBILE = "mobile";

    /**
     * 微信类型
     */
    public static final String GRANT_TYPE_WX = "wx";

    /**
     * 租户编号请求头
     */
    public static final String TENANT_CODE_HEADER = "Tenant-Code";

    /**
     * 项目Id请求头
     */
    public static final String PROJECT_ID_HEADER = "Project-Id";

    /**
     * 默认超时时间
     */
    public static final String CACHE_EXPIRE = "CACHE_EXPIRE";

    /**
     * 是运营端传递过来的数据
     */
    public static final String IS_ASOS_TRUE="1";

    /**
     * 不是运营端传递过来的数据，0或者null都不是运营端传递过来的数据
     */
    public static final String IS_ASOS_FALSE="0";

    // --- 用作 SYS_CONFIG 表的常用 param_key
    /**
     * 报表报送人
     */
    public static final String CONFIG_PARAM_KEY_REPORT_PERSON ="REPORT_PERSON";

    /**
     * 报表抄送人
     */
    public static final String CONFIG_PARAM_KEY_REPORT_COPY_PERSON ="REPORT_COPY_PERSON";

    /**
     * 统计总表打印排版
     */
    public static final String CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE ="REPORT_TOTAL_PRINT_TYPE";

    /**
     * 推送移动端常量
     */
    public interface PushMobilePhoneConstant{

        /** 推送业务Code */
        String BUSINESS_CODE = "businessCode";

        @Getter
        @AllArgsConstructor
        enum BusinessCodeEnum{
            /** 设备状态通知 */
            DEVICE_STATUS_Notice("deviceStatusNotice"),
            /** 工单审核 */
            WORK_AUDIT("workAudit"),
            /**待处理工单数 */
            WORK_UNDO_NUM_NOTICE("workUndoNumNotice"),
            /**工单详情通知 */
            WORK_DETAIL_NOTICE("workDetailNotice");

            private String code;
        }
    }

    /**
     * 系统有效期认证
     */
    public interface SysValidityAuth{
        /** 系统有效期认证KEY */
        String SYS_VALIDITY_AUTH_KEY = "UUID";
        /** KEY加密密钥 */
        String ENCRYPT_SECRET = "www.aswl.net";
    }

    /**
     * 数据库名常量
     */
    public interface DataBaseConstant{
        /** as-auth数据库 */
        String AUTH_DATABASE = "as-auth";
        /** as-gateway数据库 */
        String GATEWAY_DATABASE = "as-gateway";
        /** as-ibrs数据库 */
        String IBRS_DATABASE = "as-ibrs";
        /** as-user数据库 */
        String USER_DATABASE = "as-user";
    }

    /**
     * 视频流推送 redis key
     */
    public static final String REDIS_VIDEO_STREAM_OUTPUT = "VIDEO_STREAM_OUTPUT";
    public static final String REDIS_VIDEO_STREAM_FLV_OUTPUT = "VIDEO_STREAM_FLV_OUTPUT";

    /**
     * 事件告警类型分隔符
     */
    public interface EventAlarmSeparate {
        /** 逗号 */
        String COMMA = ",";
        /** 分号 */
        String SEMICOLON = ";";
    }
}

