import React from 'react'

const classes = {
	normal: 'rounded-full border border-gray-300 text-xs py-0.5 px-1.5 text-gray-400',
	"gray full": 'rounded-full text-xs py-0.5 px-1.5 text-gray-900 bg-gray-200',
}

export default function Badge({
	className,
	children,
	type
}) {
	const styleName = (type && type in classes) ? type : 'normal'
	return <span className={classes[styleName] + ' ' + className}>
		{children}
	</span>
}