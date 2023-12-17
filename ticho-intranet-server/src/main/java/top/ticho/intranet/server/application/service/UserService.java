package top.ticho.intranet.server.application.service;

import com.ticho.boot.view.core.PageResult;
import top.ticho.intranet.server.interfaces.dto.UserDTO;
import top.ticho.intranet.server.interfaces.dto.UserPassworUpdDTO;
import top.ticho.intranet.server.interfaces.query.UserQuery;

/**
 * 用户信息 服务接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface UserService {
    /**
     * 保存用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void save(UserDTO userDTO);

    /**
     * 删除用户信息
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改用户信息
     *
     * @param userDTO 用户信息DTO 对象
     */
    void updateById(UserDTO userDTO);

    /**
     * 修改用户密码信息
     *
     * @param userPassworUpdDTO 用户信息DTO 对象
     */
    void updatePassword(UserPassworUpdDTO userPassworUpdDTO);

    /**
     * 根据id查询用户信息
     *
     * @param id 主键
     * @return {@link UserDTO}
     */
    UserDTO getById(Long id);

    /**
     * 分页查询用户信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link UserDTO}>
     */
    PageResult<UserDTO> page(UserQuery query);

}

