package com.igoosd.service;

import com.igoosd.http.HttpService;
import com.igoosd.http.vo.ResultVo;
import com.igoosd.model.CommonVo;
import com.igoosd.model.Message;
import com.igoosd.model.response.AbsResponse;
import com.igoosd.model.response.CommonResponse;
import com.igoosd.util.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * 2017/8/22.
 */
@Service
@Slf4j
public class GatewayRegBusinessService implements BusinessService<Integer> {


    @Autowired
    private HttpService httpService;

    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.GATEWAY_REG;
    }

    @Override
    public AbsResponse exec(Message<Integer> message) {
        try {
            Integer gatewayNum = message.getObj();
            CommonVo commonVo = new CommonVo();
            commonVo.setGatewayCode(gatewayNum + "");
            Call<ResultVo> call = httpService.register(commonVo);
            Response<ResultVo> response = call.execute();
            log.info("网关注册：{}",response.toString());
            if (200 == response.code()) {
                ResultVo vo = response.body();
                if (vo.getSuccess()) {
                    log.info("网关号:{},注册成功!", gatewayNum);
                    return new CommonResponse(gatewayNum);
                }
            }
            return null;
        } catch (IOException e) {
            log.error("网关注册异常", e);
        }
        return null;
    }

    @Override
    public void respSuccess(Message<Integer> message, AbsResponse responses) {
        log.info("网关注册成功,网关号:{},响应消息内容:{}",message.getObj(),responses.getResponseByte());
    }

    @Override
    public void respException(Message<Integer> message) {
        log.warn("当前注册网关异常,注册网关号:",message.getObj());
    }


}
