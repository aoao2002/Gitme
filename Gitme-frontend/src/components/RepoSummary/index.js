import { faCircleDot, faCodeFork, faCodePullRequest, faStar } from "@fortawesome/free-solid-svg-icons";
import { faStar as faHollowStar } from '@fortawesome/free-regular-svg-icons'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Badge from "../Badge";
import Button from "../Button";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { Link } from "react-router-dom";
import StarButton from "../StarButton";


dayjs.extend(relativeTime)

export default function RepoSummary({
	repoId,
	name,
	type,
	desc,
	isStarred,
	stars,
	forks,
	issues,
	pulls,
	lastUpdated,
	to
}) {
	return (
	<div className="py-6 space-y-2">	
		<div className="flex justify-between items-center">

			<div className="space-y-1.5">
				<div className="space-x-3 flex items-center">
					<Link className="text-xl font-medium text-blue-600 hover:underline" to={to}>{name}</Link>
					<Badge type="normal">{type.toLowerCase() == 'public' ? 'Public' : 'Private'}</Badge>
				</div>
				<p className="text-sm text-gray-600 max-w-4xl pr-4">{desc}</p>
			</div>

	
			<div className="shrink-0">
				<StarButton isStarred={isStarred} repoId={repoId}/>
			</div>

		</div>

		<div className="flex justify-between text-xs text-gray-600">
			<div className="space-x-5">
				<span><FontAwesomeIcon className="mr-1" icon={faStar}/>{stars.toLocaleString('en-US')}</span>
				<span><FontAwesomeIcon className="mr-1" icon={faCodeFork}/>{forks.toLocaleString('en-US')}</span>
				<span><FontAwesomeIcon className="mr-1" icon={faCircleDot}/>{issues.toLocaleString('en-US')}</span>
				<span><FontAwesomeIcon className="mr-1" icon={faCodePullRequest}/>{pulls.toLocaleString('en-US')}</span>
			</div>
			<div>
				{lastUpdated ? "Updated " + dayjs(lastUpdated).fromNow() : 'Never updated'}
			</div>
		</div>
	</div>
	)
}