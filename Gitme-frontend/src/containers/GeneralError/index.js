import { useRouteError } from "react-router-dom";
import Alert from "../../components/Alert";
import Footer from "../../components/Footer";
import Navbar from "../../components/Navbar";

export default function GeneralError() {
	const error = useRouteError()

	return <>
		<Navbar />
		<div className="container mx-auto px-4 pt-12">
			<Alert variant="red">{error.message}</Alert>
		</div>
		<Footer/>
	</>
}