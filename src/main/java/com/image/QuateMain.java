package com.image;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lyne on 2017/4/4.
 */
public class QuateMain {
    public static void main(String[] args) {

        System.out.println(System.getProperty("line.separator"));

        String test = "\"孕妇舒适款春秋薄款外穿托腹裤春春季2017新款潮妈纯棉孕妇春装\"\t50023573";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(test);
        while (m.find()) {
            System.out.println(m.group(1));
        }
    }
}
