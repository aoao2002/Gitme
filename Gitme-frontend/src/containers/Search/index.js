import { faStar } from "@fortawesome/free-regular-svg-icons"
import { faWarehouse } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import dayjs from "dayjs"
import { Link, useLoaderData } from "react-router-dom"

export default function Search() {
	const { repos } = useLoaderData()
	return <>
		<p className="mb-6 text-2xl font-medium">{repos.length.toLocaleString('en')} repositories results</p>
		<div className="border-y border-gray-300 divide-y divide-gray-300">
			{repos.map(({creatorName, name, desc, star, lastUpdate, id}) => <div 
				className="py-6 space-y-1 pl-7"
				key={id}
			>
				<div className="relative text-gray-400">
					<FontAwesomeIcon icon={faWarehouse} className="absolute -left-7 top-0.5"/>
					<Link to={`/${creatorName}/${name}`} className="text-blue-600 hover:underline">{creatorName}/<strong>{name}</strong></Link>
				</div>
				<div>{desc}</div>
				<div className="flex space-x-4 text-gray-600">
					<div>
						<FontAwesomeIcon icon={faStar}/>
						<span className="ml-1">{star}</span>
					</div>
					<div>
						{lastUpdate ? `Updated ${dayjs(lastUpdate).fromNow()}` : 'Never updated'}
					</div>
				</div>
			</div>)}
		</div>
	</>
}