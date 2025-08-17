package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.http.HttpStatus;
import top.ticho.rainbow.application.dto.excel.OpLogExcelExport;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.starter.view.log.TiHttpLog;
import top.ticho.tool.core.TiIdUtil;
import top.ticho.tool.core.TiLocalDateTimeUtil;
import top.ticho.tool.core.TiStrUtil;
import top.ticho.tool.json.util.TiJsonUtil;
import top.ticho.trace.common.TiTraceConst;

import java.util.Objects;
import java.util.Optional;

/**
 * 日志信息 转换
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Mapper(componentModel = "spring", imports = {TiLocalDateTimeUtil.class, Objects.class, HttpStatus.class, TiIdUtil.class,
    TiJsonUtil.class, TiTraceConst.class, Optional.class, TiStrUtil.class})
public interface OpLogAssembler {

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "startTime", expression = "java(TiLocalDateTimeUtil.of(httpLog.getStart()))", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "endTime", expression = "java(TiLocalDateTimeUtil.of(httpLog.getEnd()))", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "isErr", expression = "java(Objects.equals(httpLog.getStatus(), HttpStatus.OK.value()) ? 0 : 1)")
    @Mapping(target = "resStatus", expression = "java(httpLog.getStatus())")
    @Mapping(target = "operateBy", expression = "java(httpLog.getUsername())")
    @Mapping(target = "id", expression = "java(TiIdUtil.snowId())")
    @Mapping(target = "mdc", expression = "java(TiJsonUtil.toJsonString(httpLog.getMdcMap()))")
    @Mapping(target = "traceId", expression = "java(httpLog.getMdcMap().get(TiTraceConst.TRACE_ID_KEY))")
    @Mapping(target = "ip", expression = "java(Optional.ofNullable(TiJsonUtil.toMap(httpLog.getReqHeaders(), String.class, String.class).get(\"Real-Ip\")).orElse(httpLog.getIp()))")
    @Mapping(target = "resBody", expression = "java(TiStrUtil.isNotBlank(httpLog.getResBody()) ? httpLog.getResBody() : null)")
    OpLog toEntity(TiHttpLog httpLog);

    OpLogDTO toDTO(OpLog entity);

    OpLogExcelExport toExcelExport(OpLogDTO opLogDTO);

}