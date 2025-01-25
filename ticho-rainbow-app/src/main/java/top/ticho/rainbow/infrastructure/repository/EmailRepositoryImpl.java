package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.starter.mail.component.TiMailContent;
import top.ticho.starter.mail.component.TiMailTemplate;

import java.util.List;
import java.util.Objects;

/**
 * 邮件 repository实现
 *
 * @author zhajianjun
 * @date 2024-02-06 14:32
 */
@Slf4j
@Repository
public class EmailRepositoryImpl implements EmailRepository {

    @Override
    public boolean sendMail(TiMailContent mailContent) {
        if (Objects.isNull(mailContent)) {
            return false;
        }
        return sendMailExecute(mailContent);
    }

    @Override
    public boolean sendMailBatch(List<TiMailContent> mailContents) {
        if (CollUtil.isEmpty(mailContents)) {
            return false;
        }
        return sendMailBatchExecute(mailContents);
    }

    private TiMailTemplate getMailTemplate() {
        return SpringUtil.getBean(TiMailTemplate.class);
    }

    public boolean sendMailExecute(TiMailContent mailContent) {
        TiMailTemplate mailTemplate = getMailTemplate();
        if (Objects.isNull(mailTemplate)) {
            return false;
        }
        mailTemplate.sendMail(mailContent);
        return true;
    }

    public boolean sendMailBatchExecute(List<TiMailContent> mailContents) {
        TiMailTemplate mailTemplate = getMailTemplate();
        if (Objects.isNull(mailTemplate)) {
            return false;
        }
        mailTemplate.sendMailBatch(mailContents);
        return true;
    }

}
