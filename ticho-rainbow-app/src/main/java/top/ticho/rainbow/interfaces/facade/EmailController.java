package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.starter.mail.component.TiMailContent;
import top.ticho.starter.view.core.TiResult;

/**
 * 邮件
 *
 * @author zhajianjun
 * @date 2024-02-06 11:28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("email")
public class EmailController {

    private final EmailRepository emailRepository;

    /**
     * 邮件发送测试
     *
     * @param mailContent 邮件内容
     */
    @ApiLog("邮件发送测试")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_EMAIL_SEND_TEST + "')")
    @PostMapping("test/send")
    public TiResult<Void> sendTest(TiMailContent mailContent) {
        emailRepository.sendMail(mailContent);
        return TiResult.ok();
    }

}
