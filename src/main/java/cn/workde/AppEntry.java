package cn.workde;

import cn.workde.core.launch.AppLaunch;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppEntry {

    public static void main(String[] args) {
        AppLaunch.run("example-service", AppEntry.class, args);
    }
}
