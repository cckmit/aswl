package com.aswl.as.metadata.config;

import com.aswl.as.metadata.common.Globals;
import com.aswl.as.metadata.websocket.request.SocketUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.LinkedList;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 自定义调度器，用于控制心跳线程
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        taskScheduler.initialize();

        config.enableSimpleBroker("/topic" , "/queue", "/user")
                .setHeartbeatValue(new long[]{10000,10000})
                .setTaskScheduler(taskScheduler);
        config.setApplicationDestinationPrefixes(Globals.WebSocketConsts.APP_PREFIX);
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Globals.WebSocketConsts.ENDPOINT)
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor(){
            public Message<?> preSend(Message<?> message, MessageChannel channel){
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
                    if (raw instanceof Map) {
                        Object name = ((Map) raw).get("userName");
                        if (name != null && name instanceof LinkedList) {
                            // 设置当前访问的认证用户
//                            Map userMap = new HashMap();
//                            userMap.put("userName", ((LinkedList)name).get(0).toString());
//                            accessor.setSessionAttributes(userMap);
                            accessor.setUser(new SocketUser(((LinkedList)name).get(0).toString()));
                        }
                    }
                }
                return message;
            }
        });
    }
}
