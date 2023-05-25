import {useMessageDispatchContext} from "../MessageContext";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

export function ShipStatus({ship}) {
    //const dispatch = useMessageDispatchContext();
    const navigate = useNavigate();
    //const [toggleDisplay, setToggleDisplay] = useState(false);

    // function sendShipToMission() {
    //     fetch(`/mission/`, {
    //         method: "POST",
    //         headers: {
    //             "Content-Type": "application/json",
    //         },
    //         body: JSON.stringify({
    //             shipId: ship.id,
    //             locationId: 1,
    //             activityDuration: 40,
    //         }),
    //     });
    //     setToggleDisplay(!toggleDisplay);
    // }
    //
    // function showMissionDetails() {
    //     fetch(`/api/v1/mission/${ship.missionId}`)
    //         .then(res => res.json())
    //         .then(data =>
    //             dispatch({
    //                 type: "mission",
    //                 data: data,
    //             })
    //         );
    // }


    return (<div className="ship-status row">
        {ship.missionId > 0
            ? <>
                <div>Status: On mission</div>
                <div className="button" onClick={() => navigate(`/station/mission/${ship.missionId}`)}>Show</div>
            </>
            : <div>Status: In dock</div>}
    </div>)
}
