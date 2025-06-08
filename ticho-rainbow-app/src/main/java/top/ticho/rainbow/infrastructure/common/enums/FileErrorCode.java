package top.ticho.rainbow.infrastructure.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import top.ticho.starter.view.enums.TiErrorCode;

import java.io.Serializable;

/**
 * 状态错误码
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Getter
@AllArgsConstructor
public enum FileErrorCode implements Serializable, TiErrorCode {

    UPLOAD_ERROR(10005, "上传文件失败"),
    DOWNLOAD_ERROR(10005, "下载文件失败"),
    FILE_NOT_EXIST(10005, "文件不存在"),
    FILE_SIZE_TO_LARGER(10005, "文件大小过大"),
    FILE_STATUS_ERROR(10005, "文件状态异常"),
    DELETE_OBJECT_ERROR(10006, "删除文件失败"),
    SELECT_OBJECT_URL_ERROR(10008, "查询文件外链失败"),
    ;
    private final int code;
    private final String message;

}
