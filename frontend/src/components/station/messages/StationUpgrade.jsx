import { useNavigate, useOutletContext, useParams } from "react-router-dom";
import { ResourceNeeded } from "./ResourceNeeded";
import { useEffect, useMemo, useState } from "react";
import { useStorageDispatchContext } from "../StorageContext";
import { useHangarDispatchContext } from "../HangarContext";

export default function StationUpgrade() {
    const { stationModule } = useParams();
    const { stationId } = useOutletContext();
    const navigate = useNavigate();
    const storageSetter = useStorageDispatchContext();
    const hangarSetter = useHangarDispatchContext();
    const modules = useMemo(() => ["hangar", "storage"], []);
    const [cost, setCost] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (modules.includes(stationModule)) {
            setLoading(true);
            fetch(`/api/v1/base/${stationId}/${stationModule}/upgrade`)
                .then(res => res.json())
                .then(cost => {
                    setCost(cost);
                    setLoading(false);
                })
                .catch((err) => console.error(err));
        }
    }, [stationId, stationModule, modules])

    if (!modules.includes(stationModule)) {
        return <div>404</div>
    }

    async function onConfirm() {
        setLoading(true);
        await fetch(`/api/v1/base/${stationId}/${stationModule}/upgrade`, {
            method: "POST", headers: {
                "Content-Type": "application/json"
            }, body: JSON.stringify({})
        });
        storageSetter({ type: "update" });
        if (stationModule === "hangar") {
            hangarSetter({ type: "update" });
        }
        setLoading(false);
        navigate("/station");
    }

    if (loading) {
        return <div>Loading...</div>
    }



    return <ResourceNeeded cost={cost} item={stationModule} onConfirm={onConfirm} />
}