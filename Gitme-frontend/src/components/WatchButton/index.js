import { useFetcher } from "react-router-dom"
import Button, { LoadingButton } from "../Button"
import { faCircleNotch, faEye, faEyeSlash, faStar } from "@fortawesome/free-solid-svg-icons";
import { faStar as faHollowStar } from '@fortawesome/free-regular-svg-icons'
import Badge from "../Badge";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default function WatchButton({
	isWatching,
	repoId,
	className,
}) {
	const fetcher = useFetcher()
	const disabled = fetcher.state !== 'idle'
	return <fetcher.Form method="post" action="/_repo.watch" className={"inline-block " + className}>
		<input type="hidden" value={repoId} name="repoId"/>
		<Button value={isWatching ? "1" : "0"} name="isWatching" disabled={disabled} className="w-full">
			{/* {disabled ? <FontAwesomeIcon icon={faEye} spin={true} className="mr-3" />
			          : <FontAwesomeIcon icon={faEye} className={isStarred ? "mr-3 text-yellow-500" : "mr-3"}/>} */}
			<FontAwesomeIcon icon={isWatching ? faEyeSlash : faEye} className="mr-3"/>
			{isWatching ? "Unwatch" : "Watch"}
		</Button>
	</fetcher.Form>
}