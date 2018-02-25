package ru.zetapps.filework;

import java.io.File;

public interface FDAbility {
    String getCloudPath(String path, String userLogin);
    String getAbsPath(String cloudPath);
    boolean delete(String path);
    boolean rename(String oldPath, String newPath);
}
