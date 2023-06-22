import { useEffect, useState } from "react";
import { useOutletContext } from "react-router-dom";
import ResourceList from "./ResourceList";

export function ResourceNeeded({ cost, item, onConfirm }) {
    const { stationId } = useOutletContext();
    const [storage, setStorage] = useState(null);
    const [submitting, setSubmitting] = useState(false);

    const checkStorage = () => {
        for (const resource of Object.keys(cost)) {
            if (!(resource in storage) || cost[resource] > storage[resource]) {
                return false;
            }
        }
        return true;
    }

    useEffect(() => {
        fetch(`/api/v1/base/${stationId}/storage/resources`)
            .then(res => {
                return res.json();
            })
            .then(data => {
                setStorage(data);
            })
            .catch(err => {
                console.error(err);
            });
    }, [stationId]);

    async function onClick() {
        setSubmitting(true);
        await onConfirm();
        setSubmitting(false);
    }

    if (storage === null || cost === null) {
        return <div>Loading...</div>
    }

    if (cost.detail === "Already at max level") {
        return <div style={{ color: "yellow", textShadow: "1px 1px 2px black" }}>Can't upgrade, {item} is already at max level.</div>;
    }

    return (<div className="cost">
        <ResourceList message={`Resources needed to upgrade ${item}:`} resources={cost} />
        {checkStorage()
            ? <button className="button" onClick={onClick} disabled={submitting}>I want it!</button>
            : <div style={{ color: "red", textShadow: "1px 1px black" }}>Not enough resources</div>}
    </div>);
}
