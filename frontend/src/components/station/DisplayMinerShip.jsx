import "./DisplayMinerShip.css";

export function DisplayMinerShip({message, checkStorage}) {
    const ship = message.data;
    return <div className="container" style={{display: "flex", flexFlow: "column", fontSize: "18px"}}>
        <div className="ship-name" style={{fontSize: "50px"}}>{ship.name}</div>
        <div className="ship-img"><img src="ship_five.png" style={{width: "55px", height: "55px"}}/></div>
        <div className="ship-type">Type: miner</div>
        <div className="ship-color" style={{display: "flex", flexFlow: "row"}}>
            <div> Color: {ship.color.toLowerCase()} </div>
            <div className="button">Change</div>
        </div>
        <div className="ship-status">Status: {ship.status}</div>
        <div className="ship-shield">
            Shield | lvl: {ship.shieldLevel} | {ship.shieldEnergy} / {ship.maxShieldEnergy}
        </div>
        <div className="ship-drill">
            Drill | lvl: {ship.drillLevel} | resource/hour: {ship.drillEfficiency}
        </div>
        <div className="ship-engine">
            Engine | lvl: {ship.engineLevel} | lightyear/hour: {ship.maxSpeed}
        </div>
        <div className="ship-storage">
            Storage | lvl: {ship.storageLevel} | {ship.resources.length} / {ship.maxStorageCapacity}
        </div>

    </div>
}

