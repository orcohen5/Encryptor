package main.userInterfaces;

import main.entities.ContentType;
import org.springframework.stereotype.Component;
import main.utils.IOConsoleUtil;

@Component
public class FileUI {

    private FileUI() {

    }

    public static void passPathToUser(String path, ContentType contentType) {
        IOConsoleUtil.printMessage(contentType + " file path: " + path + "\n");
    }
}
