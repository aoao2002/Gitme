import { createRef, useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Alert from "../../components/Alert";
import AuthLayout from "../../components/layouts/AuthLayout";
import { Formik, Form, Field } from 'formik'
import Button, { LoadingButton } from "../../components/Button";
import useAuth from "../../hooks/useAuth";

export default function SigninPage() {
	const { handleLogin, token, errMsg } = useAuth()
	const navigate = useNavigate()
	useEffect(() => {
		if (token) {
			const timer = setTimeout(() => {
				navigate('/')
				// console.log('redirect...')
			}, 2000)
			return () => clearTimeout(timer)
		}
	}, [token])

	return (
	<AuthLayout>
		<Formik
			initialValues={{ email: '', password: '' }}
			onSubmit={async (values, { setSubmitting }) => {
				setSubmitting(true)
				await handleLogin({ email: values.email, pwd: values.password })
				setSubmitting(false)
			}}
		>
			{({isSubmitting}) => (
			<Form className="grid grid-cols-6 gap-6">
				<div className="col-span-6">
					{ token ? <Alert variant="green">Redirect to the homepage...</Alert> : (
						errMsg ? <Alert variant="red">{errMsg}</Alert> : null
					) }
				</div>
				<div className="col-span-6">
					<label htmlFor="Email" className="block text-sm font-medium text-gray-700">
						Email
					</label>

					<Field
						id="Email"
						type="email"
						name="email"
						required
						disabled={token ? true : false}
						className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm disabled:opacity-70"
					/>
				</div>

				<div className="col-span-6">
					<label
						htmlFor="Password"
						className="block text-sm font-medium text-gray-700"
					>
						Password
					</label>

					<Field
						type="password"
						id="Password"
						name="password"
						required
						disabled={token ? true : false}
						className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm disabled:opacity-70"
					/>
				</div>

				<div className="col-span-6 sm:flex sm:items-center sm:gap-4">
					{ token ? <Button variant="green" disabled>Logged in</Button> : <LoadingButton loading={isSubmitting} variant="blue" type="submit">
						Log in
					</LoadingButton>}

					<p className="mt-4 text-sm text-gray-500 sm:mt-0">
						Not have an account? <Link to='/signup' className="text-gray-700 underline">
							Sign up
						</Link>.
					</p>
				</div>
			</Form>
			)}
		</Formik>

	</AuthLayout>
	)
}