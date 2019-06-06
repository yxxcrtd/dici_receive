package com.igoosd;

import com.igoosd.model.Message;
import com.igoosd.model.reciver.GatewayFlowInfo;
import com.igoosd.service.FlowInfoBusinessService;
import com.igoosd.util.MsgTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoSensorAcceptServerApplicationTests {

    @Autowired
    private FlowInfoBusinessService flowInfoBusinessService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testExec() throws InterruptedException {
        Random random = new Random();
        for (; ; ) {
            int exitNum = random.nextInt(10);
            GatewayFlowInfo flowInfo = new GatewayFlowInfo(UUID.randomUUID().toString().replaceAll("-", ""));
            flowInfo.setGatewayNum(261);
            flowInfo.setExitCarFlow(exitNum);
            flowInfo.setEnterCarFlow(0);
            Message<GatewayFlowInfo> message = new Message<>(UUID.randomUUID().toString().replaceAll("-", ""), MsgTypeEnum.DATA_FLOW_MSG, flowInfo);
            flowInfoBusinessService.exec(message);
            Thread.sleep(2000L);
        }

    }

}
