import { faCodeMerge, faCodePullRequest } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import dayjs from "dayjs"
import { Form, Link, useActionData, useLoaderData, useNavigation, useParams, useRouteLoaderData } from "react-router-dom"
import Button, { LoadingButton } from "../../components/Button"
import UserLink from "../../components/UserLink"
import { Diff2HtmlUI } from 'diff2html/lib/ui/js/diff2html-ui'
import 'diff2html/bundles/css/diff2html.min.css'
import { createRef, useEffect, useState } from 'react'
import { parse } from 'diff2html'
import Alert from "../../components/Alert"
import BranchBadge from "../../components/BranchBadge"

const prState = {
	UNPROCESS: 'UNPROCESS',
	ACCEPT: 'ACCEPT',
	REJECT: 'REJECT',
}

export default function RepoPull () {
	const { pr } = useLoaderData()
	const { userName, repoName } = useParams()
	// const isMerged = pr.state === 'ACCEPT'
	const { isAC } = useRouteLoaderData("repoRoot")
	const navigation = useNavigation()
	const err = useActionData()?.err

	const ref = createRef()
	const configuration = {
		drawFileList: true,
		fileListToggle: false,
	}
	const [diffSum, setDiffSum] = useState({})

	useEffect(() => {
		const diffJson = parse(pr.diff || '')	
		//console.log(diffJson)
		setDiffSum({
			changedFiles: diffJson.length,
			additions: diffJson?.map(d => d.addedLines).reduce((pv, cv) => pv + cv),
			deletions: diffJson.map(d => d.deletedLines).reduce((pv, cv) => pv + cv)
		})
		const diff2htmlUi = new Diff2HtmlUI(ref.current, diffJson, configuration)
		diff2htmlUi.draw()
		diff2htmlUi.highlightCode()
	}, [])


	return <>
	<div className="pb-3 border-b border-gray-300">
		<h1 className="text-3xl py-3">
			{pr.title} <span className="text-gray-600">#{pr.id}</span>
		</h1>
		
		<div className="flex space-x-3 items-center text-gray-500">
			{(pr.state === prState.UNPROCESS) ? 
			<div className="rounded-full py-1.5 px-3 bg-green-600 text-white flex items-center">
				<FontAwesomeIcon icon={faCodePullRequest} className="w-4 h-4 mr-1.5"/>Open
			</div> : (pr.state === prState.ACCEPT) ?
			<div className="rounded-full py-1.5 px-3 bg-purple-600 text-white flex items-center">
				<FontAwesomeIcon icon={faCodeMerge} className="w-4 h-4 mr-1.5"/>Merged
			</div> : 
			<div className="rounded-full py-1.5 px-3 bg-red-600 text-white flex items-center">
				<FontAwesomeIcon icon={faCodePullRequest} className="w-4 h-4 mr-1.5"/>Rejected
			</div>}
			<div>
				<strong>
					<UserLink userName={pr.userName}/>
				</strong> wants to merge commits into <BranchBadge userName={userName} repoName={repoName} branchId={pr.fromBranch}/> from <BranchBadge userName={pr.userName} repoName={3} branchId={pr.toBranch}/>  
			</div>
			<div style={{marginLeft: 'auto'}} className="hidden lg:block">
				Created {dayjs(pr.date).fromNow()}
			</div>
		</div>
		<div className="text-gray-500 mt-2 lg:hidden">
			Created {dayjs(pr.date).fromNow()}
		</div>
	</div>
	{navigation.state === 'idle' && err && <div className="mt-2"><Alert variant="red">{err}</Alert></div>}
	<p className='mt-3 mb-5 font-light text-gray-600'>
		Showing <strong>{diffSum.changedFiles} changed files</strong> with <strong>{diffSum.additions} additions</strong> and <strong>{diffSum.deletions} deletions</strong>.
	</p>
	<div ref={ref}></div>
	{isAC && pr.state === prState.UNPROCESS && <div className="space-x-2 mt-2">
		<Form method="post" id="formAccept" className="hidden">
			<input type="hidden" name="act" value="accept"/>
			<input type="hidden" name="prId" value={pr.id}/>
		</Form>
		<Form method="post" id="formReject" className="hidden">
			<input type="hidden" name="act" value="reject"/>
			<input type="hidden" name="prId" value={pr.id}/>
		</Form>
		<LoadingButton variant="green" type="submit" form="formAccept">Accept</LoadingButton>
		<LoadingButton variant="red" type="submit" form="formReject">Reject</LoadingButton>
	</div>}
	</>
}