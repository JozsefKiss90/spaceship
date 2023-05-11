import React, {useState} from 'react';
import jwt from 'jwt-decode';
import {useOutletContext} from "react-router-dom";

const Login = () => {
    const [, setUser] = useOutletContext()

    /*const handleLogin = (username, password) => {
        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({username, password}),
        })
            .then(res => {
                const authorizationHeader = res.headers.get('Authorization');
                const token = authorizationHeader ? authorizationHeader.split(' ')[1] : null;

                if (token) {
                    try {
                        const decodedToken = jwt.decode(token);
                        setUser(decodedToken);
                        console.log('Decoded Token:', decodedToken);
                    } catch (error) {
                        console.error('Failed to decode the token', error);
                    }
                } else {
                    console.error('No token found in the response header');
                }
            })
            .catch(error => {
                console.error('Login failed', error);
            });
    };*/

    return (
        <div>
            <form action="api/v1/auth/authenticate" method="POST">
                <input type="text" required placeholder="username">

                </input>
                <input type="password" required placeholder="password">

                </input>
            </form>
        </div>
    );
};

export default Login;
