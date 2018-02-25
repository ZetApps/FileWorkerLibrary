package ru.zetapps.filework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Directories implements FDAbility {
    private String rootPath;
    public Directories(String rootPath) {
        this.rootPath = rootPath;
        if (!Files.exists(Paths.get(rootPath))){
            try {
                Files.createDirectory(Paths.get(rootPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String makeUserDir(){
        Path dir = Paths.get(rootPath);

        System.out.println(dir.toString());
        try {
            Path newdir =Files.createDirectory(dir);
            return newdir.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String makeUserDir(String login){
        Path dir = Paths.get(rootPath+"\\"+login);

        System.out.println(dir.toString());
        try {
            Path newdir =Files.createDirectory(dir);
            return newdir.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean delete(String pathForDelete){
        Path dir = Paths.get(rootPath + "\\" + pathForDelete);
        try {
            Files.deleteIfExists(dir);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean makeDir(String folderPath){
        Path dir = Paths.get(rootPath + "\\" + folderPath);

        System.out.println(dir.toString());
        try {
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getCloudPath(String path, String userLogin){
        if (path.lastIndexOf(userLogin)==-1){
            return "";
        }
        return path.substring(path.lastIndexOf(userLogin));
    }

    @Override
    public String getAbsPath(String cloudPath) {
        return rootPath + "/" + cloudPath;
    }

    @Override
    public boolean rename(String oldPath, String newPath) {
        File oldfile = new File(rootPath+"/"+oldPath.replace("%20"," "));
        File newfile = new File(rootPath+"/"+newPath.replace("%20"," "));
        System.out.println("NEW - " + rootPath+"/"+newPath.replace("%20"," "));
        System.out.println("OLD - " + rootPath+"/"+oldPath.replace("%20"," "));
        if (oldfile.exists()){
            oldfile.renameTo(newfile);
            return true;
        }else{
            return false;
        }
    }
}
