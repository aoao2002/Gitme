package com.example.ooad.UTil.JGitAPI;

import com.example.ooad.UTil.JGitUtils;
import com.example.ooad.service.Repo.FileBaseInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LsFileList {
    public static Repository openJGitCookbookRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
    }

    public static RevCommit buildRevCommit(Repository repository, String commit) throws IOException {
        // a RevWalk allows to walk over commits based on some filtering that is defined
        try (RevWalk revWalk = new RevWalk(repository)) {
            return revWalk.parseCommit(ObjectId.fromString(commit));
        }
    }

    public static TreeWalk buildTreeWalk(Repository repository, RevTree tree, final String path) throws IOException {
        TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);

        if(treeWalk == null) {
            throw new FileNotFoundException("Did not find expected file '" + path + "' in tree '" + tree.getName() + "'");
        }

        return treeWalk;
    }

    public static String getFileMode(FileMode fileMode) {
        if (fileMode.equals(FileMode.EXECUTABLE_FILE)) {
            return "Executable File";
        } else if (fileMode.equals(FileMode.REGULAR_FILE)) {
            return "Normal File";
        } else if (fileMode.equals(FileMode.TREE)) {
            return "Directory";
        } else if (fileMode.equals(FileMode.SYMLINK)) {
            return "Symlink";
        } else {
            // there are a few others, see FileMode javadoc for details
            throw new IllegalArgumentException("Unknown type of file encountered: " + fileMode);
        }
    }

    public static String GetFileAttribute(String fileName,RevTree tree, Git git) throws IOException {
        Repository repository = git.getRepository();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(false);
        treeWalk.setFilter(PathFilter.create(fileName));
        if (!treeWalk.next()) {
            throw new IllegalStateException("Did not find expected file '" + fileName + "'");
//            return "Normal File";
        }
        FileMode fileMode = treeWalk.getFileMode(0);
        return getFileMode(fileMode);
    }

    public static String GetFileAttribute(String fileName, Git git, String dir ,String branchName) throws IOException {
        String dirNew = dir+fileName+"/";
        if (JGitUtils.lsByBranch(git,dirNew,branchName).isEmpty()){
            return "Normal File";
        }else {
            return "Directory";
        }
    }
    public List<String> ListCommitFilePath(Git git){
        List<String> filePathList = new ArrayList<>();
        try {
            String currentBranch = git.getRepository().getBranch();
            //获取当前分支管理的文件列表
            ObjectId branchId = git.getRepository().resolve(currentBranch);
            Iterable<RevCommit> commits = git.log().add(branchId).call();
            RevCommit commit = commits.iterator().next(); //Todo：commit 次数拓展
            RevTree tree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(git.getRepository());
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                String dirName = treeWalk.getPathString();
                filePathList.add(dirName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return filePathList;
    }
}
