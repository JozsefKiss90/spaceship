import { useNavigate } from "react-router-dom";
import "./StationHeader.css";

export default function StationHeader() {
    const navigate = useNavigate();
    return <div className="station-header">
        <div className="station-name">Station Name</div>
        <div className="button push" onClick={() => navigate('/station/locations')}>Locations</div>
        <div className="button" onClick={() => navigate('/station/missions')}>Missions</div>
    </div>
}