package com.igoosd;

import com.igoosd.http.HttpService;
import com.igoosd.http.vo.ResultVo;
import com.igoosd.model.CommonVo;
import com.igoosd.model.reciver.GatewayFlowInfo;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * 2017/9/4.
 */
public class SendFlowInfo {

    public static void main(String[] args) throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        //发送数据
        //sendFlowInfo(httpService);
        //注册
        //register(httpService);

        confirm(httpService);
    }



    public static void register(HttpService httpService) throws IOException {
        CommonVo commonVo = new CommonVo();
        commonVo.setGatewayCode(261 + "");
        Call<ResultVo> call =  httpService.register(commonVo);
        Response<ResultVo> response = call.execute();
        System.out.println(response);
        System.out.println(response.body());
    }

    public static void  confirm(HttpService httpService) throws IOException {
        Random random = new Random();
        int enterNum  = random.nextInt(10);
        int exitNum = random.nextInt(10);

        GatewayFlowInfo gatewayFlowInfo = new GatewayFlowInfo(UUID.randomUUID().toString().replaceAll("-",""));
        int gatewayNum = random.nextInt(2) %2 ==0 ? 261 : 361;

        gatewayFlowInfo.setGatewayNum(gatewayNum);
        gatewayFlowInfo.setEnterCarFlow(enterNum);
        gatewayFlowInfo.setExitCarFlow(exitNum);
        gatewayFlowInfo.setEqOfGateway(99);
        gatewayFlowInfo.setEqOfSensorA(99);
        gatewayFlowInfo.setEqOfSensorB(99);
        gatewayFlowInfo.setEqOfSensorC(99);
        gatewayFlowInfo.setEqOfSensorD(99);

        Call<ResultVo> call = httpService.confirmFlowInfoData(gatewayFlowInfo);
        call.execute();
    }


    public static void sendFlowInfo(HttpService httpService){
        Timer timer = new Timer("test");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();

                int enterNum  = random.nextInt(10);
                int exitNum = random.nextInt(10);

                GatewayFlowInfo gatewayFlowInfo = new GatewayFlowInfo(UUID.randomUUID().toString().replaceAll("-",""));
                int gatewayNum = random.nextInt(2) %2 ==0 ? 261 : 361;

                gatewayFlowInfo.setGatewayNum(gatewayNum);
                gatewayFlowInfo.setEnterCarFlow(enterNum);
                gatewayFlowInfo.setExitCarFlow(exitNum);
                gatewayFlowInfo.setEqOfGateway(99);
                gatewayFlowInfo.setEqOfSensorA(99);
                gatewayFlowInfo.setEqOfSensorB(99);
                gatewayFlowInfo.setEqOfSensorC(99);
                gatewayFlowInfo.setEqOfSensorD(99);
                System.out.println("网关模拟发送数据:"+gatewayFlowInfo);


                Call<ResultVo> call =  httpService.postGatewayFlowInfo(gatewayFlowInfo);
                Response<ResultVo> response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
                System.out.println(response.body());
            }
        },1000,1000);
    }
}
