package ru.zetapps.filework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;

public class Files implements FDAbility{
    private String rootpath;

    public Files(String rootpath) {
        this.rootpath = rootpath;

    }


    public boolean saveFile(byte[] buffer, String path, String filename){


        String savepath = rootpath + "\\" + path;
        String savepathfile = rootpath + "\\" + path + "\\" + filename;
        System.out.println(savepathfile + " - путь сохранения файла");
        try {
            if (!java.nio.file.Files.exists(Paths.get(savepath))){
                java.nio.file.Files.createDirectory(Paths.get(savepath));
            };
            java.nio.file.Files.write(Paths.get(savepathfile),buffer, StandardOpenOption.CREATE_NEW);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean saveFile(File file, String path, String filename){
        byte[] buffer = makeBuffer(file);
        String savepath = rootpath + "\\" + path;
        String savepathfile = rootpath + "\\" + path + "\\" + filename;
        System.out.println(savepathfile + " - путь сохранения файла");
        try {
            if (!java.nio.file.Files.exists(Paths.get(savepath))){
                java.nio.file.Files.createDirectory(Paths.get(savepath));
            };
            java.nio.file.Files.write(Paths.get(savepathfile),buffer, StandardOpenOption.CREATE_NEW);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private byte[] makeBuffer(File file){
        try {
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getCloudPath(String path, String userLogin){
        return path.substring(path.lastIndexOf(userLogin));
    }

    @Override
    public boolean delete(String pathForDelete){
        Path dir = Paths.get(rootpath + "\\" + pathForDelete);
        try {
            java.nio.file.Files.deleteIfExists(dir);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getAbsPath(String cloudPath) {
        return rootpath + "/" + cloudPath;
    }

    @Override
    public boolean rename(String oldPath, String newPath) {
        File oldfile = new File(rootpath+"/"+oldPath.replace("%20"," "));
        File newfile = new File(rootpath+"/"+newPath.replace("%20"," "));
        System.out.println("NEW - " + rootpath+"/"+newPath.replace("%20"," "));
        System.out.println("OLD - " + rootpath+"/"+oldPath.replace("%20"," "));
        if (oldfile.exists()){
            oldfile.renameTo(newfile);
            return true;
        }else{
            return false;
        }
    }
}
