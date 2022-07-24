package com.aswl.as.ibrs.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

// 到时候可能需要保存到数据库,拥有重发等校验
//@AllArgsConstructor
@Slf4j
@Service
public class MailService {

    @Value(value = "${asmail.host}")
    private String mailHost;

    @Value(value = "${asmail.port}")
    private Integer mailPort;

    @Value(value = "${asmail.auth}")
    private Boolean mailAuth;

    @Value(value = "${asmail.from}")
    private String mailFrom;

    @Value(value = "${asmail.user}")
    private String mailUser;

    @Value(value = "${asmail.pass}")
    private String mailPass;

    @Value(value = "${asmail.isSmsMail}")
    private Boolean isSmsMail;

    @Value(value = "${asmail.debug}")
    private Boolean debug;

    @Value(value = "${asmail.sslEnable}")
    private Boolean sslEnable;
    
    public static MailAccount initMailAccount;

    private MailAccount getInitMailAccount()
    {
        if(initMailAccount==null)
        {
            initMailAccount = new MailAccount();
            initMailAccount.setHost(mailHost);
            initMailAccount.setPort(mailPort);
            initMailAccount.setAuth(mailAuth);
            initMailAccount.setFrom(mailFrom);
            initMailAccount.setUser(mailUser);
            initMailAccount.setPass(mailPass);
            initMailAccount.setDebug(debug);
            initMailAccount.setSslEnable(sslEnable);
        }

        return initMailAccount;
    }

    public void sendMail(List<String> list, String subject, String content, boolean isHtml)
    {
        if(isSmsMail)
        {
            //调用短信通知发送通知
            String msg= CollectionUtil.join(list,",")+"\n"+content;
            msg.substring(0,70);//强制70个字符
        }
        else
        {
            //普通邮件
        /*  MailAccount account=getInitMailAccount();
           list= CollUtil.newArrayList("191154120@qq.com");
           subject="测试";
          content="邮件来自Hutool测试";
         isHtml=false;
        */
            MailUtil.send(getInitMailAccount(), list, subject, content, isHtml);
        }
    }


    public void sendMail(String to, String subject, String content, boolean isHtml)
    {
        sendMail(Arrays.asList(to),subject,content,isHtml);
    }

    public void sendMail(String to, String subject, String content)
    {
        sendMail(to,subject,content,false);
    }

    public static void main(String[] args) {
        MailService m = new MailService();
        m.sendMail("191154120@qq.com","dingfei","您好，测试一下",false);
    }

}
