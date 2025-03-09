package top.ticho.rainbow.application.service;

import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.dto.PasswordDTO;
import top.ticho.rainbow.application.dto.UserDTO;
import top.ticho.rainbow.application.dto.request.UserLoginDTO;
import top.ticho.rainbow.application.dto.UserPasswordDTO;
import top.ticho.rainbow.application.dto.UserRoleDTO;
import top.ticho.rainbow.application.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.command.ResetPasswordCommand;
import top.ticho.rainbow.application.dto.command.ResetPassworEmailSendCommand;
import top.ticho.rainbow.application.dto.command.SignUpEmailSendCommand;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.starter.view.core.TiPageResult;

import java.io.IOException;
import java.util.List;

/**
 * 用户信息 服务接口
 *
 * @date 2023-12-17 20:12
 */
public interface UserService {

    /**
     * 验证码
     */
    void imgCode(String imgKey) throws IOException;

    /**
     * 图片验证码校验
     */
    void imgCodeValid(String key, String imgCode);

    /**
     * 注册验证码邮箱发送
     *
     * @param signUpEmailSendCommand 邮箱验证码发送时图片验证码DTO
     */
    void signUpEmailSend(SignUpEmailSendCommand signUpEmailSendCommand);

    /**
     * 注册
     *
     * @param userSignUpOrResetDTO 注册dto
     */
    UserLoginDTO signUp(ResetPasswordCommand userSignUpOrResetDTO);

    /**
     * 重置邮箱验证码发送
     *
     * @param resetPassworEmailSendCommand 邮箱验证码发送时图片验证码DTO
     */
    String resetPasswordEmailSend(ResetPassworEmailSendCommand resetPassworEmailSendCommand);

    /**
     * 重置用户密码(用户使用)
     *
     * @param resetPasswordCommand 重置密码DTO
     */
    UserLoginDTO resetPassword(ResetPasswordCommand resetPasswordCommand);

    /**
     * 重置用户密码(管理员使用)
     */
    void resetPassword(String username);

    /**
     * 保存用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void save(UserDTO userDTO);

    /**
     * 修改用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void modify(UserDTO userDTO);

    /**
     * 修改登录用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void updateForSelf(UserDTO userDTO);

    /**
     * 用户头像上传
     *
     * @param file 文件信息
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 修改用户密码信息
     *
     * @param userPassworUpdDTO 用户信息DTO 对象
     */
    void updatePassword(UserPasswordDTO userPassworUpdDTO);

    /**
     * 修改登录人密码
     *
     * @param passwordDTO 密码信息
     */
    void updatePasswordForSelf(PasswordDTO passwordDTO);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return {@link UserDTO}
     */
    UserDTO getInfoByUsername(String username);

    /**
     * 查询登录人用户信息
     *
     * @return {@link UserDTO}
     */
    UserDTO getInfo();

    /**
     * 根据用户名查询用户角色菜单功能号信息
     *
     * @param username 用户名
     * @return {@link UserRoleMenuDtlDTO}
     */
    UserRoleMenuDtlDTO getUserDtl(String username);

    /**
     * 查询登录人用户角色菜单功能号信息
     *
     * @return {@link UserRoleMenuDtlDTO}
     */
    UserRoleMenuDtlDTO getUserDtl();

    /**
     * 分页查询用户信息列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link UserDTO}>
     */
    TiPageResult<UserDTO> page(UserQuery query);

    /**
     * 绑定角色
     *
     * @param userRoleDTO 用户角色dto
     */
    void bindRole(UserRoleDTO userRoleDTO);

    /**
     * 锁定用户
     */
    void lock(List<String> usernames);

    /**
     * 解锁用户
     */
    void unLock(List<String> usernames);

    /**
     * 注销用户
     */
    void logOut(List<String> usernames);

    /**
     * 删除用户
     */
    void remove(List<String> username);

    /**
     * 导入模板下载
     */
    void impTemplate() throws IOException;


    /**
     * 导入用户信息
     *
     * @param file 文件信息
     */
    void impExcel(MultipartFile file) throws IOException;

    /**
     * 导出用户信息
     *
     * @param query 查询条件
     */
    void expExcel(UserQuery query) throws IOException;


}

