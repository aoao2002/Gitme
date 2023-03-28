import { faFile } from "@fortawesome/free-regular-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { useState } from "react"
import { Form, useActionData, useNavigation, useParams, useRouteLoaderData } from "react-router-dom"
import Alert from "../../components/Alert"
import Breadcrumb from "../../components/Breadcrumb"
import { LoadingButton } from "../../components/Button"
import Dialog from "../../components/Dialog"
import Input from "../../components/Input"

export default function UploadFile() {
	const params = useParams()
	const dir = params["*"] || ''
	const { repoName, userName, branchId } = params
	const { repoId } = useRouteLoaderData("repoRoot")
	const baseBranchUrl = `/${userName}/${repoName}/source/${branchId}/`

	const navigation = useNavigation()
	const err = useActionData()?.err
	const loading = navigation.state !== 'idle'

	const [fileToUpload, setFileToUpload] = useState({})
	const myName = localStorage.getItem('userName')


	return <>
		<Breadcrumb dir={dir} repoName={repoName} branchBaseUrl={baseBranchUrl}/>
		<div className="mt-3 border rounded border-gray-300 py-14 text-center space-y-4">
			<div className="text-gray-600">
				<FontAwesomeIcon icon={faFile} size="2xl"/>
			</div>
			<div className="text-xl font-bold">
				{fileToUpload.name ? fileToUpload.name : 'Add a file to your repository'}
			</div>
			<div className="text-gray-600">
				<label className="text-blue-600 hover:underline" htmlFor="formFile">{fileToUpload.name ? 'Select another file' : 'Choose your file'}</label>
				<input required type="file" className="hidden" id="formFile" form="formUpload" name="file" onChange={(e) => {
					setFileToUpload(e.target.files[0])
					// console.log(e.target.files)
				}}/>
			</div>
		</div>
		<Dialog className="mt-4" userName={myName}>
			<div className="p-4">
				<div className="text-xl font-bold mb-4">
					Commit changes
				</div>
				{!loading && err && <Alert variant="red">{err}</Alert>}
				<Form className="space-y-3" id="formUpload" method="post" encType="multipart/form-data">
					<Input defaultValue="Add files via upload" name="commitMsg" required/>
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