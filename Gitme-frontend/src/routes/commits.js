import { diff, getCommit, getCommits, getRepoIdByUserNameAndRepoName } from "../utils/api"

export async function commitsLoader({ params, request }) {
	const { userName, repoName, branchId } = params
	const token = localStorage.getItem('token')
	const { data: repoId } = await getRepoIdByUserNameAndRepoName({ token, userName, repoName })
	const { data: commits } = await getCommits({ branchId, token, repoId })
	return { commits }
}

export async function commitLoader({ params, request }) {
	const { userName, repoName, branchId, commitId } = params
	const token = localStorage.getItem('token')
	const { data: repoId } = await getRepoIdByUserNameAndRepoName({ token, userName, repoName })
	const { data: commit } = await getCommit({ token, commitId, repoId })
	const parentCommitHash = commit.parentHash?.[0]
	const diffPromise = parentCommitHash ? diff({ token, branchId1: commitId, branchId0: parentCommitHash, repoId }).then(({ data }) => data) : Promise.resolve()
	return { commit, diffPromise }
}