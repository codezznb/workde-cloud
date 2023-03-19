package cn.workde.core.launch;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 项目启动器
 */
public class AppLaunch {

    public static ConfigurableApplicationContext run(String appName, Class source, String ...args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, args);
        return builder.run(args);
    }

    public static SpringApplicationBuilder createSpringApplicationBuilder(String appName, Class source, String... args) {
        Assert.hasText(appName, "[appName]服务名不能为空");

        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));
        propertySources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, environment.getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, environment.getSystemEnvironment()));

        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);

        String startJarPath = AppLaunch.class.getResource("/").getPath().split("!")[0];
        System.out.printf("----启动中，读取到的环境变量:[%s]，jar地址:[%s]----%n", "dev", startJarPath);
        Properties props = System.getProperties();
        props.setProperty("server.port", "6789");
        props.setProperty("spring.application.name", appName);
        props.setProperty("spring.profiles.active", "dev");
        props.setProperty("app.name", appName);
        props.setProperty("file.encoding", StandardCharsets.UTF_8.name());
        props.setProperty("loadbalancer.client.name", appName);
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("spring.main.allow-bean-definition-overriding", "true");
        defaultProperties.setProperty("spring.sleuth.sampler.percentage", "1.0");
        builder.properties(defaultProperties);
        return builder;
    }
}
