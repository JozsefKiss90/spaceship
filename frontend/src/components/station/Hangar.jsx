import "./Hangar.css";
import {useEffect, useState} from "react";
import {useMessageDispatchContext} from "../MessageContext";
import {useHangarContext} from "../HangarContext";

function Hangar() {
    const [minerCost, setMinerCost] = useState(null);
    const [storage, setStorage] = useState(null);
    const dispatch = useMessageDispatchContext();
    const update = useHangarContext();
    const [hangar, setHangar] = useState();


    useEffect(() => {
        fetch('http://localhost:8080/base/hangar')
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                setHangar(data);
            })
            .catch((err) => {
                console.error(err);
            });
    }, [update]);



    function getCost() {
        fetch("http://localhost:8080/ship/cost/miner")
            .then((res) => res.json())
            .then((data) => {
                setMinerCost(data.cost);
            })
    }

    function getStorage() {
        fetch("http://localhost:8080/base/storage/resources")
            .then((res) => res.json())
            .then((data) => {
                setStorage(data);
            })
    }

    useEffect(() => {
        if (minerCost && storage) {
            dispatch({
                type: 'cost',
                data: minerCost,
                storage: storage
            });
        }
    }, [minerCost, storage])


    return (<div className="hangar">
        {!hangar ?
            <div>Loading...</div> : (<>
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
                    <div className="button" onClick={() => {
                        getCost();
                        getStorage();
                    }
                    }>Add ship
                    </div>
                </div>
            </>)}
    </div>);

}

export default Hangar;