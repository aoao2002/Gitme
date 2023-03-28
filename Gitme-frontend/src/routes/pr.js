import { redirect, useParams } from "react-router-dom"
import { acceptPullRequest, rejectPullRequest, createPullRequest, getAllBranches, getPullRequestById, getPullRequests, getRepoIdByUserNameAndRepoName, getRepoInfo,  } from "../utils/api"

export async function prsLoader({ params }) {
	const token = localStorage.getItem('token')
	const { userName, repoName } = params
	const { data: repoId } = await getRepoIdByUserNameAndRepoName({ userName, repoName, token })
	const { data: prs } = await getPullRequests({ repoId, token })
	return { prs }
}

export async function prLoader({ params }) {
	const token = localStorage.getItem('token')
	const { prId } = params
	const { data: pr } = await getPullRequestById({ prId, token })
	return { pr }
}

export async function newPrLoader({ params }) {
	const { userName, repoName } = params
	const token = localStorage.getItem('token')
	const { data: repoId } = await getRepoIdByUserNameAndRepoName({ userName, repoName, token })
	const { data: repoInfo } = await getRepoInfo({ token, repoId })
	if (repoInfo.forkFromRepoName && repoInfo.forkFromUserName) {
		const { data: oriRepoId } = await getRepoIdByUserNameAndRepoName({ repoName: repoInfo.forkFromRepoName, userName: repoInfo.forkFromUserName, token })
		// const [branches, oriBranches] = Promise.all([
		// 	getAllBranches({ token, repoId }),
		// 	getAllBranches({ token, oriRepoId }),
		// ])
		const { data: oriBranches } = await getAllBranches({ token, repoId: oriRepoId })
		const oriDefaultBranch = oriBranches.find(b => b.default)
		return { oriRepoId, oriBranches, oriDefaultBranch }
	} else {
		return { }
	}
	
}

export async function newPrAction({ params, request }) {
	const token = localStorage.getItem('token')
	const formData = await request.formData()
	const { userName, repoName } = params
	const fromBranchId = formData.get('fromBranchId')
	const toBranchId = formData.get('toBranchId')
	const fromRepoId = formData.get('fromRepoId')
	const toRepoId = formData.get('toRepoId')
	const title = formData.get('title')
	try {
		const { data: prId } = await createPullRequest({ token, repoId: fromRepoId, title, toBranch: toBranchId, fromBranch: fromBranchId })
		return redirect(`/${userName}/${repoName}/pulls/${prId}`)
	} catch (err) {
		return { err: err.message }
	}

}

export async function prStateAction({ params, request }) {
	const token = localStorage.getItem('token')
	const formData = await request.formData()

	const prId = formData.get('prId')
	const act = formData.get('act')
	try {
		if (act === 'accept') await acceptPullRequest({ token, prId })
		else await rejectPullRequest({ token, prId })
		return { err: '' }
	} catch (err) {
		return { err: err.message }
	}
	
}