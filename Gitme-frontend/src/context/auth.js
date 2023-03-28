import { useState } from "react"
import { loginByEmail } from "../utils/api"
import React from "react"

export const AuthContext = React.createContext(null)

const clearCache = () => {
	localStorage.removeItem('token')
	localStorage.removeItem('token_expired_at')
	localStorage.removeItem('email')	
	localStorage.removeItem('userName')
}

export const AuthProvider = ({children}) => {
	const [token, setToken] = useState(() => {
		
		const tk = localStorage.getItem('token')
		if (tk === null) return null 

		const expiredAt = localStorage.getItem('token_expired_at')
		if (expiredAt === null || expiredAt <= Date.now()) {
			clearCache()
			return null
		}
		return tk
	})

	const [errMsg, setErrMsg] = useState('')

	const handleLogin = async ({email, pwd}) => {
		try {
			const { tokenValue: tk, tokenTimeout, userName } = await loginByEmail({email, pwd})
			localStorage.setItem('token', tk)
			localStorage.setItem('token_expired_at', Date.now() + tokenTimeout * 1000)
			localStorage.setItem('email', email)
			localStorage.setItem('userName', userName)
			setToken(tk)
			setErrMsg('')
		} catch(err) {
			setToken(null)
			setErrMsg(err.message)
		}
	}

	const handleLogout = () => {
		setToken(null)
		setErrMsg('')
		clearCache()
	}

	const value = {
		token,
		errMsg,
		handleLogin,
		handleLogout
	}

	return <AuthContext.Provider value={value}>
		{children}
	</AuthContext.Provider>
}