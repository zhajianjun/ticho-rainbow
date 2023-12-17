package top.ticho.intranet.core.prop;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.util.TichoUtil;

import java.io.File;

/**
 * nginx配置
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class NginxProperty {

    /** 域名地址 */
    private String domain;
    /** 是否开启ssl */
    private Boolean sslEnable;
    /** nginx启动命令路径 */
    private String bin;
    /** nginx配置路径 */
    private String conf;
    /** nginx路径 */
    private String home;

    public String getConf() {
        conf = TichoUtil.convertPath(conf);
        return conf;
    }

    public String getNginxBin() {
        String binDir = bin;
        StringBuilder bin = new StringBuilder();
        if (CommConst.OS_TYPE.contains("win")) {
            if (StrUtil.isBlank(bin)) {
                binDir = home;
            }
            bin.append(binDir).append(File.separatorChar).append("nginx.exe");
            return bin.toString();
        }
        bin.append(binDir).append(File.separatorChar).append("nginx");
        return bin.toString();
    }

    public String getHome() {
        home = TichoUtil.convertPath(home);
        return home;
    }

}
