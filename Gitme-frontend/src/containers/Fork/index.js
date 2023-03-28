import { Form, useActionData, useNavigation, useParams, useRouteLoaderData } from "react-router-dom"
import Alert from "../../components/Alert"
import { LoadingButton } from "../../components/Button"
import Input from "../../components/Input"

export default function Fork() {
	const { repoId } = useRouteLoaderData("repoRoot")
	const { repoName } = useParams()
	const navigation = useNavigation()
	const err = useActionData()?.err

	return <>
		<div className="pt-3 pb-6 border-b border-gray-300">
			<div className="text-2xl mb-2">Create a new fork</div>
			<div className="text-gray-600 text-sm">A fork is a copy of a repository. Forking a repository allows you to freely experiment with changes without affecting the original project.</div>
		</div>
		<Form method="post" id="formFork"></Form>
		<div className="mt-6 space-y-5">
			{navigation.state === 'idle' && err && <Alert variant="red">{err}</Alert>}
			<div className="space-y-2">
				<label htmlFor="newRepoName" className="font-bold">Repository name</label>
				<Input id="newRepoName" defaultValue={repoName} name="newRepoName" required form="formFork"/>
				<p className="text-sm">By default, forks are named the same as their upstream repository. You can customize the name to distinguish it further.</p>
			</div>
			<div className="space-y-2">
				<label htmlFor="desc" className="font-bold">Description <span className="font-normal text-sm text-gray-600">(optional)</span></label>
				<Input id="desc" name="desc" form="formFork"/>
			</div>
			<div>
				<input type="hidden" name="repoId" value={repoId} form="formFork"/>
				<LoadingButton variant="blue" form="formFork" type="submit" loading={navigation.state !== 'idle'}>Create fork</LoadingButton>
			</div>
		</div>
	</>
}