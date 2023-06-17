import "./StationBar.css";
import Storage from "./Storage";
import Hangar from "./Hangar";
import StationHeader from "./StationHeader";

const StationBar = () => {



    return (
        <div className="station-bar">
            <StationHeader/>
            <Storage/>
            <Hangar/>
        </div>
    );
}

export default StationBar;