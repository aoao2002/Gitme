import { Diff2HtmlUI } from 'diff2html/lib/ui/js/diff2html-ui'
import 'diff2html/bundles/css/diff2html.min.css'
import { createRef, useEffect, useState } from 'react'
import { parse } from 'diff2html'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCodeBranch } from '@fortawesome/free-solid-svg-icons'
import { Link, useLoaderData, useParams } from 'react-router-dom'
import Avatar from '../../components/Avatar'
import dayjs from 'dayjs'

export default function RepoCommit() {

	const { userName, repoName } = useParams()
	const { commit, diffPromise } = useLoaderData()

	const ref = createRef()
	const configuration = {
		drawFileList: true,
		fileListToggle: false,
	}
	const [diffSum, setDiffSum] = useState({})

	useEffect(() => {
		if (commit.parentHash.length) {
			diffPromise.then(d => {
				const diffJson = parse(d)	
				setDiffSum({
					changedFiles: diffJson.length,
					additions: diffJson.map(d => d.addedLines).reduce((pv, cv) => pv + cv),
					deletions: diffJson.map(d => d.deletedLines).reduce((pv, cv) => pv + cv)
				})
				const diff2htmlUi = new Diff2HtmlUI(ref.current, diffJson, configuration)
				diff2htmlUi.draw()
				diff2htmlUi.highlightCode()
			})
		}
	}, [])

	return <>
		<div className='overflow-hidden rounded border border-gray-300'>
			<div className='p-3 border-bottom border-gray-600 bg-gray-100'>
				<div className='font-bold text-md mb-2'>{commit.commit}</div>
				<div className='text-sm'>
					<FontAwesomeIcon icon={faCodeBranch}/>
					<span className='ml-2'>{commit.branchName}</span>
				</div>
			</div>
			<div className='p-3 flex space-x-2 text-sm items-center'>
				{/* <div className='rounded-full overflow-hidden w-4 h-4'>
					<Avatar className='w-full' userName='Anonymous'/>
				</div> */}
				<div className='font-bold'>Anonymous</div>
				<div>committed on {dayjs(commit.submitDate).format('MMM D')}</div>
				<div className='text-xs font-mono text-gray-400 hidden sm:block space-x-4' style={{marginLeft: 'auto'}}>
					<span>
						{commit.parentHash.length} parent
						{commit.parentHash.map(p => <Link 
							to={`/${userName}/${repoName}/commits/${p}`} 
							className="hover:text-blue-600 mx-1"
							key={p}
						>
							{p.substr(0, 7)}
						</Link>)}
					</span>
					<span>commit {commit.hash}</span>
				</div>
			</div>
		</div>
		<p className='mt-3 mb-5 font-light text-gray-600'>
			Showing <strong>{diffSum.changedFiles} changed files</strong> with <strong>{diffSum.additions} additions</strong> and <strong>{diffSum.deletions} deletions</strong>.
		</p>
		<div ref={ref}></div>
	</>
}