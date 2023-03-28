import { replace } from "formik";
import { useEffect } from "react";
import { useRouteLoaderData, useNavigate, useParams } from "react-router-dom";

export default function DefaultBranchRedirect() {
	const { defaultBranch } = useRouteLoaderData("repoRoot")
	const { userName, repoName } = useParams()
	const navigate = useNavigate()
	useEffect(() => {
		if (defaultBranch) {
			navigate(`/${userName}/${repoName}/source/${defaultBranch.name}`, {
				replace: true
			})
		}
	}, [defaultBranch])
	return <div>Redirect to the default branch</div> 
}