import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";

export default function ProtectedResource({
	redirectUrl = '/signin',
	children
}) {
	const { token } = useAuth()
	const navigate = useNavigate()
	useEffect(() => {
		if (!token) {
			navigate(redirectUrl)
		}
	}, [token])

	if (token) {
		return children
	} else {
		return <div>Redirect to the login page...</div>
	}
}