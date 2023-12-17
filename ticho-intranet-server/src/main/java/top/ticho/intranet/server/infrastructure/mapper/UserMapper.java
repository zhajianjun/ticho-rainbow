package top.ticho.intranet.server.infrastructure.mapper;

import com.ticho.boot.datasource.mapper.RootMapper;
import org.springframework.stereotype.Repository;
import top.ticho.intranet.server.infrastructure.entity.User;


/**
 * 用户信息 mapper
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Repository
public interface UserMapper extends RootMapper<User> {

}