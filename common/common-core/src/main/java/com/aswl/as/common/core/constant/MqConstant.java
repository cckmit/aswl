package com.aswl.as.common.core.constant;

/**
 * @author aswl.com
 * @date 2019/4/2 20:48
 */
public class MqConstant {

    /**
     * 提交考试
     */
    public static final String SUBMIT_EXAMINATION_QUEUE = "submit_examination_queue";

    /**
     * 市级平台交换机
     */
    public static final String CITY_PLATFORM_EXCHANGE = "cityPlatformExchange";

    /**
     * 市级设备增删改平台队列
     */
    public static final String CITY_PLATFORM_QUEUE = "cityPlatformQueue";


    /**
     * 市级平台告警数统计队列 (as_alarm_statistics表)
     */
    public static final String CITY_PLATFORM_ALARM_COUNT_QUEUE = "cityPlatformAlarmCountQueue";

    /**
     * 市级平台设备状态汇总队列 (as_device_event_alarm表)
     */
    public static final String CITY_PLATFORM_DEVICEEVENT_QUEUE = "cityPlatformDeviceEventQueue";

    /**
     * 市级平台设备在线统计队列 (as_city_alarm_statistics表)
     */
    public static final String CITY_PLATFORM_ONLINESTATICS_QUEUE = "cityPlatformOnlineStaticsQueue";

    /**
     * 市级平台主动同步交换机
     */
    public static final String CITY_PLATFORM_FANOUT_EXCHANGE = "cityPlarformFanoutExchange";

    /**
     * 市级平台主动同步队列前缀
     */
    public static final String CITY_PLATFORM_FANOUT = "cityPlarformFanout";

    /**
     * 告警统计队列 (as_city_alarm_statistics表)
     */
    public static final String CITY_PLATFORM_ALARM_STATISTICS_QUEUE = "cityPlatAlarmStatisticsQueue";

    /**
     * 在线统计 (as_online_statistics表)
     */
    public static final String CITY_PLATFORM_ONLINE_QUEUE = "cityPlatOnlineQueue";

    /**
     * 在线统计 (as_event_network表)
     */
    public static final String CITY_PLATFORM_NETWORK_QUEUE = "cityPlatNetWorkQueue";

    /**
     * 工单时间段统计
     */
    public static final String CITY_PLATFORM_RUN_PERIOD_QUEUE =  "cityPlatRunPeriodStatistics";

    /**
     * 系统MQ消息变量接口
     */
    public interface SystemMqMessage{
        /** 通用信息交换机 */
        String COMMON_MESSAGE_FANOUT_EXCHANGE = "commonMessageFanoutExchange";

        /** 系统广播信息队列 */
        String SYSTEM_BROADCAST_MESSAGE_QUEUE = "systemBroadcastMessageQueue";
    }

}
