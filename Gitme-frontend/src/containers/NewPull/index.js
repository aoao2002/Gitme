import { faCodePullRequest, faLeftLong } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { useState } from "react"
import { Form, useActionData, useLoaderData, useNavigate, useNavigation, useParams, useRouteLoaderData } from "react-router-dom"
import Alert from "../../components/Alert"
import BranchSelector from "../../components/BranchSelector"
import { LoadingButton } from "../../components/Button"
import Input from "../../components/Input"

export default function  NewPull() {
	const err = useActionData()?.err
	const navigation = useNavigation()
	const { userName, repoName } = useParams()
	const { branches, defaultBranch, repoInfo, repoId } = useRouteLoaderData("repoRoot")
	const { oriRepoId, oriBranches, oriDefaultBranch } = useLoaderData()

	const [fromBranch, setFromBranch] = useState(oriDefaultBranch?.name)
	const [toBranch, setToBranch] = useState(defaultBranch?.name)

	return <>
	<div className="text-3xl py-4 border-b border-gray-300 mb-3">
		New Pull Request
		<div className="text-sm text-gray-600 mt-2">
			Create a new pull request by comparing changes across two branches.
		</div>
	</div>
	{(err && navigation.state === 'idle') && <Alert variant="red">{err}</Alert>}
	{oriRepoId ? <div className="space-y-3">
		<Form id="formPull" method="post">
			<input type="hidden" name="fromBranchId" value={fromBranch}/>
			<input type="hidden" name="fromRepoId" value={repoId}/>
			<input type="hidden" name="toBranchId" value={toBranch}/>
			<input type="hidden" name="toRepoId" value={oriRepoId}/>
		</Form>
		<div>
			<Input placeholder="Title" name="title" form="formPull" required/>
		</div>
		<div className="py-3 px-6 space-x-3 flex items-center text-gray-600 border border-gray-300 bg-gray-100 rounded">
			<FontAwesomeIcon icon={faCodePullRequest}/>
			<div className="flex items-center">
				<span className="text-gray-800 font-medium">Base branch</span>
				<BranchSelector 
					branches={oriBranches} 
					currentBranchId={toBranch} 
					userName={repoInfo.forkFromUserName} 
					repoName={repoInfo.forkFromRepoName}
					handleChange={({branchId}) => setToBranch(branchId)}
				/>
			</div>
			<FontAwesomeIcon icon={faLeftLong}/>
			<div className="flex items-center">
				<span className="text-gray-800 font-medium">Your branch</span>
				<BranchSelector 
					branches={branches} 
					currentBranchId={fromBranch}
					userName={userName} 
					repoName={repoName}
					handleChange={({branchId}) => setFromBranch(branchId)}	
				/>
			</div>
		</div>
		<div>
			<LoadingButton form="formPull" variant="blue" type="submit" loading={navigation.state !== 'idle'}>Create pull request</LoadingButton>
		</div>
	</div> :
	<Alert variant="yellow">You must fork a repository before you create a new pull request.</Alert>}
	
	</>
}