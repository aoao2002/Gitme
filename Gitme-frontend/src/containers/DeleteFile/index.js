import { faFile } from "@fortawesome/free-regular-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { useState } from "react"
import { Form, useActionData, useNavigation, useParams, useRouteLoaderData, useSearchParams } from "react-router-dom"
import Alert from "../../components/Alert"
import Breadcrumb from "../../components/Breadcrumb"
import { LoadingButton } from "../../components/Button"
import Dialog from "../../components/Dialog"
import Input from "../../components/Input"

export default function DeleteFile() {
	const params = useParams()
	const [searchParams, setSearchParams] = useSearchParams()
	const fileName = searchParams.get('l')
	const dir = params["*"] || ''
	const { repoName, userName, branchId } = params
	const { repoId } = useRouteLoaderData("repoRoot")
	const baseBranchUrl = `/${userName}/${repoName}/source/${branchId}/`

	const navigation = useNavigation()
	const err = useActionData()?.err
	const loading = navigation.state !== 'idle'

	const myName = localStorage.getItem('userName')



	return <>
		<Breadcrumb dir={dir} repoName={repoName} branchBaseUrl={baseBranchUrl}/>
		<div className="mt-3 py-2 text-center space-y-4">
			<Alert variant="yellow">You are deleting '{fileName}'</Alert>
		</div>
		<Dialog className="mt-4" userName={myName}>
			<div className="p-4">
				<div className="text-xl font-bold mb-4">
					Commit changes
				</div>
				{!loading && err && <Alert variant="red">{err}</Alert>}
				<Form className="space-y-3" id="formUpload" method="post">
					<Input defaultValue={`Delete ${fileName}`} name="commitMsg" required/>
					<p className="text-gray-600">You are to commit directly to the <strong className="text-gray-600 bg-blue-100 font-mono px-1 py-0.5 rounded">{branchId}</strong> branch.</p>
					<input type="hidden" value={repoId} name="repoId"/>
					<input type="hidden" value={dir} name="dir"/>
					<div>
						<LoadingButton type="submit" loading={loading} variant="blue">Commit changes</LoadingButton>
					</div>
				</Form>
			</div>
		</Dialog>
	</>
}