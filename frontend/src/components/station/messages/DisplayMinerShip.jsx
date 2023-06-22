import "./DisplayMinerShip.css";
import { ResourceNeeded } from "./ResourceNeeded";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ShipName } from "./ShipName";
import { ShipColor } from "./ShipColor";
import { ShipStatus } from "./ShipStatus";
import { useStorageDispatchContext } from "../StorageContext";
import ShipResources from "./ShipResources/ShipResources";
import useHandleFetchError from "../../useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

export default function DisplayMinerShip() {
    const { id } = useParams();
    const handleFetchError = useHandleFetchError();
    const dispatch = useNotificationsDispatch();
    const [ship, setShip] = useState(null);
    const [shipLoading, setShipLoading] = useState(true);
    const [cost, setCost] = useState(null);
    const storageSetter = useStorageDispatchContext();
    const [part, setPart] = useState(null);

    useEffect(() => {
        setShipLoading(true);
        fetch(`/api/v1/ship/miner/${id}`)
            .then((res) => {
                if (res.ok) {
                    return res.json();
                } else {
                    handleFetchError(res);
                }
            })
            .then(data => {
                setShip(data);
                setShipLoading(false);
            })
            .catch(err => {
                console.error(err);
                dispatch({
                    type: "generic error"
                });
            });
    }, [id, handleFetchError, dispatch]);

    useEffect(() => {
        setPart(null);
    }, [id]);

    async function getShipPartUpgradeCost(part) {
        setCost(null);
        return fetch(`/api/v1/ship/miner/${ship.id}/upgrade?part=${part}`)
            .then((res) => res.json())
            .then((data) => setCost(data))
            .catch((err) => console.error(err));
    }

    async function onClick(part) {
        await getShipPartUpgradeCost(part);
        setPart(part);
    }

    async function upgradePart() {
        try {
            const res = await fetch(`/api/v1/ship/miner/${ship.id}/upgrade?part=${part}`, {
                method: "PATCH"
            });
            if (res.ok) {
                const data = await res.json();
                setShip(data);
                setPart(null);
                setCost(null);
                storageSetter({ type: "update" });
                dispatch({
                    type: "add",
                    message: `${part} upgraded.`,
                    timer: 5
                });
            } else {
                handleFetchError(res);
            }
        } catch (err) {
            console.error(err);
            dispatch({
                type: "generic error"
            });
        }
    }

    if (shipLoading) {
        return <div>Loading...</div>;
    }

    const resourceSum = Object.values(ship.resources).reduce((sum, next) => sum + next, 0);

    return (<>
        <div
            className="container"
            style={{ display: "flex", flexFlow: "column" }}
        >
            <ShipName ship={ship} />
            <div className="ship-type" style={{ fontSize: "13px", height: "15px" }}>
                miner ship
            </div>
            <ShipColor ship={ship} setShip={setShip} />
            <ShipStatus ship={ship} />
            <div className="ship-shield row">
                <div> Shield | lvl: {ship.shieldLevel} | {ship.shieldEnergy} / {ship.maxShieldEnergy}</div>
                <div className="button" onClick={() => onClick("SHIELD")}>Upgrade</div>
            </div>
            <div className="ship-drill row">
                <div>Drill | lvl: {ship.drillLevel} | resource/hour: {ship.drillEfficiency}</div>
                <div className="button" onClick={() => onClick("DRILL")}>Upgrade</div>
            </div>
            <div className="ship-engine row">
                <div>Engine | lvl: {ship.engineLevel} | AU/hour: {ship.maxSpeed}</div>
                <div className="button" onClick={() => onClick("ENGINE")}>Upgrade</div>
            </div>
            <div className="ship-storage row">
                <div> Storage |
                    lvl: {ship.storageLevel} | {resourceSum} / {ship.maxStorageCapacity}</div>
                <div className="button" onClick={() => onClick("STORAGE")}>Upgrade</div>
            </div>
            <ShipResources ship={ship} setShip={setShip} />
        </div>
        <div className="side-bar">
            <img
                alt="spaceship"
                src="/ship_five.png"
                style={{
                    width: "128px", height: "128px", padding: "60px", justifySelf: "end", alignSelf: "end",
                }}
            />
            <div className="resource-needed" style={{ height: "min-content", paddingTop: "15px" }}>
                {part ? (<ResourceNeeded
                    cost={cost}
                    item={part.toLowerCase()}
                    onConfirm={upgradePart}
                />) : (<></>)}
            </div>
        </div>
    </>);
}
