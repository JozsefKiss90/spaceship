import "./Hangar.css";
import {useEffect, useState} from "react";
import {useMessageDispatchContext} from "../MessageContext";
import {useHangarContext} from "../HangarContext";

function Hangar() {
    const [minerCost, setMinerCost] = useState(null);
    const [upgradeCost, setUpgradeCost] = useState(null);
    const [storage, setStorage] = useState(null);
    const dispatch = useMessageDispatchContext();
    const update = useHangarContext();
    const [hangar, setHangar] = useState();


    useEffect(() => {
        fetch('http://localhost:8080/base/1/hangar')
            .then(res => res.json())
            .then(data => {
                setHangar(data);

            })
            .catch(err => console.error(err));
    }, [update]);


    function getShipCost() {
        fetch("http://localhost:8080/ship/cost/miner")
            .then(res => res.json())
            .then(async data => {
                setMinerCost(data);

            })
            .catch(err => console.error(err));
    }

    function getHangarUpgradeCost() {
        fetch("http://localhost:8080/base/1/hangar/upgrade")
            .then(res => res.json())
            .then(async data => {
                setUpgradeCost(data);

            })
            .catch(err => console.error(err));
    }

    function getStorage() {

        fetch("http://localhost:8080/base/1/storage/resources")
            .then(res => res.json())
            .then(data => {
                setStorage(data);

            })
            .catch(err => console.error(err));
    }

    useEffect(() => {
        if (upgradeCost && storage) {
            dispatch({
                type: "hangar upgrade",
                data: upgradeCost,
                storage: storage
            })
            setUpgradeCost(null);
            setStorage(null);
        }
    }, [upgradeCost,storage,dispatch])

    useEffect(() => {
        if (minerCost && storage) {
            dispatch({
                type: 'miner cost',
                data: minerCost,
                storage: storage
            });
            setMinerCost(null);
            setStorage(null)
        }
    }, [minerCost,storage,dispatch])


    return (<div className="hangar">
        {!hangar ? <div>Loading...</div> : (<>
            <div className="menu">
                <div>{"HANGAR | " + hangar.level + " | " + (hangar.capacity - hangar.freeDocks) + " / " + hangar.capacity}</div>
                <div className="button" onClick={async () => {
                    await getStorage();
                    getHangarUpgradeCost();
                }}>Upgrade
                </div>
            </div>
            <div className="ship-list">
                {Object.keys(hangar.ships).length === 0 ? (<p>No ships yet</p>) : (hangar.ships.map((ship) => {
                    return <p key={ship.id}>{ship.name} </p>;
                }))}
            </div>
            <div className="add ship">
                <div className="button" onClick={() => {
                    getStorage();
                    getShipCost();
                }}>Add ship
                </div>
            </div>
        </>)}
    </div>);

}

export default Hangar;