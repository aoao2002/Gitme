import { faEllipsis } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { Menu, Popover } from "@headlessui/react"
import { createContext } from "react"
import { Link, useLocation } from "react-router-dom"

const pathnameContext = createContext()

export default function RepoMenu({children}) {
	const location = useLocation()
	return (
	<Menu>
		<Menu.Items static className="md:hidden text-sm font-medium text-gray-900 flex relative before:block before:bg-gray-300 before:absolute before:inset-x-0 before:h-px before:bottom-0">
			<pathnameContext.Provider value={location.pathname}>
				{children.slice(0, 2)}
			</pathnameContext.Provider>
			<Popover className="relative ml-auto">
				<Popover.Button className="sm:py-4 relative sm:px-6 outline-none cursor-pointer whitespace-nowrap py-2.5 px-4">
					<FontAwesomeIcon icon={faEllipsis}/>
				</Popover.Button>
				<Popover.Panel className="absolute z-10 w-40 right-0">
					<ul className="overflow-hidden font-normal flex flex-col bg-white border border-gray-300 rounded divide-y divide-gray-300">
						{children.slice(2).map((c,idx) => <li key={idx} 
							className="pl-4 py-1.5 hover:bg-pink-600 hover:text-white"
						>
							<Link to={c.props.to}>
								{c.props.children}
							</Link>
						</li>)}
					</ul>
				</Popover.Panel>
			</Popover>
		</Menu.Items>
		<Menu.Items static className="hidden md:flex text-sm font-medium text-gray-900 overflow-x-auto overflow-y-hidden relative before:block before:bg-gray-300 before:absolute before:inset-x-0 before:h-px before:bottom-0">
			<pathnameContext.Provider value={location.pathname}>
				{children}
			</pathnameContext.Provider>
		</Menu.Items>
	</Menu>
	)
}

RepoMenu.Item = ({children, to, display = true}) => {
	if (!display) return null
	return (
	<pathnameContext.Consumer>
		{pathname => <Menu.Item as="div" className="group sm:py-4 relative sm:px-6 outline-none cursor-pointer whitespace-nowrap py-2.5 px-4"
		>
			<span className={`${pathname.startsWith(to) ? "inline" : "hidden"} group-hover:inline absolute inset-x-0 -bottom-0 h-1 w-full bg-pink-600`}></span>
			<div className="flex items-center justify-center">
				<Link to={to}>
					{children}
				</Link>
			</div>
		</Menu.Item>}
	</pathnameContext.Consumer>
	)
} 

