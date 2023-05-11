import './App.css';
import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import Station from "./components/Station";
import Login from "./components/Login";

function App() {
    return (
        <>
            <Routes>
                <Route path={'/'} element={<Layout />}>
                    <Route path={"/station"} element={<Station />}></Route>
                    <Route path={"/login"} element={<Login />}></Route>
                </Route>
            </Routes>
        </>
    );
}

export default App;
