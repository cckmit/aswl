package com.aswl.as.common.core.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

// 可能以后需要保存到数据库，因为没有具体的需求

/**
 * 用来推送信息给app， alias使用的是userId，可以发给指定的人，一个alias 最多有十个设备
 * tag使用的是tenant_code 可以发给一个租户编码的人
 *
 * sendAlias 发送通知给指定的用户，（会显示在通知栏）
 * sendTag 发送通知给一种标记的人，比如发给一个租户所有的用户 （会显示在通知栏）
 * sendMessageAlias 发送信息给置顶用户，msgContent必填，但是跟app商量后数据放在Extras里面（不会显示在通知栏）
 * sendMessageTag 发送通知给一种标记的人，比如发给一个租户所有的用户,msgContent必填，但是跟app商量后数据放在Extras里面（不会显示在通知栏）
 *
 * 所有函数包含的参数
 * String alert 通知的主体 实际上跟title两个一起存在或者其中一个存在就行，如果两个一起存在alert会在title换一行的下面显示
 * String title 通知的标题
 * extras 扩展的内容，不会显示在通知栏里面，但是app能获得里面的数据
 * Integer timeToLive 消息离线保存时间，单位是秒，如果设置300，就是没有在线的人，5分钟后，再上线，就不会收到这条通知，默认是一天，最长保存10天，传 null 就使用默认值，传0就是不离线保存
 *
 * 包含Customize 的函数，能自定义发送的对象和信息，一般不需要用
 *
 */
@AllArgsConstructor
@Slf4j
@Service
public class JPushUtils {

//    @Value(value = "${jpush.appKey}")
    private static String APP_KEY = "cdd96785a0ac47c9ebc0df5c";

//    @Value(value = "${jpush.masterSecret}")
    private static String MASTER_SECRET = "70434feda2ec94e757b4c5bd";

    //5分钟，APP说信息最多发出去，如果设备离线，只在5分钟内接收到信息
    private static int SAVE_TIME=300;

    private static JPushClient initJPushClient;

    private static JPushClient getInitJPushClient()
    {
        if(initJPushClient==null)
        {
            //初始化
            ClientConfig config = ClientConfig.getInstance();
            // Setup the custom hostname
//            config.setPushHostName("https://api.jpush.cn");
            initJPushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, config);
        }
        return initJPushClient;
    }

    // 暂时只有安卓，所以直接用安卓

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     */
    public static JPushResult sendAll(String alert,String title,Integer timeToLive)
    {
        return sendCustomizeAudience(newCustomizeNotificationBuilder(alert,title).build(),timeToLive);
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public static JPushResult sendAll(String alert,String title,String extraKey, String extraValue,Integer timeToLive)
    {
        return sendCustomizeAudience(newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public static JPushResult sendAll(String alert,String title,String extraKey, Number extraValue,Integer timeToLive)
    {
        return sendCustomizeAudience(newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public static JPushResult sendAll(String alert,String title,String extraKey, Boolean extraValue,Integer timeToLive)
    {
        return sendCustomizeAudience(newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public static JPushResult sendAll(String alert,String title,String extraKey, JsonObject extraValue,Integer timeToLive)
    {
        return sendCustomizeAudience(newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extras 扩展的业务数据
     */
    public static JPushResult sendAll(String alert,String title,Map<String, String> extras,Integer timeToLive)
    {
        return sendCustomizeAudience(newCustomizeNotificationBuilder(alert,title).addExtras(extras).build(),timeToLive);
    }







    //----- 发送给指定的人（使用别名） 别名一般用用户id
    // 实际上如果后面有多平台的话，只要改个函数名，去掉安卓，把统一的sendAndroidAudience 和 newAndroidNotificationBuilder 修改一下多加其它ios或者winPhone的就行
    public static JPushResult sendAlias(String alert,String title,Integer timeToLive,String... alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, String extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, Number extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, Boolean extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, JsonObject extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,Map<String, String> extras,Integer timeToLive,String... alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtras(extras).build(),timeToLive);
    }

    public static JPushResult sendAlias(String alert,String title,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert, String title, String extraKey, String extraValue, Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, Number extraValue,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, Boolean extraValue,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,String extraKey, JsonObject extraValue,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendAlias(String alert,String title,Map<String, String> extras,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeAudience(Audience.alias(alias), newCustomizeNotificationBuilder(alert,title).addExtras(extras).build(),timeToLive);
    }
    //----

    // 通过 tag 来发送 ，tag一般是tenantCode，tag可以多个
    public static JPushResult sendTag(String alert,String title,Integer timeToLive,String... tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, String extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, Number extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, Boolean extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, JsonObject extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,Map<String, String> extras,Integer timeToLive,String... tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtras(extras).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert, String title, String extraKey, String extraValue, Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, Number extraValue,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, Boolean extraValue,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,String extraKey, JsonObject extraValue,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendTag(String alert,String title,Map<String, String> extras,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeAudience(Audience.tag(tag), newCustomizeNotificationBuilder(alert,title).addExtras(extras).build(),timeToLive);
    }


    //------------------------------------------------   发送自定义消息-----------------------------------------


    //----- 发送给指定的人（使用别名） 别名一般用用户id
    // 实际上如果后面有多平台的话，只要改个函数名，去掉安卓，把统一的sendMessageAndroidAudience 和 newMessageBuilder 修改一下多加其它ios或者winPhone的就行
    public static JPushResult sendMessageAlias(String msgContent,Integer timeToLive,String... alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,String extraKey, String extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,String extraKey, Number extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,String extraKey, Boolean extraValue,Integer timeToLive,String... alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent, String extraKey, JsonObject extraValue, Integer timeToLive,String... alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,Map<String, String> extras,Integer timeToLive,String... alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtras(extras).build(),timeToLive);
    }

    public static JPushResult sendMessageAlias(String msgContent, Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent, String extraKey, String extraValue, Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,String extraKey, Number extraValue,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,String extraKey, Boolean extraValue,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,String extraKey, JsonObject extraValue,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageAlias(String msgContent,Map<String, String> extras,Integer timeToLive,Collection<String> alias)
    {
        return sendCustomizeMessageAudience(Audience.alias(alias), newCustomizeMessageBuilder(msgContent).addExtras(extras).build(),timeToLive);
    }

    // 通过 tag 来发送 自定义消息 ，tag一般是tenantCode，tag可以多个
    public static JPushResult sendMessageTag(String msgContent,Integer timeToLive,String... tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, String extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, Number extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, Boolean extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, JsonObject extraValue,Integer timeToLive,String... tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,Map<String, String> extras,Integer timeToLive,String... tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtras(extras).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent, String extraKey, String extraValue, Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, Number extraValue,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, Boolean extraValue,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,String extraKey, JsonObject extraValue,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtra(extraKey,extraValue).build(),timeToLive);
    }
    public static JPushResult sendMessageTag(String msgContent,Map<String, String> extras,Integer timeToLive,Collection<String> tag)
    {
        return sendCustomizeMessageAudience(Audience.tag(tag), newCustomizeMessageBuilder(msgContent).addExtras(extras).build(),timeToLive);
    }

    //----------------------------------------------------------------------------------------------------------













    /**
     * 组合成安卓的消息，可以在这个Builder添加 addExtra 扩展数据
     * @param alert
     * @param title
     * @return
     */
    public static AndroidNotification.Builder newCustomizeNotificationBuilder(String alert, String title)
    {
        return AndroidNotification.newBuilder()
                .setAlert(alert)
                .setTitle(title);
    }

    /**
     * 构造信息
     * @param msgContent 消息内容，必填，
     * @return
     */
    public static  Message.Builder newCustomizeMessageBuilder(String msgContent)
    {
        return newCustomizeMessageBuilder(msgContent,null,null);
    }

    /**
     * 构造信息
     * @param msgContent 消息内容，必填，
     * @param title 非必填，跟APP确认也不必不需要填，
     * @param contentType 内容类型，非必填，跟前端协商也不需要这个,这个实际上可以放 CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum 的类型，但是统一放在Extras里面，到时候如果需要这个信息，
     * @return
     */
    private static  Message.Builder newCustomizeMessageBuilder(String msgContent, String title, String contentType)
    {
        return Message.newBuilder().setTitle(title).setMsgContent(msgContent).setContentType(contentType);
    }

    /**
     * 发给所有安卓用户
     * @param androidNotification
     */
    public static JPushResult sendCustomizeAudience(AndroidNotification androidNotification,Integer timeToLive)
    {
        return sendCustomizeAudience(Audience.all(),androidNotification,timeToLive);
    }

    /**
     * 发给安卓用户 （实际上不分安卓和其它平台，比如以后有了其它平台比如ios，直接添加ios的）
     * @param audience
     * @param androidNotification
     */
    public static JPushResult sendCustomizeAudience(Audience audience, AndroidNotification androidNotification,Integer timeToLive)
    {
        return sendCustomize(Platform.android(),audience,Notification.newBuilder().addPlatformNotification(androidNotification).build(),timeToLive);
    }

    /**
     * 发给 自定义消息 给安卓用户
     * @param audience
     * @param message
     */
    public static JPushResult sendCustomizeMessageAudience(Audience audience, Message message,Integer timeToLive)
    {
        return sendCustomizeMessage(Platform.android(),audience,message,timeToLive);
    }

    /**
     * 发给安卓用户
     * @param audience
     * @param notification
     */
    private static JPushResult sendCustomizeAudience(Audience audience, Notification notification,Integer timeToLive)
    {
        return sendCustomize(Platform.android(),audience,notification,timeToLive);
    }

    /**
     * 发给所有安卓用户
     * @param notification
     */
    private static JPushResult sendCustomizeAllAudience(Notification notification,Integer timeToLive)
    {
        return sendCustomize(Platform.android(),Audience.all(),notification,timeToLive);
    }

//    /**
//     * 发给所有平台所有用户
//     * @param notification
//     */
//    public static JPushResult sendAllAudience(Notification notification)
//    {
//        return send(Platform.all(),Audience.all(),notification);
//    }

    // 需要发送信息 TODO 需要有返回值 TODO
    /**
     * 发送消息，带回调参数
     * @param platform
     * @param audience
     * @param notification
     */
    private static JPushResult sendCustomize(Platform platform, Audience audience, Notification notification,Integer timeToLive) {

        JPushClient jpushClient=getInitJPushClient();

        // 添加扩展
        /*
        notification = Notification.newBuilder()
                .addPlatformNotification(AndroidNotification.newBuilder()
                        .setAlert("测试内容")
                        .setTitle("Alert test")
                        .addExtra("test","测试1234444")
                        .build())
                .build();
        */

        PushPayload.Builder payloadBuilder = new PushPayload.Builder()
                .setPlatform(platform)
                .setAudience(audience)
                .setNotification(notification)
                ;

//        payloadBuilder.build().resetOptionsTimeToLive(ConstantStr.TIMETOLIVE);

        try {
            //TODO 这里需要报错

            PushPayload p=payloadBuilder.build();
            if(timeToLive!=null)
            {
                p.resetOptionsTimeToLive(timeToLive);
            }

            PushResult result = jpushClient.sendPush(p);
//            log.info("Got result - " + result);
            return new JPushResult(true,result);

        } catch (APIConnectionException e) {

//            log.error("Connection error. Should retry later. ", e);
            return new JPushResult(false,"发送推送 连接服务器错误 ，请检查或稍后重试",e);

        } catch (APIRequestException e1) {

            //errorData可以放里面的内容
            return new JPushResult(false,"发送推送错误，服务器返回错误 Msg ID 为"+ e1.getMsgId()+"状态为 "+e1.getStatus()+" ,错误码为 "+ e1.getErrorCode() +" ,错误信息为 "+e1.getErrorMessage(),e1);

//            log.error("Error response from JPush server. Should review and fix it. ", e);
//            log.info("HTTP Status: " + e.getStatus());
//            log.info("Error Code: " + e.getErrorCode());
//            log.info("Error Message: " + e.getErrorMessage());
//            log.info("Msg ID: " + e.getMsgId());

        } catch (Exception e2)
        {
            return new JPushResult(false,"发送推送错误,错误为 "+e2.getMessage(),e2);
        }
    }

    /**
     * 发送消息，带回调参数
     * @param platform
     * @param audience
     * @param message
     */
    private static JPushResult sendCustomizeMessage(Platform platform, Audience audience, Message message,Integer timeToLive) {

        JPushClient jpushClient=getInitJPushClient();

        PushPayload.Builder payloadBuilder = new PushPayload.Builder()
                .setPlatform(platform)
                .setAudience(audience)
                .setMessage(message);

        try {

            PushPayload p=payloadBuilder.build();
            if(timeToLive!=null)
            {
                p.resetOptionsTimeToLive(timeToLive);
            }


            PushResult result = jpushClient.sendPush(p);
            return new JPushResult(true,result);

        } catch (APIConnectionException e) {

            return new JPushResult(false,"发送自定义消息 连接服务器错误 ，请检查或稍后重试",e);

        } catch (APIRequestException e1) {

            //errorData可以放里面的内容
            return new JPushResult(false,"发送自定义消息错误，服务器返回错误 Msg ID 为"+ e1.getMsgId()+"状态为 "+e1.getStatus()+" ,错误码为 "+ e1.getErrorCode() +" ,错误信息为 "+e1.getErrorMessage(),e1);

        } catch (Exception e2)
        {
            return new JPushResult(false,"发送自定义消息错误,错误为 "+e2.getMessage(),e2);
        }
    }

    public static class JPushResult
    {
        boolean isSuccess=false;

        String message;

        PushResult pushResult;

        Exception e;


        public JPushResult(boolean isSuccess, PushResult pushResult) {
            this.isSuccess = isSuccess;
            this.pushResult = pushResult;
        }

        public JPushResult(boolean isSuccess, String message, Exception e) {
            this.isSuccess = isSuccess;
            this.message = message;
            this.e = e;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public PushResult getPushResult() {
            return pushResult;
        }

        public void setPushResult(PushResult pushResult) {
            this.pushResult = pushResult;
        }

        public Exception getE() {
            return e;
        }

        public void setE(Exception e) {
            this.e = e;
        }
    }
}

