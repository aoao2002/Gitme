import dayjs from "dayjs"
import relativeTime from "dayjs/plugin/relativeTime"
import { useState } from "react"
import { Link, useLoaderData, useParams, useRouteLoaderData } from "react-router-dom"
import BranchSelector from "../../components/BranchSelector"
import Button from "../../components/Button"
dayjs.extend(relativeTime)


// branchName
// : 
// "master"
// changedFile
// : 
// ['add 0 files : , modify 0 files : , delete 1 files :  /dev/null']
// commit
// : 
// "Delete webtalk.log"
// hash
// : 
// "2a4ef809bafbaa333cb5f89f46ff448a4448f2e6"
// hashCode
// : 
// -1157912013
// parentHash
// : 
// "15410f37b7732653a3e3e323d156b654e3fa3f7f"
// repoID
// : 
// 21
// submitDate
// : 
// "2022-11-25 19:22:33"

export default function RepoCommits() {
	const params = useParams()
	const { branchId, repoName, userName } = params
	let dir = (params["*"] || '').replace(/\/+$/, '')
	const { repoId, repoInfo, branches } = useRouteLoaderData("repoRoot")
	const { commits: allCommits } = useLoaderData()

	const [ firstIdxInList, setFirstIdxInList ] = useState(0)
	const listSize = 15
	const newerDisabled = (firstIdxInList === 0)
	const olderDisabled = (firstIdxInList + listSize) >= allCommits.length

	const commits = allCommits.slice(firstIdxInList, firstIdxInList + listSize)

	return <>
		<div className="mb-6">
			<BranchSelector sector="commits/branches" repoName={repoName} userName={userName} dir={dir} branches={branches} currentBranchId={branchId}/>
		</div>
		<table className="w-full text-gray-700 table-fixed">
			<thead className="text-left border-b-2 border-gray-300 font-bold text-gray-600">
				<tr>
					<th className="py-1 pl-1 hidden md:table-cell" style={{width: '200px'}}>Author</th>
					<th className="py-1 pl-1 hidden md:table-cell" style={{width: '100px'}}>Commit</th>
					<th className="py-1 pl-1 hidden md:table-cell">Message</th>
					<th className="py-1 pl-1 hidden md:table-cell" style={{width: '200px'}}>Date</th>
					<th className="py-1 pl-1 md:hidden">Summary</th>
				</tr>
			</thead>
			<tbody className="border-b-2 border-gray-300">
				{commits.map(({hash, commit: message, submitDate: date}) => <tr className="hover:bg-gray-100" key={hash}>
					<td className="py-3 pl-1 hidden md:table-cell">Anonymous</td>
					<td className="py-3 pl-1 hidden md:table-cell">
						<Link to={`/${userName}/${repoName}/commits/${hash}`} className="text-blue-600 hover:underline">
							{hash.substring(0, 7)}
						</Link>
					</td>
					<td className="py-3 pl-1 hidden md:table-cell truncate">
						{message}
					</td>
					<td className="py-3 pl-1 hidden md:table-cell">
						{dayjs(date).fromNow()}
					</td>
					<td className="py-3 pl-1 md:hidden table-cell">
						<p className="pb-1 truncate">{message}</p>
						<p className="text-xs text-gray-600">
							Anonymous-
							<Link to={`/${userName}/${repoName}/commits/${branchId}/${hash}`} className="text-blue-600 hover:underline">
								{hash.substring(0, 7)}
							</Link>, {dayjs(date).fromNow()}
						</p>
					</td>
				</tr>)}
			</tbody>
			
		</table>
		<div className="mt-6 flex justify-center">
			<Button className="rounded-r-none" variant="light" disabled={newerDisabled} onClick={() => setFirstIdxInList(idx => idx - listSize)}>Newer</Button>
			<Button className="rounded-l-none" variant="older" disabled={olderDisabled} onClick={() => setFirstIdxInList(idx => idx + listSize)}>Older</Button>
		</div>
	</>
}