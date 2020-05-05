package com.jackson.scheduler;

import com.jackson.mapper.UserCurrMapper;
import com.jackson.model.UserCurr;
import com.jackson.webSocket.utils.SSHAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class CurrScheduler {

    @Value("${myUbuntu.address}")
    private String address = "114.55.218.143";

    @Value("${myUbuntu.username}")
    private String username = "root";

    @Value("${myUbuntu.password}")
    private String password = "Root20171211st";

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //操作SSH的工具类
    private SSHAgent sshAgent;

    @Autowired
    private UserCurrMapper userCurrMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //每隔5分钟执行一次
    @Scheduled(fixedRate = 1000*60*5)
    public void automatic_appointment(){
        log.info("定时任务执行时间：" + dateFormat.format(new Date()));
        try {
            //查询出数据库中所有记录
            List<UserCurr> currs = userCurrMapper.selectList(null);
            if(currs!=null){
                //依次获取redis中的数据
                currs.forEach(e->{
                    //获取
                    Long times = stringRedisTemplate.getExpire(e.getUserName()+"=="+e.getCurrId());
                    if(times<60*5){
                        //根据key删除缓存
                        stringRedisTemplate.delete(e.getUserName()+"=="+e.getCurrId());
                        //删除容器
                        this.sshAgent.printWriter.write("docker rm "+e.getCurrName());
                        this.sshAgent.printWriter.flush();
                    }
                });
            }
        }catch (Exception e){
            log.warn("Scheduler || 定时器出错 {}",e);
        }
        log.info("定时器结束");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message) {
        log.info("来自客户端的消息:" + message);
        //群发消息
        try {
            //通过工具类的标准输入网远程服务器中写内容
            this.sshAgent.printWriter.write( message+ "\r\n");
            this.sshAgent.printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
