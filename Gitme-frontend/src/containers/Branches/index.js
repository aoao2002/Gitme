import { useEffect, useState } from "react";
import { Form, useActionData, useNavigation, useParams, useRouteLoaderData } from "react-router-dom";
import Alert from "../../components/Alert";
import Badge from "../../components/Badge";
import BranchBadge from "../../components/BranchBadge";
import BranchSelector from "../../components/BranchSelector";
import Button, { LoadingButton } from "../../components/Button";
import Input from "../../components/Input";
import Modal from "../../components/Modal";

export default function Branches() {
	const { branches, defaultBranch, isAC, repoId } = useRouteLoaderData('repoRoot')
	const { repoName, userName } = useParams()
	const navigation = useNavigation()
	const err = useActionData()?.err
	const [success, setSuccess] = useState(false)
	const [modalShow, setModalShow] = useState(false)
	const [branchSource, setBranchSource] = useState(defaultBranch.name)
	
	useEffect(() => {
		if (navigation.state === 'idle' && (err === ''))
			setSuccess(true) 
	}, [navigation.state, err])

	return <>
		<div className="text-right">
			{isAC && <Button variant="green" onClick={() => setModalShow(true)}>New branch</Button>}
		</div>
		<div className="mt-4 rounded border border-gray-300 divide-y divide-gray-300">
			<div className="px-4 py-2 bg-gray-100 font-bold">All branches</div>
			<div className="px-4 py-2 flex items-center justify-between">
				<div className="flex items-center space-x-6 text-sm">
					<BranchBadge showUserName={false} branchId={defaultBranch.name} repoName={repoName} userName={userName}/>
					<Badge>Default</Badge>
				</div>
				<Button variant="light" as="a" to={`/${userName}/${repoName}/source/${defaultBranch.name}`}>View</Button>
			</div>
			{branches.filter(({ default: dea }) => !dea).map(({ name }) => <div 
				className="px-4 py-2 flex items-center justify-between text-sm"
				key={name}
			>
				<BranchBadge showUserName={false} branchId={name} repoName={repoName} userName={userName}/>
				<Button variant="light" as="a" to={`/${userName}/${repoName}/source/${name}`}>View</Button>
			</div>)}
		</div>
		<Modal title="Create new branch"
			showSubmitBtn={false}
			show={modalShow}
			handleClose={() => {
				setModalShow(false)
				setSuccess(false)
			}}
		>
			{err && navigation.state === 'idle' && <Alert variant='red'>{err}</Alert>}
			<Form method="post" className="space-y-4">
				<div className="space-y-2 font-medium">
					<label htmlFor="branchName">Branch name</label>
					<Input id="branchName" name="branchName" required/>
				</div>
				<div className="space-y-2">
					<label className="font-medium">Branch source</label>
					<BranchSelector 
						handleChange={({branchId}) => setBranchSource(branchId)}
						branches={branches} repoName={repoName} userName={userName} currentBranchId={branchSource}/>
					<input type="hidden" value={branchSource} name="branchSource"/>
					<input type="hidden" value={repoId} name="repoId"/>
				</div>
				<LoadingButton 
					className="w-full"
					loading={navigation.state !== 'idle'} type="submit" 
					variant={success ? "green": "blue"}
					disabled={success}
				>
					{ success ? 'Created' : 'Submit' }
				</LoadingButton>
			</Form>
		</Modal>
		
	</>
}