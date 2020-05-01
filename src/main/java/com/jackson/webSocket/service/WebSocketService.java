package com.jackson.webSocket.service;

import com.jackson.mapper.CurriculumMapper;
import com.jackson.security.utils.JwtTokenUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class WebSocketService {


    /**
     * 初始化容器
     * @param chapterId
     * @param token
     */
    public String initContainer(Integer chapterId,String token,ApplicationContext applicationContext) {

        StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);

        CurriculumMapper curriculumMapper =  applicationContext.getBean(CurriculumMapper.class);

        //解析token
        String username = JwtTokenUtils.getUsernameByToken(token);

        /**
         * 优先判断是否存在未完成的容器
         */
        if(stringRedisTemplate.hasKey(username+"=="+chapterId)){
            //获取容器名称
            String containerName =stringRedisTemplate.opsForValue()
                    .get(username+"=="+String.valueOf(chapterId));
            String command = "docker start -i "+containerName+"\r\n";
            return command;
        }

        //获取docker镜像名称
        String imageName = curriculumMapper.getDockerPath(chapterId);
        //生成容器名称 时间搓 + 用户名
        String dockerName = new Date().getTime()+username;

        String command = "docker run -it --name "+dockerName+" "+imageName+" bash"+"\r\n";

        //存在redis中
        stringRedisTemplate.opsForValue().set(username+"=="+chapterId, dockerName,60*60*60*7, TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间

        return command;
    }

    /**
     * 随机生成n位数字
     */
    public static String getRandomString(int length){
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //2.  由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //3.  长度为几就循环几次
        for(int i=0; i<length; ++i){
            //从62个的数字或字母中选择
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }
}
