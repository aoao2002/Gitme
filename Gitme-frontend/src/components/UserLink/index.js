import { Link } from "react-router-dom";

export default function UserLink({ userName }) {
	return <Link className="hover:underline hover:text-blue-600" to={`/${userName}`}>{userName}</Link>
}