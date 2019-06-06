package com.igoosd.schedule;

import com.igoosd.http.HttpService;
import com.igoosd.http.vo.ResultVo;
import com.igoosd.model.reciver.GatewayFlowInfo;
import com.igoosd.util.Constants;
import com.igoosd.util.GsonUtil;
import com.igoosd.util.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2017/9/5.
 * flowInfodata 二次确认 调度
 */
@Component
@Slf4j
public class FlowInfoDataConfirmTask {

    @Autowired
    private HttpService httpService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    // TODO 测试 每隔5分钟执行
    @Scheduled(cron = "0 */5 * * * ?")
    public void confirmTask() {
        //获取redis set 数据进行整理
        int i = 0;
        List<String> rewriteDataList = new ArrayList<>();
        for (; ; ) {
            String jsonStr = redisTemplate.opsForSet().pop(Constants.REDIS_KEY_CONFIRM_DATA);
            if (StringUtils.isEmpty(jsonStr)) {
                break;
            }
            i++;
            GatewayFlowInfo flowInfo = GsonUtil.parseJsonToObject(jsonStr, GatewayFlowInfo.class);
            //http请求 业务服务二次确认回调用
            Call<ResultVo> call = httpService.confirmFlowInfoData(flowInfo);
            try {
                Response<ResultVo> response = call.execute();
                log.info("flowInfo：{},二次确认响应结果:{}", jsonStr, response);
                if (response.code() != 200) {
                    LoggerUtils.GET_INSTANCE().log(Constants.LOGGER_NAME_FLOW_INFO_DATA, jsonStr);
                }
            } catch (IOException e) {
                //还是IO异常的话 回写到redis中进行 处理
                rewriteDataList.add(jsonStr);
                LoggerUtils.GET_INSTANCE().log(Constants.LOGGER_NAME_FLOW_INFO_DATA, jsonStr);
            }
        }
        //重新插入 待下次调度执行
        if(!rewriteDataList.isEmpty()){
            redisTemplate.opsForSet().add(Constants.REDIS_KEY_CONFIRM_DATA,rewriteDataList.toArray(new String[rewriteDataList.size()]));
        }
        log.info("日期:{},调度完成，共处理请求异常数据{}条数", new Date(), i);
    }
}
