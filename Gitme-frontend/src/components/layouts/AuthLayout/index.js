import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import Logo from "../../Logo";

export default function AuthLayout({
	children
}) {
	return (
	<section className="bg-white">
  	<div className="lg:grid lg:min-h-screen lg:grid-cols-12">
			<section
				className="relative flex h-32 items-end bg-gray-900 lg:col-span-5 lg:h-full xl:col-span-6"
			>
				<img
					alt="Night"
					src="https://picsum.photos/1600/800"
					className="absolute inset-0 h-full w-full object-cover opacity-80"
				/>
				<div className="hidden lg:relative lg:block lg:p-12">
					
					<Logo />

					<h1 className="mt-4 text-2xl font-bold text-white sm:text-3xl md:text-4xl">
						Welcome to GitMe
					</h1>

					<p className="mt-4 leading-relaxed text-white/90">
						Lorem, ipsum dolor sit amet consectetur adipisicing elit. Eligendi nam
						dolorum aliquam, quibusdam aperiam voluptatum.
					</p>
				</div>
			</section>

			<main
				className="relative flex items-center justify-center px-8 py-8 sm:px-12 lg:col-span-7 lg:py-12 lg:px-16 xl:col-span-6"
			>
				<div className="absolute text-2xl text-gray-400 top-8 left-8 hidden lg:block">
					<FontAwesomeIcon icon={faArrowLeft}/>
					<Link to='/' className="ml-4">Back to GitMe</Link>
				</div>
				<div className="max-w-xl lg:max-w-3xl">
					<div className="relative block lg:hidden">
						<Logo />

						<h1
							className="mt-2 text-2xl font-bold text-gray-900 sm:text-3xl md:text-4xl"
						>
							Welcome to GitMe
						</h1>

						<p className="mt-4 leading-relaxed text-gray-500">
							Lorem, ipsum dolor sit amet consectetur adipisicing elit. Eligendi
							nam dolorum aliquam, quibusdam aperiam voluptatum.
						</p>
					</div>

					<div className="mt-8">
						{children}
					</div>
				</div>
			</main>
		</div>
	</section>
	)
}