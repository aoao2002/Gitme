import { Form, useActionData, useFetcher, useRouteLoaderData } from "react-router-dom";
import Input from "../../components/Input";
import { Tab as Ta } from "@headlessui/react";
import Tabb from "../../components/Tabb";
import Textarea from "../../components/Textarea";
import { useState } from "react";
import Button, { LoadingButton } from "../../components/Button";
import MarkdownViewer from "../../components/MarkdownViewer";
import Alert from "../../components/Alert";
import Dialog from "../../components/Dialog";

export default function NewIssue() {
	const [issueBody, setIssueBody] = useState('')
	const fetcher = useFetcher()
	const { repoId } = useRouteLoaderData("repoRoot")
	const err = fetcher.data?.err
	const [ title, setTitle ] = useState('')

	const myName = localStorage.getItem('userName')
	return <>
		<div className="text-3xl py-4 border-gray-300">New Issue</div>
		{(err && fetcher.state === 'idle') && <Alert variant="red">{err}</Alert>}
		<Dialog className="mt-2" userName={myName}>
			<div className="p-2">
				<Input type="text" placeholder="Title" size="lg" required name='title' value={title} onChange={(e) => setTitle(e.target.value)}/>
			</div>
			<Tabb.Group>
				<Tabb.List>
					<Tabb>Write</Tabb>
					<Tabb>Preview</Tabb>
				</Tabb.List>
				<Tabb.Panels>
					<Tabb.Panel>
						<fetcher.Form className="space-y-2" method="post" action={"?repoId=" + repoId}>
							<Textarea name="body" style={{height: '200px'}} placeholder="Styling with Markdown is supported" 
								value={issueBody}
								onChange={(e) => setIssueBody(e.target.value)}
								required
							></Textarea>
							<input type="hidden" value={title} name="title"/>
							<div className="flex justify-end">
								<LoadingButton variant="blue" type="submit" loading={fetcher.state !== 'idle'}>Submit new issue</LoadingButton>
							</div>
						</fetcher.Form>

					</Tabb.Panel>
					<Tabb.Panel>
						<div className="px-3 py-1.5">
							<MarkdownViewer md={issueBody}/>
						</div>
					</Tabb.Panel>
				</Tabb.Panels>
			</Tabb.Group>
		</Dialog>
	</>
}