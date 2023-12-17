package top.ticho.intranet.server.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ticho.boot.view.core.PageResult;
import com.ticho.boot.view.enums.BizErrCode;
import com.ticho.boot.view.util.Assert;
import com.ticho.boot.web.util.valid.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.application.service.UserService;
import top.ticho.intranet.server.domain.repository.UserRepository;
import top.ticho.intranet.server.infrastructure.entity.User;
import top.ticho.intranet.server.interfaces.assembler.UserAssembler;
import top.ticho.intranet.server.interfaces.dto.UserDTO;
import top.ticho.intranet.server.interfaces.dto.UserPassworUpdDTO;
import top.ticho.intranet.server.interfaces.query.UserQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息 服务实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(UserDTO userDTO) {
        User user = UserAssembler.INSTANCE.dtoToEntity(userDTO);
        Assert.isTrue(userRepository.save(user), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Assert.isTrue(userRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(UserDTO userDTO) {
        User user = UserAssembler.INSTANCE.dtoToEntity(userDTO);
        Assert.isTrue(userRepository.updateById(user), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public void updatePassword(UserPassworUpdDTO userPassworUpdDTO) {
        ValidUtil.valid(userPassworUpdDTO);
        String username = userPassworUpdDTO.getUsername();
        String passwordAes = userPassworUpdDTO.getOldPassword();
        User user = userRepository.getByUsername(username);
        Assert.isNotNull(user, BizErrCode.FAIL, "用户不存在");
        String credentials = user.getPassword();
        boolean matches = passwordEncoder.matches(passwordAes, credentials);
        Assert.isTrue(matches, BizErrCode.FAIL, "密码不正确");
        User upd = new User();
        upd.setId(user.getId());
        upd.setPassword(passwordEncoder.encode(userPassworUpdDTO.getPassword()));
        userRepository.updateById(upd);
    }

    @Override
    public UserDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        User user = userRepository.getById(id);
        return UserAssembler.INSTANCE.entityToDto(user);
    }

    @Override
    public PageResult<UserDTO> page(UserQuery query) {
        // @formatter:off
        query.checkPage();
        Page<User> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        userRepository.list(query);
        List<UserDTO> userDTOs = page.getResult()
            .stream()
            .map(UserAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), userDTOs);
        // @formatter:on
    }
}
