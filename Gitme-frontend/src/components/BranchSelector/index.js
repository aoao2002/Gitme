import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Popover, Tab as Ta } from "@headlessui/react";
import { faCaretDown, faXmark, faCheck, faCodeBranch } from "@fortawesome/free-solid-svg-icons";
import * as path from "../../utils/path";
import { Link, useLocation } from "react-router-dom";

export default function BranchSelector({
	dir,
	branches,
	userName,
	repoName,
	currentBranchId: branchId,
	sector = 'source',
	handleChange,
}) {
	const location = useLocation()
	return <Popover className="relative">
		<Popover.Button className="bg-gray-100 text-gray-600 hover:bg-gray-200 active:bg-gray-300 inline-flex items-center px-5 py-2 rounded-lg text-sm font-medium">
			<FontAwesomeIcon icon={faCodeBranch}/>
			<span className="px-2">{branchId}</span>
			<FontAwesomeIcon icon={faCaretDown}/>
		</Popover.Button>

		<Popover.Panel className="bg-white shadow-md absolute z-10 mt-2 w-64 md:w-80 border border-gray-300 rounded-lg text-sm divide-y divide-gray-300">
			{({ close }) => (<>
			<p className="px-4 py-2 font-medium text-gray-600 flex justify-between items-center">
				Switch branches/tags
				<button onClick={close}><FontAwesomeIcon icon={faXmark}/></button>
			</p>
			<div className="px-4 py-2">
				<input type="text" placeholder="Filter branches/tags" className="leading-4 w-full border-gray-200 rounded-lg text-sm"/>
			</div>
			<div className="">
				<Ta.Group>
					<Ta.List className="text-gray-500 flex px-4 pt-2 border-b border-gray-300">
						<Ta>
							{({selected}) => selected ?
								<div
									className="outline-none text-gray-900 px-3 py-1 rounded-t-lg border border-gray-300 border-b-0 relative after:absolute after:-bottom-px after:block after:inset-x-0 after:h-px after:bg-white"
								>Branches</div> :
								<div className="outline-none px-3 py-1">
									Branches
								</div>
							}
						</Ta>
						<Ta>
							{({selected}) => selected ?
								<div
								className="outline-none text-gray-900 px-3 py-1 rounded-t-lg border border-gray-300 border-b-0 relative after:absolute after:-bottom-px after:block after:inset-x-0 after:h-px after:bg-white"
								>Tags</div> :
								<div className="outline-none px-3 py-1">
									Tags
								</div>
							}
						</Ta>
					
					</Ta.List>
						<Ta.Panels>
						<Ta.Panel>
							<ul className="text-sm divide-y divide-gray-300">
								{branches.map(({ name, objID }) => <li className="flex items-center px-4 py-2 hover:bg-gray-100 cursor-pointer"
									key={name}
								>
									{handleChange ?<a onClick={() => handleChange({ userName, repoName, branchId: name })}>
										<FontAwesomeIcon icon={faCheck} className={ name === branchId ? '' : "invisible"}/>
										<span className="ml-3">{name}</span>	
									</a>
									:
									<Link to={path.join(`/${userName}/${repoName}/${sector}/`, name, dir) + location.search}>
										<FontAwesomeIcon icon={faCheck} className={ name === branchId ? '' : "invisible"}/>
										<span className="ml-3">{name}</span>
									</Link>}
								</li>)}
						
							</ul>
						</Ta.Panel>
						<Ta.Panel>
							<div className="px-4 py-2">
								To be done...
							</div>
						</Ta.Panel>
					</Ta.Panels>
				</Ta.Group>
			</div>
			</>)}
		</Popover.Panel>
	</Popover>
}