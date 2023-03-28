import { Fragment } from "react"
import { Link } from "react-router-dom"
import { trim, join } from "../../utils/path"

export default function Breadcrumb({ repoName, dir, branchBaseUrl, fileName }) {
	dir = trim(dir)
	const pathComponents = dir ? dir.split('/') : []
	const paths = pathComponents.map((v, idx, arr) => idx ? arr[idx-1]+'/'+v : v)
	return <nav className="w-full">
		<ol className="list-reset flex">
			<li className='font-bold'><Link to={branchBaseUrl} className="text-blue-600 hover:text-blue-700">{repoName}</Link></li>
			<li><span className="text-gray-500 mx-1">/</span></li>
			{fileName ? <>
				{pathComponents.map((pc, idx) => <Fragment key={idx}>
					<li><Link to={join(branchBaseUrl, paths[idx])} className="text-blue-600 hover:text-blue-700">{pc}</Link></li>
					<li><span className="text-gray-500 mx-1">/</span></li>
				</Fragment>)}
				<li className="font-bold text-black">{fileName}</li>	
			</> : <>
				{pathComponents.slice(0, -1).map((pc, idx) => <Fragment key={idx}>
					<li><Link to={join(branchBaseUrl, paths[idx])} className="text-blue-600 hover:text-blue-700">{pc}</Link></li>
					<li><span className="text-gray-500 mx-1">/</span></li>
				</Fragment>)}	
				<li className="font-bold text-black">{pathComponents[pathComponents.length-1]}</li>		
			</>}
			{}
			
			{/* <li><a href="#" className="text-blue-600 hover:text-blue-700">src</a></li>
			<li><span className="text-gray-500 mx-2">/</span></li>
			<li><a href="#" className="text-blue-600 hover:text-blue-700">example</a></li>
			<li><span className="text-gray-500 mx-2">/</span></li>
			<li className="font-bold text-black">utils.java</li> */}
		</ol>
	</nav>
}