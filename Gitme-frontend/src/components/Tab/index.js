import { Tab as Ta } from "@headlessui/react"
import { Fragment } from "react"

export default function Tab({children}) {
	return (
	<Ta as="div" className="sm:py-4 relative sm:px-6 outline-none cursor-pointer whitespace-nowrap py-2.5 px-4 ">
		{({selected}) => selected ? (<>
			<span className="absolute inset-x-0 -bottom-0 h-1 w-full bg-pink-600"></span>
			<div className="flex items-center justify-center">
				{children}
			</div>
		</>) : (
			<div className="flex items-center justify-center">
				{children}
			</div>
		)}
	</Ta>
	)
}

Tab.Group = ({children}) => {
	return (
	<Ta.Group>
		{children}
	</Ta.Group>
	)
}

Tab.List = ({children}) => {
	return (
	<Ta.List className="flex overflow-x-auto overflow-y-hidden relative before:block before:bg-gray-300 before:absolute before:inset-x-0 before:h-px before:bottom-0">
		{children}
	</Ta.List>
	)
}

Tab.Panels = ({children}) => <Ta.Panels className="pt-4">{children}</Ta.Panels>

Tab.Panel = Ta.Panel