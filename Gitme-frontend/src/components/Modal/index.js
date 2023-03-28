import { useEffect, useRef } from "react";
import Button from "../Button";

export default function Modal({
	show,
	handleClose,
	title,
	children,
	showSubmitBtn = true,
	showCloseBtn = true,
	handleSubmit,
}) {
	const backdropRef = useRef(null)
	const panelRef = useRef(null)
	const contRef = useRef(null)
	const showRef = useRef(show)

	useEffect(() => {
		showRef.current = show
	}, [show])

	useEffect(() => {
		const handleTransitionend = (e) => {
			if(!showRef.current) contRef.current.style.display = 'none'
		}
		// const handleTransitionstart = (e) => {

		// 	console.log(showRef.current)
		// 	if(showRef.current) contRef.current.style.display = 'block'
		// }	
		backdropRef.current.addEventListener('transitionend', handleTransitionend)
		// backdropRef.current.addEventListener('transitionstart', handleTransitionstart)
	}, [])

	useEffect(() => {
		if (show) {
			contRef.current.style.display = 'block'
		}
	}, [show])

	return <div 
		ref={contRef}
		className={"relative z-10 hidden"} aria-labelledby="modal-title" role="dialog" aria-modal="true">
		{/* <!--
			Background backdrop, show/hide based on modal state.

			Entering: "ease-out duration-300"
				From: "opacity-0"
				To: "opacity-100"
			Leaving: "ease-in duration-200"
				From: "opacity-100"
				To: "opacity-0"
		--> */}
		<div className={"fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity ease-in duration-200 " + (show ? "opacity-100" : "opacity-0")}
			ref={backdropRef}
		></div>

		<div className="fixed inset-0 z-10 overflow-y-auto">
			<div className="flex min-h-full items-end justify-center p-4 text-center items-center sm:p-0">
				{/* <!--
					Modal panel, show/hide based on modal state.

					Entering: "ease-out duration-300"
						From: "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
						To: "opacity-100 translate-y-0 sm:scale-100"
					Leaving: "ease-in duration-200"
						From: "opacity-100 translate-y-0 sm:scale-100"
						To: "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
				--> */}
				<div className={"relative transform text-left shadow-xl transition-all sm:my-8 w-full sm:max-w-lg ease-in duration-200 " + (show ? " opacity-100 translate-y-0 sm:scale-100" : "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95")}
					ref={panelRef}
				>
					<div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4 rounded-t-lg">
						<div className="mt-3 sm:mt-0">
							<h3 className="text-xl font-medium leading-6 text-gray-900" id="modal-title">{title}</h3>
							<div className="mt-4">
								{children}
							</div>
						</div>
					</div>
					<div className="bg-gray-50 rounded-b-lg px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6 sm:space-x-3 sm:space-x-reverse space-y-2 sm:space-y-0">
						{showSubmitBtn && <Button variant="blue" className="w-full sm:w-auto" onClick={handleSubmit}>Submit</Button>}
						{showCloseBtn && <Button variant="light" className="w-full sm:w-auto" onClick={handleClose}>Close</Button>}
					</div>
				</div>
			</div>
		</div>
	</div>
}