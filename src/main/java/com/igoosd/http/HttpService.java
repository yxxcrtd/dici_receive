package com.igoosd.http;

import com.igoosd.http.vo.ResultVo;
import com.igoosd.model.CommonVo;
import com.igoosd.model.reciver.GatewayFlowInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 2017/8/24.
 * 类似http Client 请求http/https 服务
 */
public interface HttpService {

    @POST("acceptor/flow")
    Call<ResultVo> postGatewayFlowInfo(@Body GatewayFlowInfo flowInfo);


    @POST("acceptor/register")
    Call<ResultVo> register(@Body CommonVo vo);

    @POST("acceptor/confirm")
    Call<ResultVo> confirmFlowInfoData(@Body GatewayFlowInfo flowInfo);
}
