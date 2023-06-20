import { useRouteError } from "react-router-dom";
import NotFoundPage from "./NotFoundPage";

export default function ErrorPage() {
    const error = useRouteError();
    console.error(error);

    if (error.status === 404) {
        return <NotFoundPage />
    }

    return <>
        <div className="error-page">
            <div className="error-box">
                <div>Something went wrong, Commander</div>
            </div>
        </div>
    </>;
}