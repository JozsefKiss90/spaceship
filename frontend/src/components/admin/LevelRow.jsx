import { useState, useMemo } from "react";
import useHandleFetchError from "../../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "../notifications/NotificationContext";

export default function LevelRow({ level, updateLevels }) {
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();
  const [edit, setEdit] = useState(false);
  const [effect, setEffect] = useState(level.effect);
  const [cost, setCost] = useState(level.cost);
  const [submitting, setSubmitting] = useState(false);

  function editEffect(input) {
    setEffect(Number.isNaN(input) ? 0 : parseInt(input));
  }

  function cancelEdit(level) {
    setEdit(false);
    setEffect(level.effect);
    setCost(level.cost);
  }

  async function onConfirmUpdate() {
    if (!window.confirm("Changing levels can lead to errors. Are you sure?")) {
      setSubmitting(false);
      return;
    }
    setSubmitting(true);
    try {
      const res = await fetch(`/api/v1/level/${level.id}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          type: level.type,
          effect,
          cost: filterCost(),
        }),
      });
      if (res.ok) {
        const data = await res.json();
        updateLevels(data);
        cancelEdit(data);
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
    setSubmitting(false);
  }

  function filterCost() {
    const filteredCost = { ...cost };
    for (const resource in filteredCost) {
      if (filteredCost[resource] < 1) {
        delete filteredCost[resource];
      }
    }
    return filteredCost;
  }

  return (
    <div className="admin-list-elem">
      <div className="admin-level">
        <div>
          <div>Level:</div>
          <div>{level.level}</div>
        </div>
        <div>
          <div>Effect:</div>
          {edit ? (
            <input
              type="number"
              value={effect}
              onChange={(e) => editEffect(e.target.value)}
            ></input>
          ) : (
            <div>{level.effect}</div>
          )}
        </div>
        <div>
          <div>Cost:</div>
          {edit ? (
            <EditCost cost={cost} setCost={setCost} />
          ) : (
            <Cost cost={level.cost} />
          )}
        </div>
        <div className="admin-level-actions">
          {edit ? (
            <>
              <button
                className="button"
                onClick={onConfirmUpdate}
                disabled={submitting}
              >
                Confirm
              </button>
              <button
                className="button red"
                onClick={() => cancelEdit(level)}
                disabled={submitting}
              >
                Cancel edit
              </button>
            </>
          ) : (
            <>
              <button className="button blue" onClick={() => setEdit(true)}>
                Edit
              </button>
              {level.max && <button className="button red">Delete</button>}
            </>
          )}
        </div>
      </div>
    </div>
  );
}

function Cost({ cost }) {
  const costList = useMemo(() => {
    return Object.entries(cost);
  }, [cost]);
  if (costList.length === 0) {
    return <div>No cost</div>;
  } else {
    return (
      <div>
        {costList.map((entry) => (
          <div key={entry[0]}>
            {entry[0]}: {entry[1]}
          </div>
        ))}
      </div>
    );
  }
}

function EditCost({ cost, setCost }) {
  const resources = ["METAL", "SILICONE", "CRYSTAL", "PLUTONIUM"];
  const unusedResources = resources.filter(
    (resource) => !Object.keys(cost).includes(resource)
  );

  function editCost(resource, input) {
    const filteredInput = Number.isNaN(input)
      ? 0
      : Math.max(0, parseInt(input));
    const newCost = { ...cost };
    newCost[resource] = filteredInput;
    setCost(newCost);
  }

  function removeResorce(resource) {
    const newCost = { ...cost };
    delete newCost[resource];
    setCost(newCost);
  }

  function addResource(resource) {
    const newCost = { ...cost };
    newCost[resource] = 0;
    setCost(newCost);
  }

  return (
    <div>
      {Object.keys(cost).map((resource) => (
        <div className="admin-level-cost-edit" key={resource}>
          <div>{resource}:</div>
          <input
            type="number"
            value={cost[resource]}
            onChange={(e) => editCost(resource, e.target.value)}
          />
          <button onClick={() => removeResorce(resource)}>X</button>
        </div>
      ))}
      {unusedResources.length > 0 && (
        <select value={"Add new"} onChange={(e) => addResource(e.target.value)}>
          <option value={"Add new"} disabled>
            Add new
          </option>
          {unusedResources.map((resource) => (
            <option key={resource}>{resource}</option>
          ))}
        </select>
      )}
    </div>
  );
}
