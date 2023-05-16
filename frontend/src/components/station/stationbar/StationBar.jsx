import "./StationBar.css";
import Storage from "./Storage";
import Hangar from "./Hangar";
import {useEffect, useState} from "react";
import Mission from "../messages/Mission";

const StationBar = () => {



    return (
        <div className="station-bar">
            <Storage/>
            <Hangar/>
        </div>
    );
}

export default StationBar;