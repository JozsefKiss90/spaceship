import './App.css';
import {Route, Routes} from 'react-router-dom';
import Layout from './components/Layout';
import Station from "./components/Station";

function App() {
    return (
        <>
            <Layout/>
            <Routes>
                <Route path={"/station"} element={<Station/>}></Route>
            </Routes>
        </>
    );
}

export default App;
