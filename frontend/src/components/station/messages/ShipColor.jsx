import {useEffect, useState} from "react";
import {useOutletContext} from "react-router-dom";
import "./ShipColor.css";

export function ShipColor({message}) {
    const context = useOutletContext();
    const jwt = context.jwt;
    const [allShipColors, setAllShipColors] = useState(null);
    const [color, setColor] = useState(message.data.color);
    const [colorListDisplay, setColorListDisplay] = useState(false);

    function changeColor() {
        setColorListDisplay(false);
        if (color !== message.data.color) {
            fetch(`/ship/${message.data.id}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${jwt}`,
                },
                body: JSON.stringify({
                    color: color.toUpperCase(),
                }),
            });
        }
    }

    async function getAvailableColors() {
        if (!allShipColors) {
            await fetch(`/ship/color`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            })
                .then(res => res.json())
                .then(data => setAllShipColors(data))
                .catch(err => console.error(err));
        }
        setColorListDisplay(true);
    }

    useEffect(()=>{
        setColor(message.data.color);
    },[message])

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