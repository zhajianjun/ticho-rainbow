package top.ticho.rainbow.application.task;

import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.dto.MailContentDTO;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.starter.mail.component.TiMailContent;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.valid.TiValidUtil;
import top.ticho.trace.common.prop.TiTraceProperty;

/**
 * 邮件任务
 *
 * @author zhajianjun
 * @date 2024-05-17 16:33
 */
@Component
public class EmailTask extends AbstracTask<MailContentDTO> {

    private final EmailRepository emailRepository;

    public EmailTask(Environment environment, TiTraceProperty tiTraceProperty, TaskLogRepository taskLogRepository, EmailRepository emailRepository) {
        super(environment, tiTraceProperty, taskLogRepository);
        this.emailRepository = emailRepository;
    }

    @Override
    public void run(JobExecutionContext context) {
        MailContentDTO taskParam = getTaskParam(context);
        TiValidUtil.valid(taskParam);
        TiMailContent mailContent = new TiMailContent();
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
