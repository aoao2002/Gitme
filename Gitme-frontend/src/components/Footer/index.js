import logoImg from '../../images/logo.png'
import Logo from '../Logo'

export default function Footer() {
	return (
	<footer className="text-xs py-12">
		<div className="container mx-auto">
			<div className="flex items-center justify-center text-gray-600">
				<Logo size='xs'/>
				<span className='px-2'>
					&copy; 2022 GitMe. All rights reserved.
				</span>
			</div>
		</div>
	</footer>
	)
}