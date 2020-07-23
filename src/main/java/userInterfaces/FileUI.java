package userInterfaces;

import entities.ContentType;
import utils.IOConsoleUtil;

public class FileUI {

    private FileUI() {
        throw new IllegalStateException("User Interface class");
    }

    public static void passPathToUser(String path, ContentType contentType) {
        IOConsoleUtil.printMessage(contentType + " file path: " + path + "\n");
    }
}
