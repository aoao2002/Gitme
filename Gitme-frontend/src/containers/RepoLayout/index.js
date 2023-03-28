import { faCircleDot, faCode, faCodeBranch, faCodeFork, faCodePullRequest, faPaperclip, faStar, faWarehouse, faCodeCommit, faGear } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link, Outlet, useLoaderData, useLocation, useParams } from "react-router-dom";
import Alert from "../../components/Alert";
import Badge from "../../components/Badge";
import Button from "../../components/Button";
import RepoMenu from "../../components/RepoMenu";
import StarButton from "../../components/StarButton";
import WatchButton from "../../components/WatchButton";

export default function RepoLayout() {
	const params = useParams()
	const { userName, repoName, branchId } = params
	const baseUrl = `/${userName}/${repoName}`
	const { repoInfo, branches, defaultBranch, isAC } = useLoaderData()
	const isPublic = repoInfo.state.toUpperCase() == "PUBLIC"
	const location = useLocation()

	const isMyRepo = (userName === localStorage.getItem('userName'))
	return <>

	<div className="space-y-4 mb-4">
		<div className="flex justify-between flex-wrap items-center">
			<h3 className="text-xl text-gray-300 justify-center flex flex-col">
				<div>
					<FontAwesomeIcon icon={faWarehouse}/>
					<Link className="text-blue-600 px-2.5 hover:underline" to={`/${userName}`}>{userName}</Link>
					/
					<Link className="text-blue-600 px-2.5 hover:underline font-bold" to={baseUrl}>{repoName}</Link>
					<Badge>{isPublic ? 'Public' : 'Private'}</Badge>
				</div>
				{repoInfo.forkFromUserName && <div className="text-sm text-gray-800 mt-2">
					forked from <Link className="text-blue-600 hover:underline" to={`/${repoInfo.forkFromUserName}/${repoInfo.forkFromRepoName}`}>{repoInfo.forkFromUserName}/{repoInfo.forkFromRepoName}</Link>	
				</div>}
			</h3>
			
			<div className="space-x-2 hidden md:block">
				<StarButton starNum={repoInfo.star} repoId={repoInfo.id} isStarred={repoInfo.starOwn === 1}></StarButton>
				<Button as="a" to={`/${userName}/${repoName}/fork`} disabled={isMyRepo}><FontAwesomeIcon icon={faCodeFork}/><span className="px-1.5">Fork</span><Badge type="gray full">{repoInfo.fork}</Badge></Button>	
				<WatchButton repoId={repoInfo.id} isWatching={repoInfo.watchIt}/>
			</div>
		</div>
		<div className="space-y-4 md:hidden">
			<p className="text-md font-light">{repoInfo.desc}</p>

			<div className="text-sm space-y-2 flex flex-col text-gray-500">
				{/* <a className="" href="reactrouter.com">
					<FontAwesomeIcon icon={faPaperclip}/>
					<span className="px-2">reactrouter.com</span>
				</a> */}
				<div className="space-x-3 flex">
					<div><FontAwesomeIcon icon={faStar}/><strong className="px-2">{repoInfo.star}</strong>stars</div>
					<div><FontAwesomeIcon icon={faCodeFork}/><strong className="px-2">{repoInfo.fork}</strong>forks</div>
				</div>
			</div>

			<div className="flex space-x-2">
				<StarButton starNum={repoInfo.star} repoId={repoInfo.id} className="flex-1" isStarred={repoInfo.starOwn === 1}></StarButton>
				<Button className="flex-1" as="a" to={`/${userName}/${repoName}/fork`} disabled={isMyRepo}><FontAwesomeIcon icon={faCodeFork}/><span className="px-2">Fork</span></Button>
				{/* <WatchButton repoId={repoInfo.id} isWatching={repoInfo.watchIt} className="flex-1"/> */}
			</div>
		</div>
	</div>

	<RepoMenu>
		<RepoMenu.Item to={`${baseUrl}/source`}>
			<FontAwesomeIcon icon={faCode} fixedWidth/>
			<span className="ml-3">Code</span>
		</RepoMenu.Item>
		<RepoMenu.Item to={`${baseUrl}/issues`}>
			<FontAwesomeIcon icon={faCircleDot} fixedWidth/>
			<span className="ml-3">Issues</span>
		</RepoMenu.Item>
		<RepoMenu.Item to={`${baseUrl}/pulls`}>
			<FontAwesomeIcon icon={faCodePullRequest} fixedWidth/>
			<span className="ml-3">Pull requests</span>
		</RepoMenu.Item>
		<RepoMenu.Item to={`${baseUrl}/commits/branches/${branchId || defaultBranch?.name || ''}`}>
			<FontAwesomeIcon icon={faCodeCommit} fixedWidth/>
			<span className="ml-3">Commits</span>
		</RepoMenu.Item>
		<RepoMenu.Item to={`${baseUrl}/branches/`}>
			<FontAwesomeIcon icon={faCodeBranch} fixedWidth/>
			<span className="ml-3">Branches</span>
		</RepoMenu.Item>
		<RepoMenu.Item to={`${baseUrl}/settings`} display={isMyRepo || isAC}>
			<FontAwesomeIcon icon={faGear} fixedWidth/>
			<span className="ml-3">Settings</span>
		</RepoMenu.Item>
	</RepoMenu>
	<div className="pt-4">
		{(branches.length || location.pathname === `/${userName}/${repoName}/settings`) ? <Outlet/> : <Alert variant="red">No branches</Alert>}
	</div>
	</>
}