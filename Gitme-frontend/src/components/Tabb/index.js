import { Tab } from "@headlessui/react"

const sizeStyles = {
	sm: "px-3 py-1",
	default: "px-6 py-3"
}

export default function Tabb({
	children,
	size = "default",
}) {

	return <Tab className="outline-none">
		{({selected}) => selected ?
			<div
				className={`outline-none text-gray-900 rounded-t-lg border border-gray-300 border-b-0 relative after:absolute after:-bottom-px after:block after:inset-x-0 after:h-px after:bg-white bg-white ${sizeStyles[size]}`}
			>{children}</div> :
			<div className={`outline-none ${sizeStyles[size]}`}>
				{children}
			</div>
		}
	</Tab>
}
Tabb.Group = Tab.Group

Tabb.List = ({ children, className = '', ...props }) => {
	return <Tab.List className={`text-gray-500 flex px-2 pt-2 border-b border-gray-300 ${className}`} {...props}>{children}</Tab.List>
}

Tabb.Panels = ({ children, ...props }) => <Tab.Panels className="px-2 py-2" {...props}>{children}</Tab.Panels>
Tabb.Panel = Tab.Panel