import "./StationBar.css";
import Storage from "./Storage";
import Hangar from "./Hangar";
import {useEffect, useState} from "react";

const StationBar = () => {



    return (
        <div className="station-bar">
            <Storage></Storage>
            <Hangar></Hangar>
        </div>
    );
}

export default StationBar;