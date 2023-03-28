import Navbar from "./components/Navbar";
import { Outlet } from "react-router-dom";
import Footer from "./components/Footer";


export default function App() {
	
	return <>
		<Navbar />
		<div className="container mx-auto px-4 pt-12">
			<Outlet />
		</div>
		<Footer/>
	</>
}