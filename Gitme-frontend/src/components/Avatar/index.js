export default function Avatar({
	userName,
	className = '',
	...props
}) {
	return <img src={`https://github.com/identicons/${userName}.png`} {...props} className={`${className} aspect-square`}/>
}