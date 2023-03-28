import { useFetcher } from "react-router-dom"
import Button, { LoadingButton } from "../Button"
import { faCircleNotch, faStar } from "@fortawesome/free-solid-svg-icons";
import { faStar as faHollowStar } from '@fortawesome/free-regular-svg-icons'
import Badge from "../Badge";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default function StarButton({
	isStarred,
	repoId,
	starNum,
	className,
}) {
	const fetcher = useFetcher()
	const disabled = fetcher.state !== 'idle'
	return <fetcher.Form method="post" action="/_repo.star" className={"inline-block " + className}>
		<input type="hidden" value={repoId} name="repoId"/>
		<Button value={isStarred ? "1" : "0"} name="isStarred" disabled={disabled} className="w-full">
			{disabled ? <FontAwesomeIcon icon={faCircleNotch} spin={true} className="mr-3" />
			          : <FontAwesomeIcon icon={isStarred ? faStar : faHollowStar} className={isStarred ? "mr-3 text-yellow-500" : "mr-3"}/>}
			{isStarred ? "Starred" : "Star"}
			{(starNum !== undefined) && <Badge type="gray full" className="ml-1.5">{starNum}</Badge>}
		</Button>
	</fetcher.Form>
}