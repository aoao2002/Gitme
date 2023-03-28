import { useState } from "react";
import { Form, Link, useActionData, useFetcher, useParams, useRouteLoaderData } from "react-router-dom";
import Alert from "../../components/Alert";
import Button, { LoadingButton } from "../../components/Button";
import Input from "../../components/Input";
import Modal from "../../components/Modal";
import Avatar from "../../components/Avatar"
import Textarea from "../../components/Textarea";

export default function Settings() {
	const { repoInfo, repoId } = useRouteLoaderData("repoRoot")
	const { userName } = useParams()
	const fetcher = useFetcher()
	const msg = fetcher.data
	const [currentModal, setCurrentModal] = useState('')

	return <div className="space-y-6">
		<div>
			<div className="py-3 text-3xl">General</div>
			<div className="space-y-3">

				{/* <div>
					<label className="font-bold mb-2 block">Repository name</label>
					<div className="flex">
						<Input className="flex-1 mr-2"/>
						<Button variant="light">Rename</Button>
					</div>
				</div> */}

				<div className="border border-gray-300 rounded flex flex-col divide-y divide-gray-300">
	
					<div className="py-4 px-5 flex justify-between items-center">
						<div className="flex flex-col justify-between">
							<div className="font-bold mb-1">Edit repository details</div>
							<div className="text-sm">Update description.</div>
						</div>
						<div>
							<Button variant="light" onClick={() => setCurrentModal('detail')}>Edit</Button>
						</div>
					</div>

					<Modal title="Edit repository details" 
						show={currentModal === 'detail'} 
						showSubmitBtn={false}
						handleClose={() => setCurrentModal('')}
						showCloseBtn={fetcher.state === 'idle'}
					>
						{msg?.type === 'detail' && fetcher.state === 'idle' && <Alert className="mb-2" variant="red">{msg.err}</Alert>}
						<div className="mb-3">
							<fetcher.Form action="/_repo.detail" method="post">
								<div className="mb-2">
									<label className="mb-2 font-bold text-sm">Description</label>
									<Textarea name="desc" style={{height: '100px'}}/>
								</div>
								<input type="hidden" name="repoId" value={repoId}/>
								<LoadingButton loading={fetcher.state !== 'idle'} type="submit" variant="blue" className="w-full">Update</LoadingButton>
							</fetcher.Form>
						</div>
					</Modal>

					<div className="py-4 px-5 flex justify-between items-center">
						<div className="flex flex-col justify-between">
							<div className="font-bold mb-1">Manage access</div>
							<div className="text-sm">{(repoInfo.collaborators.length === 0) ? 'Only you have' : `${repoInfo.collaborators.length} users`} access to this repository.</div>
						</div>
						<div>
							<Button variant="light" onClick={() => setCurrentModal('access')}>Manage people</Button>
						</div>
					</div>

					<Modal title="Manage access" 
						show={currentModal === 'access'} 
						showSubmitBtn={false}
						handleClose={() => setCurrentModal('')}
						showCloseBtn={fetcher.state === 'idle'}
					>
						{msg?.type === 'access' && fetcher.state === 'idle' && <Alert className="mb-2" variant="red">{msg.err}</Alert>}
						<div className="mb-3">
							<fetcher.Form className="flex" action="/_repo.access" method="post">
								<Input placeholder="Enter username" className="flex-grow mr-2" name="userName"/>
								<input type="hidden" name="repoId" value={repoId} />
								<input type="hidden" name="invite" value="invite"/>
								<LoadingButton loading={fetcher.state !== 'idle'}>Add</LoadingButton>
							</fetcher.Form>
						</div>
						{(repoInfo.collaborators.length !== 0) && <div className="border-y border-gray-300 divide-y divide-gray-300">
							{repoInfo.collaborators.map(c => <div className="py-4 flex">
								<div className="w-12 h-12 rounded-full overflow-hidden">
									<Avatar userName={c}/>
								</div>
								<div className="flex flex-col justify-between ml-3">
									<Link className="text-blue-600 hover:underline font-bold" to={`/${c}`} target="_blank">{c}</Link>
									<div className="text-gray-500 text-sm">{c === userName ? 'Owner' : 'Collaborator'}</div>
								</div>
								<fetcher.Form className="ml-auto flex items-center" action="/_repo.access" method="post">
									<input type="hidden" name="userName" value={userName}/>
									<input type="hidden" name="repoId" value={repoId} />
									<input type="hidden" name="invite" value="uninvite"/>
									<LoadingButton type="submit" variant="red" loading={fetcher.state !== 'idle'}>Remove</LoadingButton>
								</fetcher.Form>
							</div>)}
						</div>}
					</Modal>

				</div>

			</div>
		</div>
		<div>
			<div className="py-3 text-3xl">Danger Zone</div>
			<div className="border border-red-300 rounded flex flex-col divide-y divide-gray-300">

				{/* <div className="py-4 px-5 flex justify-between items-center">
					<div className="flex flex-col justify-between">
						<div className="font-bold mb-1">Change repository visibility</div>
						<div className="text-sm">This repository is currently private.</div>
					</div>
					<div>
						<Button variant="red">Change visibility</Button>
					</div>
				</div> */}


				<div className="py-4 px-5 flex justify-between items-center">
					<div className="flex flex-col justify-between">
						<div className="font-bold mb-1">Delete this repository</div>
						<div className="text-sm">Once you delete a repository, there is no going back. Please be certain.</div>
					</div>
					<div>
						<Button variant="red" onClick={() => setCurrentModal('delete')}>Delete this repository</Button>
					</div>
				</div>

				<Modal title="Are you sure to delete this repository?" 
					show={currentModal === 'delete'} 
					showSubmitBtn={false}
					handleClose={() => setCurrentModal('')}
					showCloseBtn={fetcher.state === 'idle'}
				>
					{msg?.type === 'delete' && fetcher.state === 'idle' && <Alert className="mb-2" variant="red">{msg.err}</Alert>}
					<fetcher.Form action="/_repo.delete" method="post">
						<input type="hidden" value={repoInfo.id} name="repoId"/>
						<LoadingButton variant="red" className="w-full" loading={fetcher.state !== 'idle'}>Yes, I'm sure</LoadingButton>
					</fetcher.Form>
				</Modal>

			</div>
		</div>
	</div>
}