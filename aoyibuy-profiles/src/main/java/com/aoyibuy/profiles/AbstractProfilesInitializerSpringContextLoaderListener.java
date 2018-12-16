package com.aoyibuy.profiles;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.ContextLoaderListener;

import com.google.common.collect.ImmutableMap;

/**
 * <p>扩展Spring的ContextLoaderListener，在加载Spring容器之前动态设置profiles值，此值由配置文件profiles.properties指定。
 * 配置文件中的占位符<code>${profiles.activation}</code>会被maven profile插件的默认值(activeByDefault)动态替换.<br>
 * <b>注意</b> 如果修改了pom.xml中activeByDefault，需要重新执行Maven - Update Project以生效
 * 
 * <p>由于加入了maven profile插件，maven打包时需要指定profile值，例如 <code>mvn package -P {env}</code><br>
 * env的默认值由activeByDefault指定, 可选值为development、test、production。如指定其它值，则会忽略，继续使用默认值。
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class AbstractProfilesInitializerSpringContextLoaderListener extends ContextLoaderListener {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        initializeActiveProfiles();
        super.contextInitialized(event);
    }

    protected void initializeActiveProfiles() {
        try {
            Properties profiles = PropertiesLoaderUtils.loadProperties(new ClassPathResource("profiles.properties"));
            String activeProfile = profiles.getProperty("profiles.activation");
            handleActiveProfile(activeProfile);
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
            printSystemEnvProperties();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load 'profiles.properties':" + e.getMessage());
        }
    }

    protected abstract void handleActiveProfile(String activeProfile);

    private void printSystemEnvProperties() {
        ImmutableMap.builder()
            .put("操作系统", SystemUtils.OS_NAME)
            .put("操作系统版本", SystemUtils.OS_VERSION)
            .put("操作系统架构", SystemUtils.OS_ARCH)
            .put("文件编码", SystemUtils.FILE_ENCODING)
            .put("当前用户", SystemUtils.USER_NAME)
            .put("当前用户目录", SystemUtils.USER_HOME)
            .put("当前用户时区", SystemUtils.USER_TIMEZONE)
            .put("Java", SystemUtils.JAVA_RUNTIME_NAME)
            .put("Java供应商", SystemUtils.JAVA_VENDOR)
            .put("Java版本", SystemUtils.JAVA_RUNTIME_VERSION)
            .put("Java虚拟机", SystemUtils.JAVA_VM_NAME)
            .put("Java虚拟机供应商", SystemUtils.JAVA_VM_VENDOR)
            .put("Java虚拟机版本", SystemUtils.JAVA_VM_VERSION)
        .build()
        .forEach((k, v) -> logger.info("{} - {}", k, v));
    }

}