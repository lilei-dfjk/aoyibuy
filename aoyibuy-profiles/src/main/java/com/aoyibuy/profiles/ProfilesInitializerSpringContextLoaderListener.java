package com.aoyibuy.profiles;

/**
 * 处理activeProfile
 * 
 * @author wh
 * @since 0.0.1
 */
public class ProfilesInitializerSpringContextLoaderListener extends AbstractProfilesInitializerSpringContextLoaderListener {
    
    @Override
    protected void handleActiveProfile(String activeProfile) {
        switch (activeProfile) {
            case "development": logger.info("系统在开发环境下启动.."); break;
            case "test":        logger.info("系统在测试环境下启动.."); break;
            case "production":  logger.info("系统在生产环境下启动.."); break;
            default: throw new IllegalArgumentException("非法的profile - " + activeProfile);
        }
    }

}