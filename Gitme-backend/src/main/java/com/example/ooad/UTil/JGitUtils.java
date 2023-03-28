package com.example.ooad.UTil;

import cn.hutool.core.io.FileUtil;
import com.example.ooad.service.Repo.*;
import org.apache.commons.lang3.StringUtils;
import com.example.ooad.UTil.Handler.InputChecker;
import com.example.ooad.bean.Repo;
import com.example.ooad.dao.RepoDao;
import com.example.ooad.service.Repo.BranchInfo;
import com.example.ooad.UTil.JGitAPI.GetFileAttributes;
import com.example.ooad.controller.RepoCtrl;
import com.example.ooad.UTil.JGitAPI.LsFileList;
import com.example.ooad.service.Repo.FileBaseInfo;
import com.example.ooad.service.Repo.RepoService;
import com.example.ooad.service.Repo.VersionInfo;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.archive.ArchiveFormats;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.example.ooad.UTil.JGitAPI.LsFileList.buildRevCommit;
import static com.example.ooad.UTil.JGitAPI.LsFileList.buildTreeWalk;
import static java.util.stream.Collectors.joining;

public class JGitUtils {
    @Autowired
    private RepoService repoService;

    @Autowired
    private RepoDao repoDao;


    private static final Logger logger = LoggerFactory.getLogger(JGitUtils.class);


    // 创建新的git仓库/初始化仓库
    public static void CreateNewRepository(String dir) throws Exception {
        // prepare a new folder
        File localPath = new File(dir);
        if (localPath.exists()) {
            throw new Exception("Repository already exists!");
        }
        //create the directory
        try(Git git = Git.init().setDirectory(localPath).call()) {
           logger.info("Having repository: " + git.getRepository().getDirectory());
            //初始化就有readme.md
            File readMe = new File(localPath, "README.md");
            if (!readMe.exists()) {
                readMe.createNewFile();
            }
            git.add().addFilepattern("README.md").call();
            git.commit().setMessage("initial commit").call();
        }
    }
    // 删除/清除git仓库
    public static void CleanRepository(String dir) throws Exception {
        // prepare a new folder
        File localPath = new File(dir);//直接删除.git的上层文件
        if (!localPath.exists()) {
//            return;
            throw new Exception("Repository not exists!");
        }
        // clean up here to not keep using more and more disk-space for these samples
        FileUtils.deleteDirectory(localPath);
    }
    // 打开已有的git仓库
    public static Git OpenRepository(String dir){
        Git git = null;
        try{
            File repoDir = new File(dir, ".git");
            if (!repoDir.exists()) {
                throw new IOException("Not a git repository: " + repoDir);
            }
            Repository repository = new FileRepositoryBuilder().setGitDir(Paths.get(dir, ".git").toFile()).build();
            git = new Git(repository);
        } catch (IOException e){
            e.printStackTrace();
        }
        return git;
    }
    // 关闭git仓库
    public static void CloseRepository(Git git){
        git.close();
    }
    //克隆git仓库   -- 未写好和测试
    public static void CloneRepository(String dir,String url){
        try{
            Git.cloneRepository().setURI(url).setDirectory(new File(dir)).call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    // 添加文件到缓存区
    public static boolean AddFileToRepository(Git git, String file){
        try{
            File myFile = new File(file);
            if (!myFile.exists()) {
                throw new IOException("Not a file: " + myFile);
            }
            String fileRelaPath = file.replace(
                    git.getRepository().getDirectory().getPath()
                            .replace(".git",""),"")
                            .replace("\\","/");
            logger.info("add file "+fileRelaPath);

            git.add()
                    .addFilepattern(fileRelaPath)
                    .call();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // 移动/删除文件到git仓库
    public static void MoveFileToRepository(Git git, String file){
        try{

            git.rm()

                    .addFilepattern(file)
                    .call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //提交文件到git仓库
    public static void CommitFileToRepository(Git git, String message){
        try{
            git.commit()
                    .setMessage(message)
                    .call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //推送文件到git仓库
    public static void PushFileToRepository(Git git){
        try{
            git.push().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //拉取文件到git仓库
    public static void PullFileToRepository(Git git){
        try{
            git.pull().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //设置远程链接
    public static void SetRemoteUrl(Git git, String remoteName, String remoteUrl){
        try{
            git.remoteSetUrl().setRemoteName(remoteName).setRemoteUri(new URIish(remoteUrl)).call();
        } catch (Exception e){
            e.printStackTrace();}
    }
    //删除远程链接
    public static void RemoveRemoteUrl(Git git, String remoteName){
        try{
            git.remoteRemove().setRemoteName(remoteName).call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //合并文件到git仓库
    public static void MergeFileToRepository(Git git){
        try{
            git.merge().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //检出文件到git仓库
    public static void CheckoutFileToRepository(Git git){
        try{
            git.checkout().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean checkout(String repoDir, String branchName){
        if (!InputChecker.checkNullAndEmpty(repoDir)) {
            logger.error("checkout: repoDir is null and empty");
            return false;
        }

        Git git = JGitUtils.OpenRepository(repoDir);

        try {
            git.checkout()
                    .setName(branchName)
                    .call();
            git.checkout()
                    .setAllPaths(true)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        }
        logger.info("checkout");
        return true;
    }
    //重置文件到git仓库
    public static void ResetFileToRepository(Git git){
        try{
            git.reset().call();
            git.revert().call();
            git.rebase().call();
        } catch (Exception e){
            e.printStackTrace();}
    }
    //标签文件到git仓库
    public static void TagFileToRepository(Git git){
        try{
            git.tag().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //创建标签文件到git仓库
    public static void CreateTagFileToRepository(Git git){
        try{
            git.tag().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //删除标签文件到git仓库
    public static void DeleteTagFileToRepository(Git git){
        try{
            git.tagDelete().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //列出标签文件到git仓库
    public static void ListTagFileToRepository(Git git){
        try{
            List<Ref> call = git.tagList().call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //创建分支
    public static boolean CreateBranch(Git git, String branchName){
        try{
            git.branchCreate().setName(branchName).call();
            logger.info("addBranch "+branchName);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //删除分支
    public static boolean DeleteBranch(Git git, String branchName){
        try{
            git.branchDelete().setBranchNames(branchName).call();
//            git.branchDelete().setBranchNames(branchName).setForce(true).call();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            logger.error("DeleteBranch error");
            return false;
        }
    }
    //切换分支
    public static void SwitchBranch(Git git, String branchName){
        try{
            git.checkout().setName(branchName).call();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //合并分支
    public static boolean Merge2Branch(Git git, String branchNameFrom, String branchNameTo){
        try{
            git.checkout().setName(branchNameTo).call();
            MergeResult mergeResult=git.merge().include(git.getRepository().resolve(branchNameFrom)).call();
            MergeResult.MergeStatus status =mergeResult.getMergeStatus();
            if (status.equals(MergeResult.MergeStatus.FAILED)){
                logger.error("Merge2Branch Failed");
                return false;
            }
            if (status.equals(MergeResult.MergeStatus.CONFLICTING)){
                logger.error("Merge2Branch Conflict");
                return false;
            }
            logger.info("This merge status is "+status);
            System.out.println("This merge status is "+status);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Merge2Branch error");
            return false;
        }
    }

    //获取分支列表
//    若某个节点有多个父节点，好像就有问题
    public static List<BranchInfo> GetBranchList(Git gFit, String repoID, String uploadFilePath, RepoDao repoDao){
//        List<String> branchList = new ArrayList<>();
        String dir = findDir(repoID, uploadFilePath, repoDao);
        List<BranchInfo> branchInfos = new ArrayList<>();
        Git git = JGitUtils.OpenRepository(dir);
        try{
            Repository repository = git.getRepository();
            String currBranch = repository.getBranch();
//            local branches
            List<Ref> call = gFit.branchList().call();
//            for (Ref ref : call) {
//                branchInfos.add(new BranchInfo(ref.getName(), ref.getObjectId().getName()));
//                System.out.printf("branch name : %s, branch hash code : %s\n", ref.getName(), ref.getObjectId().getName());
//            }
//            remote branches, notice duplication
//            call = gFit.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : call) {
                String name = ref.getName(), objID = ref.getObjectId().getName();
                String[] str = name.split("/");
                name = str[str.length-1];
                /*if(name.startsWith("refs/heads/")){
                    name = name.replace("refs/heads/", "");
                }else{
                    continue;
                }*/
                BranchInfo branchInfo = new BranchInfo(name, objID);
                if(name.equals(currBranch)){
                    branchInfo.setDefault(true);
                }
                branchInfos.add(branchInfo);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return branchInfos;
    }

    public static VersionInfo getVersion(String repoID, String commitID, String uploadFilePath, RepoDao repoDao){
        String dir = findDir(repoID, uploadFilePath, repoDao);
        VersionInfo versionInfo;
        try (Git git = JGitUtils.OpenRepository(dir)) {
            dir = "./"+dir+"/.git";
            Repository repository = new FileRepository(dir);
            String curBranch = repository.getBranch();
            RevWalk walk = new RevWalk(repository);
            RevCommit commit = walk.parseCommit(repository.resolve(commitID));
            Map<ObjectId, String> map = git
                    .nameRev()
                    .addPrefix("refs/heads")
                    .add(ObjectId.fromString(commitID))
                    .call();
            String foundInBranch = map.get(commit.getId());
            List<String> cmitParents = new ArrayList<>();
            for (int i = 0; i < commit.getParentCount(); i++) {
                cmitParents.add(commit.getParent(i).getName());
            }
            Instant commitInstant = Instant.ofEpochSecond(commit.getCommitTime());
            ZoneId zoneId = commit.getAuthorIdent().getTimeZone().toZoneId();
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(commitInstant, zoneId);
            String commitTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            versionInfo = new VersionInfo(Long.parseLong(repoID), commit.hashCode(),foundInBranch, commit.getName(), cmitParents,commit.getShortMessage(), commitTime);
            if(commit.getParentCount()>0){
                String curCmit = commit.getName(), preCmit = commit.getParent(0).getName();
                versionInfo.setChangedFile(findChangedFiles(repoID, uploadFilePath, repoDao, curCmit+"^{tree}", preCmit+"^{tree}"));
            }
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
        return versionInfo;
    }

    public static List<VersionInfo> getAllVersion(String repoID, String branchName, String uploadFilePath, RepoDao repoDao){
        String dir = findDir(repoID, uploadFilePath, repoDao);
        List<VersionInfo> versionInfos = new ArrayList<>();
        try (Git git = JGitUtils.OpenRepository(dir)) {
            dir = "./"+dir+"/.git";
//            System.out.println(dir);
            Repository repository = new FileRepository(dir);
            String curBranch = repository.getBranch();
            git.checkout().setName(branchName);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
           /* Collection<Ref> allRefs = repository.getAllRefs().values();
            try (RevWalk revWalk = new RevWalk( repository )) {
                for( Ref ref : allRefs ) {
                    revWalk.markStart( revWalk.parseCommit( ref.getObjectId() ));
                }
                System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
                int count = 0;
                for( RevCommit commit : revWalk ) {
                    System.out.println("Commit: " + commit.getName());
                    count++;
                }
                System.out.println("Had " + count + " commits");
            }*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            RevWalk walk = new RevWalk(repository);
            Iterable<RevCommit> commits = git.log().all().call();
            String foundInBranch = "";
            String curCmit = "HEAD^", preCmit = "HEAD^^";
            for(RevCommit commit : commits){
                boolean foundInThisBranch = false;
                RevCommit targetCommit = walk.parseCommit(repository.resolve(
                        commit.getName()));
                for (Map.Entry<String, Ref> e : repository.getAllRefs().entrySet()) {
                    if (e.getKey().startsWith(Constants.R_HEADS)) {
                        if (walk.isMergedInto(targetCommit, walk.parseCommit(
                                e.getValue().getObjectId()))) {
                            foundInBranch = e.getValue().getName();
                            if(foundInBranch.startsWith("refs/heads/")){
                                foundInBranch = foundInBranch.replace("refs/heads/", "");
                            }
//                            System.out.printf("branchName %s, foundInBranch %s\n", branchName, foundInBranch);
                            if (foundInBranch.equals(branchName)) {
                                foundInThisBranch = true;
                                break;
                            }
                        }
                    }
                }
                if (foundInThisBranch) {
                    Instant commitInstant = Instant.ofEpochSecond(commit.getCommitTime());
                    ZoneId zoneId = commit.getAuthorIdent().getTimeZone().toZoneId();
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(commitInstant, zoneId);
                    String commitTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String parentHash = null;
                    List<String> cmitParents = new ArrayList<>();
                    for (int i = 0; i < commit.getParentCount(); i++) {
                        cmitParents.add(commit.getParent(i).getName());
                    }
                    VersionInfo versionInfo = new VersionInfo(Long.parseLong(repoID), commit.hashCode(),foundInBranch, commit.getName(), cmitParents,commit.getShortMessage(), commitTime);
                    if(commit.getParentCount()>0){
//                        String currCmit = commit.getName(), prevCmit = commit.getParent(0).getName();
//                        versionInfo.setChangedFile(findChangedFiles(repoID, uploadFilePath, repoDao, curCmit, preCmit));
                        versionInfo.setChangedFile(findChangedFiles(repoID, uploadFilePath, repoDao, curCmit+"{tree}", preCmit+"{tree}"));
                    }
                    versionInfos.add(versionInfo);
                }
                curCmit+="^";preCmit += "^";
            }
            git.checkout().setName(curBranch);
        }catch (Exception e){
            e.printStackTrace();
        }
        return versionInfos;
    }

    /*public static List<VersionInfo> getAllVersion2(String repoID, String branchName, String uploadFilePath, RepoDao repoDao){
        String dir = findDir(repoID, uploadFilePath, repoDao);
        List<VersionInfo> versionInfos = new ArrayList<>();
        try (Git git = JGitUtils.OpenRepository(dir)) {
            dir = "./"+dir+"/.git";
            Repository repository = new FileRepository(dir);
            List<Ref> branches = git.branchList().setListMode( ListBranchCommand.ListMode.ALL ).call();
            try( RevWalk walk = new RevWalk( git.getRepository() ) ) {
                for( Ref branch : branches ) {
                    RevCommit commit = walk.parseCommit( branch.getObjectId() );

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return versionInfos;
    }*/



    public static String findDir(String repoID, String uploadFilePath, RepoDao repoDao){
        long repoid;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoid = Long.parseLong(repoID);
        }else{
            return null;
        }
        Repo repo = repoDao.findById(repoid);
        return uploadFilePath + "/" + repo.getCreator().getMail()
                + "/" + repo.getName();

    }

    /**
     *暂时无法返回第一次commit的修改文件列表，因为这个方法就是比较两次commit之间的差别，但是第一次之前没有什么比的
     */
    public static List<String> findChangedFiles(String repoID, String uploadFilePath, RepoDao repoDao, String currCommit, String prevCommit) throws IOException {
        String dir = findDir(repoID, uploadFilePath, repoDao);
        List<String> changedFiles = new ArrayList<>();
        try (Git git = JGitUtils.OpenRepository(dir)){
            dir = "./"+dir+"/.git";
//            System.out.println(dir);
            Repository repository = new FileRepository(dir);
//            这里resolve里填的是一串数字还是HEAD^{tree}这样？是数字找不到吗还是啥，现在就改成循环加^来控制前移
            ObjectId oldHead = repository.resolve(prevCommit);
            ObjectId head = repository.resolve(currCommit);
//            System.out.println("Printing diff between tree: " + oldHead + " and " + head);

            // prepare the two iterators to compute the diff between
            try (ObjectReader reader = repository.newObjectReader()) {
                CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                oldTreeIter.reset(reader, oldHead);
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                newTreeIter.reset(reader, head);

                // finally get the list of changed files
                try (Git git2 = new Git(repository)) {
                    List<DiffEntry> diffs= git2.diff()
                            .setNewTree(newTreeIter)
                            .setOldTree(oldTreeIter)
                            .call();
                    int add = 0, modify = 0, delete = 0;
                    String addF = "", modifyF = "", deleteF = "";
                    String difference = "";
                    for (DiffEntry entry : diffs) {
                        if(entry.getChangeType().equals(DiffEntry.ChangeType.ADD)){
                            add += 1;
                            addF += " " + entry.getNewPath();
                        }else if(entry.getChangeType().equals(DiffEntry.ChangeType.MODIFY)){
                            modify += 1;
                            modifyF += " " + entry.getNewPath();
                        }else if(entry.getChangeType().equals(DiffEntry.ChangeType.DELETE)){
                            delete += 1;
                            deleteF += " " + entry.getNewPath();
                        }
                        difference = String.format("add %d files : %s, modify %d files : %s, delete %d files : %s", add, addF, modify, modifyF, delete, deleteF);

//                        System.out.println("old: " + entry.getOldPath() +
//                                ", new: " + entry.getNewPath() +
//                                ", entry: " + entry);
                    }
                    changedFiles.add(difference);
                } catch (GitAPIException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return changedFiles;
    }

    public static void diffOfCommits(String repoID, String uploadFilePath, RepoDao repoDao) throws IOException, GitAPIException {
        long repoId;
        if(InputChecker.checkNullAndEmpty(repoID) && InputChecker.checkNum(repoID)){
            repoId = Long.parseLong(repoID);
        }else{
            return;
        }
        Repo repo = repoDao.findById(repoId);
        String dir = uploadFilePath + "/" + repo.getCreator().getMail() + "/" + repo.getName();
        Git git = JGitUtils.OpenRepository(dir);
        File file = new File( git.getRepository().getWorkTree(), "file.txt" );
        writeFile( file, "first version" );
        RevCommit newCommit = commitChanges(git);
        writeFile( file, "second version" );
        RevCommit oldCommit = commitChanges(git);

        ObjectReader reader = git.getRepository().newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        ObjectId oldTree = git.getRepository().resolve( "HEAD^{tree}" ); // equals newCommit.getTree()
        oldTreeIter.reset( reader, oldTree );
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        ObjectId newTree = git.getRepository().resolve( "HEAD~1^{tree}" ); // equals oldCommit.getTree()
        newTreeIter.reset( reader, newTree );

        DiffFormatter df = new DiffFormatter( new ByteArrayOutputStream() ); // use NullOutputStream.INSTANCE if you don't need the diff output
        df.setRepository( git.getRepository() );
        List<DiffEntry> entries = df.scan( oldTreeIter, newTreeIter );

        for( DiffEntry entry : entries ) {
            System.out.println( entry );
        }
    }
    private static RevCommit commitChanges(Git git) throws GitAPIException {
        git.add().addFilepattern( "." ).call();
        return git.commit().setMessage( "commit message" ).call();
    }

    private static void writeFile( File file, String content ) throws IOException {
        FileOutputStream outputStream = new FileOutputStream( file );
        outputStream.write( content.getBytes( "UTF-8" ) );
        outputStream.close();
    }

    //获取当前分支
    public static String GetCurrentBranch(Git git){
        String branch = "";
        try{
            branch = git.getRepository().getBranch();
        } catch (Exception e){
            e.printStackTrace();
        }
        return branch;
    }

    //版本差异对比
    public static List<DiffEntry> getVersionDiff(Git git, String oldVersion, String newVersion){
        try{
            AbstractTreeIterator oldTreeParser = prepareTreeParser(git.getRepository(), oldVersion);
            AbstractTreeIterator newTreeParser = prepareTreeParser(git.getRepository(), newVersion);
            List<DiffEntry> diff = git.diff().setNewTree(newTreeParser).setOldTree(oldTreeParser).call();
//            for (DiffEntry diffEntry : diff) {
//                System.out.println("差异类型：" + diffEntry.getChangeType());
//                System.out.println("旧版本路径：" + diffEntry.getOldPath());
//                System.out.println("新版本路径：" + diffEntry.getNewPath());
//            }
            return diff;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //prepareTreeParser是一个把RevCommit对象转换成AbstractTreeIterator方法，Jgit中的Tree和Ref是存储git对象的类
    public static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }
    public static String DiffToRaw(List<DiffEntry> diffs){
        String rawDiff;
        rawDiff = diffs.stream().map(diff -> {
            List<String> entry = new ArrayList<>();

            entry.add(StringUtils.leftPad(diff.getOldMode().toString(), 6, "0"));
            entry.add(StringUtils.leftPad(diff.getNewMode().toString(), 6, "0"));
            entry.add(diff.getOldId().name().substring(0,7));
            entry.add(diff.getNewId().name().substring(0,7));

            switch (diff.getChangeType()) {
                case ADD, MODIFY, DELETE -> entry.add(diff.getChangeType().name().substring(0, 1)); // A, M or D
                case COPY, RENAME ->
                        entry.add(String.format("%s%d", diff.getChangeType().name().charAt(0), diff.getScore())); // C21, R43
            }

            if (!diff.getOldPath().equals("/dev/null")) {
                entry.add(diff.getOldPath());
            }

            if (!diff.getNewPath().equals("/dev/null")) {
                entry.add(diff.getNewPath());
            }

            if (diff.getOldPath().equals(diff.getNewPath())){
                entry.remove(entry.size() - 1);
            }

            return String.join(" ", entry);

        }).collect(joining("\n"));
        return rawDiff;
    }
    public static String DiffToString(List<DiffEntry> diffs,Git git) throws IOException {
        StringBuilder diffString = new StringBuilder();
        for (DiffEntry diffEntry : diffs) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter df = new DiffFormatter(out);
            df.setRepository(git.getRepository());
            df.format(diffEntry);
            String diffText = out.toString(StandardCharsets.UTF_8);
            diffString.append(diffText);
        }
        return diffString.toString();
    }

    //回溯版本
    public static void Revert(Git git, String commitId){
        try{
            RevWalk walk = new RevWalk(git.getRepository());
            RevCommit commit = walk.parseCommit(ObjectId.fromString(commitId));
            git.revert().include(commit).call();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //回滚版本
    public static void Reset(Git git, String commitId){
        try{
            RevWalk walk = new RevWalk(git.getRepository());
            ObjectId objectId = git.getRepository().resolve(commitId);
            RevCommit commit = walk.parseCommit(objectId);
            String perVision = commit.getParent(0).getName();   //获取commit的身份名
            git.reset().setMode(ResetCommand.ResetType.HARD).setRef(perVision).call();  //设置参数
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //获取当前分支仓库管理的文件列表
    public static List<FileBaseInfo> lsByBranch(Git git, String dir ,String branchName){
        List<FileBaseInfo> fileBaseInfoList = new ArrayList<>();
        try{
            //获取当前分支
//            String currentBranch = git.getRepository().getBranch();
            //切换分支
//            git.checkout().setName(branchName).call();
            //获取当前分支管理的文件列表
            ObjectId branchId = git.getRepository().resolve(branchName);
            Iterable<RevCommit> commits = git.log().add(branchId).call();
            RevCommit commit = commits.iterator().next(); //Todo：commit 次数拓展
            RevTree tree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(git.getRepository());
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            Map map = new HashMap();
            while (treeWalk.next()) {
                FileBaseInfo fileBaseInfo = new FileBaseInfo();
                String dirName = treeWalk.getPathString();
                String fileName;
                if(!dirName.contains(dir)){
                    continue;
                }
                fileName = dirName.substring(dirName.indexOf(dir)+dir.length());
                while (fileName.contains("/")){
                    fileName = fileName.substring(0, fileName.lastIndexOf("/"));     //杜绝'/'结尾命名文件夹
                }
                fileBaseInfo.setName(fileName);
                Date time=commit.getAuthorIdent().getWhen();
                fileBaseInfo.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
//                System.out.println(dirName);
                String type = "";
                fileBaseInfo.setType(type);
                if (map.get(fileName) == null){
                    map.put(fileName, fileBaseInfo);
                    fileBaseInfoList.add(fileBaseInfo);
                }
//                fileBaseInfoList.add(fileBaseInfo);
            }

            //切换回原来的分支
//            git.checkout().setName(currentBranch).call();
        } catch (Exception e){
            e.printStackTrace();
        }
        return fileBaseInfoList;
    }

    // 根据commit 和 tag 获取文件列表 --github
    public static List<String> lsByCommit(Git git, String path ,String commit) throws IOException {
        Repository repository = git.getRepository();
        RevCommit revCommit = LsFileList.buildRevCommit(repository, commit);

        // and using commit tree find the path
        RevTree tree = revCommit.getTree();
        //System.out.println("Having tree: " + tree + " for commit " + commit);

        List<String> items = new ArrayList<>();

        // shortcut for root-path
        if(path.isEmpty()) {
            try (TreeWalk treeWalk = new TreeWalk(repository)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(false);
                treeWalk.setPostOrderTraversal(false);

                while(treeWalk.next()) {
                    items.add(treeWalk.getPathString());
                }
            }
        } else {
            // now try to find a specific file
            try (TreeWalk treeWalk = LsFileList.buildTreeWalk(repository, tree, path)) {
                if((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
                    throw new IllegalStateException("Tried to read the elements of a non-tree for commit '" + commit + "' and path '" + path + "', had filemode " + treeWalk.getFileMode(0).getBits());
                }

                try (TreeWalk dirWalk = new TreeWalk(repository)) {
                    dirWalk.addTree(treeWalk.getObjectId(0));
                    dirWalk.setRecursive(false);
                    while(dirWalk.next()) {
                        items.add(dirWalk.getPathString());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return items;
    }

    //获得指定commit的文件内容
    public static FileContent getContent(String repoPath, String filePath, String commitID)  {
        Git git =  OpenRepository(repoPath);

        Repository repo = git.getRepository();
        RevTree tree = null;
        FileContent result = null;
//        System.out.println(commitID);
        try {
            if(commitID==null) tree = GetFileAttributes.getTree(repo);
            else tree = GetFileAttributes.getTree(repo, repo.resolve(commitID));
            result = GetFileAttributes.getFileStr(repo, tree, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void downLoadRepository(String repoPath, String branchName) throws IOException {
        ArchiveFormats.registerAll();
        String repoName = repoPath.substring(repoPath.lastIndexOf("/")+1);
        File file = new File(repoPath,repoName+ ".zip");

        try (OutputStream out = new FileOutputStream(file)){
            try(Git git = OpenRepository(repoPath)){
                Repository repository = git.getRepository();
                git.archive()
                        .setTree(repository.resolve(branchName))
                        .setFormat("zip")
                        .setOutputStream(out)
                        .call();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void downLoadZipToFront(HttpServletResponse response, String repoPath) throws IOException {
        String repoName = repoPath.substring(repoPath.lastIndexOf("/")+1);
        File file = new File(repoPath,repoName+ ".zip");
        response.setContentType("application/zip");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+file.getName());
        InputStream fis = new FileInputStream(file);
        IOUtils.copy(fis,response.getOutputStream());
        fis.close();
        FileUtils.forceDelete(file);
    }

    public static boolean PRMerge(String authorRepoPath, String PRerRepoPath, String branch,String title,String PRID){
        try{
        //新建一个文件夹
        String PRPath = PRerRepoPath.substring(0,PRerRepoPath.lastIndexOf("/"));
        PRPath = PRPath+"/"+"PR";
        File newRepo = new File(PRPath);
        if(newRepo.exists()){
            FileUtils.deleteDirectory(newRepo); //防止之前报错没删除掉PR文件夹
        }
        if(!newRepo.mkdir()){
            throw new IOException("创建PR文件夹失败");
        }
        //复制PRerRepoPath下的.git文件夹到PRPath
        FileUtil.copy(Paths.get(PRerRepoPath + "/.git"), Paths.get(PRPath));
        //打开PRPath下的.git文件夹
        Git git = OpenRepository(PRPath);
        //checkout到branch分支
        git.checkout().setName(branch).call();
        git.checkout().setAllPaths(true).call();
        //关闭PRPath下的.git文件夹
        CloseRepository(git);
        //删除PRPath下的.git文件夹
        FileUtils.deleteDirectory(new File(PRPath + "/.git"));
        //复制authorRepoPath下的.git文件夹到PRPath
        FileUtil.copy(Paths.get(authorRepoPath + "/.git"), Paths.get(PRPath));
        //打开PRPath下的.git文件夹
        git = OpenRepository(PRPath);
        String authBranch = git.getRepository().getBranch();
        //新建分支并且commit
        String newBranch = branch+"_"+PRID+"_feature";
        git.branchCreate().setName(newBranch).call();  //Todo:可能无法在branch下变化
        git.checkout().setName(newBranch).call();
        if(!JGitUtils.AddFileToRepository(git,".")){
            return false;
        }
        JGitUtils.CommitFileToRepository(git, title);
        git.checkout().setName(authBranch).call();
        //关闭PRPath下的.git文件夹
        CloseRepository(git);
        //删除authorRepoPath下的.git文件夹
        FileUtils.deleteDirectory(new File(authorRepoPath + "/.git"));
        //复制PRPath下的.git文件夹到authorRepoPath
        FileUtil.copy(Paths.get(PRPath + "/.git"), Paths.get(authorRepoPath));
        //删除这个文件夹
        FileUtils.deleteDirectory(newRepo);
        return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}