import {useMessageDispatchContext} from "../MessageContext";
import {useState} from "react";

export function ShipStatus({ship}) {
    const dispatch = useMessageDispatchContext();
    const [toggleDisplay, setToggleDisplay] = useState(false);

    function sendShipToMission() {
        fetch(`/mission/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                shipId: ship.id,
                locationId: 1,
                activityDuration: 40,
            }),
        });
        setToggleDisplay(!toggleDisplay);
    }

    function showMissionDetails() {
        fetch(`/mission/???`)
            .then(res => res.json())
            .then(data =>
                dispatch({
                    type: "mission",
                    data: data,
                })
            );
    }


    return (<div className="ship-status row">
        <div>Status: {ship.status}</div>
        <div
            className="button"
            onClick={() => {
                if (ship.status === "In dock") sendShipToMission();
                else showMissionDetails();
            }}
        >
            {ship.status === "In dock" ? "Send" : "Show"}
        </div>
    </div>)
}
