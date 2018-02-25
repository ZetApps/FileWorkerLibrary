package ru.zetapps.filework;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWorkMain {
    private String rootPath;
    public Directories directories;
    public Files files;
    public FileWorkMain(String root) {
        this.rootPath = root;
        directories = new Directories(this.rootPath);
        files = new Files(this.rootPath);
    }

    public String getDataList(String basePath, String login){
        System.out.println("СИНХРОНИЗАЦИЯ: " +rootPath + "/" + basePath + "||" + login);
        String res = "";
        File dir = new File(rootPath + "/" + basePath);
        for (int j = 0;j<2;j++) {
            if (j == 0) {
                res = res + "dir:";
                System.out.println("dirCount " + dir.listFiles().length);
                for (int i = 0; i < dir.listFiles().length; i++) {
                    if (java.nio.file.Files.exists(Paths.get(dir.listFiles()[i].getPath()))) {
                        System.out.println("EXISTS");
                        if (java.nio.file.Files.isDirectory(Paths.get(dir.listFiles()[i].getPath()))) {
                            System.out.println(dir.listFiles()[i].getPath());
                            res += directories.getCloudPath(dir.listFiles()[i].getPath(), login) + ";";
                        }
                    }
                }
            }else{
                res += "$$$files:";
                File file = null;
                System.out.println("dirFilesCount " + dir.listFiles().length);
                for (int i = 0; i < dir.listFiles().length; i++) {
                    if (!java.nio.file.Files.isDirectory(Paths.get(dir.listFiles()[i].getPath()))) {
                        System.out.println(Paths.get(dir.listFiles()[i].getPath()));
                        file = dir.listFiles()[i];
                        res += files.getCloudPath(dir.listFiles()[i].getPath(),login);
                        res += "###" + file.lastModified() + ";";
                    }
                }
            }
        }
        System.out.println("СИНХРОНИЗАЦИЯ_РЕЗУЛЬТАТ:" + res);
        return res;
    }
}

