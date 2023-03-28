import { faCheckCircle, faCircleDot } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import './style.css'
import MarkdownViewer from "../../components/MarkdownViewer";
import AdvancedDialog from "../../components/AdvancedDialog";
import Dialog from "../../components/Dialog";
import Tabb from "../../components/Tabb";
import Tab from "../../components/Tab";
import Textarea from "../../components/Textarea";
import { useState } from "react";
import Button from "../../components/Button";
import { Form, useActionData, useLoaderData, useNavigation, useRouteLoaderData } from "react-router-dom";
import dayjs from "dayjs";
import UserLink from "../../components/UserLink";
import Alert from "../../components/Alert";

export default function RepoIssue() {

	const [md, setMd] = useState('')
	const { issueInfo, comments } = useLoaderData()
	const navigation = useNavigation()
	const err = useActionData()?.err
	const { isAC } = useRouteLoaderData("repoRoot")
	
	const myName = localStorage.getItem('userName')

	return <>
		<div className="pb-3 border-b border-gray-300">
			<h1 className="text-3xl py-3">
				{issueInfo.title} <span className="text-gray-600">#{issueInfo.idWithinRepo}</span>
			</h1>
			<div className="flex space-x-3 items-center text-gray-500">
				{issueInfo.state ? <div className="rounded-full py-1.5 px-3 bg-green-600 text-white flex items-center">
					<FontAwesomeIcon icon={faCircleDot} className="w-4 h-4 mr-1.5"/>Open
				</div> : <div className="rounded-full py-1.5 px-3 bg-purple-600 text-white flex items-center">
					<FontAwesomeIcon icon={faCheckCircle} className="w-4 h-4 mr-1.5"/>Closed
				</div>}
				<div>
					<strong><UserLink userName={issueInfo.authorName}/></strong> opened this issue {dayjs(issueInfo.date).fromNow()}
				</div>
				<div>{issueInfo.commentNum} comments</div>
			</div>
		</div>
		<div className="py-4 space-y-4">
			{comments.map(({ content, date, id, authorName }) => <AdvancedDialog
				key={id}
				author={authorName}
				date={date}
			>
				<MarkdownViewer className="p-5 w-full" md={content}/>
			</AdvancedDialog>)}
			<Form method="patch" id="formState">
				<input type="hidden" name="repoId" value={issueInfo.repoID}/>
				<input type="hidden" name="issueId" value={issueInfo.idWithinRepo}/>
				<input type="hidden" name="isOpen" value={issueInfo.state}/>
			</Form>
			<Dialog userName={myName}>
				{issueInfo.state ?
				<Tabb.Group>
					<Tabb.List className="bg-gray-100">
						<Tabb>Write</Tabb>
						<Tabb>Preview</Tabb>
					</Tabb.List>
					<Tabb.Panels>
						<Tabb.Panel>
							{(err && navigation.state === 'idle') && <Alert variant="red">{err}</Alert>}
							<Form method="post" className="space-y-2" id="formCont">
								<Textarea style={{height: '200px'}} placeholder="Styling with Markdown is supported"
									onChange={(e) => setMd(e.target.value)}
									value={md}
									name="content"
								/>
								<input type="hidden" name="issueTrueId" value={issueInfo.id}/>
							</Form>
							<div className="flex justify-end space-x-2 mt-2">
								{isAC && <Button variant="purple" type="submit" form="formState">Close issue</Button>}
								<Button variant="blue" type="submit" form="formCont">Comment</Button>
							</div>
						</Tabb.Panel>
						<Tabb.Panel>
							<div className="px-3 py-1.5">
								<MarkdownViewer md={md}/>
							</div>
						</Tabb.Panel>
					</Tabb.Panels>
				</Tabb.Group> :
				<div className="p-2 space-y-2">
					<Alert variant="yellow">This issue is closed.</Alert>
					<div className="text-right">
						{isAC && <Button variant="green" type="submit" form="formState">Reopen issue</Button>}
					</div>
				</div>
				}
			</Dialog>

		</div>
	</>
}