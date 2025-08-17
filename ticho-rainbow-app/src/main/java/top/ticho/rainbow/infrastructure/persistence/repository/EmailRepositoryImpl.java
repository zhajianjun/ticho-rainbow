package top.ticho.rainbow.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.starter.mail.component.TiMailContent;
import top.ticho.starter.mail.component.TiMailTemplate;
import top.ticho.starter.web.util.TiSpringUtil;
import top.ticho.tool.core.TiCollUtil;

import java.util.List;
import java.util.Objects;

/**
 * 邮件 repository实现
 *
 * @author zhajianjun
 * @date 2024-02-06 14:32
 */
@RequiredArgsConstructor
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
        if (TiCollUtil.isEmpty(mailContents)) {
            return false;
        }
        return sendMailBatchExecute(mailContents);
    }

    private TiMailTemplate getMailTemplate() {
        return TiSpringUtil.getBean(TiMailTemplate.class);
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
