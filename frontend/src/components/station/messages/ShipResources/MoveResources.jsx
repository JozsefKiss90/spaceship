import { useEffect, useState } from "react";
import { useOutletContext } from "react-router";
import ResourceRow from "./ResourceRow";

export default function MoveResources({ ship, onApplyMove }) {
  const { stationId } = useOutletContext();
  const [storage, setStorage] = useState(null);
  const [moveAmount, setMoveAmount] = useState({});
  const [loading, setLoading] = useState(true);
  console.log(moveAmount);

  useEffect(() => {
    setLoading(true);
    fetch(`/api/v1/base/${stationId}/storage`)
      .then((res) => res.json())
      .then((data) => setStorage(data))
      .finally(() => setLoading(false));
  }, [stationId]);

  function confirmMove() {
    setLoading(true);
    fetch(`/api/v1/base/${stationId}/add/resource-from-ship?ship=${ship.id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(moveAmount),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data) {
          onApplyMove(moveAmount);
        } else {
          setLoading(false);
        }
      });
  }

  function updateMoveAmount(resource, amount) {
    console.log(amount);
    const newMoveAmount = { ...moveAmount };
    newMoveAmount[resource] = amount;
    setMoveAmount(newMoveAmount);
  }

  if (loading) {
    return <div>Loading...</div>;
  }

  const totalMoved = Object.values(moveAmount).reduce(
    (sum, next) => sum + next,
    0
  );
  console.log(totalMoved);
  const freeSpace = storage.freeSpace - totalMoved;

  return (
    <div className="move-resources">
      <table>
        <tbody>
          <tr>
            <td colSpan={2}></td>
            <td>Ship</td>
            <td>{"->"}</td>
            <td>Station</td>
          </tr>
          {Object.keys(ship.resources).map((resource) => {
            return (
              <ResourceRow
                key={resource}
                resource={resource}
                ship={ship.resources[resource]}
                moved={moveAmount[resource] ? moveAmount[resource] : 0}
                station={
                  storage.resources[resource] ? storage.resources[resource] : 0
                }
                onChange={updateMoveAmount}
              />
            );
          })}
        </tbody>
      </table>
      <div
        style={
          freeSpace >= 0 ? {} : { color: "red", textShadow: "1px 1px black" }
        }
      >
        Free space: {freeSpace}
      </div>
      {freeSpace >= 0 && (
        <div>
          <button className="button" onClick={confirmMove}>
            Move
          </button>
        </div>
      )}
    </div>
  );
}
