package com.example.ooad;

import com.example.ooad.UTil.*;
import com.example.ooad.service.Repo.FileBaseInfo;
import com.example.ooad.service.Repo.RepoService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class OoadApplicationTests {
	@Test
	void contextLoads() {
	}
	@Test
	void GitCreateNewRepositoryTest() {
		//Use JGit to create a new repository
		try {
			JGitUtils.CreateNewRepository("/Users/wangtianaoo/Desktop/gitTest/aoaoin");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void GitCleanRepositoryTest() {
		//Use JGit to delete a repository
		try {
			JGitUtils.CleanRepository("/Users/wangtianaoo/Desktop/gitTest/aoao");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void GitOpenRepositoryTest() {
		//Use JGit to open a repository
		try {
			Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest");
			System.out.print(git);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void GitCloseRepositoryTest() {
		//Use JGit to close a repository
		try {
			Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest");
			JGitUtils.CloseRepository(git);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void GitAddFileToRepositoryTest() {
		//Use JGit to add a file to a repository
		try {
			Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/aoaopr");
			JGitUtils.AddFileToRepository(git,"/Users/wangtianaoo/Desktop/gitTest/aoaopr/c.txt");
			System.out.println(git);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void GitCommitFileToRepositoryTest() {
		//Use JGit to commit a file to a repository
		try {
			Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/aoaopr");
			JGitUtils.CommitFileToRepository(git,"test3");
			System.out.println(git);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    @Test
    void GitLsTest(){
        //Use JGit to list all files in a repository
        try {
            Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/aoao");
			List<FileBaseInfo> list=JGitUtils.lsByBranch(git,"kkk/","master");
			for (FileBaseInfo fileBaseInfo : list) {
				System.out.println("Name: "+fileBaseInfo.getName()+"\t"+"Type: "+fileBaseInfo.getType()+"\t"+"Time: "+fileBaseInfo.getTime());
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//	@Test
//	void GitLsCommitTest(){
//		//Use JGit to list all commits in a repository
//		try {
//			Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/aoao");
//			List<String> list=JGitUtils.lsByCommit(git,"","master");
//			for (String s : list) {
//				System.out.println(s);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
    @Test
    void TestDiffBetweenTwoVision(){
        // Use Jgit to get the difference between two versions
        try {
            Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/aoao");
			RevCommit revCommit1=git.log().setMaxCount(1).call().iterator().next();
			RevCommit revCommit2=git.log().setMaxCount(2).call().iterator().next();
			System.out.println(revCommit1.getName());
			System.out.println(revCommit2.getName());
            JGitUtils.getVersionDiff(git,revCommit1.getName(),revCommit2.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//	@Test
//	void TestPRMerge() {
//		boolean t=JGitUtils.PRMerge("/Users/wangtianaoo/Desktop/gitTest/aoao",
//				"/Users/wangtianaoo/Desktop/gitTest/aoaoPR","master4");
//	}
    @Test
    void TestIsText(){
        // Use Jgit to get the difference between two versions
        try {
            System.out.println(FileUtils.isText("/Users/wangtianaoo/Desktop/gitTest/aoao/README.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void TestGetBranch(){
        try {
            Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/gitgit");
            List<Ref> call = git.branchList().call();
            for (Ref ref : call) {
                System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
            }

            System.out.println("Now including remote branches:");
            call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : call) {
                System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
            }
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }
	@Test
	void TestMerge(){
		try {
			Git git=JGitUtils.OpenRepository("/Users/wangtianaoo/Desktop/gitTest/aoao");
			System.out.println(JGitUtils.Merge2Branch(git,"master3","master6"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
