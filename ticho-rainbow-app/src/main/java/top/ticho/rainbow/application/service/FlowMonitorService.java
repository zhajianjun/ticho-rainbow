package top.ticho.rainbow.application.service;

import top.ticho.rainbow.interfaces.dto.FlowMonitorStatsDTO;

/**
 * 流量监控 服务接口
 *
 * @author zhajianjun
 * @date 2024-05-16 09:42
 */
public interface FlowMonitorService {


    /**
     * 获取监控数据
     */
    FlowMonitorStatsDTO info();


}
