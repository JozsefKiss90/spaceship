// import jwt_decode from 'jwt-decode';
// import Cookies from 'js-cookie';
import { useNavigate, useOutletContext } from "react-router-dom";

const Login = () => {
    const navigate = useNavigate();
    const [, setJwt] = useOutletContext();

    const onSubmit = (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const entries = [...formData.entries()];

        const credentials = entries.reduce((acc, entry) => {
            const [k, v] = entry;
            acc[k] = v;
            return acc;
        }, {});
        console.log(credentials);
        handleLogin(credentials);
    };

    async function handleLogin(formData) {
        try {
            await fetch('/api/v1/auth/authenticate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            }).then(response => {
                return response.json()
            }).then(data => {
                setJwt(data.token);
                // Cookies.set('user', JSON.stringify(data));
                navigate('/station');
            });
        } catch (err) {
            // setMessage('Incorrect email or password.');
        }
    }



    return (
        <div>
            <form onSubmit={onSubmit}>
                <input name='username' type="text" required placeholder="username"></input>
                <input name='password' type="password" required placeholder="password"></input>
                <button type='Submit'>Login</button>
            </form>
        </div>
    );
};

export default Login;
