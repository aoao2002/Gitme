import { faUser } from '@fortawesome/free-regular-svg-icons'
import { faBell, faMagnifyingGlass, faWarehouse } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { Form, Link, useRouteError } from 'react-router-dom'
import useAuth from '../../hooks/useAuth'
import logoImg from '../../images/logo.png'

export default function Navbar() {
	const { token, handleLogout } = useAuth()
	const err = useRouteError()
	return (
		<header className="shadow-sm">
			<div
			className="mx-auto flex h-16 max-w-screen-xl items-center justify-between px-4"
			>
				<div className="flex w-0 flex-1 lg:hidden">
					<button className="rounded-full bg-gray-100 p-2 text-gray-600" type="button">
						<FontAwesomeIcon icon={faUser} className="h-5 w-5"/>	
					</button>
				</div>

				<div className="flex items-center gap-4">
					
					<span className="h-10 rounded-lg bg-gray-800 overflow-hidden">
						<img src={logoImg} className="h-10"/>
					</span>
					
					{!err && <Form className="mb-0 hidden lg:flex" action='/search'>
						<div className="relative">
							<input
								className="h-10 rounded-lg border-gray-200 pr-10 text-sm placeholder-gray-300 focus:z-10"
								placeholder="Search..."
								type="text"
								name="q"
								required
							/>

							<button
								className="absolute inset-y-0 right-0 mr-px rounded-r-lg p-2 text-gray-600"
								type="submit"
							>
								<FontAwesomeIcon icon={faMagnifyingGlass} className="h-5 w-5"/>	
							</button>
						</div>
					</Form>}
				</div>

				<div className="flex w-0 flex-1 justify-end lg:hidden">
					<button className="rounded-full bg-gray-100 p-2 text-gray-500" type="button">
						<FontAwesomeIcon icon={faMagnifyingGlass} className="h-5 w-5"/>
					</button>
				</div>

				<nav
					className="hidden items-center justify-center gap-8 text-sm font-medium lg:flex lg:w-0 lg:flex-1"
				>
					<Link className="text-gray-900" to="/"><FontAwesomeIcon icon={faWarehouse} className="mr-1"/>Repositories</Link>
					<Link className="text-gray-900" to="/notifications"><FontAwesomeIcon icon={faBell} className="mr-1"/>Notifications</Link>
					{/* <a className="text-gray-900" href="">Projects</a>
					<a className="text-gray-900" href="">Contact</a> */}
				</nav>

				<div className="hidden items-center gap-4 lg:flex">
					{ token ? 
						<button className="rounded-lg bg-gray-100 px-5 py-2 text-sm font-medium text-gray-600" onClick={() => handleLogout()}>Logout</button>
					: <>
						<Link to='/signin' className="rounded-lg bg-gray-100 px-5 py-2 text-sm font-medium text-gray-600">Log in</Link>
						<Link to='/signup' className="rounded-lg bg-blue-600 px-5 py-2 text-sm font-medium text-white">Sign up</Link>
					</>}
				</div>
			</div>

			<div className="border-t border-gray-100 lg:hidden">
				<nav
					className="flex items-center justify-center overflow-x-auto p-4 text-sm font-medium"
				>
					<Link className="flex-shrink-0 pl-4 text-gray-900" to="/">Repositories</Link>
					<Link className="flex-shrink-0 pl-4 text-gray-900" to="/notifications">Notifications</Link>
					{/* <a className="flex-shrink-0 pl-4 text-gray-900" href="">Projects</a>
					<a className="flex-shrink-0 pl-4 text-gray-900" href="">Contact</a> */}
				</nav>
			</div>
		</header>
	)
}