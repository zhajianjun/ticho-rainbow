package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Repository;
import top.ticho.boot.mail.component.MailContent;
import top.ticho.boot.mail.component.MailTemplate;
import top.ticho.rainbow.domain.repository.EmailRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 邮件 repository实现
 *
 * @author zhajianjun
 * @date 2024-02-06 14:32
 */
@Repository
public class EmailRepositoryImpl implements EmailRepository {

    @Resource
    private MailTemplate mailTemplate;

    @Override
    public void sendMail(MailContent mailContent) {
        if (Objects.isNull(mailContent)) {
            return;
        }
        mailTemplate.sendMail(mailContent);
    }

    @Override
    public void sendMailBatch(List<MailContent> mailContents) {
        if (CollUtil.isEmpty(mailContents)) {
            return;
        }
        mailTemplate.sendMailBatch(mailContents);
    }

}
