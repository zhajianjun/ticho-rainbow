package top.ticho.rainbow.domain.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ticho.boot.mail.component.MailContent;
import top.ticho.boot.view.util.TiAssert;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.interfaces.dto.MailContentDTO;

/**
 * 邮件任务
 *
 * @author zhajianjun
 * @date 2024-05-17 16:33
 */
@Component
public class EmailTask extends AbstracTask<MailContentDTO> {

    @Autowired
    private EmailRepository emailRepository;

    @Override
    public void run(JobExecutionContext context) {
        MailContentDTO taskParam = getTaskParam(context);
        ValidUtil.valid(taskParam);
        MailContent mailContent = new MailContent();
        mailContent.setTo(taskParam.getTo());
        mailContent.setSubject(taskParam.getSubject());
        mailContent.setContent(taskParam.getContent());
        mailContent.setCc(taskParam.getCc());
        boolean sendMail = emailRepository.sendMail(mailContent);
        TiAssert.isTrue(sendMail, "发送邮件失败");
    }

    @Override
    public Class<MailContentDTO> getParamClass() {
        return MailContentDTO.class;
    }
}
