export default function Alert({ children, variant }) {
	const styles = ["rounded-lg", "py-3", "px-6", "mb-4", "text-base", "mb-2", "w-full"]
	switch (variant){
		case 'green': 
			styles.push("bg-green-100", "text-green-700")
			break
		case 'purple': 
			styles.push("bg-purple-100", "text-purple-700")
			break
		case 'red': 
			styles.push("bg-red-100", "text-red-700")
			break
		case 'yellow': 
			styles.push("bg-yellow-100", "text-yellow-700")
			break
		case 'indigo': 
			styles.push("bg-indigo-100", "text-indigo-700")
			break
		case 'gray': 
			styles.push("bg-gray-50", "text-gray-500")
			break
		case 'light': 
		default:
			styles.push("bg-green-300", "text-green-800")
			break
	}

	return <div className={styles.join(" ")} role="alert">
  	{children}
	</div>
}