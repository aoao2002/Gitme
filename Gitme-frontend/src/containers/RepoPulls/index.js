import { faCircleCheck, faCircleDot, faCommentDots } from "@fortawesome/free-regular-svg-icons";
import { faCheck, faCodePullRequest, faXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { Link, useLoaderData, useRouteLoaderData } from "react-router-dom";
import Button from "../../components/Button";
import { getIssues } from "../../utils/api";

const prState = {
	UNPROCESS: 'UNPROCESS',
	ACCEPT: 'ACCEPT',
	REJECT: 'REJECT',
}

export default function RepoPulls() {
	const { repoId } = useRouteLoaderData("repoRoot")
	const { prs } = useLoaderData()

	const [isOpenView, setIsOpenView] = useState(true)

	const openPrs = prs.filter(p => p.state === 'UNPROCESS')
	const closedPrs = prs.filter(p => p.state !== 'UNPROCESS') 
	return <>
		<div className="mb-4 flex flex-row-reverse">
			<Button variant="green" className="inline-block" as="a" to="new">New pull request</Button>
		</div>
		<div className="border border-gray-300 rounded overflow-hidden divide-y divide-gray-300">
			<div className="p-4 bg-gray-100 flex items-center text-sm">
				<a className={`cursor-pointer font-bold ${isOpenView ? 'font-bold' : 'text-gray-500'}`} onClick={() => setIsOpenView(true)}>
					<FontAwesomeIcon icon={faCodePullRequest}  className="mr-1.5" size="lg"/> {openPrs.length} Open
				</a>
				<a className={`cursor-pointer ml-6 ${isOpenView ? 'text-gray-500' : 'font-bold'}`} onClick={() => setIsOpenView(false)}>
					<FontAwesomeIcon icon={faCheck} className="mr-1.5" size="lg"/> {closedPrs.length} Closed
				</a>
			</div>
			
			{(isOpenView ? openPrs : closedPrs).map(({
				title,
				id,
				userName,
				state,
				date,
			}) => <div className="py-3 px-4 flex hover:bg-gray-50"
				key={id}
			>
				{(state === prState.UNPROCESS) ? <div className="text-green-600 mr-3 mt-0.5">
					<FontAwesomeIcon icon={faCodePullRequest}/>	
				</div> : (state === prState.ACCEPT) ?
				<div className="text-purple-600 mr-3 mt-0.5">
					<FontAwesomeIcon icon={faCircleCheck}/>	
				</div> :
				<div className="text-red-600 mr-3 mt-0.5">
					<FontAwesomeIcon icon={faXmark}/>	
				</div>}
				
				<div className="flex flex-col justify-between">
					<div>
						<Link to={(id).toString()} className="font-semibold text-lg hover:text-blue-600">
							{title}
						</Link>
					</div>
					<div className="text-sm text-gray-400">
						#{id} created on {dayjs(date).format("MMM D")} by <Link to={`/${userName}`} className="hover:text-blue-600">{userName}</Link>
					</div>
				</div>
				{/* <div className="ml-auto text-gray-600">
					<FontAwesomeIcon icon={faCommentDots} size="lg" className="mt-1 mr-1.5"/>
					{commentNum}
				</div> */}
			</div>)}	
		</div>
	</>
}