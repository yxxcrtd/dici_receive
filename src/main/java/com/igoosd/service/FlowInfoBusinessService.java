package com.igoosd.service;

import com.igoosd.http.HttpService;
import com.igoosd.http.vo.ResultVo;
import com.igoosd.model.Message;
import com.igoosd.model.reciver.GatewayFlowInfo;
import com.igoosd.model.response.AbsResponse;
import com.igoosd.model.response.CommonResponse;
import com.igoosd.util.Constants;
import com.igoosd.util.GsonUtil;
import com.igoosd.util.LoggerUtils;
import com.igoosd.util.MsgTypeEnum;
import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import static com.igoosd.util.Constants.REDIS_KEY_CONFIRM_DATA;

/**
 * 2017/8/22.
 */
@Service
@Slf4j
public class FlowInfoBusinessService implements BusinessService<GatewayFlowInfo> {

    @Autowired
    private HttpService httpService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 添加一个正常网关状态集合
     */
    private Set<Integer> gatewaySet = new ConcurrentSet<>();

    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.DATA_FLOW_MSG;
    }

    @Override
    public AbsResponse exec(Message<GatewayFlowInfo> message) {
        GatewayFlowInfo gatewayFlowInfo = message.getObj();
        if (!gatewaySet.contains(gatewayFlowInfo.getGatewayNum())) {
            File file = getFile(gatewayFlowInfo.getGatewayNum());
            if (file != null) {
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;
                try {
                    fileReader = new FileReader(file);
                    bufferedReader = new BufferedReader(fileReader);
                    String jsonStr = bufferedReader.readLine();
                    if (!StringUtils.isEmpty(jsonStr)) {
                        GatewayFlowInfo oldGatewayFlowInfo = GsonUtil.parseJsonToObject(jsonStr, GatewayFlowInfo.class);
                        if (gatewayFlowInfo.getFrameNum().equals(oldGatewayFlowInfo.getFrameNum())) {
                            log.warn("存在接入响应异常信息,对异常信息合并处理,当前车流量信息:{},异常车流量信息:{}", GsonUtil.toJson(gatewayFlowInfo), jsonStr);
                            gatewayFlowInfo.setEnterCarFlow(gatewayFlowInfo.getEnterCarFlow() - oldGatewayFlowInfo.getEnterCarFlow());
                            gatewayFlowInfo.setExitCarFlow(gatewayFlowInfo.getExitCarFlow() - oldGatewayFlowInfo.getExitCarFlow());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(bufferedReader != null){
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    }
                    if(fileReader != null){
                        try {
                            fileReader.close();
                        } catch (IOException e) {
                        }
                    }
                }
                //删除文件--tcp 顺序执行 如果存在非真序号相同的数据 说明数据已经过期 直接删除 不做处理
                file.delete();
            }
        }

        try {
            Call<ResultVo> call = httpService.postGatewayFlowInfo(gatewayFlowInfo);
            Response<ResultVo> response = call.execute();
            int code = response.code();
            ResultVo vo = response.body();
            if (200 == code && vo.getSuccess()) {
                return new CommonResponse(gatewayFlowInfo.getGatewayNum());
            } else {
                log.warn("flowInfoBusinessService 业务服务异常....");
            }
            log.warn("业务服务异常信息:{}", response);
            return null;
        } catch (IOException e) {
            //日志记录
            String jsonString = GsonUtil.toJson(gatewayFlowInfo);
            try {
                redisTemplate.opsForSet().add(REDIS_KEY_CONFIRM_DATA, jsonString);
            } catch (Exception e1) {
                log.error("redis 记录flowInfoData 异常...", e);
                // 文件记录  人工处理
                LoggerUtils.GET_INSTANCE().log(Constants.LOGGER_NAME_FLOW_INFO_DATA, gatewayFlowInfo);
            }
            // IO异常尚不明确是请求方异常还是其他方面
            log.warn("车流量信息业务服务IO异常,消息记录后直接返回正常报文");
            return new CommonResponse(gatewayFlowInfo.getGatewayNum());
        }
    }

    @Override
    public void respSuccess(Message<GatewayFlowInfo> message, AbsResponse responses) {
        //添加 正常的GatewayNum
        gatewaySet.add(message.getObj().getGatewayNum());
        log.info("业务执行完成并响应成功,网关数据:{},响应结果:{}", message.getObj(), responses.getResponseByte());
    }

    @Override
    public void respException(Message<GatewayFlowInfo> message) {
        GatewayFlowInfo flowInfo = message.getObj();
        //删除旧数据
        File file = getFile(flowInfo.getGatewayNum());
        if (file != null) {
            file.delete();
        }
        LoggerUtils.GET_INSTANCE().log(Constants.LOGGER_NAME_GATEWAY_RESP_PRE_FILENAME + flowInfo.getGatewayNum(), GsonUtil.toJson(flowInfo));
        gatewaySet.remove(flowInfo.getGatewayNum());
    }


    /**
     * 获取存在的File Instance
     *
     * @param gatewayNum
     * @return
     */
    private File getFile(Integer gatewayNum) {
        String path = LoggerUtils.GET_INSTANCE().getLogPath() + Constants.LOGGER_NAME_GATEWAY_RESP_PRE_FILENAME + gatewayNum + ".txt";
        File file = new File(path);
        return file.exists() && file.isFile() ? file : null;
    }


}
