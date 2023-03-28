const baseStyles = "block w-full font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none disabled:bg-gray-100"
const sizeStyles = {
	lg: "text-xl px-4 py-2",
	default: "text-base px-3 py-1.5",
	sm: "text-sm px-2 py-1",
}

export default function Input({
	className = '',
	size = "default",
	...props
}) {

	return <input
		className={`${baseStyles} ${sizeStyles[size]} ${className}`}
		{...props}
	/>
}