import * as path from "./path"

const API_URL = 'http://localhost:8080'
const GIT_SERVER = 'http://0.0.0.0'

const fcJSON = (...args) => fetch(...args).then(async response => {
	const data = await response.json()
	const { code, msg = 'Unknown error' } = data
	if (code === 200 && response.ok) {
		return data
	} else {
		throw new Error(msg)
	}
})

const fcJSONAuth = (url, token, conf = {}) => {
	let headers = conf.headers || {}
	headers = {
		...headers,
		'satoken': token,
	}
	return fcJSON(url, {
		...conf,
		headers
	})
} 


export function loginByEmail({
	email,
	pwd
}) {
	return fcJSON(`${API_URL}/user/doLoginEmail`, {
		method: 'POST',
		body: JSON.stringify({ email, pwd }),
		headers: {
			'content-type': 'application/json'
		}
	})
		.then(data => {
			const { tokenValue, tokenTimeout, userName } = data.data
			return { email, tokenTimeout, tokenValue, userName }
		})
}

export function register({
	email,
	pwd,
	username,
}) {
	return fcJSON(API_URL + '/user/register', {
		method: 'POST',
		body: JSON.stringify({ email, pwd, name: username }),
		headers: {
			'content-type': 'application/json'
		}
	}).then(data => {
		return true
	})
}

export function getUserInfo({ name, mail, token }) {
	if (mail)
		return fcJSONAuth(API_URL + '/user/getUserInfo?' + `mail=${mail}`, token)
	else 
		return fcJSONAuth(API_URL + '/user/getUserInfo?' + `name=${name}`, token)
}

export function getMyRepos({ token }) {
	return fcJSONAuth(API_URL + '/repo/getRepoByMyself', token)
}

export function getUserRepos({ username, token }) {
	return fcJSONAuth(API_URL + '/repo/getRepoByUserName?' + `userName=${username}`, token)
}

export function editMyProfile({ token, bio, phone }) {
	return fcJSONAuth(API_URL + '/user/editMyInfo?' + new URLSearchParams({
		bio,
		phoneNumber: phone,
		sex: "8"
	}), token, {
		method: "POST"
	})
}

export function getRepoIdByUserNameAndRepoName({ userName, repoName, token }) {
	return fcJSONAuth(API_URL + '/repo/getRepoIDByCreator_NameAndRepo_Name?'
		+ new URLSearchParams({ cName: userName, rName: repoName })
	, token)
}

export function getAllBranches({ repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/view/getAllBranchByRepoID?repoID=' + repoId, token)
}

export function getDirStructure({ commitId, dir, repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/view/ls?'
		+ new URLSearchParams({ commitID: commitId, dir, repoID: repoId })
	, token)
}

export function getRepoInfo({ repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/view/getRepoByRepoID?repoID=' + repoId, token)
}

export function getDir({ repoId, commitId, dir, token }) {
	return fcJSONAuth(API_URL + '/repo/view/ls?'
	 + new URLSearchParams({ commitID: commitId, dir, repoID: repoId }), token)
}

export function getFile({ repoId, commitId, filePath, token }) {
	return fcJSONAuth(API_URL + '/repo/view/getFileByPath?'
	 + new URLSearchParams({ branchName: commitId, filePath, repoID: repoId }), token)
}

export function createRepository({ repoName, state, desc, token }) {
	return fcJSONAuth(API_URL + '/repo/iniRepo?'
	 + new URLSearchParams({ repoName, state, desc }), token, {
		method: 'POST'
	 })
}

export function getIssues({ repoId, token }) {
	return fcJSONAuth(API_URL + '/issue/getAllIssueByRepoID?repoID=' + repoId, token)
}

export function getCloneUrl({ repoName, userName, token }) {
	return getUserInfo({ name: userName, token }).then(({ data: {mail} }) => mail )
		.then(mail => path.join(GIT_SERVER, 'repo', mail, repoName))
}

export function createIssue({ repoId, title, body, token }) {
	// return fcJSONAuth(API_URL + '/issue/addIssue?'
	// 	+ new URLSearchParams({ repoID: repoId, title }), token, {
	// 		method: 'POST',
	// 		body: body
	// 	})
	return fcJSONAuth(API_URL + '/issue/addIssue?', token, {
			method: 'POST',
			headers: {
				'Content-type': 'application/json',
			},
			body: JSON.stringify({
				repoID: repoId,
				issue: title,
				comment: body,
			})
		})
}

export function getComments({ issueId, token }) {
	return fcJSONAuth(API_URL + '/comment/getCommentByIssueID?issueID' + issueId, token)
}

export function getStarredRepos({ token }) {
	return fcJSONAuth(API_URL + '/repo/getRepoByMyStar', token)
}

export function starRepo({ token, repoId }) {
	return fcJSONAuth(API_URL + '/star/addStar?repoID=' + repoId, token, {
		method: 'POST',
	})
}
export function undoStarRepo({ token, repoId }) {
	return fcJSONAuth(API_URL + '/star/deleteStarsByRepo?repoID=' + repoId, token, {
		method: 'DELETE',
	})
}

export function getIssueInfo({ token, issueId, repoId }) {
	return fcJSONAuth(API_URL + '/issue/findByRepoIDAndIdWithinRepo?'
		+ new URLSearchParams({ issueId, repoID: repoId }), token)
}

export function getIssueCommentsByIssueTrueId({ token, issueTrueId }) {
	return fcJSONAuth(API_URL + '/comment/getCommentByIssueID?issueID=' + issueTrueId, token)
}

export function createComment({ token, content, issueTrueId }) {
	return fcJSONAuth(API_URL + '/comment/add', token, {
		method: 'POST',
		headers: {
			'content-type': 'application/json'
		},
		body: JSON.stringify({
			content,
			issueID: issueTrueId,
		})
	})
}


export function openIssue({ token, repoId, issueId }) {
	return fcJSONAuth(API_URL + '/issue/edit/openIssue?' 
		+ new URLSearchParams({ idWithin: issueId, repoID: repoId }), token, {
			method: 'POST'
		})
}

export function closeIssue({ token, repoId, issueId }) {
	return fcJSONAuth(API_URL + '/issue/edit/closeIssue?' 
		+ new URLSearchParams({ idWithin: issueId, repoID: repoId }), token, {
			method: 'POST'
		})
}

export function deleteRepository({ repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/edit/deleteRepo?repoID=' + repoId, token, {
		method: 'POST'
	})
}
export function updateRepoDesc({ desc, repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/edit/updateDescriById?'
		+ new URLSearchParams({ descri: desc, repoID: repoId })
	, token, {
		method: 'POST'
	})
}

export function getFileContent({ commitId, filePath, repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/view/getFileByPath?'
		+ new URLSearchParams({ filePath, branchName: commitId, repoID: repoId }), token)
}

export function forkRepo({ desc, newRepoName, repoId, token }) {
	return fcJSONAuth(API_URL + '/fork/edit/fork?'
		+ new URLSearchParams({ desc, rename: newRepoName, repoID: repoId }), token, {
			method: 'POST'
		})
}

export async function getPullRequests({ repoId, token }) {
	return fcJSONAuth(API_URL + '/PR/view/getAllPR?repoID=' + repoId, token)
}

export async function uploadFile({ token, branchId, commitMsg, dir, repoId, file }) {
	const requestData = new FormData()
	requestData.append('uploadFile', file)

	return fcJSONAuth(API_URL + '/repo/edit/upFile?'
		+ new URLSearchParams({ 
			branchName: branchId, 
			commitMsg, 
			path: path.join(dir, file.name), 
			repoID: repoId,
		}), 
	token, {
		method: 'POST',
		body: requestData
	})
}

export async function deleteFile({ token, branchId, commitMsg, path, repoId }) {
	return fcJSONAuth(API_URL + '/repo/edit/deleFile?' 
		+ new URLSearchParams({ branchName: branchId, commitMsg, path, repoID: repoId }),
		token, {
			method: 'POST'
		})
}

export async function getCommits({ branchId, repoId, token }) {
	return fcJSONAuth(API_URL + '/repo/view/getAllVersionByRepoID?'
	 + new URLSearchParams({ branchName: branchId, repoID: repoId }), token)
}

export async function getCommit({ repoId, commitId, token }) {
	return fcJSONAuth(API_URL + '/repo/getVersionByRepoID?'
		+ new URLSearchParams({ commitID: commitId, repoID: repoId }), token)
}

export function getRepoDownloadUrl({ repoId, branchId, token }) {
	return API_URL + '/repo/downloadRepo?'
		+ new URLSearchParams({ branch: branchId, repoID: repoId, tokenValue: token })
}

export function invite({ token, userName, repoId }) {
	return fcJSONAuth(API_URL + '/user/edit/invite?'
		+ new URLSearchParams({ repoID: repoId, userName: userName }), token, {
			method: 'post'
		})
}

export function unInvite({ token, userName, repoId }) {
	return fcJSONAuth(API_URL + '/user/edit/unInvite?'
		+ new URLSearchParams({ repoID: repoId, userName: userName }), token, {
			method: 'post'
		})
}

export async function search({ token, keyword }) {
	return fcJSONAuth(API_URL + '/repo/FuzzySearch?'
		+ new URLSearchParams({ keyword }), token)
}

export async function diff({ token, branchId0, branchId1, repoId }) {
	return fcJSONAuth(API_URL + '/repo/view/getVersionDiff?'
		+ new URLSearchParams({
			branchName1: branchId0,
			branchName2: branchId1,
			repoID: repoId
		}), token, {
			method: 'POST'
		})
}

export async function getPullRequestById({ token,  prId }) {
	return fcJSONAuth(API_URL + '/PR/view/getPRByID?'
		+ new URLSearchParams({
			PRID: prId
		}), token)
} 

export async function getUnreadNotifications({ token }) {
	return fcJSONAuth(API_URL + '/watch/getWatchInfoByMe', token)
}

export async function setNotificationRead({ token, id }) {
	return fcJSONAuth(API_URL + '/watch/SetRead?WatchInfoID=' + id, token)
}

export async function addWatch({ token, repoId }) {
	return fcJSONAuth(API_URL + '/watch/addWatch?repoID=' + repoId, token, {
		method: 'POST'
	})
}

export async function removeWatch({ token, repoId }) {
	return fcJSONAuth(API_URL + '/watch/deleWatch?repoID=' + repoId, token, {
		method: 'POST'
	})
}

export async function createPullRequest({ token, repoId, title, toBranch, fromBranch }) {
	return fcJSONAuth(API_URL + '/PR/view/makePR?'
		+ new URLSearchParams({ repoID: repoId, title, toBranch, fromBranch }), token, {
			method: 'POST'
		})
}

export function acceptPullRequest({ token, prId }) {
	return fcJSONAuth(API_URL + '/PR/edit/acceptPR?'
		+ new URLSearchParams({ PRID: prId }), token, {
			method: 'POST'
		})
}

export function rejectPullRequest({ token, prId }) {
	return fcJSONAuth(API_URL + '/PR/edit/rejectPR?'
		+ new URLSearchParams({ PRRSId: prId }), token, {
			method: 'POST'
		})
}

export function addBranch({ token, branchSource, branchName, repoId }) {
	return fcJSONAuth(API_URL + '/repo/edit/addBranch?'
		+ new URLSearchParams({
			branchName,
			fromBranchName: branchSource,
			repoID: repoId
		}), token, {
			method: 'POST'
		})
}