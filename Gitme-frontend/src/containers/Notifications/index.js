import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import dayjs from "dayjs";
import { Form, Link, useActionData, useLoaderData, useNavigation } from "react-router-dom";
import Alert from "../../components/Alert";
import Button from "../../components/Button";

export default function Notifications() {
	const { notifications } = useLoaderData()
	const err = useActionData()?.err
	const navigation = useNavigation()

	return <>
		<div className="mb-4 font-medium text-3xl">
			{notifications.length} Unread notification
		</div>
		{navigation.state === 'idle' && err && <Alert variant="red">{err}</Alert>}
		<div className="divide-y divide-y-gray-300 rounded border border-gray-300 overflow-hidden">
			{ notifications.map(({reponame: repoName, username: userName, info, date, watch_id: id}) => <div 
				className="flex justify-between px-6 py-2 group hover:bg-blue-100 hover:border-l hover:border-l-blue-800 hover:border-l-4"
				key={id}
			>
				<div className="flex flex-col justify-between">
					<Link to={`/${userName}/${repoName}`} className="text-sm">{userName}/{repoName}</Link>
					<p>{info}</p>
				</div>
				<div className="flex items-center">
					<Form method="post" className="hidden" id="formRead">
						<input type="hidden" value={id} name="id"/>
					</Form>
					<div className="text-sm group-hover:hidden">{dayjs(date).fromNow()}</div>
					<Button className="hidden group-hover:block" variant="lightBlue" type="submit" form="formRead"><FontAwesomeIcon icon={faCheck} size="xl"/></Button>
				</div>
			</div>)}
		</div>
	</>
}