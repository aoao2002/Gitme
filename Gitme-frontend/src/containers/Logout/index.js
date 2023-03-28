import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import useAuth from "../../hooks/useAuth"

export default function Logout() {
	const { handleLogout } = useAuth()
	const navigate = useNavigate()
	useEffect(() => {
		handleLogout()
		navigate('/')
	}, [])
	return <div>logout</div>
}