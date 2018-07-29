package com.ul;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.jcraft.jsch.*;
import org.junit.Test;

import java.io.*;


public class SshTest {
    private static final String USER="root";
    private static final String PASSWORD="root";
    private static final String HOST="192.168.101.104";
    private static final int DEFAULT_SSH_PORT=22;
    //@Test
    public static void main(String[] args) {

        try {
            Connection conn = new Connection(HOST,DEFAULT_SSH_PORT);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(
                    USER,PASSWORD);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();

            //sess.execCommand("cd test");//这么写是不行的
            //sess.execCommand("cd /home/gitrepo/test; git log --oneline --decorate --graph;");
            sess.execCommand("ssh -p 29418 admin@192.168.101.104 gerrit stream-events");

            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());
            BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(
                    new InputStreamReader(stderr));

            System.out.println("Here is the output from stdout:");
            while (true) {
                String line = stdoutReader.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }

            System.out.println("Here is the output from stderr:");
            while (true) {
                String line = stderrReader.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }
            sess.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }
}
