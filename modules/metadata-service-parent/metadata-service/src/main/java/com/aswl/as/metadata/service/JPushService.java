package com.aswl.as.metadata.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

//TODO 可能以后需要保存到数据库，因为没有具体的需求
@AllArgsConstructor
@Slf4j
@Service
public class JPushService {

    @Value(value = "${jpush.appKey}")
    private static String APP_KEY = "cdd96785a0ac47c9ebc0df5c";

    @Value(value = "${jpush.masterSecret}")
    private static String MASTER_SECRET = "70434feda2ec94e757b4c5bd";

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
    public JPushResult sendAllAndroid(String alert,String title)
    {
        return sendAndroidAudience(newAndroidNotificationBuilder(alert,title).build());
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public JPushResult sendAllAndroid(String alert,String title,String extraKey, String extraValue)
    {
        return sendAndroidAudience(newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public JPushResult sendAllAndroid(String alert,String title,String extraKey, Number extraValue)
    {
        return sendAndroidAudience(newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public JPushResult sendAllAndroid(String alert,String title,String extraKey, Boolean extraValue)
    {
        return sendAndroidAudience(newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extraKey 扩展的业务数据键
     * @param extraValue 扩展的业务数据值
     */
    public JPushResult sendAllAndroid(String alert,String title,String extraKey, JsonObject extraValue)
    {
        return sendAndroidAudience(newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }

    /**
     * 直接发给所有安卓用户消息
     * @param alert
     * @param title
     * @param extras 扩展的业务数据
     */
    public JPushResult sendAllAndroid(String alert,String title,Map<String, String> extras)
    {
        return sendAndroidAudience(newAndroidNotificationBuilder(alert,title).addExtras(extras).build());
    }

    //----- 发送给指定的人（使用别名）
    // 实际上如果后面有多平台的话，只要改个函数名，去掉安卓，把统一的sendAndroidAudience 修改一下多加其它ios或者winPhone的就行
    public JPushResult sendAliasAndroid(String alert,String title,String... alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, String extraValue,String... alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, Number extraValue,String... alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, Boolean extraValue,String... alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, JsonObject extraValue,String... alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,Map<String, String> extras,String... alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtras(extras).build());
    }

    public JPushResult sendAliasAndroid(String alert,String title,Collection<String> alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).build());
    }
    public JPushResult sendAliasAndroid(String alert, String title, String extraKey, String extraValue, Collection<String> alias)
    {
        return sendAndroidAudience(Audience.alias(),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, Number extraValue,Collection<String> alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, Boolean extraValue,Collection<String> alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,String extraKey, JsonObject extraValue,Collection<String> alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtra(extraKey,extraValue).build());
    }
    public JPushResult sendAliasAndroid(String alert,String title,Map<String, String> extras,Collection<String> alias)
    {
        return sendAndroidAudience(Audience.alias(alias),newAndroidNotificationBuilder(alert,title).addExtras(extras).build());
    }
    //----

    /**
     * 组合成安卓的消息，可以在这个Builder添加 addExtra 扩展数据
     * @param alert
     * @param title
     * @return
     */
    public AndroidNotification.Builder newAndroidNotificationBuilder(String alert,String title)
    {
        return AndroidNotification.newBuilder()
                .setAlert(alert)
                .setTitle(title);
    }

    /**
     * 发给所有安卓用户
     * @param androidNotification
     */
    public static JPushResult sendAndroidAudience(AndroidNotification androidNotification)
    {
        return sendAndroidAudience(Audience.all(),androidNotification);
    }

    /**
     * 发给安卓用户
     * @param audience
     * @param androidNotification
     */
    public static JPushResult sendAndroidAudience(Audience audience,AndroidNotification androidNotification)
    {
        return send(Platform.android(),audience,Notification.newBuilder().addPlatformNotification(androidNotification).build());
    }

    /**
     * 发给安卓用户
     * @param audience
     * @param notification
     */
    private static JPushResult sendAndroidAudience(Audience audience,Notification notification)
    {
        return send(Platform.android(),audience,notification);
    }

    /**
     * 发给所有安卓用户
     * @param notification
     */
    private static JPushResult sendAllAndroidAudience(Notification notification)
    {
        return send(Platform.android(),Audience.all(),notification);
    }

    /**
     * 发给所有平台所有用户
     * @param notification
     */
    public static JPushResult sendAllAudience(Notification notification)
    {
        return send(Platform.all(),Audience.all(),notification);
    }

    // 需要发送信息 TODO 需要有返回值 TODO
    /**
     * 发送消息，带回调参数
     * @param platform
     * @param audience
     * @param notification
     */
    private static JPushResult send(Platform platform,Audience audience,Notification notification) {

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
                .setNotification(notification);

        try {
            //TODO 这里需要报错
            PushResult result = jpushClient.sendPush(payloadBuilder.build());
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

        public boolean getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            isSuccess = isSuccess;
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

