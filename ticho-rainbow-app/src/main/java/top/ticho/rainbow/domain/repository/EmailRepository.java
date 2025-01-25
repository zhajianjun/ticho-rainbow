package top.ticho.rainbow.domain.repository;

import top.ticho.starter.mail.component.TiMailContent;

import java.util.List;

/**
 * 邮件 repository接口
 *
 * @author zhajianjun
 * @date 2024-02-06 14:30
 */
public interface EmailRepository {

    /**
     * 邮件发送
     *
     * @param mailContent 邮件内容
     */
    boolean sendMail(TiMailContent mailContent);

    /**
     * 邮件发送(批量)
     *
     * @param mailContent 邮件内容列表
     */
    boolean sendMailBatch(List<TiMailContent> mailContent);

}
