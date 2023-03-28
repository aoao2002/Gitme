import dayjs from "dayjs";
import { Link } from "react-router-dom";
import Dialog from "../Dialog";

export default function AdvancedDialog({ 
	children, 
	author,
	date,
	...props 
}) {
	return <Dialog {...props} userName={author}>
		<div className="bg-gray-100 px-5 py-2 text-gray-600 border-b border-gray-300">
			<strong className="text-black"><Link to={`/${author}`} className="hover:text-blue-600 hover:underline">{author}</Link></strong> commented {dayjs(date).fromNow()}
		</div>
		{children}
	</Dialog>
}