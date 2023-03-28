import { redirect } from "react-router-dom"
import { getUnreadNotifications, setNotificationRead } from "../utils/api"

export async function notificationsLoader({ params, request }) {
	const token = localStorage.getItem('token')
	const { data: notifications } = await getUnreadNotifications({ token })
	return { notifications }
}

export async function setReadAction({ params, request }) {
	const token = localStorage.getItem('token')
	const formData = await request.formData()
	const notificationId = formData.get('id')
	try {
		await setNotificationRead({ token, id: notificationId })
		return redirect('/notifications')
	} catch (err) {
		return { err: err.message }
	}
}