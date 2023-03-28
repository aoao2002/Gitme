import { Link } from "react-router-dom";
import logoImg from "../../images/logo.png"


export default function Logo({
	size='normal'
}) {
	const sizeMap = {
		normal: 'h-10',
		xs: 'h-6',
	}
	return (
	<Link to='/' className="rounded-md p-0.25 bg-black text-blue-600">
		<span className="sr-only">Home</span>
		<img src={logoImg} className={sizeMap[size]}/>
	</Link>
	)
}