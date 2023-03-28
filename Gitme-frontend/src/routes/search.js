import { redirect } from "react-router-dom"
import { search } from "../utils/api"

export async function searchLoader({ request, params }) {
	const token = localStorage.getItem('token')
	const searchParams = new URLSearchParams(new URL(request.url).search)
	const keyword = searchParams.get('q')
	if (keyword) {
		const { data: repos } = await search({
			token,
			keyword: keyword
		})
		return { repos }
	} else {
		return redirect('/')
	}
}