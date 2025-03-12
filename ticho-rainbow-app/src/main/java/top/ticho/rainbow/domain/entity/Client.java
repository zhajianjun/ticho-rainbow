package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.vo.ClientModifyVO;

import java.time.LocalDateTime;

/**
 * 客户端信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Getter
@Builder
public class Client {

    /** 主键标识 */
    private Long id;    /** 客户端秘钥 */
    private String accessKey;    /** 客户端名称 */
    private String name;    /** 过期时间 */
    private LocalDateTime expireAt;    /** 状态;1-启用,0-停用 */
    private Integer status;    /** 排序 */
    private Integer sort;    /** 备注信息 */
    private String remark;    /** 版本号 */
    private Long version;    /** 创建人 */
    private String createBy;    /** 创建时间 */
    private LocalDateTime createTime;    /** 修改人 */
    private String updateBy;    /** 修改时间 */
    private LocalDateTime updateTime;
    public void modify(ClientModifyVO vo) {
        this.name = vo.getName();
        this.expireAt = vo.getExpireAt();
        this.status = vo.getStatus();
        this.sort = vo.getSort();
        this.remark = vo.getRemark();
        this.version = vo.getVersion();
    }

}