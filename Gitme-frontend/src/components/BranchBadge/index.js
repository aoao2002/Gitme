import { Link } from "react-router-dom";

export default function BranchBadge({
	repoName,
	userName,
	branchId,
	showUserName = true,
}) {
	
	return <Link to={`/${userName}/${repoName}/source/${branchId}`}
		className="text-blue-500 bg-blue-50 px-1.5 py-1 rounded"
	>{showUserName ? `${userName}:${branchId}` : `${branchId}`}</Link>
}