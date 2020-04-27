package com.vousembrasser.netty.netty;

import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NettyApplicationTests {

    @Test
    public void contextLoads() {
        String ip = "21";
        Integer[] ips = new Integer[4];
        String[] sip = ip.split("\\.");


        for (int i = sip.length; i < 4; i++) {
            ip += ".*";
        }
        String w = sip[sip.length - 1];
        if (w.equals("25")) {
            sip[sip.length - 1] = sip[sip.length - 1] + "5";
        }
        if (w.length() <= 2) {
            for (int i = w.length(); i < 3; i++) {
                String temp = sip[sip.length - 1];
                if (Integer.valueOf(temp + "9") < 255) {
                    sip[sip.length - 1] = temp + "9";
                }
            }
        }
        System.out.println(sip[sip.length - 1]);
        String f = ip;
        String e = "";
        String[] sipTemp = ip.split("\\.");
        for (int i = 0; i < sipTemp.length; i++) {

            if (i == sip.length - 1) {
                e += sip[sip.length - 1] + ".";
            } else {
                e += sipTemp[i] + ".";
            }

        }
        f = f.replace("*", "0");
        e = e.replace("*", "255");
        System.out.println(f);
        System.out.println(e);
    }


}
