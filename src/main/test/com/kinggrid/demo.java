package com.kinggrid;

import java.io.File;

/**
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 15:09
 */
public class demo {
    public static void main(String[] args) {
        File file = new File("F:\\ideaproject\\trunk\\BatchScanSeal/src/main/webapp/pdf//stamp/\\b1541a45a36641d8bb43063bf52e87d9test1.pdf");
        System.out.println(file.isFile());
    }
}
