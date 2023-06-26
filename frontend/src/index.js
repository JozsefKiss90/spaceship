import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import Layout from './components/Layout';
import ErrorPage from './components/errorPages/ErrorPage';
import Home from './components/Home';
import Station from './components/station/Station';
import Welcome from './components/station/messages/Welcome';
import AddShip from './components/station/messages/AddShip';
import DisplayMinerShip from './components/station/messages/DisplayMinerShip';
import LocationList from './components/station/messages/LocationList';
import MissionList from './components/station/messages/MissionList';
import Mission from './components/station/messages/Mission';
import StationUpgrade from './components/station/messages/StationUpgrade';
import Login from './components/Login';
import Register from './components/Register';
import TermsAndConditions from './components/TermsAndConditions';
import PrivacyPolicy from './components/PrivacyPolicy';
import Issues from './components/Issues';
import NotFoundPage from './components/errorPages/NotFoundPage';
import AccessDeniedPage from './components/errorPages/AccessDeniedPage';
import GenericErrorPage from './components/errorPages/GenericErrorPage';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Layout />,
        errorElement: <ErrorPage />,
        children: [
            {
                path: '/',
                element: <Home />
            },
            {
                path: '/station',
                element: <Station />,
                children: [
                    {
                        path: '/station/',
                        element: <Welcome />
                    },
                    {
                        path: '/station/ship/add',
                        element: <AddShip />
                    },
                    {
                        path: '/station/ship/:id',
                        element: <DisplayMinerShip />
                    },
                    {
                        path: '/station/locations',
                        element: <LocationList />
                    },
                    {
                        path: '/station/missions',
                        element: <MissionList />
                    },
                    {
                        path: '/station/mission/:id',
                        element: <Mission />
                    },
                    {
                        path: '/station/upgrade/:stationModule',
                        element: <StationUpgrade />
                    }
                ]
            },
            {
                path: '/login',
                element: <Login />
            },
            {
                path: '/register',
                element: <Register />
            },
            {
                path: '/terms',
                element: <TermsAndConditions />
            },
            {
                path: '/privacy',
                element: <PrivacyPolicy />
            },
            {
                path: '/issues',
                element: <Issues />
            }
        ]
    },
    {
        path: '/error',
        element: <GenericErrorPage />
    },
    {
        path: '/403',
        element: <AccessDeniedPage />
    },
    {
        path: '/404',
        element: <NotFoundPage />
    }
])

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <RouterProvider router={router} />
);
