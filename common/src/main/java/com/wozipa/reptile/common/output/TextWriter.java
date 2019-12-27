package com.wozipa.reptile.common.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextWriter implements Writer {

    private File file = null;

    public TextWriter(String filePathStr) throws IOException {
        if(filePathStr == null || filePathStr.equals("")) {
            throw new NullPointerException("Argument filePathStr is empty.");
        }

        this.file = new File(filePathStr);

        // 进行初始化
        File parent = this.file.getParentFile();
        if(!parent.exists()) parent.mkdirs();
        if(!file.exists()) file.createNewFile();
    }

    public boolean overwrite(String content) {
        return writeContent(content, true);
    }

    public boolean append(String content) {
        return writeContent(content, true);
    }

    private boolean writeContent(String content, boolean append){
        boolean success = false;
        BufferedWriter writer = null;

        try{
            writer = new BufferedWriter(new FileWriter(this.file, append));
            if(writer != null ){
                writer.write(content);
                writer.write("\r\n");
                writer.flush();
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if(writer != null){
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }
    }

}
