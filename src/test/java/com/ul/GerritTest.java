package com.ul;

import com.sonymobile.tools.gerrit.gerritevents.GerritConnection;
import com.sonymobile.tools.gerrit.gerritevents.GerritConnectionConfig;
import com.sonymobile.tools.gerrit.gerritevents.GerritHandler;
import com.sonymobile.tools.gerrit.gerritevents.ssh.Authentication;
import org.eclipse.jgit.util.FileUtils;
import org.junit.Test;
import ul.gerrit.MyEventListener;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

public class GerritTest {

    public static void main(String[] args) {
        Authentication au = new Authentication(new File("C:\\Users\\ztw\\.ssh\\id_rsa"),"admin");
        MyEventListener gl = new MyEventListener();
        GerritConnection connection = new GerritConnection("gerrit","192.168.101.104",29418 ,au);
        GerritHandler handler = new GerritHandler();
        //handler.setIgnoreEMail(name, config.getGerritEMail());
        connection.setHandler(handler);
        //connection.addListener(gl);
        handler.addEventListeners(Collections.singletonList(gl));
        connection.start();
    }
}
