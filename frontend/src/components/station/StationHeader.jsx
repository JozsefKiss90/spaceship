import { useNavigate } from "react-router-dom";
import "./StationHeader.css";

export default function StationHeader() {
    const navigate = useNavigate();
    return <div className="station-header">
        <div className="station-name">Station Name</div>
        <div onClick={() => navigate('/station/locations')}>Locations</div>
        <div>Missions</div>
    </div>
}