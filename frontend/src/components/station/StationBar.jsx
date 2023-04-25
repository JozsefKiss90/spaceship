import "./StationBar.css";
import Storage from "./Storage";
import Hangar from "./Hangar";
import {useEffect, useState} from "react";

const StationBar = () => {
    const [station, setStation] = useState();

    useEffect(() => {
        fetch('http://localhost:8080/base')
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                setStation(data);
            })
            .catch((err) => {
                console.error(err);
            });
    }, []);


    return (<>{!station ?
        <div className="station-bar">Loading...</div> :
        <div className="station-bar">
            <Storage storage={station.storage}></Storage>
            <Hangar hangar={station.hangar}></Hangar>
        </div>
    }</>);
}

export default StationBar;