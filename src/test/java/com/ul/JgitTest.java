package com.ul;

import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectIdRef;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefFilter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JgitTest {

    //@Test
    public void test() throws GitAPIException, IOException {
        String url = "https://github.com/ueu2715/ul.git";
        UsernamePasswordCredentialsProvider upc = new UsernamePasswordCredentialsProvider("ueu2715","along2715");
        Collection<Ref> git = Git.lsRemoteRepository().setRemote(url)
                .setTags(true)

                .setCredentialsProvider(upc)
                .call();
        System.out.println(git.size());
        git.forEach(ref -> {
            if (ref instanceof ObjectIdRef.PeeledTag){
                System.out.println("self:"+ref.getObjectId().getName());
                System.out.println(ref.getPeeledObjectId().getName());
            }else{
                System.out.println(ref.getObjectId().getName());
            }
        });


    }

    @Test
    public void test1() throws IOException, GitAPIException {
        String REMOTE_URL = "https://github.com/ueu2715/ul.git";
        // prepare a new folder for the cloned repository
        File localPath = File.createTempFile("TestGitRepository", "");
        if(!localPath.delete()) {
            throw new IOException("Could not delete temporary file " + localPath);
        }

        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
        StopWatch sw = new StopWatch();
        sw.start();
        try (Git result = Git.cloneRepository()
                .setCloneAllBranches(true)
                //.setBare(true)
                //.setBranch("test")
                .setURI(REMOTE_URL)
                .setDirectory(localPath)
                .setTimeout(120000)
                .call()) {


            sw.stop();
            System.out.println(sw.getTime());


            System.out.println(result.describe().setLong(true).call());



            // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
            /*System.out.println("Having repository: " + result.getRepository().getDirectory());

            Repository repo = result.getRepository();
            RevWalk walk = new RevWalk(repo);
            result.tagList().call()
                    .forEach(tag->{
                        try {
                            RevObject obj = walk.parseAny(tag.getObjectId());
                            if (obj instanceof RevTag){
                                RevTag t = (RevTag) obj;
                                System.out.println(t.getFullMessage());
                            }else{
                                System.out.println(tag+""+obj.getClass());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });

                    result.branchList().setListMode(ListBranchCommand.ListMode.ALL)
                            .call().stream().forEach(System.out::println);

                    result.checkout()
                            .setCreateBranch(true)
                            .setStartPoint("refs/remotes/origin/test").setName("test").call();


                    result.branchList()
                            .setContains("0096a36")
                    .call()
                    .stream().forEach(System.out::println);


                    //result.checkout().setName("test").call();
                    result.log()

                            .call()
                            .iterator()
                            .forEachRemaining(l->{

                                System.out.println(l.getId().getName());
                            });



                */



        }

        // clean up here to not keep using more and more disk-space for these samples
        localPath.delete();
        FileUtils.delete(localPath);
    }

    @Test
    public void test3() {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec("git shortlog",new String[]{"PATH=D:\\prog\\tools\\Git\\bin"},new File("F:\\work\\source\\02_GIT\\ul"));
            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());

        } catch (Throwable e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test4() throws JSONException {
        RestTemplate rt = new RestTemplate();
        String url = "http://127.0.0.1:8080/t?user={user}&id={id}&cp={cp}";
        Map<String , Object> param = new HashMap<>();
        JSONObject js = new JSONObject();
        js.put("cp","1234");
        param.put("user","xiaobai");
        param.put("id","123");
        param.put("cp",js);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("email", "first.last@example.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> res = rt.exchange(url, HttpMethod.POST, request, String.class, param);
    }
}
