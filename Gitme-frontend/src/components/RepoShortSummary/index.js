import { faCodeFork, faStar } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import Badge from "../Badge";

export default function RepoShortSummary({
	name,
	type,
	desc,
	stars,
	forks,
	to,
}) {

	return (
	<div className="p-4 space-y-2 border border-gray-300 shadow-sm rounded-lg">	
		<div className="flex justify-between items-center">
			<Link to={to} className="text-lg font-medium text-blue-600 hover:underline">
				{name}
			</Link>
			<Badge type="normal">{type.toLowerCase() == 'public' ? 'Public' : 'Private'}</Badge>
		</div>
		<p className="text-sm text-gray-600 max-w-4xl pr-4">{desc}</p>
		<div className="text-xs text-gray-600">
			<div className="space-x-5">
				<span><FontAwesomeIcon className="mr-1" icon={faStar}/>{stars.toLocaleString('en-US')}</span>
				<span><FontAwesomeIcon className="mr-1" icon={faCodeFork}/>{forks.toLocaleString('en-US')}</span>
			</div>
		</div>
	</div>
	)
}