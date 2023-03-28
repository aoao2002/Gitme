import { faCircleNotch } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import React from 'react'
import { Link } from 'react-router-dom'

// const classes = {
// 	normal: ['bg-gray-100', 'text-gray-600', 'hover:bg-gray-200', 'active:bg-gray-300'],
// 	green: ['bg-green-500', 'text-white', 'hover:bg-green-600', 'active:bg-green-700'],

// }

// export default function Button({
// 	type, children, className = ''
// }) {
// 	const styleName = (type && type in classes) ? type : "normal"
// 	return <button className={classes[styleName].join(' ') + ' inline-flex items-center px-5 py-2 rounded-lg text-sm font-medium ' + className}>{children}</button>
// }



const extraStyles = {
	blue: ['text-white', 'bg-blue-600', 'active:bg-blue-800', 'hover:bg-blue-700', 'focus:bg-blue-700'],
	purple: ['text-white', 'bg-purple-600', 'active:bg-purple-800', 'hover:bg-purple-700', 'focus:bg-purple-700'],
	green: ['text-white', 'bg-green-500', 'active:bg-green-700', 'hover:bg-green-700', 'focus:bg-green-600'],
	red: ['text-white', 'bg-red-600', 'active:bg-red-800', 'hover:bg-red-700', 'focus:bg-red-700'],
	yellow: ['text-white', 'bg-yellow-500', 'active:bg-yellow-700', 'hover:bg-yellow-600', 'focus:bg-yellow-600'],
	lightBlue: ['text-white', 'bg-blue-400', 'hover:bg-blue-500', 'focus:bg-blue-500', 'active:bg-blue-600'],
	light: ['text-gray-700', 'bg-gray-200', 'active:bg-gray-400', 'hover:bg-gray-300', 'focus:bg-gray-300'],
	dark: ['text-white', 'bg-gray-800', 'active:bg-gray-900', 'hover:bg-gray-900', 'focus:bg-gray-900']
}

const baseStyles = {
	fill: ['inline-block', 'px-6', 'py-2.5', 'font-medium', 'text-xs', 'leading-tight', 'rounded', 'shadow-md', 'hover:shadow-lg', 'focus:shadow-lg', 'focus:outline-none', 'focus:ring-0', 'active:shadow-lg', 'transition', 'duration-150', 'ease-in-out', 'text-center'],
	outline: [],
	fillSmall: ['inline-block', 'px-3', 'py-0.5', 'font-medium', 'text-xs', 'leading-tight', 'rounded', 'shadow-md', 'hover:shadow-lg', 'focus:shadow-lg', 'focus:outline-none', 'focus:ring-0', 'active:shadow-lg', 'transition', 'duration-150', 'ease-in-out', 'text-center'],
}

export default function Button({
	variant = "light",
	outline = false,
	className = '',
	children,
	disabled = false,
	as = "button",
	...props
}) {
	const baseStyleName = outline ? "outline" : "fill"
	const extraStyleName = (variant && variant in extraStyles) ? variant : "light"
	const disabledStyle = "pointer-events-none opacity-60"
	return as === "a" ? 
		<Link {...props}
			to={disabled ? '' : props.to}
			className={`${baseStyles[baseStyleName].join(" ")} ${extraStyles[extraStyleName].join(" ")} ${disabled ? disabledStyle : ''} ${className}`}
		>
			{children}
		</Link> : 
		<button {...props}
			className={`${baseStyles[baseStyleName].join(" ")} ${extraStyles[extraStyleName].join(" ")} ${disabled ? disabledStyle : ''} ${className}`}
		>
			{children}
		</button>
}

export function LoadingButton({
	loading,
	children,
	disabled,
	...props
}) {
	return <Button disabled={loading || disabled} {...props}>
		{loading && <FontAwesomeIcon icon={faCircleNotch} spin={true} className="mr-2"/>}
		{children}
	</Button>
}