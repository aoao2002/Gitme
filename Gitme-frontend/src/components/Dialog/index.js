import Avatar from '../Avatar'

export default function Dialog({
	userName,
	children, className = '', ...props
}) {
	return <div className={`flex flex-row ${className}`} {...props}>
		<div className="mr-4 rounded-full overflow-hidden sm:w-10 sm:h-10 md:w-16 md:h-16 hidden sm:block shrink-0">
			<Avatar userName={userName}/>
		</div>
		<div className="grow rounded border border-gray-300 relative commentBox before:bg-gray-300 after:bg-gray-100 before:hidden after:hidden sm:before:block sm:after:block">
			{ children }
		</div>
	</div>
}