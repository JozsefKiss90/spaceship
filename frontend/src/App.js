import './App.css';
import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import Station from "./components/station/Station";
import Login from "./components/Login";
import Home from './components/Home';
import Register from './components/Register';
import TermsAndConditions from './components/TermsAndConditions';
import PrivacyPolicy from './components/PrivacyPolicy';
import Issues from './components/Issues';
import DisplayMinerShip from './components/station/messages/DisplayMinerShip';
import Welcome from './components/station/messages/Welcome';

function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={<Layout />}>
                    <Route path='/' element={<Home />}></Route>
                    <Route path="/station" element={<Station />}>
                        <Route path='/station/' element={<Welcome />}/>
                        <Route path='/station/ship/:id' element={<DisplayMinerShip />}/>
                    </Route>
                    <Route path="/login" element={<Login />}></Route>
                    <Route path="/register" element={<Register />}></Route>
                    <Route path="/terms" element={<TermsAndConditions />}></Route>
                    <Route path="/privacy" element={<PrivacyPolicy />}></Route>
                    <Route path="/issues" element={<Issues />}></Route>
                </Route>
            </Routes>
        </>
    );
}

export default App;
