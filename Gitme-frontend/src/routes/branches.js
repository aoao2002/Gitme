import { addBranch } from "../utils/api"

export async function newBranchAction({ params, request }) {
	const formData = await request.formData()
	const repoId = formData.get('repoId')
	const branchSource = formData.get('branchSource')
	const branchName = formData.get('branchName')
	const token = localStorage.getItem('token')
	try {
		await addBranch({ token, branchSource, branchName, repoId })
		return { err: '' }
	} catch (err) {
		return { err: err.message }
	}
}