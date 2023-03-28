import { faCaretDown, faTerminal, faCopy, faFolder, faFile, faCodeBranch, faCodeFork, faCodePullRequest, faPaperclip, faStar, faWarehouse, faCodeCommit } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Button from "../../components/Button";
import { Popover } from "@headlessui/react";
import remarkGfm from "remark-gfm";
import avatarImg from '../../images/avatar.png'
import ReactMarkdown from "react-markdown";
import { Link, useLoaderData, useParams, useRouteLoaderData, useSearchParams } from "react-router-dom";
import { getDir, getFile, getFileContent, getRepoDownloadUrl } from "../../utils/api";
import { useEffect, useState } from "react";
import dayjs from "dayjs";
import * as path from "../../utils/path";
import Alert from "../../components/Alert";
import Breadcrumb from "../../components/Breadcrumb";
import { faFileZipper } from "@fortawesome/free-regular-svg-icons";
import BranchSelector from "../../components/BranchSelector";
import CodeHighlighter from "../../components/CodeHighlighter";
import Avatar from "../../components/Avatar";

const loadDir = async ({ repoId, commitId, dir }) => {
	const { data: content } = await getDir({ 
		repoId, 
		commitId, 
		dir : (dir.endsWith('/') || dir === '') ? dir : dir + '/', 
		token: localStorage.getItem('token') 
	})
	return content
}

const loadFile = async ({ repoId, commitId, filePath }) => {
	const { data } = getFile({ repoId, commitId, filePath, token: localStorage.getItem('token') })
	return data
}

export default function RepoCode() {
	const params = useParams()
	const { branchId, repoName, userName } = params
	const [ searchParams, setSearchParams ] = useSearchParams()
	const { repoId, repoInfo, branches, cloneUrl, isAC } = useRouteLoaderData("repoRoot")

	const loaderData = useLoaderData()
	const file = loaderData?.file

	const fileName = searchParams.get("l")
	const baseBranchUrl = `/${userName}/${repoName}/source/${branchId}/`

	let dir = (params["*"] || '').replace(/\/+$/, '')
	const isRoot = dir === ''
	const filePath = path.join(dir, fileName)


	const [dirContent, setDirContent] = useState(null)
	const [fileContent, setFileContent] = useState(null)

	const dirExist = dirContent?.length
	const hasREADME = dirContent?.some(({ name }) => name === 'README.md')

	const token = localStorage.getItem('token')

	const [readme, setReadme] = useState('')
	useEffect(() => {
		if (hasREADME) {
			getFileContent({ 
				commitId: branchId,  
				filePath: path.join(dir, 'README.md'),
				repoId,
				token: localStorage.getItem('token')
			}).then(({data : f}) => {
				if (f.text) {
					setReadme(f.content)
				}
			})
		}
	}, [hasREADME])


	useEffect(() => {
		if (fileName) {
			loadFile({ repoId, commitId: branchId, filePath })
				.then(d => setFileContent(d))
		} else {
			loadDir({ repoId, commitId: branchId, dir })
				.then(d => {
					d.sort((a, b) => {
						return (a.type === "Directory" > b.type === "Directory")
					})
					setDirContent(d)
				})
		}
	}, [repoId, branchId, dir, fileName])
	return fileName === null ? (<>
	{/* dir */}
	<div className="flex justify-between items-start">
		<div className="flex items-center space-x-6">
			<BranchSelector repoName={repoName} userName={userName} dir={dir} branches={branches} currentBranchId={branchId}/>
			{isRoot ? <div className="hidden lg:block space-x-4 text-sm text-gray-600">
				<Link className="inline-flex items-center hover:text-blue-600" to={`/${userName}/${repoName}/branches`}>
					<FontAwesomeIcon icon={faCodeBranch}/>
					<span className="px-1 font-medium text-gray-900 hover:text-blue-600">{branches.length}</span>
					{(branches.length === 1) ? 'branch' : 'branches'}
				</Link>
				{/* <div className="inline-flex items-center">
					<FontAwesomeIcon icon={faTag}/>
					<span className="px-1 font-medium text-gray-900">1</span>
					tag
				</div> */}
			</div> : 
			<div>
				<Breadcrumb dir={dir} fileName={fileName} repoName={repoName} branchBaseUrl={baseBranchUrl}/>
			</div>
			}
			
		</div>
		<div className="flex items-center space-x-2">
			{isAC && <Button as="a" to={`/${path.join(userName, repoName, 'upload', branchId, dir)}`}>Upload file</Button>}
			<Popover className="relative">
				<Popover.Button className="hidden md:block bg-green-500 text-white hover:bg-green-600 active:bg-green-700 inline-flex items-center px-5 py-2 rounded-lg text-sm font-medium">
					<span className="mr-2">Clone</span>
					<FontAwesomeIcon icon={faCaretDown}/>
				</Popover.Button>

				<Popover.Panel className="bg-white shadow-md absolute z-10 mt-2 w-64 md:w-80 border border-gray-300 rounded-lg text-sm right-0 divide-y divide-gray-300">
					
					<p className="px-4 py-3 font-medium text-gray-600 flex items-center">
						<FontAwesomeIcon icon={faTerminal} className="mr-2 bg-gray-600 text-white p-px rounded"/>
						Clone with HTTPS
					</p>
					<div className="px-4 py-2 flex">
						<input type="text" disabled className="bg-gray-50 select-all leading-4 w-full border-gray-200 rounded-l-lg text-sm border border-r-0"
						value={cloneUrl}
						/>
						<button className="bg-gray-50 py-2 px-3 border border-gray-200 -m-l-px rounded-r-lg hover:bg-gray-200 active:bg-gray-300"
						onClick={() => navigator.clipboard.writeText({cloneUrl})}
						>
							<FontAwesomeIcon icon={faCopy}/>
						</button>
					</div>
					<a className="block px-4 py-4 hover:bg-gray-100 font-medium cursor-pointer"
						href={getRepoDownloadUrl({ repoId, branchId, token })}
						target="_blank"
					>
						<FontAwesomeIcon icon={faFileZipper} className="mr-2"/>
						<span>Download ZIP</span>
					</a>
				</Popover.Panel>
			</Popover>
		</div>
	</div>
	<div className="flex flex-col md:flex-row mt-6 md:space-x-6">
		<div className="grow space-y-4 min-w-0">
			{dirExist ? <>
				<ul className="border border-gray-300 divide-y divide-gray-300 text-base rounded-lg overflow-hidden">
					{!isRoot && <li className="flex items-center px-4 py-2 hover:bg-gray-100">
						<div className="flex items-center sm:w-1/4 shrink-0 pr-2">
							<Link className="truncate ml-4 hover:text-blue-600 hover:underline"
								to={path.join(baseBranchUrl, path.parent(dir))}
							>. . .</Link>
						</div>
					</li>}
					{dirContent && dirContent.map(({ name, type, time }) => <li 
						className="flex items-center px-4 py-2 hover:bg-gray-100"
						key={name + type}
					>
						<div className="flex items-center sm:w-1/4 shrink-0 pr-2">
							{type === 'Directory' ? <FontAwesomeIcon icon={faFolder} className="text-blue-400 w-4 h-4"/>
																		: <FontAwesomeIcon icon={faFile} className="text-gray-600 w-4 h-4"/>}
							<Link 
								className="truncate ml-4 hover:text-blue-600 hover:underline"
								to={type === 'Directory' ? path.join(baseBranchUrl, dir, name) : `${path.join(baseBranchUrl, dir)}?l=${name}`}
							>{name}</Link>
						</div>
						<p className="grow hidden sm:block text-gray-600 truncate">----{/* TODO: commit message */}</p>
						<p className="text-gray-600 shrink-0 ml-auto pl-3">{dayjs(time).fromNow()}</p>
					</li>)}
				</ul>
				{hasREADME && readme && <div className="border border-gray-300 rounded-lg overflow-hidden">
					<div className="px-4 py-2 bg-gray-100 text-base border-b border-gray-300">
						<FontAwesomeIcon icon={faFile} className="mr-3"/>
						README.md
					</div>
					<article className="p-8 prose max-w-none">
						<ReactMarkdown children={readme} remarkPlugins={[remarkGfm]}/>
					</article>
				</div>}
				</>:
				dirContent === null ? null : <>
					<Alert variant="red">{`The '${userName}/${repoName}' repository doesn't contain the '${dir}' path in '${branchId}'`}</Alert>
				</>
				}
		</div>
		<div className="w-full md:w-52 lg:w-72 md:shrink-0 divide-y divide-gray-300">
			<div className="hidden md:block space-y-4 pb-4">
				<h3 className="font-medium text-lg">About</h3>
				<p className="font-light">{repoInfo.desc}</p>
				<div className="flex flex-col space-y-3 text-gray-600">
					{/* <a href="reactrouter.com"><FontAwesomeIcon icon={faPaperclip}/><span className="px-2 font-bold">reactrouter.com</span></a> */}
					<div><FontAwesomeIcon icon={faStar} fixedWidth/><strong className="px-2">{repoInfo.star}</strong>stars</div>
					<div><FontAwesomeIcon icon={faCodeFork} fixedWidth/><strong className="px-2">{repoInfo.fork}</strong>forks</div>
				</div>
			</div>
			<div className="space-y-4 py-4">
				<h3 className="font-medium text-lg">Contributors</h3>
				<ul className="flex flex-wrap">
					{repoInfo.collaborators.map(name => (
					<li className="mr-2 mb-2 w-10 h-10 rounded-full overflow-hidden" key={name}>
						<Link to={`/${name}/`}>
							<Avatar userName={name} alt={name}/>
						</Link>
					</li>
					))}
					{(repoInfo.collaborators.length === 0) && <li>No collaborators</li>}
				</ul>
			</div>
		</div>
	</div></>) : (<>
	{/* file content */}
	<div className="flex space-x-6 items-center">
		<BranchSelector repoName={repoName} userName={userName} dir={dir} branches={branches} currentBranchId={branchId}/> 
		<Breadcrumb dir={dir} fileName={fileName} repoName={repoName} branchBaseUrl={baseBranchUrl}/>
		<div className="ml-auto space-x-2">
			{isAC && <Button as="a" to={`/${path.join(userName, repoName, 'delete', branchId, dir)}?l=${fileName}`} variant="red">
				Delete
			</Button>}
		</div>
	</div>
	<div className="mt-6">
		{file.err ? <Alert variant="red">{`The '${userName}/${repoName}' repository doesn't contain the '${fileName}' file in '${branchId}'`}</Alert>
		          : file.text ? <CodeHighlighter fileName={fileName} content={file.content} linesNum={file.linesNum} size={file.contentSize}/>
						              : <Alert variant="yellow">This type of file is not supported to show.</Alert>}
	</div>
	</>)
}

