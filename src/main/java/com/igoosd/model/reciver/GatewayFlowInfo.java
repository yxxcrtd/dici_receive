package com.igoosd.model.reciver;

import com.igoosd.model.AbsModel;
import com.igoosd.util.ByteUtils;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 2017/8/9.
 * socket server 接收 来自网关的数据模型
 */
@Data
@ToString
public class GatewayFlowInfo extends AbsModel {

    /**
     * 网关号
     */
    private Integer gatewayNum;
    /**
     * 网关电量
     */
    private Integer eqOfGateway;

    /**
     *  驶入车流量
     */
    private Integer enterCarFlow;

    private Integer exitCarFlow;

    /**
     * 探测器A 电量
     */
    private Integer eqOfSensorA;

    /**
     * 探测器B 电量
     */
    private Integer eqOfSensorB;

    /**
     * 探测器C 电量
     */
    private Integer eqOfSensorC;
    /**
     * 探测器D 电量
     */
    private Integer eqOfSensorD;

    /**
     * 帧序号
     */
    private Integer frameNum;

    /**
     * 创建时间
     */
    private Long createTime = new Date().getTime();



    //测试 修饰符由private-->public 请上线前 变更回 public
    public GatewayFlowInfo(String key){
        super(key);
    }


    /**
     * 解析生成 业务对象
     * @param data  bytes-->data int[] 后 数据
     * @return
     */
    public static final GatewayFlowInfo parseGatewayInfo(byte[] data,String key){
        GatewayFlowInfo gatewayInfo = new GatewayFlowInfo(key);
        gatewayInfo.setGatewayNum(ByteUtils.getOriginVal(data[3],data[4]));
        gatewayInfo.setEqOfGateway((int) data[5]);
        gatewayInfo.setExitCarFlow(ByteUtils.getOriginVal(data[8],data[9]));
        gatewayInfo.setEqOfSensorA((int) data[10]);
        gatewayInfo.setEqOfSensorB((int) data[11]);
        gatewayInfo.setEnterCarFlow(ByteUtils.getOriginVal(data[13],data[14]));
        gatewayInfo.setEqOfSensorC((int) data[15]);
        gatewayInfo.setEqOfSensorD((int) data[16]);
        gatewayInfo.setFrameNum((int) data[27]);

        return gatewayInfo;
    }

}
