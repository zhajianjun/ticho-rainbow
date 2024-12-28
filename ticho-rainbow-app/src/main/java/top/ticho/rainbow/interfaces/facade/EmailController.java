package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.mail.component.MailContent;
import top.ticho.boot.view.core.TiResult;
import top.ticho.rainbow.domain.repository.EmailRepository;

import javax.annotation.Resource;

/**
 * 邮件
 *
 * @author zhajianjun
 * @date 2024-02-06 11:28
 */
@RestController
@RequestMapping("email")
@Api(tags = "邮件")
@ApiSort(120)
public class EmailController {

    @Resource
    private EmailRepository emailRepository;

    @PreAuthorize("@perm.hasPerms('system:email:sendTest')")
    @ApiOperation(value = "邮件发送测试")
    @ApiOperationSupport(order = 10)
    @PostMapping("sendTest")
    public TiResult<Void> sendTest(MailContent mailContent) {
        emailRepository.sendMail(mailContent);
        return TiResult.ok();
    }

}
