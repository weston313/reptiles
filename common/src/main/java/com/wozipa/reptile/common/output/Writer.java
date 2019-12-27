package com.wozipa.reptile.common.output;

public interface Writer {

    public boolean overwrite(String content);

    public boolean append(String file);

}
