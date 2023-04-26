import "./Hangar.css";
import {useEffect, useState} from "react";
import {useMessageDispatchContext} from "../MessageContext";

function Hangar(props) {
    const hangar = props.hangar;
    const [minerCost, setMinerCost] = useState(null);
    const dispatch = useMessageDispatchContext();


     function getCost() {
         fetch("http://localhost:8080/ship/cost/miner")
            .then((res) => res.json())
            .then((data) => {
                setMinerCost(data.cost);
            })
    }

    useEffect(()=>{
        if (minerCost !== null) {
            dispatch({
                type: 'cost',
                data: minerCost
            });
        }
    },[minerCost])




    return (<div className="hangar">
        <div className="menu">
            <div>{"HANGAR | " + hangar.level + " | " + (hangar.capacity - hangar.freeDocks) + " / " + hangar.capacity}</div>
            <div className="button">Upgrade</div>
        </div>
        <div className="ship-list">
            {Object.keys(hangar.ships).length === 0 ? (<p>No ships yet</p>) : (
                hangar.ships.map((ship) => {
                    return <p key={ship.id}>{ship.name} - {ship.type}</p>;
                })
            )}
        </div>
        <div className="add ship">
            <div className="button" onClick={getCost}>Add ship
            </div>
        </div>
    </div>);

}

export default Hangar;