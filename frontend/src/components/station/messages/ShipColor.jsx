import {useEffect, useState} from "react";
import "./ShipColor.css";

export function ShipColor({ship}) {
    const [allShipColors, setAllShipColors] = useState(null);
    const [color, setColor] = useState(ship.color);
    const [colorListDisplay, setColorListDisplay] = useState(false);

    function changeColor() {
        setColorListDisplay(false);
        if (color !== ship.color) {
            fetch(`/ship/${ship.id}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    color: color.toUpperCase(),
                }),
            });
        }
    }

    async function getAvailableColors() {
        if (!allShipColors) {
            await fetch(`/ship/color`)
                .then(res => res.json())
                .then(data => setAllShipColors(data))
                .catch(err => console.error(err));
        }
        setColorListDisplay(true);
    }

    useEffect(()=>{
        setColor(ship.color);
    },[ship])

    function ColorList() {
        return (
            <div className="ship-color">
                <select name="colors" defaultValue={color} onChange={(e) => {
                    setColor(e.target.value);
                }}>
                    {allShipColors.map((colorM) => (
                        <option key={colorM} value={colorM}>
                            {colorM}
                        </option>
                    ))}
                </select>
                <div className="button" onClick={changeColor}> Choose</div>
            </div>)
    }

    return (
        <div className="ship-color row">
            {colorListDisplay
                ? <ColorList/>
                : (<>
                    <div> Color: {color.toLowerCase()}</div>
                    <div className="button" onClick={getAvailableColors}>Change</div>
                </>)}
        </div>)
}
