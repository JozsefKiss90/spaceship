import "./DisplayMinerShip.css";
import { ResourceNeeded } from "./ResourceNeeded";
import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ShipName } from "./ShipName";
import { ShipColor } from "./ShipColor";
import { ShipStatus } from "./ShipStatus";
import { useStorageDispatchContext } from "../StorageContext";
import ShipResources from "./ShipResources/ShipResources";
import useHandleFetchError from "../../../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

export default function DisplayMinerShip() {
    const { id } = useParams();
    const handleFetchError = useHandleFetchError();
    const notifDispatch = useNotificationsDispatch();
    const [ship, setShip] = useState(null);
    const [cost, setCost] = useState(null);
    const storageSetter = useStorageDispatchContext();
    const [part, setPart] = useState(null);

    const fetchShip = useCallback(async () => {
        setShip(null);
        try {
            const res = await fetch(`/api/v1/ship/${id}/detail`);
            if (res.ok) {
                const data = await res.json();
                setShip(data);
            } else {
                handleFetchError(res);
            }
        } catch (err) {
            console.error(err);
            notifDispatch({
                type: "generic error"
            })
        }
    }, [id, notifDispatch, handleFetchError])

    useEffect(() => {
        fetchShip();
    }, [fetchShip]);

    useEffect(() => {
        setPart(null);
    }, [id]);

    async function getShipPartUpgradeCost(part) {
        setCost(null);
        try {
            const res = await fetch(`/api/v1/ship/${ship.id}/upgrade?part=${part}`)
            if (res.ok) {
                const data = await res.json();
                setCost(data);
                return true
            } else {
                handleFetchError(res);
            }
        } catch (err) {
            console.error(err);
            notifDispatch({
                type: "generic error"
            })
        }
        return false;
    }

    async function onClick(part) {
        setPart(null);
        if (await getShipPartUpgradeCost(part)) {
            setPart(part);
        }
    }

    async function upgradePart() {
        try {
            const res = await fetch(`/api/v1/ship/${ship.id}/upgrade?part=${part}`, {
                method: "PATCH"
            });
            if (res.ok) {
                const data = await res.json();
                setShip(data);
                setPart(null);
                setCost(null);
                storageSetter({ type: "update" });
                notifDispatch({
                    type: "add",
                    message: `${part} upgraded.`,
                    timer: 5
                });
            } else {
                handleFetchError(res);
            }
        } catch (err) {
            console.error(err);
            notifDispatch({
                type: "generic error"
            });
        }
    }

    if (ship === null) {
        return <div>Loading...</div>;
    }

    const resourceSum = Object.values(ship.resources).reduce((sum, next) => sum + next, 0);

    return (<>
        <div
            className="container"
            style={{ display: "flex", flexFlow: "column" }}
        >
            <ShipName ship={ship} setShip={setShip} />
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
