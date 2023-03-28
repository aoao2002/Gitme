import { redirect } from "react-router-dom"
import { addWatch, createRepository, deleteFile, deleteRepository, forkRepo, getAllBranches, getCloneUrl, getContent, getFileContent, getRepoIdByUserNameAndRepoName, getRepoInfo, invite, removeWatch, starRepo, undoStarRepo, unInvite, updateRepoDesc, uploadFile } from "../utils/api"
import * as path from "../utils/path"

export default async function repoInfoLoader({ params }) {
	const { userName, repoName } = params
	const token = localStorage.getItem('token')
	const { data: repoId } = await getRepoIdByUserNameAndRepoName({ token, userName, repoName })
	const [{ data: branches }, {data: repoInfo }, cloneUrl] = await Promise.all([
		getAllBranches({ token, repoId }),
		getRepoInfo({ token, repoId }),
		getCloneUrl({ repoName, userName, token })
	])
	const myName = localStorage.getItem('userName')
	const isAC = (myName === userName) || repoInfo.collaborators.some(c => c === myName)
	const defaultBranch = branches?.find(b => b.default)
	return { repoId, repoInfo, branches, defaultBranch, cloneUrl, isAC }
}

export async function repoAction({ params, request }) {
	const formData = await request.formData()
	const method = request.method
	const token = localStorage.getItem('token')

	if (method === 'POST') {
		const repoName = formData.get('repName')
		const desc = formData.get('repDesc')
		const state = formData.get('repType') === 'PUBLIC' ? 'PUBLIC' : 'PRIVATE'
		try {
			await createRepository({ repoName, desc, state, token })
			return { err: '' }
		} catch (err) {
			return { err: err.message }
		}
	} else {
		return { err: 'Method not supported' }
	}
}


export async function starAction({ params, request }) {
	const token = localStorage.getItem('token')

	const formData = await request.formData()
	const isStarred = formData.get('isStarred')
	const repoId = formData.get('repoId')

	const valid = repoId && isStarred
	if (!valid) {
		throw new Error('Missing parameters')
	}

	if (isStarred === "1") {
		// undo star
		return undoStarRepo({ token, repoId })
	} else {
		return starRepo({ token, repoId })
	}
}

export async function deleteRepoAction({ request }) {
	const formData = await request.formData()
	const repoId = formData.get('repoId')
	const token = localStorage.getItem('token')
	try {
		await deleteRepository({ repoId, token })
		return redirect('/')
	} catch (err) {
		return { err: err.message, type: 'delete' }
	}
}

export async function fileContentLoader({ request, params }) {
	const token = localStorage.getItem('token')
	const searchParams = new URLSearchParams(new URL(request.url).search)
	const fileName = searchParams.get('l')
	const dir = params["*"]
	const { branchId, userName, repoName } = params
	if (fileName) {
		const { data: repoId } = await getRepoIdByUserNameAndRepoName({
			userName, repoName, token
		})
		try {
			const { data: file } = await getFileContent({
				commitId: branchId,
				token,
				repoId,
				filePath: path.join(dir, fileName)
			})
			return { file }
		} catch (err) {
			return { file: { err: err.message } }
		}
	}
	
}

export async function forkAction({ request, params }) {
	const formData = await request.formData()
	const token = localStorage.getItem('token')
	const repoId = formData.get('repoId')
	const newRepoName = formData.get('newRepoName')
	const desc = formData.get('desc')
	const myName = localStorage.getItem('userName')
	try {
		await forkRepo({ desc, newRepoName, repoId, token })
		return redirect(`/${myName}/${newRepoName}`)
	} catch (err) {
		return { err: err.message }
	}
}

export async function uploadAction({ request, params }) {
	const formData = await request.formData()
	const token = localStorage.getItem('token')
	const repoId = formData.get('repoId')
	const dir = formData.get('dir')
	const {branchId, userName, repoName} = params
	const file = formData.get('file')
	const commitMsg = formData.get('commitMsg')
	try {
		await uploadFile({ token, branchId, commitMsg, repoId, dir, file })
		return redirect(`/${path.join(userName, repoName, 'source', branchId, dir)}`)
	} catch (err) {
		return { err: err.message }
	}
}

export async function deleteAction({ request, params }) {
	const token = localStorage.getItem('token')
	const formData = await request.formData()
	const repoId = formData.get('repoId')
	const dir = formData.get('dir')
	const {branchId, userName, repoName} = params	
	const commitMsg = formData.get('commitMsg')
	const searchParams = new URLSearchParams(new URL(request.url).search)
	const fileName = searchParams.get('l')

	try {
		await deleteFile({
			token,
			branchId,
			commitMsg,
			repoId,
			path: path.join(dir, fileName)
		})
		return redirect(`/${path.join(userName, repoName, 'source', branchId, dir)}`)	
	} catch (err) {
		return { err: err.message }
	}
}

export async function accessAction({ request, params }) {

	const token = localStorage.getItem('token')
	const formData = await request.formData()
	const repoId = formData.get('repoId')
	const userName  = formData.get('userName')
	const isInvite = formData.get('invite') === 'invite'

	try {
		if (isInvite) {
			await invite({ token, userName, repoId })
		} else {
			await unInvite({ token, userName, repoId })
		}
	} catch (err) {
		return { err: err.message, type: 'access' }
	}
}

export async function detailAction({ request, params }) {
	const token = localStorage.getItem('token')
	const formData = await request.formData()
	const repoId = formData.get('repoId')
	const desc  = formData.get('desc')

	try {
		await updateRepoDesc({ token, desc, repoId })
	} catch (err) {
		return { err: err.message, type: 'detail' }
	}	
}

export async function watchAction({ request, params }) {
	const token = localStorage.getItem('token')
	const formData = await request.formData()
	const repoId = formData.get('repoId')
	const isWatching = formData.get('isWatching') === "1"
	try {
		if (isWatching) await removeWatch({ token, repoId })
		else await addWatch({ token, repoId })
	} catch (err) {
		console.log(err)
		return { err: err.message }
	}
}