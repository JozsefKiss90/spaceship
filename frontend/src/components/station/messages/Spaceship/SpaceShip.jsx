import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import useHandleFetchError from "../../../../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "../../../notifications/NotificationContext";
import { useStorageDispatchContext } from "../../StorageContext";
import DisplayMinerShip from "./DisplayMinerShip";
import DisplaySpaceShip from "./DisplaySpaceShip";

export default function SpaceShip() {
    const { id } = useParams();
    const handleFetchError = useHandleFetchError();
    const notifDispatch = useNotificationsDispatch();
    const storageSetter = useStorageDispatchContext();
    const [ship, setShip] = useState(null);
    const [part, setPart] = useState(null);
    const [cost, setCost] = useState(null);


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
    }, [id, notifDispatch, handleFetchError]);

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

    useEffect(() => {
        setPart(null);
    }, [ship]);

    useEffect(() => {
        fetchShip();
    }, [fetchShip]);

    const spaceShipProps = {
        ship,
        setShip,
        cost,
        part,
        upgradePart
    }

    if (ship === null) {
        return <div>Loading...</div>;
    } else if (ship.type === "MINER") {
        return <DisplaySpaceShip {...spaceShipProps} >
            <DisplayMinerShip ship={ship} setShip={setShip} onClick={onClick}/>
        </DisplaySpaceShip>
    }
}