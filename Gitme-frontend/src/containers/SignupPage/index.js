import { Link, useNavigate } from "react-router-dom";
import AuthLayout from "../../components/layouts/AuthLayout";
import { Formik, Form, Field } from 'formik'
import Button, { LoadingButton } from "../../components/Button";
import Alert from "../../components/Alert";
import { useEffect, useState } from "react";
import { register } from "../../utils/api";
import useAuth from "../../hooks/useAuth";


function validateConfPwd(pwd, confPwd) {
	let error = ''
	if (pwd !== confPwd) {
		error = 'Password not match'
	}
	return error
}

export default function SignupPage() {
	const [success, setSuccess] = useState(false)
	const [errMsg, setErrMsg] = useState('')
	const navigate = useNavigate()
	const { token } = useAuth()

	useEffect(() => {
		if (token) navigate('/')
	}, [token])

	useEffect(() => {
		if (success) {
			const timer = setTimeout(() => {
				navigate('/signin')
			}, 2000)
			return () => clearTimeout(timer)
		}
	}, [success])

	return (
	<AuthLayout>
		<Formik
			initialValues={{ email: '', password: '',  password_confirmation: ''}}
			onSubmit={async (values, { setSubmitting }) => {
				setErrMsg('')
				setSubmitting(true)
				try {
					await register({
						email: values.email,
						pwd: values.password,
						username: values.username
					})
					setSuccess(true)
				} catch (err) {
					setSuccess(false)
					setErrMsg(err.message)
				}

				setSubmitting(false)
		
			}}
		>
			{({ isSubmitting, values, errors, touched }) => (
			<Form action="#" className="grid grid-cols-6 gap-6">
				<div className="col-span-6">
					{success ? <Alert variant="green">Redirect to the login page...</Alert>
						       : errMsg ? <Alert variant="red">{errMsg}</Alert> : null}
				</div>
				<div className="col-span-6">
					<label htmlFor="Email" className="block text-sm font-medium text-gray-700">
						Email
					</label>

					<Field
						type="email"
						id="Email"
						name="email"
						className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm disabled:opacity-70"
						disabled={success || isSubmitting}
						required
					/>
				</div>

				<div className="col-span-6">
					<label htmlFor="Username" className="block text-sm font-medium text-gray-700">
						User name
					</label>

					<Field
						type="text"
						id="Username"
						name="username"
						className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm disabled:opacity-70"
						disabled={success || isSubmitting}
						required
					/>
				</div>

				<div className="col-span-6 sm:col-span-3">
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
						className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm disabled:opacity-70"
						disabled={success || isSubmitting}
						required
					/>
				</div>

				<div className="col-span-6 sm:col-span-3">
					<label
						htmlFor="PasswordConfirmation"
						className="block text-sm font-medium text-gray-700"
					>
						Password Confirmation
					</label>

					<Field
						type="password"
						id="PasswordConfirmation"
						name="password_confirmation"
						className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm disabled:opacity-70"
						disabled={success || isSubmitting}
						required
						validate={(v) => validateConfPwd(values.password, v)}
					/>
					{errors.password_confirmation && touched.password_confirmation && <div className="text-sm text-red-600">{errors.password_confirmation}</div>}
				</div>

				<div className="col-span-6">
					<p className="text-sm text-gray-500">
						By creating an account, you agree to our <a href="#" className="text-gray-700 underline">
							terms and conditions
						</a> and <a href="#" className="text-gray-700 underline">privacy policy</a>.
					</p>
				</div>

				<div className="col-span-6 sm:flex sm:items-center sm:gap-4">
					
					<LoadingButton type="submit" variant="blue" loading={isSubmitting} disabled={success}>Create an account</LoadingButton>

					<p className="mt-4 text-sm text-gray-500 sm:mt-0">
						Already have an account? <Link to='/signin' className="text-gray-700 underline">
							Log in
						</Link>.
					</p>
				</div>
			</Form>
			)}
			
		</Formik>
		
	</AuthLayout>
	)
}