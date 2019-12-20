package com.llipter.sigdict.test;

import org.springframework.util.StringUtils;

public class StorageTest {

    public static void main(String[] args) {
        String path = "/../../b/a/../a/./a/b/c/../ax./../../../../../..";
        System.out.println(StringUtils.cleanPath(path));
    }
}
