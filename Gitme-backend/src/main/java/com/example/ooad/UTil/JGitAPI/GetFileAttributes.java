package com.example.ooad.UTil.JGitAPI;

/*
   Copyright 2013, 2014 Dominik Stadler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import com.example.ooad.controller.RepoCtrl;
import com.example.ooad.service.Repo.FileBaseInfo;
import com.example.ooad.service.Repo.FileContent;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Snippet which shows how to use RevWalk and TreeWalk to read the file
 * attributes like execution-bit and type of file/directory/...
 *
 * @author dominik.stadler at gmx.at
 */
public class GetFileAttributes {
    private static final Logger logger = LoggerFactory.getLogger(GetFileAttributes.class);


    public static RevTree getTree(Repository repository) throws IOException {
        System.out.println("gettree");
        return getTree(repository, repository.resolve(Constants.HEAD));
    }

    public static RevTree getTree(Repository repository, ObjectId targetCommitId) throws IOException {

        // a RevWalk allows to walk over commits based on some filtering

        try (RevWalk revWalk = new RevWalk(repository)) {
            RevCommit commit = revWalk
                                    .parseCommit(targetCommitId);
            RevTree tree = commit.getTree();
            return tree;
        }
    }

    public static FileContent getFileStr(Repository repository, RevTree tree, String path)
            throws IOException {
        logger.info("getFileStr "+path);

        // now try to find a specific file
        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(path));
            if (!treeWalk.next()) {
                throw new IllegalStateException("Did not find expected file: "+path);
            }
            ObjectLoader loader = repository.open(treeWalk.getObjectId(0));
            long contentSize =  loader.getSize();
            String content = new String(loader.getBytes());
            long linesNum = content.split("\n").length;

            return new FileContent(content,false,linesNum,contentSize);
        }
//        try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree)) {
//            ObjectId blobId = treeWalk.getObjectId(0);
//            try (ObjectReader objectReader = repository.newObjectReader()) {
//                ObjectLoader objectLoader = objectReader.open(blobId);
//                byte[] bytes = objectLoader.getBytes();
//                return new String(bytes, StandardCharsets.UTF_8);
//        }
//    }
    }



}

