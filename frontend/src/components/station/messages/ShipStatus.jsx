import {useMessageDispatchContext} from "../MessageContext";
import {useOutletContext} from "react-router-dom";
import {useState} from "react";

export function ShipStatus({message}) {
    const dispatch = useMessageDispatchContext();
    const context = useOutletContext();
    const [toggleDisplay, setToggleDisplay] = useState(false);
    const jwt = context.jwt;

    function sendShipToMission() {
        fetch(`/mission/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${jwt}`,
            },
            body: JSON.stringify({
                shipId: message.data.id,
                locationId: 1,
                activityDuration: 40,
            }),
        });
        setToggleDisplay(!toggleDisplay);
    }

    function showMissionDetails() {
        fetch(`/mission/???`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
        })
            .then(res => res.json())
            .then(data =>
                dispatch({
                    type: "mission",
                    data: data,
                })
            );
    }


    return (<div className="ship-status row">
        <div>Status: {message.data.status}</div>
        <div
            className="button"
            onClick={() => {
                if (message.data.status === "In dock") sendShipToMission();
                else showMissionDetails();
            }}
        >
            {message.data.status === "In dock" ? "Send" : "Show"}
        </div>
    </div>)
}