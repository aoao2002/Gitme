import { useLoaderData } from "react-router-dom"
import React from "react"

export const ProfileContext = React.createContext(null)

export function UserProfileProvider({
	children
}) {
	const { profile } = useLoaderData()
	return <ProfileContext.Provider value={profile}>
		{children}
	</ProfileContext.Provider>
}