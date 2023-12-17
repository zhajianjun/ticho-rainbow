package top.ticho.intranet.core.server.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.constant.NginxConst;
import top.ticho.intranet.core.enums.ProtocolType;
import top.ticho.intranet.core.enums.SysType;
import top.ticho.intranet.core.prop.NginxProperty;
import top.ticho.intranet.core.server.entity.PortInfo;
import top.ticho.intranet.core.server.entity.TemplateInfo;
import top.ticho.intranet.core.util.BeetlUtil;
import top.ticho.intranet.core.util.TichoUtil;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
@AllArgsConstructor
public class NginxHandler {

    private final NginxProperty nginxProperty;

    /**
     * 初始化 https 证书配置
     */
    private void initHttpsCerts() {
        String domainName = Optional.ofNullable(nginxProperty.getDomain()).filter(StrUtil::isNotBlank).orElse(CommConst.LOCALHOST);
        // 如果不开启https,则不拷贝证书
        if (!Boolean.TRUE.equals(nginxProperty.getSslEnable())) {
            return;
        }
        String format1 = "{}" + File.separatorChar + domainName + StrUtil.C_DOT + "{}";
        String format2 = "{}" + File.separatorChar + NginxConst.NGINX_CONF_D_FILE_NAME + File.separatorChar + domainName + StrUtil.DOT + "{}";
        // key 证书文件拷贝
        File keyFile = new File(StrUtil.format(format2, nginxProperty.getConf(), NginxConst.KEY));
        if (!keyFile.exists()) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(StrUtil.format(format1, NginxConst.CONF, NginxConst.KEY));
            if (Objects.nonNull(is)) {
                FileUtil.writeFromStream(is, keyFile);
            }
        }
        // pem 证书文件拷贝
        File pemFile = new File(StrUtil.format(format2, nginxProperty.getConf(), NginxConst.PEM));
        if (!pemFile.exists()) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(StrUtil.format(format1, NginxConst.CONF, NginxConst.PEM));
            if (Objects.nonNull(is)) {
                FileUtil.writeFromStream(is, pemFile);
            }
        }
    }

    private void initNginxConfig(String port) {
        // @formatter:off
        String domainName = nginxProperty.getDomain();
        String nginxConf = nginxProperty.getConf();
        boolean isHttps = Boolean.TRUE.equals(nginxProperty.getSslEnable());
        String type = CommConst.OS_TYPE.contains("win") ? SysType.WIN.code() : SysType.LINUX.code();
        // 1.nginx 默认配置文件初始化
        String nginxHttpFilePath = nginxConf + File.separatorChar + NginxConst.NGINX_CONF_FILE_NAME;
        writeFile(nginxHttpFilePath, NginxConst.DEFAULT_NGINX_CONFIG);
        // 2.conf.d文件夹初始化
        // nginx 默认配置文件夹处理
        String nginxServerFolderFilePath = nginxConf + File.separatorChar + NginxConst.NGINX_CONF_D_FILE_NAME;
        File nginxServerFolderFile = new File(nginxServerFolderFilePath);
        if (!nginxServerFolderFile.exists()) {
            FileUtil.mkdir(nginxServerFolderFile);
        }
        // 3.nginx server配置初始化
        String nginxServerFilePath = nginxServerFolderFilePath + File.separatorChar + NginxConst.DEFAULT_NGINX_CONFIG_FILE_NAME;
        String templateContent = NginxConst.MAIN_NGINX_HTTP_CONFIG_TEMPLATE;;
        Map<String, String> nginxServerTemplateMap = new HashMap<>(16);
        nginxServerTemplateMap.put("proxyPass", StrUtil.format("http://localhost:{}/", port));
        if (isHttps) {
            // 查询所有文件名
            List<String> strings = listAllFile();
            String keyFileName = domainName + StrUtil.DOT + NginxConst.KEY;
            String pemFileName = domainName + StrUtil.DOT + NginxConst.PEM;
            // 匹配是否所有证书是否存在
            boolean hasAllCerts = strings.contains(keyFileName);
            hasAllCerts = hasAllCerts && strings.contains(pemFileName);
            // 当少一个证书时，则使用nginx http 配置，否则使用https
            templateContent = hasAllCerts ? NginxConst.MAIN_NGINX_HTTPS_CONFIG_TEMPLATE : NginxConst.MAIN_NGINX_HTTP_CONFIG_TEMPLATE;
            String pathPrefix = nginxServerFolderFilePath + File.separatorChar;
            String keyFilePath = pathPrefix + keyFileName;
            String pemFilePath = pathPrefix + pemFileName;
            nginxServerTemplateMap.put("sslCertificateKey", TichoUtil.convertPath(keyFilePath));
            nginxServerTemplateMap.put("sslCertificate", TichoUtil.convertPath(pemFilePath));
            nginxServerTemplateMap.put("serverName", domainName);
            nginxServerTemplateMap.put("defaultServer", getDefaultServer(domainName));
        }
        templateToFile(nginxServerFilePath, templateContent, null, nginxServerTemplateMap);
        // 4.nginx重启
        TichoUtil.exec(nginxReload());
        // @formatter:on
    }

    public List<String> listAllFile() {
        // @formatter:off
        String filePathPrefix = getNginxServerFoldFilePath();
        File file = new File(filePathPrefix);
        if (!file.isDirectory() || !file.exists()) {
            return Collections.emptyList();
        }
        String[] list = file.list();
        if (ArrayUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return Arrays
            .stream(list)
            .sorted()
            .collect(Collectors.toList());
        // @formatter:on
    }

    /**
     * 获取nginx服务配置文件夹路径
     */
    public String getNginxServerFoldFilePath() {
        return nginxProperty.getConf() + File.separatorChar + NginxConst.NGINX_CONF_D_FILE_NAME + File.separatorChar;
    }

    /**
     * 模板生成文件
     *
     * @param path               文件全路径
     * @param templateContent    模板内容
     * @param templateParamsJson 模板参数
     * @param templateParamsMap  模板参数Map
     */
    private void templateToFile(String path, String templateContent, String templateParamsJson, Map<String, String> templateParamsMap) {
        Map<String, Object> jsonConstantMap = JSONUtil.parseObj(templateParamsJson);
        if (CollUtil.isNotEmpty(templateParamsMap)) {
            jsonConstantMap.putAll(templateParamsMap);
        }
        String reader = BeetlUtil.render(templateContent, jsonConstantMap);
        writeFile(path, reader);
    }

    private void writeFile(String path, String reader) {
        File file = new File(path);
        try {
            FileUtil.writeString(reader, file, Charset.defaultCharset());
        } catch (IORuntimeException e) {
            log.error("执行命令失败:{}", e.getMessage(), e);
        }
    }

    public String getDefaultServer(String domainName) {
        if (domainName != null && domainName.startsWith("www")) {
            return " default_server";
        }
        return StrUtil.EMPTY;
    }

    public String nginxReload() {
        return nginxProperty.getNginxBin() + String.format(NginxConst.NGINX_RELOAD, nginxProperty.getHome());
    }

    public void deleteProxy(PortInfo portInfo) {
        if (portInfo == null) {
            return;
        }
        if (!NginxConst.HTTPS.equalsIgnoreCase(ProtocolType.getByCode(portInfo.getType()).type())) {
            return;
        }
        // conf配置
        String filePath = getNginxServerFoldFilePath() + portInfo.getDomain() + ".conf";
        try {
            FileUtil.del(new File(filePath));
        } catch (Exception ignore) {
        }
    }

    /**
     * 添加http或者https代理
     *
     * @param portInfo 端口对象
     */
    public void addProxy(PortInfo portInfo, TemplateInfo templateInfo) {
        // @formatter:off
        if (portInfo == null) {
            return;
        }
        // 判断类型
        if (NginxConst.HTTPS.equalsIgnoreCase(ProtocolType.getByCode(portInfo.getType()).type())) {
            return;
        }
        // 如果是https
        try {
            String domain = nginxProperty.getDomain();
            String filePathPrefix = getNginxServerFoldFilePath() + domain + StrUtil.DOT;
            // conf配置
            String filePath =  filePathPrefix+ "conf";
            // 证书key 绝对路径
            String keyPath = filePathPrefix + NginxConst.KEY;
            // 证书pem 绝对路径
            String pemPath = filePathPrefix + NginxConst.PEM;
            Map<String, String> nginxServerTemplateMap = new HashMap<>(16);
            nginxServerTemplateMap.put("sslCertificateKey", TichoUtil.convertPath(keyPath));
            nginxServerTemplateMap.put("sslCertificate", TichoUtil.convertPath(pemPath));
            nginxServerTemplateMap.put("serverName", domain);
            nginxServerTemplateMap.put("proxyPass", StrUtil.format("http://localhost:{}/", portInfo.getPort()));
            nginxServerTemplateMap.put("defaultServer", getDefaultServer(domain));
            templateToFile(filePath, templateInfo.getContent(), templateInfo.getJsonConstant(), nginxServerTemplateMap);
        } catch (Exception e) {
            log.error("添加代理失败:{}", e.getMessage(), e);
        }
        // @formatter:on
    }

}
