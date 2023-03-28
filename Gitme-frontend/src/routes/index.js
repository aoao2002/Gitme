import { createBrowserRouter, Navigate, redirect } from "react-router-dom";
import App from "../App";
import RepoLayout from "../containers/RepoLayout";
import ProtectedResource from "../components/ProtectedResource";
import GeneralError from "../containers/GeneralError";
import Logout from "../containers/Logout";
import RepoCode from "../containers/RepoCode";
import RepoCommit from "../containers/RepoCommit";
import RepoCommits from "../containers/RepoCommits";
import RepoIssue from "../containers/RepoIssue";
import RepoIssues from "../containers/RepoIssues";
import SigninPage from "../containers/SigninPage";
import SignupPage from "../containers/SignupPage";
import UserPage from "../containers/UserPage";
import repoInfoLoader, { accessAction, contentLoader, deleteAction, deleteRepoAction, detailAction, fileContentLoader, forkAction, repoAction, starAction, uploadAction, watchAction } from "./repo";
import { editProfileAction, userDataLoader } from "./user";
import DefaultBranchRedirect from "../containers/DefaultBranchRedirect";
import NewIssue from "../containers/NewIssue";
import { createIssueAction, issueAction, issueLoader, issueStateAction, newCommentAction } from "./issues";
import Settings from "../containers/Settings";
import Fork from "../containers/Fork";
import RepoPulls from "../containers/RepoPulls";
import { newPrAction, newPrLoader, prLoader, prsLoader, prStateAction } from "./pr";
import UploadFile from "../containers/UploadFile";
import DeleteFile from "../containers/DeleteFile";
import { commitLoader, commitsLoader } from "./commits";
import Search from "../containers/Search";
import { searchLoader } from "./search";
import RepoPull from "../containers/RepoPull";
import NewPull from "../containers/NewPull";
import { notificationsLoader, setReadAction } from "./notifications";
import Notifications from "../containers/Notifications";
import Branches from "../containers/Branches";
import { newBranchAction } from "./branches";

const router = createBrowserRouter([
	{
		path: '/',
		element: <ProtectedResource><App /></ProtectedResource>,
		errorElement: <ProtectedResource><GeneralError/></ProtectedResource>,
		children: [
			{
				index: true,
				loader: userDataLoader,
				element: <UserPage me={true}/>
			},
			{
				path: '/search',
				loader: searchLoader,
				// errorElement: <ProtectedResource><GeneralError /></ProtectedResource>,
				element: <Search/>
			},
			{
				path: '/notifications',
				loader: notificationsLoader,
				action: setReadAction,
				element: <Notifications/>
			},
			{
				path: ':userName',
				loader: userDataLoader,
				element: <UserPage />,
			},
			{
				path: ':userName/:repoName',
				id: "repoRoot",
				loader: repoInfoLoader,
				element: <RepoLayout />,
				children: [
					{
						index: true,
						element: <Navigate replace={true} to='source'/>
					},
					{
						path: 'source',
						element: <DefaultBranchRedirect/>,
					},
					{
						path: 'fork',
						element: <Fork/>,
						action: forkAction,
					},
					{
						path: 'upload/:branchId/*',
						element: <UploadFile/>,
						action: uploadAction,
					},
					{
						path: 'delete/:branchId/*',
						element: <DeleteFile/>,
						action: deleteAction,
					},
					{
						path: 'branches',
						element: <Branches/>,
						action: newBranchAction,
					},
					{
						path: 'source/:branchId',
						children: [
							{
								index: true,
								loader: fileContentLoader,
								element: <RepoCode/>
							},
							{
								path: '*',
								loader: fileContentLoader,
								element: <RepoCode/>
							}
						]
					},
					{
						path: 'commits/branches/:branchId',
						loader: commitsLoader,
						element: <RepoCommits />
					},
					{
						path: 'commits/:commitId',
						element: <RepoCommit />,
						loader: commitLoader
					},
					{
						path: 'issues',
						children: [
							{
								index: true,
								element: <RepoIssues />
							},
							{
								path: ':issueId',
								loader: issueLoader,
								action: issueAction,
								element: <RepoIssue />
							},
							{
								path: 'new',
								action: createIssueAction,
								element: <NewIssue />
							}
						]
					},
					{
						path: 'pulls',
						children: [
							{
								index: true,
								element: <RepoPulls/>,
								loader: prsLoader,
							},
							{
								path: 'new',
								element: <NewPull/>,
								loader: newPrLoader,
								action: newPrAction,
							},
							{
								path: ':prId',
								element: <RepoPull/>,
								loader: prLoader,
								action: prStateAction,
							},
						]
					},
					{
						path: 'settings',
						element: <Settings/>
					}
				]
			}
		]
	},
	{
		path: '/signin',
		element: <SigninPage />
	},
	{
		path: '/signup',
		element: <SignupPage />
	},
	{
		path: '/logout',
		element: <Logout/>
	},
	{
		path: '_profile.update',
		action: editProfileAction,
	},
	{
		path: '_repo',
		action: repoAction,
	},
	{
		path: '_repo.star',
		action: starAction,
	},
	{
		path: '_repo.delete',
		action: deleteRepoAction,
	},
	{
		path: '_repo.access',
		action: accessAction,
	},
	{
		path: '_repo.detail',
		action: detailAction,
	},
	{
		path: '_repo.watch',
		action: watchAction,
	}
	// {
	// 	path: '_issue.state',
	// 	action: issueStateAction,
	// }
])

export default router