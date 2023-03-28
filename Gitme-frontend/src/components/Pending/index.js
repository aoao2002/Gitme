import { useState } from "react";
import Spinner from "../Spinner";

export default function Pending({
	promise, 
	children,
}) {
	const [loading, setLoading] = useState(true)
	const [data, setData] = useState()
	promise.then(d => {
		setData(d)
		setLoading(false)
	})
	return loading ? <Spinner/> : children(data)
}