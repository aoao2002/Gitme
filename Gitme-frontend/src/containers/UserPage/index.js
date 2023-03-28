import { faBookOpen, faLocationDot, faLock, faLockOpen, faPaperclip, faPhone, faStar, faWarehouse } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useState } from "react";
import dayjs from "dayjs";
import Button, { LoadingButton } from "../../components/Button";
import Select from "../../components/Select";
import avatarImg from '../../images/avatar.png'
import RepoSummary from "../../components/RepoSummary";
import Tab from "../../components/Tab";
import RepoShortSummary from "../../components/RepoShortSummary";
import Badge from "../../components/Badge";
import { Await, Form, useFetcher, useLoaderData, useParams } from "react-router-dom";
import Modal from "../../components/Modal";
import Input from "../../components/Input";
import Alert from "../../components/Alert";
import Spinner from "../../components/Spinner";
import Avatar from "../../components/Avatar";


const repoTypeOptions = [
	{name: 'All', value: 'all'},
	{name: 'Public', value: 'public'},
	{name: 'Private', value: 'private'},
]

const sortByOptions = [
	{name: 'Last updated', value: 'last_updated'},
	{name: 'Name', value: 'name'},
	{name: 'Stars', value: 'stars'},
]

// const heruistic = (repo) => {
// 	return repo.star + repo.fork
// }


export default function UserPage({
	me = false
}) {
	const [repoType, setRepoType] = useState(repoTypeOptions[0])
	const [sortBy, setSortBy] = useState(sortByOptions[0])
	const { profile, repos, starredRepos } = useLoaderData()

	const [modalShow, setModalShow] = useState(false)
	const fetcher = useFetcher()
	const updateSuccess = fetcher.state === 'idle' && fetcher.data?.err === ''

	const [cpModalShow, setCpModalShow] = useState(false)
	const cpFetcher = useFetcher()
	const cpSuccess = cpFetcher.state === 'idle' && cpFetcher.data?.err === ''	

	const myName = localStorage.getItem('userName')
	const { userName = myName } = useParams()
	return (
			<div className="flex flex-col space-y-4 md:space-y-0 md:flex-row md:space-x-4">
				<div className="w-full md:w-60 lg:w-64 shrink-0">
					<div className="flex flex-col space-y-4 -mt-6">
						{/* avatar */}
						<div className="flex items-center">
							<div className="w-32 shadow-md border border-gray-300 aspect-square md:w-full rounded-full overflow-hidden">
								<Avatar userName={userName} className="w-full"/>
							</div>
							<div className="md:hidden p-4">
								<div className="font-bold text-2xl">{ profile.name }</div>
								<div className="text-gray-500">{profile.mail}</div>
							</div>
						</div>
						
						{/* username */}
						<div className="hidden md:block">
							<div className="font-bold text-2xl">{profile.name}</div>
							<div className="text-gray-500">{profile.mail}</div>
						</div>
						{me && <Button type='normal' onClick={() => setModalShow(true)}>Edit profile</Button>}

						<p className="text-md font-light">
							{profile.bio}
						</p>
						<div className="text-sm space-y-1 pt-4 text-gray-600">
							<p>
								<FontAwesomeIcon icon={faPhone} className="mr-2 w-4"/>
								{profile.phoneNumber}
							</p>
							<p>
								<FontAwesomeIcon icon={faPaperclip} className="mr-2 w-4"/>
								<a href="#">Your website here</a>
							</p>
						</div>
					</div>
				</div>

				<div className="grow md:px-3">
				
					<Tab.Group>
						<Tab.List>
							<Tab>
								<FontAwesomeIcon icon={faBookOpen}/>
								<span className="ml-3 text-sm font-medium text-gray-900">Overview</span>
							</Tab>
							<Tab>
								<FontAwesomeIcon icon={faWarehouse}/>
								<span className="ml-3 mr-2 text-sm font-medium text-gray-900">Repositories</span>
								<React.Suspense>
									<Await resolve={repos} children={(resolvedRepos) => <Badge type="gray full">{resolvedRepos.all.length}</Badge>}/>
								</React.Suspense>
							</Tab>
							{(myName === userName) && <Tab>
								<FontAwesomeIcon icon={faStar}/>
								<span className="ml-3 mr-2 text-sm font-medium text-gray-900">Stars</span>
								<Badge type="gray full">{starredRepos.length}</Badge>
							</Tab>}
						</Tab.List>
						<Tab.Panels>
							<Tab.Panel>
								<h3 className="mb-3">Popular repositories</h3>
								
									<React.Suspense fallback={<Spinner/>}>
										<Await
											resolve={repos}
											children={(resolvedRepos) => <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
												{resolvedRepos.popular.map(({ name, desc, state, star, fork }) => 
													<RepoShortSummary 
														key={name}
														name={name}
														type={state}
														desc={desc}
														stars={star}
														forks={fork}
														to={`/${profile.name}/${name}`}	
													/>)}
											</div>}
										/>
									</React.Suspense>
								
							</Tab.Panel>
							<Tab.Panel>
								{/* search utilities */}
								<div className="flex flex-col lg:flex-row space-y-2 lg:space-y-0 lg:space-x-2 lg:justify-between">
									<div className="space-y-2 space-y-reverse sm:space-y-0 sm:space-x-2 flex flex-col-reverse sm:flex-row grow">
										<input
											type="text"
											placeholder="Find a repository"
											className="w-full rounded-md border-gray-200 shadow-sm sm:text-sm grow"
										/>
										<Button className="lg:hidden shrink-0" variant="green" onClick={() => {setCpModalShow(true)}}><FontAwesomeIcon icon={faWarehouse} className="mr-1"/>New</Button>
									</div>
									
									<div className="flex space-x-2">
										<Select title="Type" className="w-36" options={repoTypeOptions} selected={repoType} setSelected={setRepoType}/>
										<Select title="Language" className="w-36" options={sortByOptions} selected={sortBy} setSelected={setSortBy}/>
										<Button className="hidden lg:block" variant="green" onClick={() => {setCpModalShow(true)}}><FontAwesomeIcon icon={faWarehouse} className="mr-1"/>New</Button>	
									</div>
								</div>

								<Modal show={cpModalShow} title="Create a new repository"
									showSubmitBtn={false}
									handleClose={() => {
										setCpModalShow(false)
										cpFetcher.data = undefined
									}}
								>
									<cpFetcher.Form className="flex flex-col space-y-3" action="/_repo" method="POST">
										{(cpFetcher.state == 'idle' && cpFetcher.data?.err) && <Alert variant="red">{cpFetcher.data.err}</Alert>}
										<div>
											<label htmlFor="repName" className="mb-2 text-gray-600 block">Repository name</label>
											<Input type="text" id="repName" name="repName" required/>
										</div>
										<div>
											<label htmlFor="repDesc" className="mb-2 text-gray-600 block">
												Description <span className="text-gray-600 text-sm">(optional)</span>
											</label>
											<Input type="text" id="repDesc" name="repDesc"/>
										</div>
										<div>
											<label className="flex items-center space-x-3 mb-2" htmlFor="repTypePublic">
												<input type="radio" name="repType" value="PUBLIC" defaultChecked id="repTypePublic"/>
												<FontAwesomeIcon icon={faLockOpen} fixedWidth className="text-gray-400" size="xl"/>
												<div className="flex flex-col items-between">
													<div className="font-bold">Public</div>
													<div className="text-sm text-gray-600">Anyone on the internet can see this repository.</div>
												</div>
											</label>
											<label className="flex items-center space-x-3" htmlFor="repTypePrivate">
												<input type="radio" name="repType" value="PRIVATE" id="repTypePrivate"/>
												<FontAwesomeIcon icon={faLock} fixedWidth className="text-gray-400" size="xl"/>
												<div className="flex flex-col items-between">
													<div className="font-bold">Private</div>
													<div className="text-sm text-gray-600">You choose who can see and commit to this repository.</div>
												</div>
											</label>
										</div>
										<LoadingButton  className="mt-3 w-full" type="submit" 
											loading={cpFetcher.state !== 'idle'}
											disabled={cpSuccess}
											variant={cpSuccess ? 'green' : 'blue'}
										>{cpSuccess ? 'Created!' : 'Create repository'}</LoadingButton>
									</cpFetcher.Form>
								</Modal>
								
								{/* repo list */}
								<div className="space-y-4 divide-y divide-gray-200 border-t border-gray-200 mt-6">
									<React.Suspense fallback={<Spinner/>}>
										<Await
											resolve={repos}
											children={(resolvedRepos) => resolvedRepos.all.map(({ id, name, state, desc, star, fork, issue, pr, lastUpdate, starOwn }) => (
												<RepoSummary 
													key={name}
													name={name}
													type={state}
													desc={desc}
													stars={star}
													isStarred={starOwn}
													forks={fork}
													issues={issue}
													lastUpdated={lastUpdate}
													pulls={pr}
													to={`/${profile.name}/${name}`}
													repoId={id}
												/>	
											))}
										/>
									</React.Suspense>
								</div>
							</Tab.Panel>
							{(myName === userName) && <Tab.Panel>
								<div className="space-y-4 divide-y divide-gray-200 border-t border-gray-200 mt-6">
									{starredRepos.map(({ id, name, state, desc, star, fork, issue, pr, lastUpdate, starOwn, creatorName }) => (
										<RepoSummary 
											key={name}
											name={name}
											type={state}
											desc={desc}
											stars={star}
											isStarred={starOwn}
											forks={fork}
											issues={issue}
											lastUpdated={lastUpdate}
											pulls={pr}
											to={`/${creatorName}/${name}`}
											repoId={id}
										/>	
									))}

								</div>
							</Tab.Panel>}
						</Tab.Panels>
					</Tab.Group>
					
				</div>
				<Modal title="Edit profile"
					showSubmitBtn={false}
					show={modalShow}
					handleClose={() => {
						setModalShow(false)
						fetcher.data = undefined
					}}
				>
			
					<fetcher.Form className="flex space-y-3 flex-col" action="/_profile.update" method="delete">
						{(fetcher.state == 'idle' && fetcher.data?.err) && <Alert variant="red">{fetcher.data.err}</Alert>}
						<div>
							<label htmlFor="userBio" className="mb-2 text-gray-600 block">Biography</label>
							<Input type="text" id="userBio" name="bio" required defaultValue={profile.bio}/>
						</div>
						<div>
							<label htmlFor="Phone" className="mb-2 text-gray-600 block">Phone</label>
							<Input type="text" id="Phone" name="phone" required defaultValue={profile.phoneNumber}/>
						</div>
						<div>
							<LoadingButton  className="mt-3 w-full" type="submit" 
								loading={fetcher.state !== 'idle'}
								disabled={updateSuccess}
								variant={updateSuccess ? 'green' : 'blue'}
							>{updateSuccess ? 'Updated!' : 'Update'}</LoadingButton>
						</div>
					</fetcher.Form>
				</Modal>
			</div>
	)
}